/**
 */
package privacy_ext.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl;

import privacy_ext.CommunicationLinkPrivacy;
import privacy_ext.Privacy_extPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Communication Link Privacy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link privacy_ext.impl.CommunicationLinkPrivacyImpl#getConnectionType <em>Connection Type</em>}</li>
 *   <li>{@link privacy_ext.impl.CommunicationLinkPrivacyImpl#getSsid <em>Ssid</em>}</li>
 *   <li>{@link privacy_ext.impl.CommunicationLinkPrivacyImpl#getBssid <em>Bssid</em>}</li>
 *   <li>{@link privacy_ext.impl.CommunicationLinkPrivacyImpl#getCarrier <em>Carrier</em>}</li>
 *   <li>{@link privacy_ext.impl.CommunicationLinkPrivacyImpl#getProtocol <em>Protocol</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommunicationLinkPrivacyImpl extends CommunicationLinkResourceSpecificationImpl implements CommunicationLinkPrivacy {
	/**
	 * The default value of the '{@link #getConnectionType() <em>Connection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionType()
	 * @generated
	 * @ordered
	 */
	protected static final String CONNECTION_TYPE_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getConnectionType() <em>Connection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionType()
	 * @generated
	 * @ordered
	 */
	protected String connectionType = CONNECTION_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSsid() <em>Ssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSsid()
	 * @generated
	 * @ordered
	 */
	protected static final String SSID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSsid() <em>Ssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSsid()
	 * @generated
	 * @ordered
	 */
	protected String ssid = SSID_EDEFAULT;

	/**
	 * The default value of the '{@link #getBssid() <em>Bssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBssid()
	 * @generated
	 * @ordered
	 */
	protected static final String BSSID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBssid() <em>Bssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBssid()
	 * @generated
	 * @ordered
	 */
	protected String bssid = BSSID_EDEFAULT;

	/**
	 * The default value of the '{@link #getCarrier() <em>Carrier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCarrier()
	 * @generated
	 * @ordered
	 */
	protected static final String CARRIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCarrier() <em>Carrier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCarrier()
	 * @generated
	 * @ordered
	 */
	protected String carrier = CARRIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProtocol()
	 * @generated
	 * @ordered
	 */
	protected static final String PROTOCOL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProtocol() <em>Protocol</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProtocol()
	 * @generated
	 * @ordered
	 */
	protected String protocol = PROTOCOL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommunicationLinkPrivacyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return Privacy_extPackage.Literals.COMMUNICATION_LINK_PRIVACY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getConnectionType() {
		return connectionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnectionType(String newConnectionType) {
		String oldConnectionType = connectionType;
		connectionType = newConnectionType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE, oldConnectionType, connectionType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSsid(String newSsid) {
		String oldSsid = ssid;
		ssid = newSsid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__SSID, oldSsid, ssid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBssid() {
		return bssid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBssid(String newBssid) {
		String oldBssid = bssid;
		bssid = newBssid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__BSSID, oldBssid, bssid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCarrier(String newCarrier) {
		String oldCarrier = carrier;
		carrier = newCarrier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CARRIER, oldCarrier, carrier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProtocol(String newProtocol) {
		String oldProtocol = protocol;
		protocol = newProtocol;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__PROTOCOL, oldProtocol, protocol));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE:
				return getConnectionType();
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__SSID:
				return getSsid();
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__BSSID:
				return getBssid();
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CARRIER:
				return getCarrier();
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__PROTOCOL:
				return getProtocol();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE:
				setConnectionType((String)newValue);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__SSID:
				setSsid((String)newValue);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__BSSID:
				setBssid((String)newValue);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CARRIER:
				setCarrier((String)newValue);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__PROTOCOL:
				setProtocol((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE:
				setConnectionType(CONNECTION_TYPE_EDEFAULT);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__SSID:
				setSsid(SSID_EDEFAULT);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__BSSID:
				setBssid(BSSID_EDEFAULT);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CARRIER:
				setCarrier(CARRIER_EDEFAULT);
				return;
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__PROTOCOL:
				setProtocol(PROTOCOL_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CONNECTION_TYPE:
				return CONNECTION_TYPE_EDEFAULT == null ? connectionType != null : !CONNECTION_TYPE_EDEFAULT.equals(connectionType);
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__SSID:
				return SSID_EDEFAULT == null ? ssid != null : !SSID_EDEFAULT.equals(ssid);
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__BSSID:
				return BSSID_EDEFAULT == null ? bssid != null : !BSSID_EDEFAULT.equals(bssid);
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__CARRIER:
				return CARRIER_EDEFAULT == null ? carrier != null : !CARRIER_EDEFAULT.equals(carrier);
			case Privacy_extPackage.COMMUNICATION_LINK_PRIVACY__PROTOCOL:
				return PROTOCOL_EDEFAULT == null ? protocol != null : !PROTOCOL_EDEFAULT.equals(protocol);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (connectionType: ");
		result.append(connectionType);
		result.append(", ssid: ");
		result.append(ssid);
		result.append(", bssid: ");
		result.append(bssid);
		result.append(", carrier: ");
		result.append(carrier);
		result.append(", protocol: ");
		result.append(protocol);
		result.append(')');
		return result.toString();
	}

} //CommunicationLinkPrivacyImpl
