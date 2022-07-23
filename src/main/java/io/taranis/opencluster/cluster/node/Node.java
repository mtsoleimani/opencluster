package io.taranis.opencluster.cluster.node;


public class Node extends BasicNode {
	
	public Node(NodeClient nodeClient) {
		super();
		this.nodeClient = nodeClient;
		this.address = nodeClient.host();
	}
	
	private NodeClient nodeClient;

	@Override
	public Node kill() {
		super.kill();
		if(nodeClient == null)
			return this;
		
		try {
			nodeClient.close();
			nodeClient = null;
		} catch (Exception e) {
			
		}
		return this;
	}

	public NodeClient getNodeClient() {
		return nodeClient;
	}

	public Node setNodeClient(NodeClient nodeClient) {
		this.nodeClient = nodeClient;
		return this;
	}
}
