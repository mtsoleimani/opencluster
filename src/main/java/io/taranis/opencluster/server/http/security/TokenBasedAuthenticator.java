package io.taranis.opencluster.server.http.security;

import io.taranis.opencluster.common.utils.HttpUtils;
import io.vertx.ext.web.RoutingContext;

public class TokenBasedAuthenticator implements IVertxAuthHandler {
	
	private final String token;
	
	public TokenBasedAuthenticator(String token) {
		this.token = token;
	}


	@Override
	public boolean authenticate(RoutingContext routingContext) {
		String authValue = HttpUtils.getBearerAuthorization(routingContext);
		return (authValue != null && authValue.equals(token));
	}
	

}
