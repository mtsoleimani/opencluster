package io.taranis.opencluster.server;


import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.Message;
import io.taranis.opencluster.MessageHandler;
import io.taranis.opencluster.exception.InvalidMessageException;
import io.taranis.opencluster.messages.parser.JsonMessageParser;
import io.taranis.opencluster.server.transport.Transport;
import io.taranis.opencluster.server.transport.WebSocketTransport;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.PemKeyCertOptions;

public class WebSocketServer extends BasicServer implements Handler<ServerWebSocket> {

	private HttpServer httpServer;
	//(Logger.ROOT_LOGGER_NAME)
	private final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketServer.class);

	WebSocketServer(Vertx vertx, String host, int port,
			TcpOptionsConf tcpOptionsConf, MessageHandler messageHandler) {
		super(vertx, host, port, tcpOptionsConf, messageHandler);
	}


	WebSocketServer(Vertx vertx, String host, int port,
			TcpOptionsConf tcpOptionsConf, String pemCert, String pemKey,
			MessageHandler messageHandler) {
		super(vertx, host, port, tcpOptionsConf, pemCert, pemKey, messageHandler);
	}


	@Override
	protected void createServer() {

		HttpServerOptions options = new HttpServerOptions()
				.setPort(port)
				.setHost(host);

		if(tcpOptionsConf != null) {
			options.setAcceptBacklog(tcpOptionsConf.getTcpBacklog());
			options.setSendBufferSize(tcpOptionsConf.getTcpTxBuffer());
			options.setReceiveBufferSize(tcpOptionsConf.getTcpRxBuffer());
			options.setTcpFastOpen(tcpOptionsConf.isTcpFastOpen());
			options.setTcpNoDelay(tcpOptionsConf.isTcpNoDelay());
			options.setTcpQuickAck(tcpOptionsConf.isTcpQuickAck());
		}

		if(serverCertPath != null && serverKeyPath != null) {

			PemKeyCertOptions keyCertOptions = new PemKeyCertOptions()
				    .setKeyPaths(Arrays.asList(serverKeyPath))
				    .setCertPaths(Arrays.asList(serverCertPath)
				    );

			options.setSsl(true).setPemKeyCertOptions(keyCertOptions);
			httpServer = vertx.createHttpServer(options).webSocketHandler(this);

		} else {
			httpServer = vertx.createHttpServer(options).webSocketHandler(this);
		}
	}

	@Override
	public void start(Promise<Void> future) {

		httpServer.listen(port, host, res -> {
			if(res.succeeded()) {
				logger.debug("WebSocket Server: successful listening: " + host + ":" + port ) ;
			} else {
				logger.error("WebSocks Server: error in port binding: \" + port");
				logger.error("exiting WebSocks Server");
				System.exit(0);
			}
		});

		vertx.exceptionHandler(e -> {
			e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
		});
	}

	@Override
	protected void handleMessage(Message message, Transport transport) {

		vertx.executeBlocking(
				promise -> {
					messageHandler.onIncomingMessage(message, transport);
					promise.complete();
				},

				result -> {
					if(result.failed())
						logger.error(result.cause().getLocalizedMessage(), result.cause());
			});

	}


	@Override
	public void handle(ServerWebSocket webSocket) {

		Transport transport = new WebSocketTransport(webSocket);

		webSocket.textMessageHandler(text -> {

			logger.debug("incoming message " + transport.toString() + " from:" + transport.toString());

			try {
				handleMessage(JsonMessageParser.parse(text), transport);
			} catch (InvalidMessageException e) {
				logger.debug("invalid message " + text + " from: " + transport.toString());
				messageHandler.onFailure(transport);
				return;
			}
		});

		webSocket.exceptionHandler( cause -> {
			logger.error(cause.getLocalizedMessage(), cause);
			messageHandler.onFailure(transport);
		});

		webSocket.closeHandler(h -> {
			messageHandler.onFailure(transport);
		});

	}


	@Override
	public void stop(Promise<Void> stopFuture) {
		if (httpServer == null)
			return;

		httpServer.close(result -> {
			if (result.succeeded())
				stopFuture.complete();
			else
				logger.error(result.cause().getLocalizedMessage(), result.cause());
		});
	}

}
