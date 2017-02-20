/**
 */
package privacy_ext;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see privacy_ext.Privacy_extFactory
 * @model kind="package"
 * @generated
 */
public interface Privacy_extPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "privacy_ext";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://github.com/research-iobserve/mobile";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "pcm.privacy";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Privacy_extPackage eINSTANCE = privacy_ext.impl.Privacy_extPackageImpl.init();

	/**
	 * The meta object id for the '{@link privacy_ext.impl.CommunicationLinkPrivacyImpl <em>Communication Link Privacy</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see privacy_ext.impl.CommunicationLinkPrivacyImpl
	 * @see privacy_ext.impl.Privacy_extPackageImpl#getCommunicationLinkPrivacy()
	 * @generated
	 */
	int COMMUNICATION_LINK_PRIVACY = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__ID = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__ID;

	/**
	 * The feature id for the '<em><b>Linking Resource Communication Link Resource Specification</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION;

	/**
	 * The feature id for the '<em><b>Failure Probability</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__FAILURE_PROBABILITY = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY;

	/**
	 * The feature id for the '<em><b>Communication Link Resource Type Communication Link Resource Specification</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION;

	/**
	 * The feature id for the '<em><b>Latency Communication Link Resource Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION;

	/**
	 * The feature id for the '<em><b>Throughput Communication Link Resource Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION;

	/**
	 * The feature id for the '<em><b>Connection Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Ssid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__SSID = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bssid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__BSSID = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Carrier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__CARRIER = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Protocol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY__PROTOCOL = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Communication Link Privacy</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMMUNICATION_LINK_PRIVACY_FEATURE_COUNT = ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION_FEATURE_COUNT + 5;

	/**
	 * Returns the meta object for class '{@link privacy_ext.CommunicationLinkPrivacy <em>Communication Link Privacy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Communication Link Privacy</em>'.
	 * @see privacy_ext.CommunicationLinkPrivacy
	 * @generated
	 */
	EClass getCommunicationLinkPrivacy();

	/**
	 * Returns the meta object for the attribute '{@link privacy_ext.CommunicationLinkPrivacy#getConnectionType <em>Connection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Connection Type</em>'.
	 * @see privacy_ext.CommunicationLinkPrivacy#getConnectionType()
	 * @see #getCommunicationLinkPrivacy()
	 * @generated
	 */
	EAttribute getCommunicationLinkPrivacy_ConnectionType();

	/**
	 * Returns the meta object for the attribute '{@link privacy_ext.CommunicationLinkPrivacy#getSsid <em>Ssid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ssid</em>'.
	 * @see privacy_ext.CommunicationLinkPrivacy#getSsid()
	 * @see #getCommunicationLinkPrivacy()
	 * @generated
	 */
	EAttribute getCommunicationLinkPrivacy_Ssid();

	/**
	 * Returns the meta object for the attribute '{@link privacy_ext.CommunicationLinkPrivacy#getBssid <em>Bssid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bssid</em>'.
	 * @see privacy_ext.CommunicationLinkPrivacy#getBssid()
	 * @see #getCommunicationLinkPrivacy()
	 * @generated
	 */
	EAttribute getCommunicationLinkPrivacy_Bssid();

	/**
	 * Returns the meta object for the attribute '{@link privacy_ext.CommunicationLinkPrivacy#getCarrier <em>Carrier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Carrier</em>'.
	 * @see privacy_ext.CommunicationLinkPrivacy#getCarrier()
	 * @see #getCommunicationLinkPrivacy()
	 * @generated
	 */
	EAttribute getCommunicationLinkPrivacy_Carrier();

	/**
	 * Returns the meta object for the attribute '{@link privacy_ext.CommunicationLinkPrivacy#getProtocol <em>Protocol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Protocol</em>'.
	 * @see privacy_ext.CommunicationLinkPrivacy#getProtocol()
	 * @see #getCommunicationLinkPrivacy()
	 * @generated
	 */
	EAttribute getCommunicationLinkPrivacy_Protocol();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Privacy_extFactory getPrivacy_extFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link privacy_ext.impl.CommunicationLinkPrivacyImpl <em>Communication Link Privacy</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see privacy_ext.impl.CommunicationLinkPrivacyImpl
		 * @see privacy_ext.impl.Privacy_extPackageImpl#getCommunicationLinkPrivacy()
		 * @generated
		 */
		EClass COMMUNICATION_LINK_PRIVACY = eINSTANCE.getCommunicationLinkPrivacy();

		/**
		 * The meta object literal for the '<em><b>Connection Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE = eINSTANCE.getCommunicationLinkPrivacy_ConnectionType();

		/**
		 * The meta object literal for the '<em><b>Ssid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMUNICATION_LINK_PRIVACY__SSID = eINSTANCE.getCommunicationLinkPrivacy_Ssid();

		/**
		 * The meta object literal for the '<em><b>Bssid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMUNICATION_LINK_PRIVACY__BSSID = eINSTANCE.getCommunicationLinkPrivacy_Bssid();

		/**
		 * The meta object literal for the '<em><b>Carrier</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMUNICATION_LINK_PRIVACY__CARRIER = eINSTANCE.getCommunicationLinkPrivacy_Carrier();

		/**
		 * The meta object literal for the '<em><b>Protocol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMMUNICATION_LINK_PRIVACY__PROTOCOL = eINSTANCE.getCommunicationLinkPrivacy_Protocol();

	}

} //Privacy_extPackage
