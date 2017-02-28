package org.iobserve.shared.callback.data;

import java.util.ArrayList;
import java.util.List;

import rocks.fasterxml.jackson.annotation.JsonProperty;
import rocks.inspectit.android.callback.data.MobileDefaultData;

/**
 * Contains multiple monitoring records and the session id. This is the
 * data-type which is sent to the monitoring server.
 * 
 * @author David Monschein
 * @author Robert Heinrich
 *
 */
public class MobileCallbackData {

	/**
	 * Container for monitoring records.
	 */
	@JsonProperty
	private List<MobileDefaultData> childData;

	/**
	 * The Session id.
	 */
	@JsonProperty
	private String sessionId;

	/**
	 * The timestamp when this container was created or reseted.
	 */
	@JsonProperty
	private long creationTimestamp;

	/**
	 * Creates an empty container.
	 */
	public MobileCallbackData() {
		this.creationTimestamp = System.currentTimeMillis();
		this.childData = new ArrayList<MobileDefaultData>();
	}

	/**
	 * Gets the creation timestamp.
	 * 
	 * @return creation timestmap
	 */
	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	/**
	 * Gets the monitoring records in this container.
	 * 
	 * @return monitoring records in this container
	 */
	public List<MobileDefaultData> getChildData() {
		return childData;
	}

	/**
	 * Sets the monitoring records in this container.
	 * 
	 * @param childData
	 *            monitoring records to set
	 */
	public void setChildData(List<MobileDefaultData> childData) {
		this.childData = childData;
	}

	/**
	 * Adds a monitoring record to the container.
	 * 
	 * @param data
	 *            monitoring record to add
	 */
	public void addChildData(MobileDefaultData data) {
		this.childData.add(data);
	}

	/**
	 * Clears the container and removes all current monitoring records.
	 */
	public void clear() {
		creationTimestamp = System.currentTimeMillis();
		this.childData.clear();
	}

	/**
	 * Gets the session id.
	 * 
	 * @return session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the session id.
	 * 
	 * @param sessionId
	 *            session id to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
