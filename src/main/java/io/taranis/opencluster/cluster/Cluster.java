package io.taranis.opencluster.cluster;

import java.util.List;


public interface Cluster {

	public void start() throws Exception;  
	
	public void shutdown() throws Exception;
	
	public void join(String host);
	
	public void join(List<String> hosts);
	
	public void leave(String host);
	
	public void leave(List<String> hosts);
	
	public void sendData(String host, String key, String value) throws Exception;
}
