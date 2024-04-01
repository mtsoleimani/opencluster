package io.taranis.opencluster.server.http.routes;

public interface HttpRoutesServices extends HttpRoutesBase {

	public static final String ROUTE_DISCOVER_ME = ROUTE_BASE + "/v1/service/whatismyip";
	
	public static final String ROUTE_REGISTER_SERVICE = ROUTE_BASE + "/v1/service/register";
	
	public static final String ROUTE_DEREGISTER_SERVICE = ROUTE_BASE + "/v1/service/deregister";
	
	public static final String ROUTE_FETCH_SERVICE_DETAILS = ROUTE_BASE + "/v1/service/details";
	
	public static final String ROUTE_FETCH_SERVICES_LIST = ROUTE_BASE + "/v1/service/list";
	
	public static final String ROUTE_FETCH_SERVICES_DISCOVERY = ROUTE_BASE + "/v1/service/discovery";

}

