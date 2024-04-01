package io.taranis.opencluster.server.http.handlers;

import io.vertx.ext.web.RoutingContext;

public interface HttpServiceHandler {

	public void handleRegisterServiceRequest(RoutingContext routingContext);

	public void handleUnregisterServiceRequest(RoutingContext routingContext);

	public void handleGetServiceByIdRequest(RoutingContext routingContext);

	public void handleGetServicesRequest(RoutingContext routingContext);
	
	public void handleDiscoverMeRequest(RoutingContext routingContext);
	
	public void handleServiceDiscoveryRequest(RoutingContext routingContext);
	
	public void handlePingServiceRequest(RoutingContext routingContext);
}
