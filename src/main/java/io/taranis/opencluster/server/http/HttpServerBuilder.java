package io.taranis.opencluster.server.http;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.configs.HttpConf;
import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class HttpServerBuilder {
	
	private final Logger logger = LoggerFactory.getLogger(HttpServerBuilder.class);
	
	
	private int threads;
	
	private List<VertxCallable> routes;
	
	private HttpConf httpConf;
	
	private TcpOptionsConf tcpOptionsConf;
	
	
	private Vertx vertx;
	
	private HttpServerBuilder(Vertx vertx, int threads) {
		this.vertx = vertx;
		this.threads = threads;
		this.routes = new ArrayList<>();
	}
	
	public static HttpServerBuilder newBuilder(Vertx vertx, int threads) {
		return new HttpServerBuilder(vertx, threads);
	}
	
	public HttpServerBuilder withHttpConfigs(HttpConf httpConf) {
		this.httpConf = httpConf;
		return this;
	}
	
	public HttpServerBuilder withTcpOptionsConfigs(TcpOptionsConf tcpOptionsConf) {
		this.tcpOptionsConf = tcpOptionsConf;
		return this;
	}
	
	public HttpServerBuilder addRoute(VertxCallable callable) {
		routes.add(callable);
		return this;
	}
	
	List<HttpServer> servers;
	
	public HttpServerBuilder start() {
		
		servers = new ArrayList<>(threads);
		
		IntStream.range(0, threads).forEach(i -> {
			servers.add(startHttpServer());
		});
		
		return this;
	}
	
	public HttpServerBuilder close() {
		
		servers.stream().forEach(server -> {
			try {
				server.close();
			} catch (Exception e) {

			}
		});
		
		return this;
	}
	
	private HttpServer startHttpServer() {
		
		HttpServer server = new HttpServer(vertx, httpConf, tcpOptionsConf, routes);
		server.start(new Promise<Void>() {

			@Override
			public boolean tryFail(String arg0) {
				return false;
			}

			@Override
			public boolean tryFail(Throwable arg0) {
				return false;
			}

			@Override
			public boolean tryComplete(Void arg0) {
				return false;
			}

			@Override
			public boolean tryComplete() {
				return false;
			}

			@Override
			public void handle(AsyncResult<Void> arg0) {
				/* do nothing */
			}

			@Override
			public void fail(String cause) {
				logger.error(cause);
				System.exit(0);

			}

			@Override
			public void fail(Throwable cause) {
				logger.error(cause.getMessage(), cause);
				System.exit(0);
			}

			@Override
			public void complete(Void arg0) {
				/* do nothing */
			}

			@Override
			public void complete() {
				/* do nothing */
			}

			@Override
			public Future<Void> future() {
				return null;
			}
		});
		
		return server;
	}

}
