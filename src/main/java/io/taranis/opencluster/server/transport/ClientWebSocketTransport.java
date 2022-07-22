package io.taranis.opencluster.server.transport;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;
import io.vertx.core.net.SocketAddress;

public class ClientWebSocketTransport implements Transport {

	private final WebSocket socket;

	public ClientWebSocketTransport(WebSocket socket) {
		this.socket = socket;
	}

	@Override
	public TransportType socketType() {
		return TransportType.WEBSOCKET_CLIENT;
	}

	@Override
	public SocketAddress remoteAddress() {
		return socket.remoteAddress();
	}

	@Override
	public String host() {
		return socket.remoteAddress().host();
	}

	@Override
	public String key() {
		return socket.remoteAddress().toString();
	}

	@Override
	public void close() throws Exception {
		socket.close();
	}

	@Override
	public void write(Buffer data) {
		socket.write(data);
	}

	@Override
	public void write(String text) {
		socket.writeTextMessage(text);
	}

}
