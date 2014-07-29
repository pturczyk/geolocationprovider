package com.vaadin.pturczyk.geolocationprovider.gwt.client;

public enum GeoLocationError {
	/** An unknown error occurred. */
	UNKNOWN_ERROR(0),

	/** The user declined access to their position to this application. */
	PERMISSION_DENIED(1),

	/** The browser was unable to locate the user. */
	POSITION_UNAVAILABLE(2),

	/** The browser was unable to locate the user in enough time. */
	TIMEOUT(3);

	private int errorId;

	private GeoLocationError(int errorId) {
		this.errorId = errorId;
	}

	public int getErrorId() {
		return errorId;
	}

	public static GeoLocationError fromErrorId(int errorId) {
		GeoLocationError result = UNKNOWN_ERROR;

		for (GeoLocationError error : values()) {
			if (error.getErrorId() == errorId) {
				result = error;
				break;
			}
		}

		return result;
	}
}
