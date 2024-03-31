package io.taranis.opencluster.common.utils;

import io.taranis.opencluster.exception.Exceptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class Utils {
	
	
	private Utils() {
		
	}

	
	public static JsonObject getJson(RoutingContext routingContext) throws NullPointerException {
		JsonObject json = routingContext.body().asJsonObject();
		Exceptions.throwIf((json == null), NullPointerException::new);
		return json;
	}
	
	
	public static String getRemoteHost(RoutingContext routingContext) {
		return routingContext.request().remoteAddress().host();
	}
}
