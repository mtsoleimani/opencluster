package io.taranis.opencluster.server.transport;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.SocketAddress;

public class WebSocketTransport implements Transport {

	private final ServerWebSocket socket;

	public WebSocketTransport(ServerWebSocket socket) {
		this.socket = socket;
	}

	@Override
	public Future<Void> close() {
		return socket.close();
	}

	@Override
	public void close(Handler<AsyncResult<Void>> handler) {
		socket.close(handler);
	}

	@Override
	public Future<Void> write(Buffer data) {
		return socket.write(data);
	}

	@Override
	public void write(Buffer data, Handler<AsyncResult<Void>> handler) {
		socket.write(data, handler);
	}

	@Override
	public Future<Void> write(String text) {
		return socket.writeTextMessage(text);
	}

	@Override
	public void write(String text, Handler<AsyncResult<Void>> handler) {
		socket.writeTextMessage(text, handler);
	}

	@Override
	public SocketAddress remoteAddress() {
		return socket.remoteAddress();
	}
	
	@Override
	public String toString() {
		return socket.remoteAddress().toString();
	}

	@Override
	public TransportType socketType() {
		return TransportType.WEBSOCKET;
	}

	@Override
	public String key() {
		return socket.remoteAddress().toString();
	}
}
