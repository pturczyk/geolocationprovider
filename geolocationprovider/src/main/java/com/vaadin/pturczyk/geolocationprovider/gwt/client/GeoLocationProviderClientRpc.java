package com.vaadin.pturczyk.geolocationprovider.gwt.client;

import com.vaadin.shared.communication.ClientRpc;

/**
 * GeoLocation Client Rpc.
 */
public interface GeoLocationProviderClientRpc extends ClientRpc {
	/**
	 * Requests geo location information
	 */
	public void requestGeoLocation();
}
