package io.taranis.opencluster.cluster.node;

import java.util.List;

public interface NodeListener {

	public void onHearbeat(NodeClient node, List<String> neighbors);
	
	public void onNodeConnected(NodeClient node);

	public void onNodeFailure(NodeClient node);
	
//	public void onJoinedNode(NodeClient node);
//	
//	public void onJoinedNodes(List<String> nodes);
	
	public void onLeftNode(String host);
	
	public void onAck(NodeClient node);
	
	public void onReceiveData(NodeClient node, String key, String value);

}
