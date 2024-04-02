package io.taranis.opencluster.server.http.routes;

public interface HttpRoutesStorage extends HttpRoutesBase {

	public static final String ROUTE_STORAGE_SET = ROUTE_BASE + "/v1/storage/set";
	
	public static final String ROUTE_STORAGE_REMOVE = ROUTE_BASE + "/v1/storage/remove";
	
	public static final String ROUTE_STORAGE_GET = ROUTE_BASE + "/v1/storage/get";
	
	public static final String ROUTE_STORAGE_UPDATE_TTL = ROUTE_BASE + "/v1/storage/ttl";
	

}

