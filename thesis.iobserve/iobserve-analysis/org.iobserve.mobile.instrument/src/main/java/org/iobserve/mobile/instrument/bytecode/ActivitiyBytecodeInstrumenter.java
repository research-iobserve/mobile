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
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.mobile.agent.core.AndroidAgent;
import org.iobserve.mobile.agent.core.ExternalConfiguration;
import org.iobserve.mobile.instrument.config.InstrumentationConfiguration;
import org.iobserve.mobile.instrument.config.xml.ConnectionInfoXml;
import org.iobserve.mobile.instrument.config.xml.InstrumentationPointConfigurationXml;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import android.app.Activity;

/**
 * Bytecode instrumenter which is responsible for instrumenting Android Activity
 * classes {@link Activity}.
 * 
 * @author Robert Heinrich
 * @author David Monschein
 *
 */
public class ActivitiyBytecodeInstrumenter implements IBytecodeInstrumenter {

	/** Logger for this class. */
	private static final Logger LOG = LogManager.getLogger(ActivitiyBytecodeInstrumenter.class);

	/** Type for {@link ExternalConfiguration}. */
	private static final Type EXTCONFIGURATION_TYPE = Type.getType(ExternalConfiguration.class);

	/** Type for {@link AndroidAgent}. */
	private static final Type ANDROIDAGENT_TYPE = Type.getType(AndroidAgent.class);

	/**
	 * Information about the server which persists the monitoring data. -> This
	 * has to be set at instrumentation time!
	 */
	private ConnectionInfoXml connectionConfig;

	/**
	 * Mapping between methods of the Activity and methods which should be
	 * called in the agent.
	 */
	private Map<String, String> activityPointMapping;

	// CORRESPONDENT METHODS
	/**
	 * Method link to {@link ExternalConfiguration#setBeaconUrl(String)}.
	 */
	private Method setBeaconUrlMethod;

	/**
	 * Type for the {@link ExternalConfiguration#setBeaconUrl(String)} method.
	 */
	private Type setBeaconUrlType;

	/**
	 * Method link to {@link ExternalConfiguration#setHelloUrl(String)}.
	 */
	private Method setHelloUrlMethod;

	/**
	 * Type for the {@link ExternalConfiguration#setHelloUrl(String)} method.
	 */
	private Type setHelloUrlType;

	/**
	 * Method link to {@link AndroidAgent#onStartActivity(Activity)}.
	 */
	private Method onStartActivityMethod;

	/**
	 * Type for the {@link AndroidAgent#onStartActivity(Activity)} method.
	 */
	private Type onStartActivityType;

	/**
	 * Method link to {@link AndroidAgent#onStopActivity(Activity)}.
	 */
	private Method onStopActivityMethod;

	/**
	 * Type for the {@link AndroidAgent#onStopActivity(Activity)} method.
	 */
	private Type onStopActivityType;

	/**
	 * Creates a new bytecode instrumenter for an activity class.
	 */
	public ActivitiyBytecodeInstrumenter() {
		try {
			setBeaconUrlMethod = ExternalConfiguration.class.getMethod("setBeaconUrl", String.class);
			setBeaconUrlType = Type.getType(setBeaconUrlMethod);

			setHelloUrlMethod = ExternalConfiguration.class.getMethod("setHelloUrl", String.class);
			setHelloUrlType = Type.getType(setHelloUrlMethod);

			onStartActivityMethod = AndroidAgent.class.getMethod("onStartActivity", Activity.class);
			onStartActivityType = Type.getType(onStartActivityMethod);

			onStopActivityMethod = AndroidAgent.class.getMethod("onStopActivity", Activity.class);
			onStopActivityType = Type.getType(onStopActivityMethod);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			LOG.warn("Couldn't find all corresponding methods!");
		}
	}

	/**
	 * Creates a instance of the InitBytecodeInstrumenter.
	 * 
	 * @param configuration
	 *            configuration which specifies the mapping between activity
	 *            methods and agent methods.
	 */
	public ActivitiyBytecodeInstrumenter(final InstrumentationConfiguration configuration) {
		this();
		this.connectionConfig = configuration.getXmlConfiguration().getConnectionInfo();
		this.activityPointMapping = new HashMap<String, String>();

		for (InstrumentationPointConfigurationXml point : configuration.getXmlConfiguration()
				.getInstrumentationRootConfiguration().getInstrPointConfigs()) {
			this.activityPointMapping.put(point.getType(), point.getValue());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodEnter(final String owner, final String name, final String desc, final AdviceAdapter parent,
			final MethodVisitor mv) {
		if ("onCreate".equals(name)) {
			final String belAgentPoint = activityPointMapping.get("onCreate");

			// INSERT CONFIG CALLS
			mv.visitLdcInsn(connectionConfig.getBeaconUrl());
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, EXTCONFIGURATION_TYPE.getInternalName(),
					setBeaconUrlMethod.getName(), setBeaconUrlType.getDescriptor(), false);

			mv.visitLdcInsn(connectionConfig.getHelloUrl());
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, EXTCONFIGURATION_TYPE.getInternalName(),
					setHelloUrlMethod.getName(), setHelloUrlType.getDescriptor(), false);

			// INIT AGENT
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), belAgentPoint,
					"(Landroid/app/Activity;)V", false);
		} else if ("onDestroy".equals(name)) {
			final String belAgentPoint = activityPointMapping.get("onDestroy");

			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(), belAgentPoint, "()V", false);
		} else if ("onStart".equals(name)) {
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					onStartActivityMethod.getName(), onStartActivityType.getDescriptor(), false);
		} else if ("onStop".equals(name)) {
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, ANDROIDAGENT_TYPE.getInternalName(),
					onStopActivityMethod.getName(), onStopActivityType.getDescriptor(), false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMethodExit(final int opcode, final String owner, final String name, final String desc,
			final AdviceAdapter parent, final MethodVisitor mv) {
	}

}
