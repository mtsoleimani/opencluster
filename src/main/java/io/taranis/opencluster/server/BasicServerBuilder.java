package io.taranis.opencluster.server;

import io.taranis.opencluster.common.configs.TcpOptionsConf;
import io.taranis.opencluster.server.messages.MessageHandler;
import io.taranis.opencluster.server.messages.MessageParser;
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
	
	protected MessageParser messageParser;
	
}
