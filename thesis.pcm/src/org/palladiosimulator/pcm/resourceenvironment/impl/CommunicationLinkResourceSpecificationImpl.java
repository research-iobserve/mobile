/**
 * Copyright 2005-2009 by SDQ, IPD, University of Karlsruhe, Germany
 */
package org.palladiosimulator.pcm.resourceenvironment.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;

import de.uka.ipd.sdq.identifier.impl.IdentifierImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Communication Link
 * Resource Specification</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getLinkingResource_CommunicationLinkResourceSpecification <em>Linking Resource Communication Link Resource Specification</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getFailureProbability <em>Failure Probability</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getCommunicationLinkResourceType_CommunicationLinkResourceSpecification <em>Communication Link Resource Type Communication Link Resource Specification</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getLatency_CommunicationLinkResourceSpecification <em>Latency Communication Link Resource Specification</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getThroughput_CommunicationLinkResourceSpecification <em>Throughput Communication Link Resource Specification</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getConnectionType <em>Connection Type</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getSsid <em>Ssid</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getBssid <em>Bssid</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getOperator <em>Operator</em>}</li>
 *   <li>{@link org.palladiosimulator.pcm.resourceenvironment.impl.CommunicationLinkResourceSpecificationImpl#getProtocol <em>Protocol</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CommunicationLinkResourceSpecificationImpl extends IdentifierImpl
		implements CommunicationLinkResourceSpecification {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2005-2017 by palladiosimulator.org";

	/**
	 * The default value of the '{@link #getFailureProbability() <em>Failure Probability</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getFailureProbability()
	 * @generated
	 * @ordered
	 */
	protected static final double FAILURE_PROBABILITY_EDEFAULT = 0.0;

	/**
	 * The default value of the '{@link #getConnectionType() <em>Connection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnectionType()
	 * @generated
	 * @ordered
	 */
	protected static final String CONNECTION_TYPE_EDEFAULT = null;

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
	 * The default value of the '{@link #getBssid() <em>Bssid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBssid()
	 * @generated
	 * @ordered
	 */
	protected static final String BSSID_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperator()
	 * @generated
	 * @ordered
	 */
	protected static final String OPERATOR_EDEFAULT = null;

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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected CommunicationLinkResourceSpecificationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LinkingResource getLinkingResource_CommunicationLinkResourceSpecification() {
		return (LinkingResource) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				true, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLinkingResource_CommunicationLinkResourceSpecification(
			LinkingResource newLinkingResource_CommunicationLinkResourceSpecification, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newLinkingResource_CommunicationLinkResourceSpecification,
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLinkingResource_CommunicationLinkResourceSpecification(
			LinkingResource newLinkingResource_CommunicationLinkResourceSpecification) {
		eDynamicSet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				newLinkingResource_CommunicationLinkResourceSpecification);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getFailureProbability() {
		return (Double) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY,
				true, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFailureProbability(double newFailureProbability) {
		eDynamicSet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY,
				newFailureProbability);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CommunicationLinkResourceType getCommunicationLinkResourceType_CommunicationLinkResourceSpecification() {
		return (CommunicationLinkResourceType) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				true, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public CommunicationLinkResourceType basicGetCommunicationLinkResourceType_CommunicationLinkResourceSpecification() {
		return (CommunicationLinkResourceType) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				false, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCommunicationLinkResourceType_CommunicationLinkResourceSpecification(
			CommunicationLinkResourceType newCommunicationLinkResourceType_CommunicationLinkResourceSpecification) {
		eDynamicSet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				newCommunicationLinkResourceType_CommunicationLinkResourceSpecification);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PCMRandomVariable getLatency_CommunicationLinkResourceSpecification() {
		return (PCMRandomVariable) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				true, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLatency_CommunicationLinkResourceSpecification(
			PCMRandomVariable newLatency_CommunicationLinkResourceSpecification, NotificationChain msgs) {
		msgs = eDynamicInverseAdd((InternalEObject) newLatency_CommunicationLinkResourceSpecification,
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatency_CommunicationLinkResourceSpecification(
			PCMRandomVariable newLatency_CommunicationLinkResourceSpecification) {
		eDynamicSet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				newLatency_CommunicationLinkResourceSpecification);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PCMRandomVariable getThroughput_CommunicationLinkResourceSpecification() {
		return (PCMRandomVariable) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				true, true);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetThroughput_CommunicationLinkResourceSpecification(
			PCMRandomVariable newThroughput_CommunicationLinkResourceSpecification, NotificationChain msgs) {
		msgs = eDynamicInverseAdd((InternalEObject) newThroughput_CommunicationLinkResourceSpecification,
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setThroughput_CommunicationLinkResourceSpecification(
			PCMRandomVariable newThroughput_CommunicationLinkResourceSpecification) {
		eDynamicSet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
				newThroughput_CommunicationLinkResourceSpecification);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getConnectionType() {
		return (String) eDynamicGet(
				ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE, true,
				true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setConnectionType(String newConnectionType) {
		eDynamicSet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE,
				newConnectionType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSsid() {
		return (String) eDynamicGet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID, true, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSsid(String newSsid) {
		eDynamicSet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID, newSsid);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getBssid() {
		return (String) eDynamicGet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID, true, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBssid(String newBssid) {
		eDynamicSet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID, newBssid);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getOperator() {
		return (String) eDynamicGet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR, true, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperator(String newOperator) {
		eDynamicSet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR, newOperator);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getProtocol() {
		return (String) eDynamicGet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL, true, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProtocol(String newProtocol) {
		eDynamicSet(ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL,
				ResourceenvironmentPackage.Literals.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL, newProtocol);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetLinkingResource_CommunicationLinkResourceSpecification((LinkingResource) otherEnd, msgs);
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			PCMRandomVariable latency_CommunicationLinkResourceSpecification = getLatency_CommunicationLinkResourceSpecification();
			if (latency_CommunicationLinkResourceSpecification != null)
				msgs = ((InternalEObject) latency_CommunicationLinkResourceSpecification).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE
								- ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
						null, msgs);
			return basicSetLatency_CommunicationLinkResourceSpecification((PCMRandomVariable) otherEnd, msgs);
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			PCMRandomVariable throughput_CommunicationLinkResourceSpecification = getThroughput_CommunicationLinkResourceSpecification();
			if (throughput_CommunicationLinkResourceSpecification != null)
				msgs = ((InternalEObject) throughput_CommunicationLinkResourceSpecification).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE
								- ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION,
						null, msgs);
			return basicSetThroughput_CommunicationLinkResourceSpecification((PCMRandomVariable) otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return basicSetLinkingResource_CommunicationLinkResourceSpecification(null, msgs);
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return basicSetLatency_CommunicationLinkResourceSpecification(null, msgs);
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return basicSetThroughput_CommunicationLinkResourceSpecification(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return eInternalContainer().eInverseRemove(this,
					ResourceenvironmentPackage.LINKING_RESOURCE__COMMUNICATION_LINK_RESOURCE_SPECIFICATIONS_LINKING_RESOURCE,
					LinkingResource.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return getLinkingResource_CommunicationLinkResourceSpecification();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY:
			return getFailureProbability();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			if (resolve)
				return getCommunicationLinkResourceType_CommunicationLinkResourceSpecification();
			return basicGetCommunicationLinkResourceType_CommunicationLinkResourceSpecification();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return getLatency_CommunicationLinkResourceSpecification();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return getThroughput_CommunicationLinkResourceSpecification();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE:
			return getConnectionType();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID:
			return getSsid();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID:
			return getBssid();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR:
			return getOperator();
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL:
			return getProtocol();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setLinkingResource_CommunicationLinkResourceSpecification((LinkingResource) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY:
			setFailureProbability((Double) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setCommunicationLinkResourceType_CommunicationLinkResourceSpecification(
					(CommunicationLinkResourceType) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setLatency_CommunicationLinkResourceSpecification((PCMRandomVariable) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setThroughput_CommunicationLinkResourceSpecification((PCMRandomVariable) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE:
			setConnectionType((String) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID:
			setSsid((String) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID:
			setBssid((String) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR:
			setOperator((String) newValue);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL:
			setProtocol((String) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setLinkingResource_CommunicationLinkResourceSpecification((LinkingResource) null);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY:
			setFailureProbability(FAILURE_PROBABILITY_EDEFAULT);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setCommunicationLinkResourceType_CommunicationLinkResourceSpecification(
					(CommunicationLinkResourceType) null);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setLatency_CommunicationLinkResourceSpecification((PCMRandomVariable) null);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			setThroughput_CommunicationLinkResourceSpecification((PCMRandomVariable) null);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE:
			setConnectionType(CONNECTION_TYPE_EDEFAULT);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID:
			setSsid(SSID_EDEFAULT);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID:
			setBssid(BSSID_EDEFAULT);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR:
			setOperator(OPERATOR_EDEFAULT);
			return;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL:
			setProtocol(PROTOCOL_EDEFAULT);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LINKING_RESOURCE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return getLinkingResource_CommunicationLinkResourceSpecification() != null;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__FAILURE_PROBABILITY:
			return getFailureProbability() != FAILURE_PROBABILITY_EDEFAULT;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__COMMUNICATION_LINK_RESOURCE_TYPE_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return basicGetCommunicationLinkResourceType_CommunicationLinkResourceSpecification() != null;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__LATENCY_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return getLatency_CommunicationLinkResourceSpecification() != null;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__THROUGHPUT_COMMUNICATION_LINK_RESOURCE_SPECIFICATION:
			return getThroughput_CommunicationLinkResourceSpecification() != null;
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__CONNECTION_TYPE:
			return CONNECTION_TYPE_EDEFAULT == null ? getConnectionType() != null
					: !CONNECTION_TYPE_EDEFAULT.equals(getConnectionType());
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__SSID:
			return SSID_EDEFAULT == null ? getSsid() != null : !SSID_EDEFAULT.equals(getSsid());
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__BSSID:
			return BSSID_EDEFAULT == null ? getBssid() != null : !BSSID_EDEFAULT.equals(getBssid());
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__OPERATOR:
			return OPERATOR_EDEFAULT == null ? getOperator() != null : !OPERATOR_EDEFAULT.equals(getOperator());
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION__PROTOCOL:
			return PROTOCOL_EDEFAULT == null ? getProtocol() != null : !PROTOCOL_EDEFAULT.equals(getProtocol());
		}
		return super.eIsSet(featureID);
	}

} // CommunicationLinkResourceSpecificationImpl
