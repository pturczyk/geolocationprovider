package com.vaadin.pturczyk.geolocationprovider.gwt.client;

import com.vaadin.shared.communication.ClientRpc;

public interface GeoLocationProviderClientRpc extends ClientRpc {
	public void requestGeoLocation();
}
