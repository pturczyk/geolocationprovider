package com.vaadin.pturczyk.geolocationprovider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.vaadin.pturczyk.geolocationprovider.event.GeoLocationEventListener;
import com.vaadin.pturczyk.geolocationprovider.event.GeoLocationFailureEvent;
import com.vaadin.pturczyk.geolocationprovider.event.GeoLocationSuccessEvent;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationProviderClientRpc;
import com.vaadin.pturczyk.geolocationprovider.gwt.client.GeoLocationProviderServerRpc;
import com.vaadin.shared.communication.ClientRpc;
import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.ui.UI;

public class GeoLocationProviderTest {

	private static final GeoLocationError GEOLOCATION_ERROR = GeoLocationError.POSITION_UNAVAILABLE;
	private static final double LATITUDE = 53.4167;
	private static final double LONGITUDE = 14.5833;
	private static final double ACCURACY = 10.0;
	private static final GeoLocation GEOLOCATION = new GeoLocation(LATITUDE, LONGITUDE, ACCURACY);

	@Test
	public void shouldCallSuccessOnRegisteredListener() {
		// given
		GeoLocationProvider geoLocationProvider = new GeoLocationProviderWithRpcClientStub(true);
		GeoLocationEventListener geoLocationListenerMock = mock(GeoLocationEventListener.class);
		geoLocationProvider.addGeoLocationEventListener(geoLocationListenerMock);

		UI.setCurrent(mock(UI.class));

		// when
		geoLocationProvider.requestGeoLocation();

		// then
		ArgumentCaptor<GeoLocationSuccessEvent> eventCaptor = ArgumentCaptor.forClass(GeoLocationSuccessEvent.class);
		verify(geoLocationListenerMock).onSuccess(eventCaptor.capture());
		assertEquals(eventCaptor.getValue().getLocation(), GEOLOCATION);
	}

	@Test
	public void shouldCallFailureOnRegisteredListener() {
		// given
		GeoLocationProvider geoLocationProvider = new GeoLocationProviderWithRpcClientStub(false);
		GeoLocationEventListener geoLocationListenerMock = mock(GeoLocationEventListener.class);
		geoLocationProvider.addGeoLocationEventListener(geoLocationListenerMock);

		UI.setCurrent(mock(UI.class));

		// when
		geoLocationProvider.requestGeoLocation();

		// then
		ArgumentCaptor<GeoLocationFailureEvent> eventCaptor = ArgumentCaptor.forClass(GeoLocationFailureEvent.class);
		verify(geoLocationListenerMock).onFailure(eventCaptor.capture());
		assertEquals(eventCaptor.getValue().getError(), GEOLOCATION_ERROR);
	}

	@Test
	public void shouldNotCallSuccessNorFailureForUnregisteredListeners() {
		// given
		GeoLocationProvider geoLocationProvider = new GeoLocationProviderWithRpcClientStub(true);
		GeoLocationEventListener geoLocationListenerMock = mock(GeoLocationEventListener.class);

		UI.setCurrent(mock(UI.class));

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
		@SuppressWarnings("unchecked")
		protected <T extends ClientRpc> T getRpcProxy(java.lang.Class<T> rpcInterface) {
			final GeoLocationProviderClientRpc geoClientRpcMock = mock(GeoLocationProviderClientRpc.class);
			Mockito.doAnswer(new Answer<Void>() {
				@Override
				public Void answer(InvocationOnMock invocation) throws Throwable {
					if (success) {
						serverRpc.onSuccess(LATITUDE, LONGITUDE, ACCURACY);
					} else {
						serverRpc.onFailure(GeoLocationError.POSITION_UNAVAILABLE.getErrorId());
					}
					return null;
				}
			}).when(geoClientRpcMock).requestGeoLocation();
			return (T) geoClientRpcMock;
		}
	}
}
