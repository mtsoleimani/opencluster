package io.taranis.opencluster.server.transport;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public interface ServerTransport extends Transport {
	
	public Future<Void> closeAsync();
	
	public void closeAsync(Handler<AsyncResult<Void>> handler);

	public Future<Void> writeAsync(Buffer data);

	public void writeAsync(Buffer data, Handler<AsyncResult<Void>> handler);

	public Future<Void> writeAsync(String text);

	public void writeAsync(String text, Handler<AsyncResult<Void>> handler);

}
