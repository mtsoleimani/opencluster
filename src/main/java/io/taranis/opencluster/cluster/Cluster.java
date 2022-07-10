package io.taranis.opencluster.cluster;

import java.util.List;


public interface Cluster {

	public void start() throws Exception;  
	
	public void shutdown() throws Exception;
	
	public void setSeedNodes(List<String> seedNodes);
	
	public void join(String node);
	
	public void join(List<String> nodes);
	
	public void leave(String node);
	
	public void leave(List<String> nodes);
	
	public void sendData(String node, String key, String value) throws Exception;
	
	public List<String> toList();
}
