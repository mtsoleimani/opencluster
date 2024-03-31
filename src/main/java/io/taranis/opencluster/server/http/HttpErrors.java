package io.taranis.opencluster.server.http;

public class HttpErrors {
	
	private HttpErrors() {
		
	}


	public static final int OK_NO_CONTENT_OK = 204;
	
	public static final int OK_CODE = 200;

	public static final int ERROR_BAD_REQUEST_CODE = 400;

	public static final int ERROR_UNAUTHORIZED_CODE = 401;
	
	public static final int ERROR_NOT_ALLOWED_CODE = 403;

	public static final int ERROR_NOTFOUND_CODE = 404;

	public static final int ERROR_TOO_MANY_REQUESTS_CODE = 429;

	public static final int ERROR_INTERNAL_CODE = 500;
	
	public static final int ERROR_SERVICE_UNAVAILABLE_CODE = 503;
	
	public static final int ERROR_CONFLICT_CODE = 409;
	
	public static final int ERROR_NOT_ACCEPTABLE_CODE = 406;

	public static final int ERROR_FORCE_UPDATE_CODE = 426;
	
	
	public static String errorMessage(int type) {
	
		switch (type) {
		case OK_CODE:
			return "ok";
			
		case ERROR_BAD_REQUEST_CODE:
			return "bad request";
			
		case ERROR_UNAUTHORIZED_CODE:
			return "unauthorized";
			
		case ERROR_NOT_ALLOWED_CODE:
			return "not allowed";
			
		case ERROR_NOTFOUND_CODE:
			return "not found";
			
		case ERROR_TOO_MANY_REQUESTS_CODE:
			return "too many requests";

		case ERROR_INTERNAL_CODE:
			return "internal error";
			
		case ERROR_CONFLICT_CODE:
			return "conflict";
			
		case ERROR_SERVICE_UNAVAILABLE_CODE:
			return "Service Unavailable";
		
		default:
			return "";
		}
	}
}
