package io.taranis.opencluster.server.transport;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.SocketAddress;

public class WebSocketTransport implements ServerTransport {

	private final ServerWebSocket socket;

	public WebSocketTransport(ServerWebSocket socket) {
		this.socket = socket;
	}

	@Override
	public Future<Void> closeAsync() {
		return socket.close();
	}

	@Override
	public void closeAsync(Handler<AsyncResult<Void>> handler) {
		socket.close(handler);
	}

	@Override
	public Future<Void> writeAsync(Buffer data) {
		return socket.write(data);
	}

	@Override
	public void writeAsync(Buffer data, Handler<AsyncResult<Void>> handler) {
		socket.write(data, handler);
	}

	@Override
	public Future<Void> writeAsync(String text) {
		return socket.writeTextMessage(text);
	}

	@Override
	public void writeAsync(String text, Handler<AsyncResult<Void>> handler) {
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
		return TransportType.WEBSOCKET_SERVER;
	}

	@Override
	public String key() {
		return socket.remoteAddress().toString();
	}

	@Override
	public String host() {
		return socket.remoteAddress().host();
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
