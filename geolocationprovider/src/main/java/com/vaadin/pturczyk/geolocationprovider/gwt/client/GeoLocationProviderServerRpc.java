package com.vaadin.pturczyk.geolocationprovider.gwt.client;

import com.vaadin.shared.communication.ServerRpc;

public interface GeoLocationProviderServerRpc extends ServerRpc {
	/**
	 * Invoked on successful geolocation acquisition
	 *  
	 * @param location
	 */
	void onSuccess(GeoLocation location);
	
	/**
	 * Invoked on geolocation acquisition failure
	 *  
	 * @param error
	 */
	void onFailure(GeoLocationError error);
}
