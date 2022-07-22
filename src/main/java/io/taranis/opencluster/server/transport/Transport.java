package io.taranis.opencluster.server.transport;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;

public interface Transport {

	public TransportType socketType();
	
	public SocketAddress remoteAddress();
	
	public String host();
	
	public String key();
	
	public void close() throws Exception;

	public void write(Buffer data) throws Exception;
	
	public void write(String text) throws Exception;
}
