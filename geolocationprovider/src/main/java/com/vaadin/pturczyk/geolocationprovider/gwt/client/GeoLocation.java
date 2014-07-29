package com.vaadin.pturczyk.geolocationprovider.gwt.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GeoLocation implements Serializable {
	private final double latitude;
	private final double longitude;
	private final double accuracy;
	
	public GeoLocation(double latitude, double longitude, double accuracy) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.accuracy = accuracy;
	}

	/**
	 * @return latitude
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return longitude
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return accuracy in meters
	 */
	public double getAccuracy() {
		return accuracy;
	}
	
}
