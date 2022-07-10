package io.taranis.opencluster.server.transport;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;

public interface Transport {

	public TransportType socketType();
	
	public Future<Void> close();

	public void close(Handler<AsyncResult<Void>> handler);

	public Future<Void> write(Buffer data);

	public void write(Buffer data, Handler<AsyncResult<Void>> handler);

	public Future<Void> write(String text);

	public void write(String text, Handler<AsyncResult<Void>> handler);

	public SocketAddress remoteAddress();
	
	public String host();
	
	public String key();
}
