/**
 */
package privacy_ext.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import privacy_ext.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Privacy_extFactoryImpl extends EFactoryImpl implements Privacy_extFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Privacy_extFactory init() {
		try {
			Privacy_extFactory thePrivacy_extFactory = (Privacy_extFactory)EPackage.Registry.INSTANCE.getEFactory(Privacy_extPackage.eNS_URI);
			if (thePrivacy_extFactory != null) {
				return thePrivacy_extFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Privacy_extFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Privacy_extFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY: return createCommunicationLinkPrivacy();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommunicationLinkPrivacy createCommunicationLinkPrivacy() {
		CommunicationLinkPrivacyImpl communicationLinkPrivacy = new CommunicationLinkPrivacyImpl();
		return communicationLinkPrivacy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Privacy_extPackage getPrivacy_extPackage() {
		return (Privacy_extPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Privacy_extPackage getPackage() {
		return Privacy_extPackage.eINSTANCE;
	}

} //Privacy_extFactoryImpl
