package com.vaadin.pturczyk.geolocationprovider.gwt.client;

import com.vaadin.shared.communication.ServerRpc;

/**
 * GeoLocation server RPC interface
 */
public interface GeoLocationProviderServerRpc extends ServerRpc {
	/**
	 * Invoked on successful geolocation acquisition
	 * 
	 * @param latitude
	 * @param longitude
	 * @param accuracy
	 */
	void onSuccess(double latitude, double longitude, double accuracy);

	/**
	 * Invoked on geolocation acquisition failure
	 * 
	 * @param errorId
	 */
	void onFailure(int errorId);
}
