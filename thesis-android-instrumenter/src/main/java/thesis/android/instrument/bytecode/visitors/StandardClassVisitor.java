package thesis.android.instrument.bytecode.visitors;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import thesis.android.instrument.bytecode.InitBytecodeInstrumenter;
import thesis.android.instrument.bytecode.InstrumentationPoint;
import thesis.android.instrument.bytecode.SensorBytecodeInstrumenter;
import thesis.android.instrument.config.InstrumentationConfiguration;

public class StandardClassVisitor extends ClassVisitor {

	// STATIC
	private static final InstrumentationPoint POINT_AGENT_INIT = new InstrumentationPoint("android/app/Activity",
			"onCreate", "(Landroid/os/Bundle;)V");
	private static final InstrumentationPoint POINT_AGENT_DESTROY = new InstrumentationPoint("android/app/Activity",
			"onDestroy", "()V");

	private static final Set<InstrumentationPoint> POINTS_INIT = new HashSet<InstrumentationPoint>();

	static {
		POINTS_INIT.add(POINT_AGENT_INIT);
		POINTS_INIT.add(POINT_AGENT_DESTROY);
	}

	// INNER
	private final InitBytecodeInstrumenter initInstrumenter;

	private InstrumentationConfiguration config;
	private boolean written;

	private String className;
	private String superName;

	private boolean classWithInit;
	private boolean classWithSensor;
	private boolean classFromApplication;

	private Set<InstrumentationPoint> sensorPoints;

	public StandardClassVisitor(int api, ClassVisitor cv, InstrumentationConfiguration config) {
		super(api, cv);
		this.config = config;
		this.initInstrumenter = new InitBytecodeInstrumenter(config);
		this.written = false;
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access, name, signature, superName, interfaces);

		this.className = name;
		this.superName = superName;

		if (this.superName.equalsIgnoreCase(POINT_AGENT_INIT.getClassName())) {
			this.classWithInit = true;
		} else {
			sensorPoints = config.matchesInstrumentationPoint(name, superName, interfaces);
			this.classWithSensor = sensorPoints.size() > 0;

			this.classFromApplication = name.startsWith(config.getApplicationPackage()); // monitor
		}
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		if (classWithInit && matchesInstrumentationPoint(name, desc, POINTS_INIT) != null) {
			setWritten(true);
			return new DefaultInstrumentationAdapter(Opcodes.ASM5, className, access, name, desc, mv, initInstrumenter);
		} else if (this.classWithSensor) {
			InstrumentationPoint sensorPoint = matchesInstrumentationPoint(name, desc, sensorPoints);
			if (sensorPoint != null) {
				setWritten(true);
				return new DefaultInstrumentationAdapter(Opcodes.ASM5, className, access, name, desc, mv,
						new SensorBytecodeInstrumenter(config.getSensor(sensorPoint)));
			}
		} else if (this.classFromApplication) {
			setWritten(true);
			return new DefaultInstrumentationAdapter(Opcodes.ASM5, className, access, name, desc, mv,
					new SensorBytecodeInstrumenter(config.getKiekerSensor()));
		}

		// DEFAULT
		return new DefaultMethodCodeVisitor(config, this, api, mv, access, name, desc);
	}

	public boolean isWritten() {
		return written;
	}

	public void setWritten(boolean written) {
		this.written = written;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	private InstrumentationPoint matchesInstrumentationPoint(String methodName, String desc,
			Set<InstrumentationPoint> set) {
		for (InstrumentationPoint point : set) {
			if (point.getMethodName().equalsIgnoreCase(methodName) && point.getDescription().equals(desc))
				return point;
		}
		return null;
	}

}
