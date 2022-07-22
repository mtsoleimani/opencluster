package io.taranis.opencluster.cluster;

import java.util.List;

public interface ClusterListener {

	public void onDiscovery(List<String> hosts);

	public void onJoinedNode(String host);
	
	public void onLeftNode(String host);
	
	public void onNodeFailure(String host);
	
	public void onReceiveData(String host, String key, String value);

}
