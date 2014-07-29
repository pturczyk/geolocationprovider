package com.vaadin.pturczyk.geolocationprovider;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.vaadin.pturczyk.geolocationprovider.GeoLocationProvider.GeoLocationEventListener;
import com.vaadin.pturczyk.geolocationprovider.GeoLocationProvider.GeoLocationFailureEvent;
import com.vaadin.pturczyk.geolocationprovider.GeoLocationProvider.GeoLocationSuccessEvent;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocation;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationError;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationProviderClientRpc;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationProviderServerRpc;
import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.ServerRpc;

public class GeoLocationProviderTest {

	private static final GeoLocationError GEOLOCATION_ERROR = GeoLocationError.POSITION_UNAVAILABLE;
	private static final GeoLocation GEOLOCATION = new GeoLocation(0.0, 0.0, 0.0);
	
	@Test
	public void shouldCallSuccessOnRegisteredListener() {
		// given
		GeoLocationProvider geoLocationProvider = new GeoLocationProviderWithRpcClientStub(true);
		GeoLocationEventListener geoLocationListenerMock = mock(GeoLocationEventListener.class);
		geoLocationProvider.addGeoLocationEventListener(geoLocationListenerMock);

		// when
		geoLocationProvider.requestGeoLocation();

		// then
		verify(geoLocationListenerMock).onSuccess(any(GeoLocationSuccessEvent.class));

	}

	@Test
	public void shouldCallFailureOnRegisteredListener() {
		// given
		GeoLocationProvider geoLocationProvider = new GeoLocationProviderWithRpcClientStub(false);
		GeoLocationEventListener geoLocationListenerMock = mock(GeoLocationEventListener.class);
		geoLocationProvider.addGeoLocationEventListener(geoLocationListenerMock);

		// when
		geoLocationProvider.requestGeoLocation();

		// then
		verify(geoLocationListenerMock).onFailure(any(GeoLocationFailureEvent.class));
	}

	@Test
	public void shouldNotCallSuccessNorFailureForUnregisteredListeners() {
		// given
		GeoLocationProvider geoLocationProvider = new GeoLocationProviderWithRpcClientStub(true);
		GeoLocationEventListener geoLocationListenerMock = mock(GeoLocationEventListener.class);

		geoLocationProvider.addGeoLocationEventListener(geoLocationListenerMock);
		geoLocationProvider.removeGeoLocationEventListener(geoLocationListenerMock);

		// when
		geoLocationProvider.requestGeoLocation();

		// then
		verify(geoLocationListenerMock, never()).onFailure(any(GeoLocationFailureEvent.class));
		verify(geoLocationListenerMock, never()).onSuccess(any(GeoLocationSuccessEvent.class));
	}

	@SuppressWarnings("serial")
	private class GeoLocationProviderWithRpcClientStub extends GeoLocationProvider {

		private boolean success;

		private GeoLocationProviderServerRpc serverRpc;

		private GeoLocationProviderWithRpcClientStub(boolean success) {
			this.success = success;
		}

		@Override
		protected <T extends ServerRpc> void registerRpc(T serverRpc) {
			this.serverRpc = (GeoLocationProviderServerRpc) serverRpc;
		}

		/**
		 * On {@link GeoLocationProviderClientRpc#requestGeoLocation()} call
		 * imitate response coming from client side by invoking either
		 * {@link GeoLocationProviderServerRpc#onSuccess(GeoLocation)} or
		 * {@link GeoLocationProviderServerRpc#onFailure(GeoLocationError)}
		 * depending on the success flag provided in the constructor
		 */
		protected <T extends ClientRpc> T getRpcProxy(java.lang.Class<T> rpcInterface) {
			final GeoLocationProviderClientRpc geoClientRpcMock = mock(GeoLocationProviderClientRpc.class);
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					if (success) {
						serverRpc.onSuccess(GEOLOCATION);
					} else {
						serverRpc.onFailure(GEOLOCATION_ERROR);
					}
					return null;
				}
			}).when(geoClientRpcMock).requestGeoLocation();
			return (T) geoClientRpcMock;
		}
	}
}
