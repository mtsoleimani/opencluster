package io.taranis.opencluster.server.http;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.configs.HttpConf;
import io.taranis.opencluster.common.configs.Metadata;
import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class HttpServer extends AbstractVerticle {

	private Router router;

	private final HttpConf httpConf;
	
	private final TcpOptionsConf tcpOptionsConf;
	
	private final List<VertxCallable> routes;
	
	private final Logger logger = LoggerFactory.getLogger(HttpServer.class);
	
	HttpServer(Vertx vertx, HttpConf httpConf, TcpOptionsConf tcpOptionsConf, List<VertxCallable> routes) {
		this.vertx = vertx;
		this.tcpOptionsConf = tcpOptionsConf;
		this.httpConf = httpConf;
		this.routes = routes;
	}
	
	private io.vertx.core.http.HttpServer server;


	@Override
	public void start(Promise<Void> future) { 
		
		initRoutes(routes);
		
		HttpServerOptions options = new HttpServerOptions();
		if(tcpOptionsConf.isTcpOptionEnabled()) {
			options.setAcceptBacklog(tcpOptionsConf.getTcpBacklog());
			options.setSendBufferSize(tcpOptionsConf.getTcpTxBuffer());
			options.setReceiveBufferSize(tcpOptionsConf.getTcpRxBuffer());
			options.setTcpFastOpen(tcpOptionsConf.isTcpFastOpen());
			options.setTcpNoDelay(tcpOptionsConf.isTcpNoDelay());
			options.setTcpQuickAck(tcpOptionsConf.isTcpQuickAck());
		}
		
		if(httpConf.isTlsEnabled()) {
			
			PemKeyCertOptions keyCertOptions = new PemKeyCertOptions()
				    .setKeyPaths(Arrays.asList(httpConf.getServerKeyPath()))
				    .setCertPaths(Arrays.asList(httpConf.getServerCertPath())
				    );
			
			
			options.setSsl(true).setPemKeyCertOptions(keyCertOptions);
			
			server = vertx.createHttpServer(options).requestHandler(router).listen(httpConf.getPort(), result -> {
				if (result.succeeded()) {
					future.complete();
					logger.info("https server is listening at [{}]", httpConf.getPort());
	
				} else {
					logger.error(result.cause().getLocalizedMessage(), result.cause());
					future.fail(result.cause());
				}
			});
			
		} else {
			server = vertx.createHttpServer(options).requestHandler(router).listen(httpConf.getPort(), result -> {
				if (result.succeeded()) {
					future.complete();
					logger.info("http server is listening at [{}]", + httpConf.getPort());
	
				} else {
					logger.error(result.cause().getLocalizedMessage(), result.cause());
					future.fail(result.cause());
				}
			});
		}
	}
	
	
	public void close() {
		if(server != null) {
			try {
				server.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	private void initCors(final Router router) {
	    final Set<String> allowedHeaders = new HashSet<>();
	    allowedHeaders.add("x-requested-with");
	    allowedHeaders.add("Access-Control-Allow-Origin");
	    allowedHeaders.add("Access-Control-Allow-Methods");
	    allowedHeaders.add("Access-Control-Allow-Headers");
	    allowedHeaders.add("Access-Control-Allow-Credentials");
	    allowedHeaders.add("origin");
	    allowedHeaders.add("Content-Type");
	    allowedHeaders.add("accept");
	    allowedHeaders.add("X-PINGARUNER");
	    allowedHeaders.add("Authorization");
	    
	    final Set<HttpMethod> allowedMethods = new HashSet<>();
	    allowedMethods.add(HttpMethod.GET);
	    allowedMethods.add(HttpMethod.HEAD);
	    allowedMethods.add(HttpMethod.PUT);
	    allowedMethods.add(HttpMethod.PATCH);
	    allowedMethods.add(HttpMethod.POST);
	    allowedMethods.add(HttpMethod.OPTIONS);
	    allowedMethods.add(HttpMethod.DELETE);
	    
	    router.route().handler(CorsHandler.create()
	    		.addOrigin("*")
	            .allowCredentials(true)
	            .allowPrivateNetwork(true)
	            .allowedMethods(allowedMethods)
	            .allowedHeaders(allowedHeaders));
	}
	
	private void initRoutes(List<VertxCallable> routes) {
		
		router = Router.router(vertx);
		initCors(router);
		router.route().handler(BodyHandler.create()
				.setUploadsDirectory(null)
				.setHandleFileUploads(false))
				.failureHandler(failureRoutingContext -> {
					int statusCode = failureRoutingContext.statusCode();
					// Status code will be 500 for the RuntimeException or 403 for the other failure
					HttpServerResponse response = failureRoutingContext.response();
					response.setStatusCode(statusCode).end();
				});
		
		
		for(VertxCallable route : routes) {
			
			switch(route.httpMethod()) {
			
			case POST:
				router.post(route.url())
				.handler(routingContext -> handleAuthentication(route, routingContext))
				.handler(routingContext -> handleHttpRequest(route, routingContext));
				break;
				
			case PUT:
				router.put(route.url())
				.handler(routingContext -> handleAuthentication(route, routingContext))
				.handler(routingContext -> handleHttpRequest(route, routingContext));
				break;
				
			case GET:
				router.get(route.url())
				.handler(routingContext -> handleAuthentication(route, routingContext))
				.handler(routingContext -> handleHttpRequest(route, routingContext));
				break;
				
			case DELETE:
				router.delete(route.url())
				.handler(routingContext -> handleAuthentication(route, routingContext))
				.handler(routingContext -> handleHttpRequest(route, routingContext));
				break;
				
			default:
				break;
			
			}
		}
	}
	
	
	private void handleAuthentication(VertxCallable route, RoutingContext routingContext) {
		
		if(route.authHandler() == null) {
			routingContext.next();
			return;
		}
			
		vertx.executeBlocking(future -> {
			
			if(!route.authHandler().test(routingContext)) {
				future.complete();
				routingContext.response().setStatusCode(401).end();
			}
			
			future.complete();
			routingContext.next();
		}, res -> {
			if(res.failed()) {
				logger.warn(res.cause().getLocalizedMessage(), res.cause());
				routingContext.response().setStatusCode(401).end();
			}
		});
	}
	
	private void handleHttpRequest(VertxCallable route, RoutingContext routingContext) {
		vertx.executeBlocking(future -> {
			routingContext.put(Metadata.ROUTE, route.url());
			route.fucntion().accept(routingContext);
			future.complete();
		}, res -> {
			if(res.failed()) {
				routingContext.response().setStatusCode(500).end();
				logger.warn(res.cause().getLocalizedMessage(), res.cause());
			}
		});
	}
	
}
