/**
 */
package privacy_ext;

import org.eclipse.emf.ecore.EObject;

import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Communication Link Privacy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link privacy_ext.CommunicationLinkPrivacy#getConnectionType <em>Connection Type</em>}</li>
 *   <li>{@link privacy_ext.CommunicationLinkPrivacy#getSsid <em>Ssid</em>}</li>
 *   <li>{@link privacy_ext.CommunicationLinkPrivacy#getBssid <em>Bssid</em>}</li>
 *   <li>{@link privacy_ext.CommunicationLinkPrivacy#getCarrier <em>Carrier</em>}</li>
 *   <li>{@link privacy_ext.CommunicationLinkPrivacy#getProtocol <em>Protocol</em>}</li>
 * </ul>
 *
 * @see privacy_ext.Privacy_extPackage#getCommunicationLinkPrivacy()
 * @model
 * @generated
 */
public interface CommunicationLinkPrivacy extends EObject, CommunicationLinkResourceSpecification {
	/**
	 * Returns the value of the '<em><b>Connection Type</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection Type</em>' attribute.
	 * @see #setConnectionType(String)
	 * @see privacy_ext.Privacy_extPackage#getCommunicationLinkPrivacy_ConnectionType()
	 * @model default=""
	 * @generated
	 */
	String getConnectionType();

	/**
	 * Sets the value of the '{@link privacy_ext.CommunicationLinkPrivacy#getConnectionType <em>Connection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection Type</em>' attribute.
	 * @see #getConnectionType()
	 * @generated
	 */
	void setConnectionType(String value);

	/**
	 * Returns the value of the '<em><b>Ssid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ssid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ssid</em>' attribute.
	 * @see #setSsid(String)
	 * @see privacy_ext.Privacy_extPackage#getCommunicationLinkPrivacy_Ssid()
	 * @model
	 * @generated
	 */
	String getSsid();

	/**
	 * Sets the value of the '{@link privacy_ext.CommunicationLinkPrivacy#getSsid <em>Ssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ssid</em>' attribute.
	 * @see #getSsid()
	 * @generated
	 */
	void setSsid(String value);

	/**
	 * Returns the value of the '<em><b>Bssid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bssid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bssid</em>' attribute.
	 * @see #setBssid(String)
	 * @see privacy_ext.Privacy_extPackage#getCommunicationLinkPrivacy_Bssid()
	 * @model
	 * @generated
	 */
	String getBssid();

	/**
	 * Sets the value of the '{@link privacy_ext.CommunicationLinkPrivacy#getBssid <em>Bssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bssid</em>' attribute.
	 * @see #getBssid()
	 * @generated
	 */
	void setBssid(String value);

	/**
	 * Returns the value of the '<em><b>Carrier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Carrier</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Carrier</em>' attribute.
	 * @see #setCarrier(String)
	 * @see privacy_ext.Privacy_extPackage#getCommunicationLinkPrivacy_Carrier()
	 * @model
	 * @generated
	 */
	String getCarrier();

	/**
	 * Sets the value of the '{@link privacy_ext.CommunicationLinkPrivacy#getCarrier <em>Carrier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Carrier</em>' attribute.
	 * @see #getCarrier()
	 * @generated
	 */
	void setCarrier(String value);

	/**
	 * Returns the value of the '<em><b>Protocol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Protocol</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Protocol</em>' attribute.
	 * @see #setProtocol(String)
	 * @see privacy_ext.Privacy_extPackage#getCommunicationLinkPrivacy_Protocol()
	 * @model
	 * @generated
	 */
	String getProtocol();

	/**
	 * Sets the value of the '{@link privacy_ext.CommunicationLinkPrivacy#getProtocol <em>Protocol</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Protocol</em>' attribute.
	 * @see #getProtocol()
	 * @generated
	 */
	void setProtocol(String value);

} // CommunicationLinkPrivacy
