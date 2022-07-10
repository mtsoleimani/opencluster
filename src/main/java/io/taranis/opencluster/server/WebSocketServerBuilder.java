package io.taranis.opencluster.server;

import io.taranis.opencluster.MessageHandler;
import io.vertx.core.Vertx;

public class WebSocketServerBuilder extends BasicServerBuilder {
	
	private WebSocketServerBuilder() {
		super();
	}
	
	public static WebSocketServerBuilder newInstance() {
		return new WebSocketServerBuilder();
	}
	
	
	public WebSocketServerBuilder withHost(String host) {
		this.host = host;
		return this;
	}
	
	public WebSocketServerBuilder withPort(int port) {
		this.port = port;
		return this;
	}
	
	public WebSocketServerBuilder withVertx(Vertx vertx) {
		this.vertx = vertx;
		return this;
	}

	public WebSocketServerBuilder withMessageHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		return this;
	}
	
	public WebSocketServerBuilder withTcpOptionsConf(TcpOptionsConf tcpOptionsConf) {
		this.tcpOptionsConf = tcpOptionsConf;
		return this;
	}
	
	public WebSocketServerBuilder withTls(String pemCert, String pemKey) {
		this.pemCert = pemCert;
		this.pemKey = pemKey;
		return this;
	}
	
	public WebSocketServer build() {
		return new WebSocketServer(vertx, host, port, tcpOptionsConf, messageHandler);
	}
	
	
	
	
	
}
