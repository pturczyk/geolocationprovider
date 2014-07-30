package com.vaadin.pturczyk.geolocationprovider.event;

/**
 * Listener for GeoLocation events
 */
public interface GeoLocationEventListener {
	/**
	 * Invoked on successful geolocation retrieval
	 * 
	 * @param successEvent
	 *            holds geolocation event information
	 */
	void onSuccess(GeoLocationSuccessEvent successEvent);

	/**
	 * Invoked on failed geolocation retrieval.
	 * 
	 * @param failureEvent
	 *            holds geolocation failure information
	 */
	void onFailure(GeoLocationFailureEvent failureEvent);
}