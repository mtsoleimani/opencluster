package io.taranis.opencluster.cluster;

import java.util.List;

public interface ClusterListener {

	public void onHearbeat(String node, List<String> neighbors);
	
	public void onNodeConnected(String node);

	public void onNodeFailure(String node);
	
	public void onJoinedNode(String node);
	
	public void onJoinedNode(List<String> nodes);
	
	public void onLeftNode(String node);
	
	public void onReceiveData(String node, String key, String value);

}
