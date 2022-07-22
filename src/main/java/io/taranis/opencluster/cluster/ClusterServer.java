package io.taranis.opencluster.cluster;


import io.taranis.opencluster.MessageHandler;
import io.taranis.opencluster.messages.AckMessage;
import io.taranis.opencluster.messages.HeartBeatMessage;
import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.server.BasicServer;
import io.taranis.opencluster.server.TcpOptionsConf;
import io.taranis.opencluster.server.WebSocketServerBuilder;
import io.taranis.opencluster.server.transport.Transport;
import io.taranis.opencluster.server.transport.TransportType;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class ClusterServer implements MessageHandler {

	private BasicServer server;
	
	private ClusterListener listener;
	
	public ClusterServer(Vertx vertx, int port, TransportType transportType, 
			TcpOptionsConf tcpOptionsConf, ClusterListener listener) {
		this.server = createServer(port, transportType, tcpOptionsConf, vertx);
	}
	
	
	private BasicServer createServer(int port, TransportType transportType, TcpOptionsConf tcpOptionsConf, Vertx vertx) {
		return WebSocketServerBuilder.newInstance()
				.withHost("0.0.0.0")
				.withMessageHandler(this)
				.withPort(port)
				.withTcpOptionsConf(tcpOptionsConf)
				.withVertx(vertx)
				.build();
	}
	
	public void start(Promise<Void> startPromise) throws Exception {
		server.start(startPromise);
	}
	
	public void stop() {
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onIncomingMessage(Message message, Transport transport) {
		switch (message.type()) {
		case HEARTBEAT: 
			listener.onDiscovery(((HeartBeatMessage) message).getNodes());
			break;

		case DATA:
			listener.onReceiveData(transport.host(), message.key(), message.value());
			break;
			
		case LEAVE:
			listener.onLeftNode(transport.host());
			try {
				transport.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
				transport.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void onFailure(Throwable throwable, Transport transport) {
		try {
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
