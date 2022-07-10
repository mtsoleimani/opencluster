package io.taranis.opencluster.server;

import io.taranis.opencluster.MessageHandler;
import io.vertx.core.Vertx;

public class BasicServerBuilder {

	protected int port;
	
	protected String host;
	
	protected Vertx vertx;
	
	protected MessageHandler messageHandler;
	
	protected TcpOptionsConf tcpOptionsConf;
	
	protected boolean tlsEnabled = false;
	
	protected String pemCert;
	
	protected String pemKey;
	
	
}
