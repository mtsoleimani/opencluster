package io.taranis.opencluster.cluster.node;

import io.taranis.opencluster.server.transport.Transport;

public interface ConnectionListener {

	public void onConnected(String host, int port);
	
	public void onData(String text, Transport transport);

	public void onFailure(Throwable throwable, String host, int port);
	
}
