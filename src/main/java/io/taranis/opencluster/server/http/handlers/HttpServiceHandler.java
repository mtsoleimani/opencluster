package io.taranis.opencluster.server.http.handlers;

import io.vertx.ext.web.RoutingContext;

public interface HttpServiceHandler {

	public void handleRegisterServiceRequest(RoutingContext routingContext);

	public void handleUnregisterServiceRequest(RoutingContext routingContext);

	public void handleGetServicesByIdRequest(RoutingContext routingContext);

	public void handleGetServicesRequest(RoutingContext routingContext);
	
	public void handleDiscoverMeRequest(RoutingContext routingContext);
}
