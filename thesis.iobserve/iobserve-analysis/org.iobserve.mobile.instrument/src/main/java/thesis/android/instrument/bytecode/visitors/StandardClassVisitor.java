package thesis.android.instrument.bytecode.visitors;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import android.app.Activity;
import thesis.android.instrument.bytecode.ActivitiyBytecodeInstrumenter;
import thesis.android.instrument.bytecode.InstrumentationPoint;
import thesis.android.instrument.bytecode.SensorBytecodeInstrumenter;
import thesis.android.instrument.config.InstrumentationConfiguration;

/**
 * {@link ClassVisitor} for instrumenting whole classes.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class StandardClassVisitor extends ClassVisitor {

	// STATIC
	/** Activity creation method. */
	private static final InstrumentationPoint POINT_AGENT_INIT = new InstrumentationPoint("android/app/Activity",
			"onCreate", "(Landroid/os/Bundle;)V");

	/** Activity destruction method. */
	private static final InstrumentationPoint POINT_AGENT_DESTROY = new InstrumentationPoint("android/app/Activity",
			"onDestroy", "()V");

	/** Activity start method. */
	private static final InstrumentationPoint POINT_ACT_START = new InstrumentationPoint("android/app/Activity",
			"onStart", "()V");

	/** Activity stop method. */
	private static final InstrumentationPoint POINT_ACT_STOP = new InstrumentationPoint("android/app/Activity",
			"onStop", "()V");

	/** Set of all activity methods listed above. */
	private static final Set<InstrumentationPoint> POINTS_INIT = new HashSet<InstrumentationPoint>();

	/**
	 * Create the static set.
	 */
	static {
		POINTS_INIT.add(POINT_AGENT_INIT);
		POINTS_INIT.add(POINT_AGENT_DESTROY);
		POINTS_INIT.add(POINT_ACT_START);
		POINTS_INIT.add(POINT_ACT_STOP);
	}

	// INNER
	/** Bytecode instrumenter for {@link Activity} class. */
	private final ActivitiyBytecodeInstrumenter initInstrumenter;

	/** Configuration for the instrumentation. */
	private InstrumentationConfiguration config;

	/** Flag whether the bytecode of the class has been changed or not. */
	private boolean written;

	/** Name of the class. */
	private String className;

	/** Name of the super class. */
	private String superName;

	/** Flag whether it is an {@link Activity} class. */
	private boolean classWithInit;

	/** Flag whether the class is from the application or not. */
	private boolean classFromApplication;

	/**
	 * Creates a new standard class visitor from a given {@link ClassVisitor}.
	 * 
	 * @param api
	 *            the api version
	 * @param cv
	 *            the class visitor
	 * @param config
	 *            the instrumentation config
	 */
	public StandardClassVisitor(int api, ClassVisitor cv, InstrumentationConfiguration config) {
		super(api, cv);
		this.config = config;
		this.initInstrumenter = new ActivitiyBytecodeInstrumenter(config);
		this.written = false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		super.visit(version, access & (~Opcodes.ACC_FINAL), name, signature, superName, interfaces);

		this.className = name;
		this.superName = superName;
		this.classFromApplication = name.startsWith(config.getApplicationPackage()); // monitor

		if (this.superName != null) {
			Type superType = Type.getType(this.superName);
			this.classWithInit = matchesActivityClass(superType.getInternalName()) && !matchesActivityClass(name);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void visitEnd() {
		super.visitEnd();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		mv = new DefaultMethodCodeVisitor(config, this, api, mv, access, name, desc);

		if (classWithInit && matchesInstrumentationPoint(name, desc, POINTS_INIT) != null) {
			setWritten(true);
			return new DefaultInstrumentationAdapter(Opcodes.ASM5, className, access, name, desc, mv, initInstrumenter);
		} else if (this.classFromApplication) {
			setWritten(true);
			return new DefaultInstrumentationAdapter(Opcodes.ASM5, className, access, name, desc, mv,
					new SensorBytecodeInstrumenter(config.getKiekerSensor()));
		}

		// DEFAULT
		return mv;
	}

	/**
	 * @return the written
	 */
	public boolean isWritten() {
		return written;
	}

	/**
	 * @param written
	 *            the written to set
	 */
	public void setWritten(boolean written) {
		this.written = written;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Determines whether a class representation is an activity representing
	 * class.
	 * 
	 * @param icn
	 *            internal class representation
	 * @return true if it is an activity class - false otherwise
	 */
	private boolean matchesActivityClass(String icn) {
		return icn.equals("android/app/Activity") || icn.equals("android/support/v7/app/AppCompatActivity");
	}

	/**
	 * Determines whether there is a instrumentation point for a given method.
	 * 
	 * @param methodName
	 *            the method name
	 * @param desc
	 *            the method description
	 * @param set
	 *            set of instrumentation points
	 * @return the point which matches or null if there is none
	 */
	private InstrumentationPoint matchesInstrumentationPoint(String methodName, String desc,
			Set<InstrumentationPoint> set) {
		for (InstrumentationPoint point : set) {
			if (point.getMethodName().equalsIgnoreCase(methodName) && point.getDescription().equals(desc))
				return point;
		}
		return null;
	}

}
