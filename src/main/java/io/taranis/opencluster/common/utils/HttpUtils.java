package io.taranis.opencluster.common.utils;

import io.taranis.opencluster.exception.Exceptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class HttpUtils {
	
	private static final String AUTHORIZATION_HTTP_HEADER = "Authorization";
	private static final String BASIC_AUTHORIZATION_HTTP_HEADER = "Basic";
	private static final String BEARER_AUTHORIZATION_HTTP_HEADER = "Bearer";
	
	private HttpUtils() {
		
	}
	
	public static JsonObject getJson(RoutingContext routingContext) throws NullPointerException {
		JsonObject json = routingContext.body().asJsonObject();
		Exceptions.throwIf((json == null), NullPointerException::new);
		return json;
	}
	
	
	public static String getRemoteHost(RoutingContext routingContext) {
		return routingContext.request().remoteAddress().host();
	}
	
	
	public static String getAuthorization(RoutingContext routingContext, String type) {
    	String autHeader = routingContext.request().getHeader(AUTHORIZATION_HTTP_HEADER);
		if (autHeader == null) 
			return null;
		
		String[] authItems = autHeader.split("\\s+");
		if (authItems.length > 1 && authItems[0] != null
		&& authItems[0].equalsIgnoreCase(type))
			return authItems[1].trim();
		
		return null;
    }
    
    public static String getBearerAuthorization(RoutingContext routingContext) {
		return getAuthorization(routingContext, BEARER_AUTHORIZATION_HTTP_HEADER);
    }
    
    public static String getBasicAuthorization(RoutingContext routingContext) {
		return getAuthorization(routingContext, BASIC_AUTHORIZATION_HTTP_HEADER);
    }
}
