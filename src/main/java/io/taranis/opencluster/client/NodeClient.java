package io.taranis.opencluster.client;

import java.util.List;

import io.taranis.opencluster.cluster.ClusterListener;

public interface NodeClient {
	
	public void connect(String host, int port) throws Exception;
	
	public void leave() throws Exception;
	
	public void heartbeat(List<String> hosts) throws Exception;
	
	public void setListener(ClusterListener listener);
	
	public void close() throws Exception;;	
}
