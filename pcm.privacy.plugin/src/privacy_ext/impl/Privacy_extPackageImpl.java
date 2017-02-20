/**
 */
package privacy_ext.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.palladiosimulator.pcm.PcmPackage;

import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

import privacy_ext.CommunicationLinkPrivacy;
import privacy_ext.Privacy_extFactory;
import privacy_ext.Privacy_extPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Privacy_extPackageImpl extends EPackageImpl implements Privacy_extPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass communicationLinkPrivacyEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see privacy_ext.Privacy_extPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Privacy_extPackageImpl() {
		super(eNS_URI, Privacy_extFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link Privacy_extPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Privacy_extPackage init() {
		if (isInited) return (Privacy_extPackage)EPackage.Registry.INSTANCE.getEPackage(Privacy_extPackage.eNS_URI);

		// Obtain or create and register package
		Privacy_extPackageImpl thePrivacy_extPackage = (Privacy_extPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Privacy_extPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Privacy_extPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		PcmPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePrivacy_extPackage.createPackageContents();

		// Initialize created meta-data
		thePrivacy_extPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePrivacy_extPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Privacy_extPackage.eNS_URI, thePrivacy_extPackage);
		return thePrivacy_extPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCommunicationLinkPrivacy() {
		return communicationLinkPrivacyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommunicationLinkPrivacy_ConnectionType() {
		return (EAttribute)communicationLinkPrivacyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommunicationLinkPrivacy_Ssid() {
		return (EAttribute)communicationLinkPrivacyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommunicationLinkPrivacy_Bssid() {
		return (EAttribute)communicationLinkPrivacyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommunicationLinkPrivacy_Carrier() {
		return (EAttribute)communicationLinkPrivacyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCommunicationLinkPrivacy_Protocol() {
		return (EAttribute)communicationLinkPrivacyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Privacy_extFactory getPrivacy_extFactory() {
		return (Privacy_extFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		communicationLinkPrivacyEClass = createEClass(COMMUNICATION_LINK_PRIVACY);
		createEAttribute(communicationLinkPrivacyEClass, COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE);
		createEAttribute(communicationLinkPrivacyEClass, COMMUNICATION_LINK_PRIVACY__SSID);
		createEAttribute(communicationLinkPrivacyEClass, COMMUNICATION_LINK_PRIVACY__BSSID);
		createEAttribute(communicationLinkPrivacyEClass, COMMUNICATION_LINK_PRIVACY__CARRIER);
		createEAttribute(communicationLinkPrivacyEClass, COMMUNICATION_LINK_PRIVACY__PROTOCOL);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ResourceenvironmentPackage theResourceenvironmentPackage = (ResourceenvironmentPackage)EPackage.Registry.INSTANCE.getEPackage(ResourceenvironmentPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		communicationLinkPrivacyEClass.getESuperTypes().add(theResourceenvironmentPackage.getCommunicationLinkResourceSpecification());

		// Initialize classes and features; add operations and parameters
		initEClass(communicationLinkPrivacyEClass, CommunicationLinkPrivacy.class, "CommunicationLinkPrivacy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCommunicationLinkPrivacy_ConnectionType(), ecorePackage.getEString(), "connectionType", "", 0, 1, CommunicationLinkPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommunicationLinkPrivacy_Ssid(), ecorePackage.getEString(), "ssid", null, 0, 1, CommunicationLinkPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommunicationLinkPrivacy_Bssid(), ecorePackage.getEString(), "bssid", null, 0, 1, CommunicationLinkPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommunicationLinkPrivacy_Carrier(), ecorePackage.getEString(), "carrier", null, 0, 1, CommunicationLinkPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCommunicationLinkPrivacy_Protocol(), ecorePackage.getEString(), "protocol", null, 0, 1, CommunicationLinkPrivacy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //Privacy_extPackageImpl
