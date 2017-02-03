package rocks.inspectit.android;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import kieker.common.record.IMonitoringRecord;
import rocks.inspectit.android.broadcast.AbstractBroadcastReceiver;
import rocks.inspectit.android.broadcast.NetworkBroadcastReceiver;
import rocks.inspectit.android.callback.CallbackManager;
import rocks.inspectit.android.callback.data.CrashResponse;
import rocks.inspectit.android.callback.data.HelloRequest;
import rocks.inspectit.android.callback.data.MobileDefaultData;
import rocks.inspectit.android.callback.strategies.AbstractCallbackStrategy;
import rocks.inspectit.android.callback.strategies.ImmediatelyStrategy;
import rocks.inspectit.android.module.AbstractAndroidModule;
import rocks.inspectit.android.module.CPUModule;
import rocks.inspectit.android.module.MemoryModule;
import rocks.inspectit.android.module.special.NetworkModule;
import rocks.inspectit.android.module.util.ExecutionProperty;
import rocks.inspectit.android.sensor.ISensor;
import rocks.inspectit.android.util.DependencyManager;

/**
 * Created by David on 22.10.16.
 */

public class AndroidAgent {
	private static final String LOG_TAG = ExternalConfiguration.getLogTag();

	private static final Class<?>[] BROADCAST_RECVS = new Class<?>[] { NetworkBroadcastReceiver.class };

	private static final Class<?>[] MODULES = new Class<?>[] { CPUModule.class, MemoryModule.class,
			NetworkModule.class, };

	private static Map<Class<?>, AbstractAndroidModule> instantiatedModules = new HashMap<Class<?>, AbstractAndroidModule>();
	private static Map<Long, ISensor> sensorMap = new HashMap<>();
	private static List<BroadcastReceiver> createdReceivers = new ArrayList<BroadcastReceiver>();
	private static Handler mHandler = new Handler();

	private static Context initContext;

	private static CallbackManager callbackManager;
	private static TagCollector tagCollector;
	private static NetworkModule networkModule;
	private static Thread.UncaughtExceptionHandler defaultUEH;

	// QUEUES FOR INIT
	private static List<IMonitoringRecord> kiekerQueueInit = new ArrayList<>();
	private static List<MobileDefaultData> defaultQueueInit = new ArrayList<>();

	private static long currentId;

	private static boolean inited = false;
	private static boolean closed = true;

	public static synchronized void initAgent(Activity ctx) {
		if (inited)
			return;
		if (ctx == null)
			return;
		// INITING VARS
		initContext = ctx;
		currentId = 0;

		Log.i(LOG_TAG, "Initing mobile agent for Android.");

		// INIT BEFORE CRASH HANDLER
		defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);

		// INIT ANDROID DATA COLLECTOR
		AndroidDataCollector androidDataCollector = new AndroidDataCollector();
		androidDataCollector.initDataCollector(ctx);
		DependencyManager.setAndroidDataCollector(androidDataCollector);

		// INITING TAG COLLECTOR
		tagCollector = new TagCollector(ctx);
		tagCollector.collectStaticTags();
		DependencyManager.setTagCollector(tagCollector);

		// INITING CALLBACK
		AbstractCallbackStrategy callbackStrategy = new ImmediatelyStrategy();
		DependencyManager.setCallbackStrategy(callbackStrategy);

		callbackManager = new CallbackManager();
		DependencyManager.setCallbackManager(callbackManager);

		// OPEN COMMUNICATION WITH CMR
		HelloRequest helloRequest = new HelloRequest();
		helloRequest.setAppName(androidDataCollector.resolveAppName());
		helloRequest.setDeviceId(androidDataCollector.getDeviceId());

		callbackManager.pushHelloMessage(helloRequest, tagCollector.getStaticTags());

		// INITING MODULES
		for (Class<?> exModule : MODULES) {
			try {
				AbstractAndroidModule createdModule = (AbstractAndroidModule) exModule.newInstance();
				instantiatedModules.put(exModule, createdModule);

				injectDependencies(createdModule);

				createdModule.initModule(ctx);
			} catch (InstantiationException e) {
				Log.e(LOG_TAG, "Failed to create module of class '" + exModule.getClass().getName() + "'");
			} catch (IllegalAccessException e) {
				Log.e(LOG_TAG, "Failed to create module of class '" + exModule.getClass().getName() + "'");
			}
		}

		// INIT MODULE REOCCURING CALLS
		Log.i(LOG_TAG, "Creating and initializing existing modules.");
		for (Class<?> moduleEntry : MODULES) {
			AbstractAndroidModule module = instantiatedModules.get(moduleEntry);

			if (module instanceof NetworkModule) {
				networkModule = (NetworkModule) module;
			}

			setupScheduledMethods(module);
		}

		// INIT BROADCASTS
		Log.i(LOG_TAG, "Initializing broadcast receivers programmatically.");
		for (Class<?> bRecvEntry : BROADCAST_RECVS) {
			try {
				AbstractBroadcastReceiver bRecv = (AbstractBroadcastReceiver) bRecvEntry.newInstance();
				injectDependencies(bRecv);

				// create filter
				IntentFilter filter = new IntentFilter();
				for (String action : bRecv.getFilterActions()) {
					filter.addAction(action);
				}

				// register
				ctx.registerReceiver((BroadcastReceiver) bRecv, filter);

			} catch (InstantiationException e) {
				Log.e(LOG_TAG, "Failed to init broadcast receiver of class '" + bRecvEntry.getClass().getName() + "'");
			} catch (IllegalAccessException e) {
				Log.e(LOG_TAG, "Failed to init broadcast receiver of class '" + bRecvEntry.getClass().getName() + "'");
			}
		}

		// SET VALUES
		inited = true;
		closed = false;

		swapInitQueues();

		Log.i(LOG_TAG, "Finished initializing the Android Agent.");
	}

	public static synchronized void destroyAgent() {
		if (closed)
			return;
		Log.i(LOG_TAG, "Shutting down the Android Agent.");
		// SHUTDOWN MODULES
		mHandler.removeCallbacksAndMessages(null);

		for (Class<?> exModule : MODULES) {
			AbstractAndroidModule module = instantiatedModules.get(exModule);
			if (module != null) {
				module.shutdownModule();
			}
		}

		// SHUTDOWN BROADCASTS
		for (BroadcastReceiver exReceiver : createdReceivers) {
			if (exReceiver != null) {
				initContext.unregisterReceiver(exReceiver);
			}
		}

		// SHUTDOWN CALLBACK
		callbackManager.shutdown();

		// SET VALUES
		inited = false;
		closed = true;
	}

	public static synchronized long enterBody(String sensorClassName, String methodSignature, String owner) {
		try {
			Class<?> clazz = Class.forName(sensorClassName);
			Constructor<?> constructor = clazz.getConstructor();
			ISensor nSensor = (ISensor) constructor.newInstance();
			nSensor.setOwner(owner); // BEFORE ALL OTHER
			nSensor.setSignature(methodSignature);
			nSensor.beforeBody();
			sensorMap.put(currentId, nSensor);
			return currentId++;
		} catch (InstantiationException e) {
			Log.e(LOG_TAG, "Failed to create sensor instance.");
			return -1;
		} catch (IllegalAccessException e) {
			Log.e(LOG_TAG, "Failed to create sensor instance.");
			return -1;
		} catch (ClassNotFoundException e) {
			Log.e(LOG_TAG, "Failed to create sensor instance.");
			return -1;
		} catch (NoSuchMethodException e) {
			Log.e(LOG_TAG, "Failed to create sensor instance.");
			return -1;
		} catch (InvocationTargetException e) {
			Log.e(LOG_TAG, "Failed to create sensor instance.");
			return -1;
		}
	}

	public static synchronized void exitErrorBody(Throwable e, long enterId) {
		if (enterId >= 0 && sensorMap.containsKey(enterId)) {
			ISensor eSensor = sensorMap.get(enterId);

			// call methods
			eSensor.exceptionThrown(e.getClass().getName());

			// clear
			sensorMap.remove(enterId);
		}
	}

	public static synchronized void exitBody(long enterId) {
		if (enterId >= 0 && sensorMap.containsKey(enterId)) {
			ISensor eSensor = sensorMap.get(enterId);

			// call methods
			eSensor.firstAfterBody();
			eSensor.secondAfterBody();

			// clear
			sensorMap.remove(enterId);
			if (sensorMap.isEmpty())
				currentId = 0;
		}
	}

	public static void webViewLoad(String url) {
		networkModule.webViewLoad(url, "GET");
	}

	public static void webViewLoadPost(String url, byte[] data) {
		networkModule.webViewLoad(url, "POST");
	}

	public static void webViewLoad(String url, Map<?, ?> params) {
		networkModule.webViewLoad(url, "GET");
	}

	public static void httpConnect(HttpURLConnection connection) {
		networkModule.openConnection((HttpURLConnection) connection);
	}

	public static OutputStream httpOutputStream(HttpURLConnection connection) throws IOException {
		return networkModule.getOutputStream((HttpURLConnection) connection);
	}

	public static int httpResponseCode(HttpURLConnection connection) throws IOException {
		return networkModule.getResponseCode((HttpURLConnection) connection);
	}

	public static void queueForInit(IMonitoringRecord afterOperationEvent) {
		kiekerQueueInit.add(afterOperationEvent);
	}

	public static void queueForInit(MobileDefaultData data) {
		defaultQueueInit.add(data);
	}

	private static void swapInitQueues() {
		for (IMonitoringRecord ikiek : kiekerQueueInit) {
			callbackManager.pushKiekerData(ikiek);
		}

		for (MobileDefaultData def : defaultQueueInit) {
			callbackManager.pushData(def);
		}

		defaultQueueInit.clear();
		kiekerQueueInit.clear();
	}

	private static void setupScheduledMethods(AbstractAndroidModule module) {
		for (Method method : module.getClass().getMethods()) {
			if (method.isAnnotationPresent(ExecutionProperty.class)) {
				ExecutionProperty exProp = method.getAnnotation(ExecutionProperty.class);

				// CREATE FINALS
				final long iVal = exProp.interval();
				final Method invokedMethod = method;
				final AbstractAndroidModule receiver = module;
				final String className = module.getClass().getName();

				Runnable loopRunnable = new Runnable() {
					@Override
					public void run() {
						try {
							invokedMethod.invoke(receiver);
							mHandler.postDelayed(this, iVal);
						} catch (IllegalAccessException e) {
							Log.e(LOG_TAG, "Failed to invoke interval method from module '" + className + "'");
						} catch (InvocationTargetException e) {
							Log.e(LOG_TAG, "Failed to invoke interval method from module '" + className + "'");
						}
					}
				};
				mHandler.postDelayed(loopRunnable, exProp.interval());
			}
		}
	}

	private static void injectDependencies(AbstractBroadcastReceiver recv) {
		recv.setCallbackManager(DependencyManager.getCallbackManager());
		recv.setAndroidDataCollector(DependencyManager.getAndroidDataCollector());
	}

	private static void injectDependencies(AbstractAndroidModule androidModule) {
		androidModule.setCallbackManager(DependencyManager.getCallbackManager());
		androidModule.setAndroidDataCollector(DependencyManager.getAndroidDataCollector());
	}

	// handler listener
	private static Thread.UncaughtExceptionHandler _unCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			CrashResponse crashResponse = new CrashResponse();
			crashResponse.setExceptionName(ex.getClass().getName());
			crashResponse.setExceptionMessage(ex.getMessage());

			callbackManager.pushData(crashResponse);

			// do something
			defaultUEH.uncaughtException(thread, ex);
		}
	};
}
