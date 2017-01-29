/**
 * Copyright 2005-2009 by SDQ, IPD, University of Karlsruhe, Germany
 */
package org.palladiosimulator.pcm.resourceenvironment.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.core.entity.NamedElement;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.HDDProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

import de.uka.ipd.sdq.identifier.Identifier;

/**
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the
 * call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for
 * each class of the model, starting with the actual class of the object and proceeding up the
 * inheritance hierarchy until a non-null result is returned, which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage
 * @generated
 */
public class ResourceenvironmentSwitch<T> extends Switch<T> {

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "Copyright 2005-2017 by palladiosimulator.org";

	/**
	 * The cached model package
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static ResourceenvironmentPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceenvironmentSwitch() {
		if (modelPackage == null) {
			modelPackage = ResourceenvironmentPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
		case ResourceenvironmentPackage.RESOURCE_ENVIRONMENT: {
			ResourceEnvironment resourceEnvironment = (ResourceEnvironment) theEObject;
			T result = caseResourceEnvironment(resourceEnvironment);
			if (result == null)
				result = caseNamedElement(resourceEnvironment);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ResourceenvironmentPackage.LINKING_RESOURCE: {
			LinkingResource linkingResource = (LinkingResource) theEObject;
			T result = caseLinkingResource(linkingResource);
			if (result == null)
				result = caseEntity(linkingResource);
			if (result == null)
				result = caseIdentifier(linkingResource);
			if (result == null)
				result = caseNamedElement(linkingResource);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ResourceenvironmentPackage.RESOURCE_CONTAINER: {
			ResourceContainer resourceContainer = (ResourceContainer) theEObject;
			T result = caseResourceContainer(resourceContainer);
			if (result == null)
				result = caseEntity(resourceContainer);
			if (result == null)
				result = caseIdentifier(resourceContainer);
			if (result == null)
				result = caseNamedElement(resourceContainer);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ResourceenvironmentPackage.PROCESSING_RESOURCE_SPECIFICATION: {
			ProcessingResourceSpecification processingResourceSpecification = (ProcessingResourceSpecification) theEObject;
			T result = caseProcessingResourceSpecification(processingResourceSpecification);
			if (result == null)
				result = caseIdentifier(processingResourceSpecification);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ResourceenvironmentPackage.COMMUNICATION_LINK_RESOURCE_SPECIFICATION: {
			CommunicationLinkResourceSpecification communicationLinkResourceSpecification = (CommunicationLinkResourceSpecification) theEObject;
			T result = caseCommunicationLinkResourceSpecification(communicationLinkResourceSpecification);
			if (result == null)
				result = caseIdentifier(communicationLinkResourceSpecification);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		case ResourceenvironmentPackage.HDD_PROCESSING_RESOURCE_SPECIFICATION: {
			HDDProcessingResourceSpecification hddProcessingResourceSpecification = (HDDProcessingResourceSpecification) theEObject;
			T result = caseHDDProcessingResourceSpecification(hddProcessingResourceSpecification);
			if (result == null)
				result = caseProcessingResourceSpecification(hddProcessingResourceSpecification);
			if (result == null)
				result = caseIdentifier(hddProcessingResourceSpecification);
			if (result == null)
				result = defaultCase(theEObject);
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Environment</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Environment</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceEnvironment(ResourceEnvironment object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Linking Resource</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Linking Resource</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLinkingResource(LinkingResource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resource Container</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resource Container</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResourceContainer(ResourceContainer object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Processing Resource Specification</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Processing Resource Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProcessingResourceSpecification(ProcessingResourceSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Communication Link Resource Specification</em>'.
	 * <!-- begin-user-doc --> This implementation returns null;
	 * returning a non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Communication Link Resource Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCommunicationLinkResourceSpecification(CommunicationLinkResourceSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>HDD Processing Resource Specification</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a
	 * non-null result will terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>HDD Processing Resource Specification</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHDDProcessingResourceSpecification(HDDProcessingResourceSpecification object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedElement(NamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Identifier</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Identifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIdentifier(Identifier object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntity(Entity object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!--
	 * begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch, but this is the last case anyway. <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} // ResourceenvironmentSwitch
