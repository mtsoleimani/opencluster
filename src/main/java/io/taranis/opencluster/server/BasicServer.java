package io.taranis.opencluster.server;

import io.taranis.opencluster.cluster.MessageHandler;
import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.server.transport.Transport;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public abstract class BasicServer extends AbstractVerticle {
	
	protected int port;
	
	protected String host;
	
	protected MessageHandler messageHandler;
	
	protected TcpOptionsConf tcpOptionsConf;
	
	protected String serverKeyPath;
	
	protected String serverCertPath;
	
	
	BasicServer(Vertx vertx, String host, int port, 
			TcpOptionsConf tcpOptionsConf, MessageHandler messageHandler) {
		super();
		this.vertx = vertx;
		this.port = port;
		this.host = host;
		this.messageHandler = messageHandler;
		this.tcpOptionsConf = tcpOptionsConf;
		createServer();
	}
	
	BasicServer(Vertx vertx, String host, int port, 
			TcpOptionsConf tcpOptionsConf, String serverCertPath, String serverKeyPath, 
			MessageHandler messageHandler) {
		super();
		this.vertx = vertx;
		this.port = port;
		this.host = host;
		this.messageHandler = messageHandler;
		this.tcpOptionsConf = tcpOptionsConf;
		this.serverKeyPath = serverKeyPath;
		this.serverCertPath = serverCertPath;
		createServer();
	}
	
	protected abstract void createServer();
	
	protected abstract void handleMessage(Message message, Transport transport);
	
	
}
