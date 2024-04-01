package io.taranis.opencluster.server.http.security;

import io.vertx.ext.web.RoutingContext;

public class AllowAllAuthenticator implements IVertxAuthHandler {

//	@Override
//	public boolean authenticate(RoutingContext routingContext) {
//		String authValue = HttpUtils.getBearerAuthorization(routingContext);
//		return (authValue != null);
//	}
	
	
	@Override
	public boolean authenticate(RoutingContext routingContext) {
		return true;
	}

}
