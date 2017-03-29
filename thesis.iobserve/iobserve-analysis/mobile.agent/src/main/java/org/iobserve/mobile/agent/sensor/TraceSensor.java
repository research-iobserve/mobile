/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.mobile.agent.sensor;

import java.security.SecureRandom;
import java.util.Stack;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.iobserve.mobile.agent.callback.CallbackManager;
import org.iobserve.mobile.agent.core.AndroidAgent;
import org.iobserve.mobile.agent.util.DependencyManager;

import android.util.LongSparseArray;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * Sensor for dealing with operations executed before and after method
 * instrumented executions. This class uses the trace registry class of Kieker
 * in a modified version. The modified version is provided under
 * {@link TraceRegistry} and the original form is
 * {@link kieker.monitoring.core.registry.TraceRegistry}.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class TraceSensor implements ISensor {
	/**
	 * Link to the {@link TraceRegistry}.
	 */
	private static final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;

	/**
	 * Reference to the {@link CallbackManager}.
	 */
	private CallbackManager callbackManager = DependencyManager.getCallbackManager();

	/**
	 * Name of the class.
	 */
	private LongSparseArray<String> clazz;

	/**
	 * Signature of the method.
	 */
	private LongSparseArray<String> signature;

	// INNER
	/**
	 * Whether it is a new trace or not.
	 */
	private LongSparseArray<Boolean> newTrace;

	/**
	 * The id of the trace.
	 */
	private LongSparseArray<Long> traceId;

	/**
	 * Trace meta data Kieker record.
	 */
	private LongSparseArray<TraceMetadata> trace;

	/**
	 * Creates a new instance.
	 */
	public TraceSensor() {
		clazz = new LongSparseArray<String>();
		signature = new LongSparseArray<String>();
		newTrace = new LongSparseArray<Boolean>();
		traceId = new LongSparseArray<Long>();
		trace = new LongSparseArray<TraceMetadata>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeBody(long id) {
		TraceMetadata traceMeta = TRACEREGISTRY.getTrace();

		boolean nTrace = traceMeta == null;
		newTrace.put(id, nTrace);
		if (nTrace) {
			traceMeta = TRACEREGISTRY.registerTrace();
			pushEvent(traceMeta);
		}
		trace.put(id, traceMeta);

		long traceIdCache = traceMeta.getTraceId();
		traceId.put(id, traceIdCache);

		final BeforeOperationEvent event = new BeforeOperationEvent(System.nanoTime(), traceIdCache,
				traceMeta.getNextOrderId(), signature.get(id), clazz.get(id));
		pushEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exceptionThrown(long id, final String caused) {
		if (newTrace.get(id)) {
			TRACEREGISTRY.unregisterTrace();
		}

		final AfterOperationFailedEvent event = new AfterOperationFailedEvent(System.nanoTime(), traceId.get(id),
				trace.get(id).getNextOrderId(), signature.get(id), clazz.get(id), caused);
		pushEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void firstAfterBody(long id) {
		if (newTrace.get(id)) {
			TRACEREGISTRY.unregisterTrace();
		}

		final AfterOperationEvent event = new AfterOperationEvent(System.nanoTime(), traceId.get(id),
				trace.get(id).getNextOrderId(), signature.get(id), clazz.get(id));
		pushEvent(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void secondAfterBody(long id) {
		clazz.remove(id);
		signature.remove(id);
		trace.remove(id);
		traceId.remove(id);
		newTrace.remove(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOwner(long id, final String owner) {
		clazz.put(id, owner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSignature(long id, final String methodSignature) {
		signature.put(id, methodSignature);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCallbackManager(CallbackManager callbackManager) {
		this.callbackManager = callbackManager;
	}

	/**
	 * Executes the callback of the event in another thread.
	 * 
	 * @param event
	 *            the event which should be sent to the monitoring server.
	 */
	private void pushEvent(final IMonitoringRecord event) {
		if (callbackManager == null) {
			AndroidAgent.queueForInit(event);
		} else {
			callbackManager.pushKiekerData(event);
		}
	}

	/***************************************************************************
	 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
	 *
	 * Licensed under the Apache License, Version 2.0 (the "License"); you may
	 * not use this file except in compliance with the License. You may obtain a
	 * copy of the License at
	 *
	 * http://www.apache.org/licenses/LICENSE-2.0
	 *
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	 * License for the specific language governing permissions and limitations
	 * under the License.
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
			final String sessionId = UUID.randomUUID().toString();
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
