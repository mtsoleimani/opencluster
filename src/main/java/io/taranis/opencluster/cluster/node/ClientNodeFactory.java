package io.taranis.opencluster.cluster.node;

import io.taranis.opencluster.cluster.node.websocket.WebSocketNodeClient;
import io.taranis.opencluster.server.transport.TransportType;
import io.vertx.core.Vertx;

public class ClientNodeFactory {
	
	private static TransportType transportType = TransportType.WEBSOCKET_CLIENT;
	
	public static void setTransportType(TransportType type) {
		transportType = type; 
	}
	
	public static TransportType getTransportType() {
		return transportType; 
	}
	
	public static NodeClient get(Vertx vertx, TransportType type) {
		return new WebSocketNodeClient(vertx);
	}
	
	public static NodeClient get(Vertx vertx) {
		return new WebSocketNodeClient(vertx);
	}
}
