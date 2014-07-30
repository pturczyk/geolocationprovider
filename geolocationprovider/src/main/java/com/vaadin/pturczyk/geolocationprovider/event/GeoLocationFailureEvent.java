package com.vaadin.pturczyk.geolocationprovider.event;

import java.util.EventObject;

import com.vaadin.pturczyk.geolocationprovider.GeoLocationError;

/**
 * Represents GeoLocation failure event.
 *
 * <p>
 * Use {@link #getError()} to obtain failure reason
 * </p>
 */
public class GeoLocationFailureEvent extends EventObject {

	private static final long serialVersionUID = 6997160154024312291L;

	private final GeoLocationError error;

	public GeoLocationFailureEvent(Object source, GeoLocationError error) {
		super(source);
		this.error = error;
	}

	/**
	 * @return geo location error
	 */
	public GeoLocationError getError() {
		return error;
	}

}