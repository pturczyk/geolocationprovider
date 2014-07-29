package com.vaadin.pturczyk.geolocationprovider;

import java.lang.reflect.Method;
import java.util.EventObject;

import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocation;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationError;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationProviderClientRpc;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationProviderServerRpc;
import com.vaadin.server.AbstractExtension;

/**
 * Provides geolocation information.
 * 
 * <p>
 * This extension is internally making use of the HTML5 geolocation feature
 * exposed via the GWT GeoLocation API
 * </p>
 */
@SuppressWarnings("serial")
public class GeoLocationProvider extends AbstractExtension {

	public GeoLocationProvider() {
		registerRpc(new GeoLocationProviderServerRpc() {

			@Override
			public void onSuccess(GeoLocation location) {
				fireEvent(new GeoLocationSuccessEvent(GeoLocationProvider.this, location));
			}

			@Override
			public void onFailure(GeoLocationError error) {
				fireEvent(new GeoLocationFailureEvent(GeoLocationProvider.this, error));
			}
		});
	}

	/**
	 * Requests geolocation asynchronously. The result will be returned via the
	 * registered listeners.
	 * 
	 * @param geoLocationListener
	 *            to call with result.
	 * 
	 * @see #addGeoLocationEventListener(GeoLocationEventListener)
	 * @see #removeGeoLocationEventListener(GeoLocationEventListener)
	 */
	public void requestGeoLocation() {
		getRpcProxy(GeoLocationProviderClientRpc.class).requestGeoLocation();
	}

	/**
	 * Registers {@link GeoLocationEventListener} to the GeoLocation events
	 * 
	 * @param geoLocationListener
	 *            to register.
	 */
	public void addGeoLocationEventListener(GeoLocationEventListener geoLocationListener) {
		addListener(GeoLocationSuccessEvent.class, geoLocationListener, getListenerSuccessMethod());
		addListener(GeoLocationFailureEvent.class, geoLocationListener, getListenerFailureMethod());
	}

	private Method getListenerSuccessMethod() {
		return getMethod("onSuccess", new Class[] { GeoLocationSuccessEvent.class });
	}

	private Method getListenerFailureMethod() {
		return getMethod("onFailure", new Class[] { GeoLocationFailureEvent.class });
	}

	private Method getMethod(String methodName, Class[] parameters) {
		final Method requestedMethod;

		try {
			requestedMethod = GeoLocationEventListener.class.getMethod(methodName, parameters);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("should never happen", e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("should never happen", e);
		}

		return requestedMethod;
	}

	/**
	 * Unregisters {@link GeoLocationEventListener} from the GeoLocation
	 * listeners
	 * 
	 * @param geoLocationListener
	 *            to unregister.
	 */
	public void removeGeoLocationEventListener(GeoLocationEventListener geoLocationListener) {
		removeListener(GeoLocationSuccessEvent.class, geoLocationListener);
		removeListener(GeoLocationFailureEvent.class, geoLocationListener);
	}

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

	/**
	 * Represents a GeoLocation acquisition success event.
	 * 
	 * <p>
	 * Invoke {@link #getLocation()} to obtain acquired GeoLocation
	 * </p>
	 */
	public static class GeoLocationSuccessEvent extends EventObject {

		private final GeoLocation location;

		public GeoLocationSuccessEvent(Object source, GeoLocation location) {
			super(source);
			this.location = location;
		}

		public GeoLocation getLocation() {
			return location;
		}
	}

	/**
	 * Represents GeoLocation failure event.
	 *
	 * <p>
	 * Use {@link #getError()} to obtain failure reason
	 * </p>
	 */
	public static class GeoLocationFailureEvent extends EventObject {

		private final GeoLocationError error;

		public GeoLocationFailureEvent(Object source, GeoLocationError error) {
			super(source);
			this.error = error;
		}

		public GeoLocationError getError() {
			return error;
		}

	}

}
