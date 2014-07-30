package com.vaadin.pturczyk.geolocationprovider;

import static java.lang.Double.doubleToLongBits;

import java.io.Serializable;

/**
 * Geographical location holder. Immutable class.
 */
public class GeoLocation implements Serializable {
	private static final long serialVersionUID = 7343991328790716903L;

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
	 * @return latitude of the geo location
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * @return longitude of the geo location
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * @return location accuracy in meters
	 */
	public double getAccuracy() {
		return accuracy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = doubleToLongBits(accuracy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		if (this == obj) { 
			result = true;
		} 
		
		if (obj != null && getClass() == obj.getClass()) {
			GeoLocation other = (GeoLocation) obj;
			
			result = doubleToLongBits(accuracy) == doubleToLongBits(other.accuracy)
					&& doubleToLongBits(latitude) == doubleToLongBits(other.latitude)
					&& doubleToLongBits(longitude) == doubleToLongBits(other.longitude);
		}

		return result;
	}

	@Override
	public String toString() {
		return "GeoLocation [latitude=" + latitude + ", longitude=" + longitude + ", accuracy=" + accuracy + "]";
	}

}
