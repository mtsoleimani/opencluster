package io.taranis.opencluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.configs.HttpConf;
import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.taranis.opencluster.core.SystemCoordinator;
import io.taranis.opencluster.server.http.security.AllowAllAuthenticator;
import io.taranis.opencluster.server.http.security.IVertxAuthHandler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Launcher {

	Logger logger = LoggerFactory.getLogger(Launcher.class);

	private IVertxAuthHandler authHandler;

	private Vertx vertx;
	
	private final SystemCoordinator systemCoordinator = new SystemCoordinator();
	
	private IVertxAuthHandler getAuthHandler() {
		return new AllowAllAuthenticator();
	}

	private Vertx getVertx() {
		return Vertx.vertx(new VertxOptions());
	}

	private HttpServerLauncher startHttpServer() throws Exception {
		TcpOptionsConf tcpOptionsConf = new TcpOptionsConf();
		if (!tcpOptionsConf.readConfig())
			throw new RuntimeException("tcp options configuration is invalid..!");

		HttpConf httpConf = new HttpConf();
		if (!httpConf.readConfig())
			throw new RuntimeException("http configuration is invalid..!");

		HttpServerLauncher httpServerLauncher = new HttpServerLauncher(systemCoordinator, authHandler);
		httpServerLauncher.start(vertx, httpConf, tcpOptionsConf);
		return httpServerLauncher;
	}
	

	private void init() throws Exception {
		this.vertx = getVertx();
		this.authHandler = getAuthHandler();
	}

	public void start() throws Exception {
		init();
		HttpServerLauncher httpServerLauncher = startHttpServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {

			if (httpServerLauncher != null)
				httpServerLauncher.shutdown();
			
			systemCoordinator.shutdown();
		}));
	}
}
