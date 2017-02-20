/**
 */
package privacy_ext;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see privacy_ext.Privacy_extPackage
 * @generated
 */
public interface Privacy_extFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Privacy_extFactory eINSTANCE = privacy_ext.impl.Privacy_extFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Communication Link Privacy</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Communication Link Privacy</em>'.
	 * @generated
	 */
	CommunicationLinkPrivacy createCommunicationLinkPrivacy();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Privacy_extPackage getPrivacy_extPackage();

} //Privacy_extFactory
