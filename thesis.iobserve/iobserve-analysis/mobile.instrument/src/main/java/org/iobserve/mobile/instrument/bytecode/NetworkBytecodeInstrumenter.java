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
package org.iobserve.mobile.instrument.bytecode;

import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.mobile.agent.core.AndroidAgent;
import org.iobserve.mobile.instrument.bytecode.visitors.DefaultMethodCodeVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import android.webkit.WebView;

/**
 * Class which is responsible for instrumenting calls which result in a network
 * activity.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class NetworkBytecodeInstrumenter {

	/** Method name for {@link HttpURLConnection#getOutputStream()}. */
	private static final String METHOD_GETOUTPUTSTREAM = "getOutputStream";

	/** Method name for {@link HttpURLConnection#getResponseCode()}. */
	private static final String METHOD_GETRESPONSECODE = "getResponseCode";

	/** Logger for this class. */
	private static final Logger LOG = LogManager.getLogger(NetworkBytecodeInstrumenter.class);

	/** Type of {@link HttpURLConnection}. */
	private static final Type HTTPURLCONNECTION_TYPE = Type.getType(HttpURLConnection.class);

	/** Type of {@link URLConnection}. */
	private static final Type URLCONNECTION_TYPE = Type.getType(URLConnection.class);

	/** Type of {@link AndroidAgent}. */
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	/** Type of {@link WebView}. */
	private static final Type ANDROIDWEBVIEW_TYPE = Type.getType(android.webkit.WebView.class);

	// METHODS AND THEIR TYPES
	/**
	 * Method link to {@link AndroidAgent#httpConnect(HttpURLConnection)}.
	 */
	private Method httpConnectMethod;

	/**
	 * Type of method {@link AndroidAgent#httpConnect(HttpURLConnection)}.
	 */
	private Type httpConnectType;

	/**
	 * Method link to {@link AndroidAgent#httpConnect(URLConnection)}.
	 */
	private Method httpConnectMethod2;

	/**
	 * Type of method {@link AndroidAgent#httpConnect(URLConnection)}.
	 */
	private Type httpConnectType2;

	/**
	 * Method link to {@link AndroidAgent#httpOutputStream(HttpURLConnection)}.
	 */
	private Method getOutputStreamMethod;

	/**
	 * Type of method {@link AndroidAgent#httpOutputStream(HttpURLConnection)}.
	 */
	private Type getOutputStreamType;

	/**
	 * Method link to {@link AndroidAgent#httpResponseCode(HttpURLConnection)}.
	 */
	private Method getResponseCodeMethod;

	/**
	 * Type of method {@link AndroidAgent#httpResponseCode(HttpURLConnection)}.
	 */
	private Type getResponseCodeType;

	/**
	 * Method link to {@link AndroidAgent#webViewLoad(String)}.
	 */
	private Method webViewLoadMethod;

	/**
	 * Type of method {@link AndroidAgent#webViewLoad(String)}.
	 */
	private Type webViewLoadType;

	/**
	 * Creates a new instance which resolves all corresponding agent methods.
	 */
	public NetworkBytecodeInstrumenter() {
		try {
			httpConnectMethod = AndroidAgent.class.getMethod("httpConnect", HttpURLConnection.class);
			httpConnectType = Type.getType(httpConnectMethod);

			httpConnectMethod2 = AndroidAgent.class.getMethod("httpConnect", URLConnection.class);
			httpConnectType2 = Type.getType(httpConnectMethod2);

			getOutputStreamMethod = AndroidAgent.class.getMethod("httpOutputStream", HttpURLConnection.class);
			getOutputStreamType = Type.getType(getOutputStreamMethod);

			getResponseCodeMethod = AndroidAgent.class.getMethod("httpResponseCode", HttpURLConnection.class);
			getResponseCodeType = Type.getType(getResponseCodeMethod);

			webViewLoadMethod = AndroidAgent.class.getMethod("webViewLoad", String.class);
			webViewLoadType = Type.getType(webViewLoadMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.warn("Couldn't find all corresponding methods!");
		}
	}

	/**
	 * Visits an operation within a method and if it is a network action it will
	 * be instrumented.
	 * 
	 * @param mv
	 *            the belonging method code visitor
	 * @param mvInternal
	 *            the method visitor which is used internally
	 * @param opcode
	 *            the opcode
	 * @param owner
	 *            the owner
	 * @param name
	 *            the name
	 * @param desc
	 *            the description
	 * @param itf
	 *            if it is an interface method or not
	 * @return true if it was a network action and it is instrumented now -
	 *         false otherwise
	 */
	public boolean visit(final DefaultMethodCodeVisitor mv, final MethodVisitor mvInternal, final int opcode,
			final String owner, final String name, final String desc, final boolean itf) {
		// BYTECODE MAGIC
		if (descReturnType(desc).equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getDescriptor())) {
			mv.increaseStack(1);
			// write old
			mvInternal.visitMethodInsn(opcode, owner, name, desc, itf);
			// DUP
			mv.dup();
			// write own
			mvInternal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					httpConnectMethod.getName(), httpConnectType.getDescriptor(), false);
		} else if (descReturnType(desc).equalsIgnoreCase(URLCONNECTION_TYPE.getDescriptor())) {
			mv.increaseStack(1);
			// write old
			mvInternal.visitMethodInsn(opcode, owner, name, desc, itf);
			// DUP
			mv.dup();
			// write own
			mvInternal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					httpConnectMethod2.getName(), httpConnectType2.getDescriptor(), false);
		} else if (owner.equalsIgnoreCase(HTTPURLCONNECTION_TYPE.getInternalName())) {
			if (METHOD_GETOUTPUTSTREAM.equals(name)) {
				// write own
				mvInternal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
						getOutputStreamMethod.getName(), getOutputStreamType.getDescriptor(), false);
			} else if (METHOD_GETRESPONSECODE.equals(name)) {
				// write own
				mvInternal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
						getResponseCodeMethod.getName(), getResponseCodeType.getDescriptor(), false);
			} else {
				return false;
			}
		} else if (owner.equalsIgnoreCase(ANDROIDWEBVIEW_TYPE.getInternalName())) {
			if ("loadUrl".equals(name)) {
				if (!desc.contains("java/util/Map")) {
					mv.increaseStack(2);

					mv.dup2();
					// write old
					mvInternal.visitMethodInsn(opcode, owner, name, desc, itf);

					// write own
					mvInternal.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
							webViewLoadMethod.getName(), webViewLoadType.getDescriptor(), false);

					mv.pop();
				}
			} else {
				return false;
			}
		} else {
			return false;
		}

		return true;
	}

	/**
	 * Helping method to determine the return type from a full method signature.
	 * 
	 * @param desc
	 *            full description of a method
	 * @return return type of that method
	 */
	private String descReturnType(final String desc) {
		final String[] sp = desc.split("\\)");
		return sp[1];
	}

}
