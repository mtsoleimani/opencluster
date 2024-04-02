package io.taranis.opencluster.server.http.handlers;

import io.vertx.ext.web.RoutingContext;

public interface HttpStorageHandler {
	
	public void handleSetValueInStorageRequest(RoutingContext routingContext);

	public void handleGetValueFromStorageRequest(RoutingContext routingContext);

	public void handleRemoveValueFromStorageRequest(RoutingContext routingContext);

	public void handleUpdateTtlInStorageRequest(RoutingContext routingContext);
	
}
