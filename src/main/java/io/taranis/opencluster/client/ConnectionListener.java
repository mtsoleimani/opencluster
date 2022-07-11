package io.taranis.opencluster.client;

public interface ConnectionListener {

	public void onConnected(String host, int port);
	
	public void onFailure(Throwable throwable, String host, int port);
	
}
