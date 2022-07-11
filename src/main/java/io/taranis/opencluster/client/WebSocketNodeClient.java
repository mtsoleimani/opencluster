package io.taranis.opencluster.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.cluster.ClusterListener;
import io.taranis.opencluster.messages.HeartBeatMessage;
import io.taranis.opencluster.messages.LeaveMessage;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class WebSocketNodeClient implements NodeClient, ConnectionListener {

	private final Logger logger = (Logger) LoggerFactory.getLogger(WebSocketNodeClient.class);

	private final Vertx vertx;
	
	private WebSocketClient client;
	
	private String host;
	
	private int port;
	
	private ClusterListener listener;
	
	public WebSocketNodeClient(Vertx vertx) {
		this.vertx = vertx;
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
				listener.onNodeFailure(host);
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
	public void setListener(ClusterListener listener) {
		this.listener = listener;
	}


	@Override
	public void close() throws Exception {
		logger.debug(String.format("closing connection (%s:%d)", host, port));
		leave();
		client.stop();
		logger.debug(String.format("connection has been closed (%s:%d)", host, port));

	}


	@Override
	public void onConnected(String host, int port) {
		logger.debug(String.format("connection has been established to (%s:%d)", host, port));
		listener.onNodeConnected(host);
	}


	@Override
	public void onFailure(Throwable throwable, String host, int port) {
		throwable.printStackTrace();
		listener.onNodeFailure(host);
	}
	
	
}
