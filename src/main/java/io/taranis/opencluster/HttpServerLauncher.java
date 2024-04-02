package io.taranis.opencluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.configs.HttpConf;
import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.taranis.opencluster.core.SystemCoordinator;
import io.taranis.opencluster.server.http.AppHttpMethod;
import io.taranis.opencluster.server.http.HttpServerBuilder;
import io.taranis.opencluster.server.http.VertxCallable;
import io.taranis.opencluster.server.http.handlers.HttpServiceHandler;
import io.taranis.opencluster.server.http.handlers.HttpStorageHandler;
import io.taranis.opencluster.server.http.handlers.impl.HttpServiceHandlerImpl;
import io.taranis.opencluster.server.http.handlers.impl.HttpStorageHandlerImpl;
import io.taranis.opencluster.server.http.routes.HttpRoutesServices;
import io.taranis.opencluster.server.http.routes.HttpRoutesStorage;
import io.taranis.opencluster.server.http.security.IVertxAuthHandler;
import io.vertx.core.Vertx;

public class HttpServerLauncher {
	
	private final Logger logger = LoggerFactory.getLogger(HttpServerLauncher.class);
	
	private final SystemCoordinator systemCoordinator;
	
	private HttpServerBuilder httpServerBuilder;
	
	private final IVertxAuthHandler authHandler;
	
	public HttpServerLauncher(SystemCoordinator systemCoordinator, IVertxAuthHandler authHandler) {
		this.systemCoordinator = systemCoordinator;
		this.authHandler = authHandler;
	}
	
	
	public void start(Vertx vertx, HttpConf httpConf, TcpOptionsConf tcpOptionsConf) {
		logger.debug("http server is starting to listen on: {}:{}", httpConf.getHost(), httpConf.getPort());
		httpServerBuilder = HttpServerBuilder
			.newBuilder(vertx, Runtime.getRuntime().availableProcessors()).withHttpConfigs(httpConf)
			.withTcpOptionsConfigs(tcpOptionsConf);

		initServiceHttpRoutes(httpServerBuilder, authHandler);
		initStorageHttpRoutes(httpServerBuilder, authHandler);
		httpServerBuilder.start();
	}

	public void shutdown() {
		httpServerBuilder.close();
	}
	
	private HttpServerBuilder initServiceHttpRoutes(final HttpServerBuilder httpServerBuilder, final IVertxAuthHandler authHandler) {
		HttpServiceHandler handler = new HttpServiceHandlerImpl(systemCoordinator);

		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.POST, HttpRoutesServices.ROUTE_REGISTER_SERVICE,
			handler::handleRegisterServiceRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.POST, HttpRoutesServices.ROUTE_DEREGISTER_SERVICE,
				handler::handleUnregisterServiceRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_DISCOVER_ME,
				handler::handleDiscoverMeRequest, null));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_FETCH_SERVICE_DETAILS,
				handler::handleGetServiceByIdRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_FETCH_SERVICES_LIST,
				handler::handleGetServicesRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_FETCH_SERVICES_DISCOVERY,
				handler::handleServiceDiscoveryRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesServices.ROUTE_SERVICE_PING,
				handler::handlePingServiceRequest, authHandler::authenticate));

		return httpServerBuilder;
	}
	
	private HttpServerBuilder initStorageHttpRoutes(final HttpServerBuilder httpServerBuilder, final IVertxAuthHandler authHandler) {
		HttpStorageHandler handler = new HttpStorageHandlerImpl(systemCoordinator);
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.POST, HttpRoutesStorage.ROUTE_STORAGE_SET,
				handler::handleSetValueInStorageRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.DELETE, HttpRoutesStorage.ROUTE_STORAGE_REMOVE,
				handler::handleRemoveValueFromStorageRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.PUT, HttpRoutesStorage.ROUTE_STORAGE_UPDATE_TTL,
				handler::handleUpdateTtlInStorageRequest, authHandler::authenticate));
		
		httpServerBuilder.addRoute(new VertxCallable(AppHttpMethod.GET, HttpRoutesStorage.ROUTE_STORAGE_GET,
				handler::handleGetValueFromStorageRequest, authHandler::authenticate));
		
		return httpServerBuilder;
	}

}
