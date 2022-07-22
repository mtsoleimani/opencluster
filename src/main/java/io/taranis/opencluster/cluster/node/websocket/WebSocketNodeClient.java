package io.taranis.opencluster.cluster.node.websocket;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.cluster.node.ConnectionListener;
import io.taranis.opencluster.cluster.node.NodeClient;
import io.taranis.opencluster.cluster.node.NodeListener;
import io.taranis.opencluster.exception.InvalidMessageException;
import io.taranis.opencluster.messages.AckMessage;
import io.taranis.opencluster.messages.HeartBeatMessage;
import io.taranis.opencluster.messages.LeaveMessage;
import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.messages.parser.JsonMessageParser;
import io.taranis.opencluster.server.transport.Transport;
import io.taranis.opencluster.server.transport.TransportType;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;

public class WebSocketNodeClient implements NodeClient, ConnectionListener {

	private final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketNodeClient.class);

	private Vertx vertx;
	
	private WebSocketClient client;
	
	private String host;
	
	private int port;
	
	private NodeListener listener;
	
	private final WebSocketNodeClient instance;
	
	public WebSocketNodeClient(Vertx vertx) {
		this.vertx = vertx;
		instance = this;
	}
	
	public WebSocketNodeClient setVertx(Vertx vertx) {
		this.vertx = vertx;
		return this;
	}
	
	
	@Override
	public void connect(String host, int port) throws Exception {
		this.host = host;
		this.port = port;
		logger.debug(String.format("connecting to %s:%d", host, port));
		client = new WebSocketClient(vertx, host, port, this);	
		client.start(new Promise<Void>() {
			
			@Override
			public boolean tryFail(Throwable cause) {
				logger.error(String.format("failure (%s:%d) : cause: %s", cause.getLocalizedMessage(), host, port), cause);
				listener.onNodeFailure(instance);
				return false;
			}
			
			@Override
			public boolean tryComplete(Void result) {
				return true;
			}
			
			@Override
			public Future<Void> future() {
				return null;
			}
		});
	}

	@Override
	public void leave() throws Exception {
		client.writeTextMessage(new LeaveMessage().toString());
	}
	
	@Override
	public void heartbeat(List<String> hosts) throws Exception {
		client.writeTextMessage(new HeartBeatMessage(hosts).toString());
	}

	@Override
	public void setListener(NodeListener listener) {
		this.listener = listener;
	}


	@Override
	public void disconnect() throws Exception {
		logger.debug(String.format("closing connection (%s:%d)", host, port));
		leave();
		client.stop();
		logger.debug(String.format("connection has been closed (%s:%d)", host, port));
	}


	@Override
	public void onConnected(String host, int port) {
		logger.debug(String.format("connection has been established to (%s:%d)", host, port));
		listener.onNodeConnected(this);
	}


	@Override
	public void onFailure(Throwable throwable, String host, int port) {
		throwable.printStackTrace();
		listener.onNodeFailure(this);
	}



	@Override
	public void onMessage(Message message, Transport transport) {
		
		switch (message.type()) {
		
		case HEARTBEAT:
			listener.onHearbeat(this, ((HeartBeatMessage)message).getNodes());
			break;
			
		case DATA:
			listener.onReceiveData(this, message.key(), message.value());
			break;
			
		case LEAVE:
			listener.onLeftNode(transport.host());
			break;
			
		case ACK:
			listener.onAck(this);
			break;

		default:
			break;
		}
		
		sendAck(message, transport);
	}
	
	
	private void sendAck(Message message, Transport transport) {
		try {
			transport.write((new AckMessage(message.id())).toString());
		} catch (Exception e) {
			try {
				client.stop();
				listener.onNodeFailure(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}


	@Override
	public void onData(String text, Transport transport) {

		try {
			onMessage(JsonMessageParser.parse(text), transport);
		} catch (InvalidMessageException e) {
			e.printStackTrace();
			try {
				client.stop();
				listener.onNodeFailure(this);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	@Override
	public String host() {
		return host;
	}

	@Override
	public TransportType socketType() {
		return TransportType.WEBSOCKET_CLIENT;
	}


	@Override
	public String key() {
		return host;
	}

	@Override
	public SocketAddress remoteAddress() {
		return null;
	}

	@Override
	public void close() throws Exception {
		client.stop();
	}

	@Override
	public void write(Buffer data) throws Exception {
		client.writeTextMessage(data.getBytes());
	}

	@Override
	public void write(String text) throws Exception {
		client.writeTextMessage(text);
	}


	
	
}
