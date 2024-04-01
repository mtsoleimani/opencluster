package io.taranis.opencluster.server.http.security;

import io.vertx.ext.web.RoutingContext;

public interface IVertxAuthHandler {

	public boolean authenticate(RoutingContext routingContext);
}