package com.vaadin.pturczyk.geolocationprovider.gwt.client;

import static com.google.gwt.geolocation.client.PositionError.UNKNOWN_ERROR;

import com.google.gwt.core.client.Callback;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.Position.Coordinates;
import com.google.gwt.geolocation.client.PositionError;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.pturczyk.geolocationprovider.GeoLocationProvider;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(GeoLocationProvider.class)
public class GeoLocationProviderConnector extends AbstractExtensionConnector {

	private GeoLocationProviderServerRpc serverRpcProxy;

	public GeoLocationProviderConnector() {
		initServerRpcProxy();
		initClientRpc();
	}

	private void initServerRpcProxy() {
		this.serverRpcProxy = RpcProxy.create(GeoLocationProviderServerRpc.class, this);
	}

	private void initClientRpc() {
		registerRpc(GeoLocationProviderClientRpc.class, new GeoLocationProviderClientRpc() {

			@Override
			public void requestGeoLocation() {
				if (Geolocation.isSupported()) {
					Geolocation.getIfSupported().getCurrentPosition(new Callback<Position, PositionError>() {

						@Override
						public void onSuccess(Position result) {
							Coordinates coordinates = result.getCoordinates();
							serverRpcProxy.onSuccess(
									coordinates.getLatitude(), 
									coordinates.getLongitude(),
									coordinates.getAccuracy());
						}

						@Override
						public void onFailure(PositionError reason) {
							serverRpcProxy.onFailure(reason.getCode());
						}
					});
				} else {
					// let the server know that geo location will not be retrieved
					serverRpcProxy.onFailure(UNKNOWN_ERROR);
				}
			}
		});
	}

	@Override
	protected void extend(ServerConnector target) {
		// nothing to do here
	}

}
