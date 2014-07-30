package com.vaadin.pturczyk.geolocationprovider;

import java.lang.reflect.Method;

import com.vaadin.pturczyk.geolocationprovider.event.GeoLocationEventListener;
import com.vaadin.pturczyk.geolocationprovider.event.GeoLocationFailureEvent;
import com.vaadin.pturczyk.geolocationprovider.event.GeoLocationSuccessEvent;
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
 * 
 * <pre>
 * {@code
 *   // Usage 
 *   
 *   // create geolocation provider
 *   GeoLocationProvider provider = new GeoLocationProvider();
 * 
 *   // register listener
 *   provider.addGeoLocationEventListener(new GeoLocationEventListener() {...});
 * 
 *   // request location
 *   provider.requestGeoLocation();
 * }
 * </pre>
 */
public class GeoLocationProvider extends AbstractExtension {

	private static final long serialVersionUID = 4124855957131429090L;

	public GeoLocationProvider() {
		registerRpc(new GeoLocationProviderServerRpc() {

			private static final long serialVersionUID = -8896383685859888835L;

			@Override
			public void onSuccess(double latitude, double longitude, double accuracy) {
				fireEvent(new GeoLocationSuccessEvent(GeoLocationProvider.this, new GeoLocation(latitude, longitude,
						accuracy)));
			}

			@Override
			public void onFailure(int errorId) {
				fireEvent(new GeoLocationFailureEvent(GeoLocationProvider.this, GeoLocationError.fromErrorId(errorId)));
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

	private Method getMethod(String methodName, Class<?>[] parameters) {
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

}
