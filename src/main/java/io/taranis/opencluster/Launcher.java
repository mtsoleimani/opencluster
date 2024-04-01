package io.taranis.opencluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.configs.HttpConf;
import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.taranis.opencluster.server.http.security.AllowAllAuthenticator;
import io.taranis.opencluster.server.http.security.IVertxAuthHandler;
import io.taranis.opencluster.service.ServiceRegistry;
import io.taranis.opencluster.service.ServiceRepository;
import io.taranis.opencluster.service.impl.InMemoryServiceRepository;
import io.taranis.opencluster.service.impl.ServiceRegistryImpl;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class Launcher {

	Logger logger = LoggerFactory.getLogger(Launcher.class);

	private IVertxAuthHandler authHandler;

	private Context context;

	private Vertx vertx;
	
	private ServiceRepository serviceRepository;
	
	private ServiceRegistry serviceRegistry;

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

		HttpServerLauncher httpServerLauncher = new HttpServerLauncher(context, authHandler);
		httpServerLauncher.start(vertx, httpConf, tcpOptionsConf);
		return httpServerLauncher;
	}
	
	private ServiceRepository getServiceRepository() {
		if (serviceRepository == null)
			serviceRepository = new InMemoryServiceRepository();
		return serviceRepository;
	}

	private ServiceRegistry getServiceRegistry() {
		if(serviceRegistry == null)
			serviceRegistry = new ServiceRegistryImpl(getServiceRepository());
		return serviceRegistry;
	}

	private void init() throws Exception {
		this.vertx = getVertx();
		this.authHandler = getAuthHandler();
		context = new Context();
		this.context.setServiceRepository(getServiceRepository());
		this.context.setServiceRegistry(getServiceRegistry());
	}

	public void start() throws Exception {
		init();
		HttpServerLauncher httpServerLauncher = startHttpServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {

			if (httpServerLauncher != null)
				httpServerLauncher.shutdown();
		}));
	}
}
