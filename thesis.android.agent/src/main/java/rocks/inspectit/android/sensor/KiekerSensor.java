package rocks.inspectit.android.sensor;

import java.security.SecureRandom;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.registry.SessionRegistry;
import rocks.inspectit.android.AndroidAgent;
import rocks.inspectit.android.callback.CallbackManager;
import rocks.inspectit.android.util.DependencyInjector;

/**
 * Created by DMO on 09.01.2017.
 */
public class KiekerSensor implements ISensor {

	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	private CallbackManager callbackManager = DependencyInjector.getCallbackManager();
	private String clazz;
	private String signature;

	// INNER
	private boolean newTrace;
	private long traceId;
	private TraceMetadata trace;

	public KiekerSensor() {
	}

	@Override
	public void beforeBody() {
		trace = TRACEREGISTRY.getTrace();
		newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			if (callbackManager == null) {
				AndroidAgent.queueForInit(trace);
			} else {
				callbackManager.pushKiekerData(trace);
			}
		}

		traceId = trace.getTraceId();

		BeforeOperationEvent event = new BeforeOperationEvent(System.nanoTime(), traceId, trace.getNextOrderId(),
				signature, clazz);
		if (callbackManager == null) {
			AndroidAgent.queueForInit(event);
		} else {
			callbackManager.pushKiekerData(event);
		}
	}

	@Override
	public void exceptionThrown(String caused) {
		if (newTrace) {
			TRACEREGISTRY.unregisterTrace();
		}

		AfterOperationFailedEvent event = new AfterOperationFailedEvent(System.nanoTime(), traceId,
				trace.getNextOrderId(), signature, clazz, caused);
		if (callbackManager == null) {
			AndroidAgent.queueForInit(event);
		} else {
			callbackManager.pushKiekerData(event);
		}
	}

	@Override
	public void firstAfterBody() {
		if (newTrace) {
			TRACEREGISTRY.unregisterTrace();
		}

		AfterOperationEvent event = new AfterOperationEvent(System.nanoTime(), traceId, trace.getNextOrderId(),
				signature, clazz);
		if (callbackManager == null) {
			AndroidAgent.queueForInit(event);
		} else {
			callbackManager.pushKiekerData(event);
		}
	}

	@Override
	public void secondAfterBody() {
	}

	@Override
	public void setOwner(String owner) {
		clazz = owner;
	}

	@Override
	public void setSignature(String methodSignature) {
		signature = methodSignature;
	}
	
	/***************************************************************************
	 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 *
	 *     http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 ***************************************************************************/
	private enum TraceRegistry { // Singleton (Effective Java #3)
		/**
		 * The singleton instance.
		 */
		INSTANCE;

		private final AtomicInteger nextTraceId = new AtomicInteger(0);
		private final long unique = ((long) new SecureRandom().nextInt()) << 32; // NOCS
		private final String hostname = "";

		/**
		 * the current trace; null if new trace.
		 */
		private final ThreadLocal<TraceMetadata> traceStorage = new ThreadLocal<TraceMetadata>();

		/**
		 * used to store the stack of enclosing traces; null if no sub trace
		 * created yet.
		 */
		private final ThreadLocal<Stack<TraceMetadata>> enclosingTraceStack = new ThreadLocal<Stack<TraceMetadata>>();

		/**
		 * store the parent Trace.
		 */
		private final WeakHashMap<Thread, TracePoint> parentTrace = new WeakHashMap<Thread, TracePoint>();

		private final long getId() {
			return this.unique | this.nextTraceId.getAndIncrement();
		}

		/**
		 * Gets a Trace for the current thread. If no trace is active, null is
		 * returned.
		 *
		 * @return Trace object or null
		 */
		public final TraceMetadata getTrace() {
			return this.traceStorage.get();
		}

		/**
		 * This creates a new unique Trace object and registers it.
		 *
		 * @return Trace object
		 */
		public final TraceMetadata registerTrace() {
			final TraceMetadata enclosingTrace = this.traceStorage.get();
			if (enclosingTrace != null) { // we create a subtrace
				Stack<TraceMetadata> localTraceStack = this.enclosingTraceStack.get();
				if (localTraceStack == null) {
					localTraceStack = new Stack<TraceMetadata>();
					this.enclosingTraceStack.set(localTraceStack);
				}
				localTraceStack.push(enclosingTrace);
			}
			final Thread thread = Thread.currentThread();
			final TracePoint tp = this.getAndRemoveParentTraceId(thread);
			final long traceId = this.getId();
			final long parentTraceId;
			final int parentOrderId;
			if (tp != null) { // we have a known split point
				parentTraceId = tp.traceId;
				parentOrderId = tp.orderId;
			} else if (enclosingTrace != null) { // we create a sub trace
													// without a known split
													// point
				parentTraceId = enclosingTrace.getTraceId();
				parentOrderId = -1; // we could instead get the last orderId ...
									// But this would make it harder to
									// distinguish from known split points
			} else { // we create a new trace without a parent
				parentTraceId = traceId;
				parentOrderId = -1;
			}
			final String sessionId = SessionRegistry.INSTANCE.recallThreadLocalSessionId();
			final TraceMetadata trace = new TraceMetadata(traceId, thread.getId(), sessionId, this.hostname,
					parentTraceId, parentOrderId);
			this.traceStorage.set(trace);
			return trace;
		}

		/**
		 * Unregisters the current Trace object.
		 * <p>
		 * Future calls of getTrace() will either return null or the enclosing
		 * trace object.
		 */
		public final void unregisterTrace() {
			final Stack<TraceMetadata> localTraceStack = this.enclosingTraceStack.get();
			if (localTraceStack != null) { // we might have an enclosing trace
											// and and are able to restore it
				if (!localTraceStack.isEmpty()) { // we actually found something
					this.traceStorage.set(localTraceStack.pop());
				} else {
					this.enclosingTraceStack.remove();
					this.traceStorage.remove();
				}
			} else {
				this.traceStorage.remove();
			}
		}

		private final TracePoint getAndRemoveParentTraceId(final Thread t) {
			synchronized (this) {
				return this.parentTrace.remove(t);
			}
		}

		/**
		 * Sets the parent for the next created trace inside this thread. This
		 * method should be used by probes in connection with SpliEvents.
		 *
		 * @param t
		 *            the thread the new trace belongs to
		 * @param traceId
		 *            the parent trace id
		 * @param orderId
		 *            the parent order id
		 */
		public final void setParentTraceId(final Thread t, final long traceId, final int orderId) {
			synchronized (this) {
				this.parentTrace.put(t, new TracePoint(traceId, orderId));
			}
		}

		/**
		 * @author Jan Waller
		 */
		private static final class TracePoint {
			public final long traceId; // NOCS (public no setters or getters)
			public final int orderId; // NOCS (public no setters or getters)

			public TracePoint(final long traceId, final int orderId) {
				this.traceId = traceId;
				this.orderId = orderId;
			}
		}
	}

}
