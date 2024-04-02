package io.taranis.opencluster.server.http;

import io.taranis.opencluster.exception.*;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class HttpPostman {

	protected static final String HTTP_HEADER_JSON = "application/json; charset=utf-8";
	protected static final String HTTP_HEADER_CONTENT_TYPE = "content-type";
	
	protected static final String HTTP_HEADER_X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
	protected static final String NOSNIFF = "nosniff";
	
	protected static final String HTTP_HEADER_X_FRAME_OPTIONS = "X-Frame-Options";
	protected static final String DENY = "deny";
	
	protected static final String DEFAULT_SRC_NONE = "default-src 'none'";
	protected static final String HTTP_HEADER_CONTENT_SECURITY_POLICY = "Content-Security-Policy";
	
	protected static final String DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN = "*";
	protected static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	
	protected static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	protected static final String DEFAULT_ACCESS_CONTROL_ALLOW_METHODS = "POST, GET, OPTIONS, PUT, DELETE";
	
	protected static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	protected static final String DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS = "*";
	
	protected static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
	protected static final String DEFAULT_ACCESS_CONTROL_ALLOW_CREDENTIALS = "true";
	
	protected static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	protected static final String DEFAULT_ACCESS_CONTROL_MAX_AGE = "3600";

	
//	public static void handleException(RoutingContext routingContext, Exception e) {
//		int code = ExceptionCodeMapper.code(e);
//		String errorMessage = StringUtils.isNullOrEmpty(e.getMessage()) ? "" : e.getMessage();
//		
//		if(code == ExceptionCodeMapper.BASE_CODE)
//			errorMessage = "unknown error";
//		
//		JsonObject json = new JsonObject().put("errorMessage", errorMessage).put("errorCode", ExceptionCodeMapper.code(e));
//		sendJson(routingContext, ExceptionCodeMapper.statusCode(e), json);
//	}
	
	public static void handleException(RoutingContext routingContext, Exception e) {
		
		if(e instanceof UnauthorizedException) {
			send(routingContext, HttpErrors.ERROR_UNAUTHORIZED_CODE);
			return;
		}
		
		if(e instanceof DuplicatedRecordException) {
			send(routingContext, HttpErrors.ERROR_CONFLICT_CODE);
			return;
		}
		
		
		if(e instanceof InvalidMessageException) {
			send(routingContext, HttpErrors.ERROR_BAD_REQUEST_CODE);
			return;
		}
		
		if(e instanceof NotAllowedException) {
			send(routingContext, HttpErrors.ERROR_NOT_ALLOWED_CODE);
			return;
		}
		
		if(e instanceof NotFoundException) {
			send(routingContext, HttpErrors.ERROR_NOTFOUND_CODE);
			return;
		}
		
		if(e instanceof TooManyRequestsException) {
			send(routingContext, HttpErrors.ERROR_TOO_MANY_REQUESTS_CODE);
			return;
		}
		
		send(routingContext, HttpErrors.ERROR_INTERNAL_CODE);
	}
	
	
	private static HttpServerResponse addExtraHeaders(HttpServerResponse httpServerResponse) {
		return httpServerResponse
				
//				.putHeader(HTTP_HEADER_X_CONTENT_TYPE_OPTIONS, NOSNIFF)
//				.putHeader(HTTP_HEADER_X_FRAME_OPTIONS, DENY)
//				.putHeader(HTTP_HEADER_CONTENT_SECURITY_POLICY, DEFAULT_SRC_NONE)
				
//	            .putHeader("Access-Control-Allow-Origin", "*")
	            .putHeader("access-control-allow-origin", "*")
	            .putHeader("Access-Control-Allow-Methods", "OPTIONS, POST, GET, DELETE, PUT")
	            .putHeader("Access-Control-Max-Age", "3600")
	            .putHeader("Access-Control-Allow-Credentials", "true")
	            .putHeader("Access-Control-Allow-Headers", "Content-Type");
	            
//				.putHeader(ACCESS_CONTROL_ALLOW_ORIGIN, DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN)
//				.putHeader(ACCESS_CONTROL_ALLOW_METHODS, DEFAULT_ACCESS_CONTROL_ALLOW_METHODS)
//				.putHeader(ACCESS_CONTROL_ALLOW_HEADERS, DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS)
//				.putHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, DEFAULT_ACCESS_CONTROL_ALLOW_CREDENTIALS)
//				.putHeader(ACCESS_CONTROL_MAX_AGE, DEFAULT_ACCESS_CONTROL_MAX_AGE);
	}
	
	
	public static void sendOK(RoutingContext routingContext) {
		send(routingContext, HttpErrors.OK_NO_CONTENT_OK);
	}
	
	public static void send(RoutingContext routingContext, int statusCode) {
		routingContext.response().putHeader("access-control-allow-origin", "*")
		.putHeader("Access-Control-Allow-Methods", "OPTIONS, POST, GET, DELETE, PUT")
         .putHeader("Access-Control-Max-Age", "3600")
         .putHeader("Access-Control-Allow-Credentials", "true")
         .putHeader("Access-Control-Allow-Headers", "Content-Type")
//		addExtraHeaders(routingContext.response())
		.setStatusCode(statusCode).end();
	}

	public static void sendJson(RoutingContext routingContext, int statusCode, JsonObject json) {
		sendJson(routingContext, statusCode, json.encodePrettily());
	}
	
	public static void sendJson(RoutingContext routingContext, JsonObject json) {
		sendJson(routingContext, HttpErrors.OK_CODE, json.encodePrettily());
	}
	
	
	public static void sendJson(RoutingContext routingContext, int statusCode, String json) {
		addExtraHeaders(routingContext.response().setStatusCode(statusCode))
		.putHeader(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_JSON)
		.end(json);
	}
	
	public static void sendJson(RoutingContext routingContext, String json) {
		
		routingContext.response().putHeader("Access-Control-Allow-Origin", "*")
		.putHeader("Access-Control-Allow-Methods", "OPTIONS, POST, GET, DELETE, PUT")
        .putHeader("Access-Control-Max-Age", "3600")
        .putHeader("Access-Control-Allow-Credentials", "true")
        .putHeader("Access-Control-Allow-Headers", "Content-Type")
        .putHeader(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_JSON)
        .setStatusCode(HttpErrors.OK_CODE)
		.end(json);
        
//		addExtraHeaders(routingContext.response().setStatusCode(HttpErrors.OK_CODE))
//		.putHeader(HTTP_HEADER_CONTENT_TYPE, HTTP_HEADER_JSON)
//		.end(json);
	}
	
}
