package io.taranis.opencluster.server.http;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class VertxWebHelper {
	
	private VertxWebHelper() {
		
	}

	
	protected static final String HTTP_HEADER_JSON = "application/json; charset=utf-8";
	protected static final String HTTP_HEADER_TEXT = "text/plain; charset=utf-8";
	protected static final String HTTP_HEADER_HTML = "text/html; charset=utf-8";
	protected static final String HTTP_HEADER_CONTENT_TYPE = "content-type";
	
	protected static final String HTTP_HEADER_X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	protected static final String NOSNIFF‬‬ = "nosniff";
	
	protected static final String HTTP_HEADER_X_FRAME_OPTIONS = "X-Frame-Options";
	protected static final String DENY‬‬ = "deny";
	
	protected static final String HTTP_HEADER_‪CONTENT_SECURITY_POLICY‬‬ = "Content-Security-Policy";
	protected static final String DEFAULT_SRC_NONE‫‪ = "default-src 'none'";
	
	
	public static void send(RoutingContext routingContext, int statusCode, JsonObject json) {
		routingContext.response().setStatusCode(statusCode).putHeader(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_JSON)
		.putHeader(HTTP_HEADER_X_CONTENT_TYPE_OPTIONS, NOSNIFF‬‬)
		.putHeader(HTTP_HEADER_X_FRAME_OPTIONS, DENY‬‬)
		.putHeader(HTTP_HEADER_‪CONTENT_SECURITY_POLICY‬‬, DEFAULT_SRC_NONE‫‪)
		.putHeader("access-control-allow-origin", "*")
        .putHeader("Access-Control-Allow-Methods", "OPTIONS, POST, GET, DELETE, PUT")
        .putHeader("Access-Control-Max-Age", "3600")
        .putHeader("Access-Control-Allow-Credentials", "true")
        .putHeader("Access-Control-Allow-Headers", "Content-Type")
		.end(json.toString());
	}
	
	public static void send(RoutingContext routingContext, int statusCode) {
		routingContext.response().setStatusCode(statusCode)
		.putHeader(HTTP_HEADER_X_CONTENT_TYPE_OPTIONS, NOSNIFF‬‬)
		.putHeader(HTTP_HEADER_X_FRAME_OPTIONS, DENY‬‬)
		.putHeader(HTTP_HEADER_‪CONTENT_SECURITY_POLICY‬‬, DEFAULT_SRC_NONE‫‪)
		.putHeader("access-control-allow-origin", "*")
        .putHeader("Access-Control-Allow-Methods", "OPTIONS, POST, GET, DELETE, PUT")
        .putHeader("Access-Control-Max-Age", "3600")
        .putHeader("Access-Control-Allow-Credentials", "true")
        .putHeader("Access-Control-Allow-Headers", "Content-Type")
		.end();
	}
}
