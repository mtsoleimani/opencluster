package io.taranis.opencluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.configs.HttpConf;
import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.taranis.opencluster.server.http.AppHttpMethod;
import io.taranis.opencluster.server.http.HttpServerBuilder;
import io.taranis.opencluster.server.http.VertxCallable;
import io.taranis.opencluster.server.http.handlers.HttpServiceHandler;
import io.taranis.opencluster.server.http.handlers.impl.HttpServiceHandlerImpl;
import io.taranis.opencluster.server.http.routes.HttpRoutesServices;
import io.taranis.opencluster.server.http.security.IVertxAuthHandler;
import io.vertx.core.Vertx;

public class HttpServerLauncher {
	
	private final Logger logger = LoggerFactory.getLogger(HttpServerLauncher.class);
	
	private final Context context;
	
	private HttpServerBuilder httpServerBuilder;
	
	private final IVertxAuthHandler authHandler;
	
	public HttpServerLauncher(Context context, IVertxAuthHandler authHandler) {
		this.context = context;
		this.authHandler = authHandler;
	}
	
	
	public void start(Vertx vertx, HttpConf httpConf, TcpOptionsConf tcpOptionsConf) {
		logger.debug("http server is starting to listen on: {}:{}", httpConf.getHost(), httpConf.getPort());
		httpServerBuilder = HttpServerBuilder
			.newBuilder(vertx, Runtime.getRuntime().availableProcessors()).withHttpConfigs(httpConf)
			.withTcpOptionsConfigs(tcpOptionsConf);

		initServiceHttpRoutes(httpServerBuilder, authHandler);
		httpServerBuilder.start();
	}

	public void shutdown() {
		httpServerBuilder.close();
	}
	
	private HttpServerBuilder initServiceHttpRoutes(final HttpServerBuilder httpServerBuilder, final IVertxAuthHandler authHandler) {
		HttpServiceHandler handler = new HttpServiceHandlerImpl(context.getServiceRegistry());

		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.POST, HttpRoutesServices.ROUTE_REGISTER_SERVICE,
			handler::handleRegisterServiceRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.POST, HttpRoutesServices.ROUTE_DEREGISTER_SERVICE,
				handler::handleUnregisterServiceRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_DISCOVER_ME,
				handler::handleDiscoverMeRequest, null));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_FETCH_SERVICE_DETAILS,
				handler::handleGetServicesByIdRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_FETCH_SERVICES_LIST,
				handler::handleGetServicesRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_FETCH_SERVICES_DISCOVERY,
				handler::handleServiceDiscoveryRequest, authHandler::authenticate));

		return httpServerBuilder;
	}

}
