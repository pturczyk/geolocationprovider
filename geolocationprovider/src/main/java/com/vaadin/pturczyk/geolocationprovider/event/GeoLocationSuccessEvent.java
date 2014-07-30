package com.vaadin.pturczyk.geolocationprovider.event;

import java.util.EventObject;

import com.vaadin.pturczyk.geolocationprovider.GeoLocation;

/**
 * Represents a GeoLocation acquisition success event.
 * 
 * <p>
 * Invoke {@link #getLocation()} to obtain acquired GeoLocation
 * </p>
 */
public class GeoLocationSuccessEvent extends EventObject {

	private static final long serialVersionUID = 3198405261624945452L;

	private final GeoLocation location;

	public GeoLocationSuccessEvent(Object source, GeoLocation location) {
		super(source);
		this.location = location;
	}

	/**
	 * @return geographical location
	 */
	public GeoLocation getLocation() {
		return location;
	}
}