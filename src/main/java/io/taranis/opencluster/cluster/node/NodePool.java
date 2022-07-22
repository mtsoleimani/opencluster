package io.taranis.opencluster.cluster.node;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.vertx.core.Vertx;

public class NodePool {

	private final Map<String, Node> pool = new ConcurrentHashMap<>();
	
	private final Map<String, Long> blockList = new ConcurrentHashMap<>();
	
	private int timeoutSeconds;
	
	private final  Vertx vertx;
	
	public NodePool(Vertx vertx, int timeoutMinutes) {
		this.vertx = vertx;
		this.timeoutSeconds = timeoutMinutes * 60;
	}
	
	public Node newIfExistsGet(String host) {
		Node node = pool.putIfAbsent(host, new Node(ClientNodeFactory.get(vertx)));
		return (node == null) ? pool.get(host) : node;
	}
	
	public String refresh(NodeClient nodeClient) {
		Node node = pool.putIfAbsent(nodeClient.host(), new Node(nodeClient));
		if (node == null)
			node = pool.get(nodeClient.host());
		
		node.refresh();
		node.setNodeClient(nodeClient);
		return nodeClient.host();
	}
	

	public String reset(String node) {
		if (pool.containsKey(node)) {
			pool.get(node).kill();
			return node;
		}

		return null;
	}

	public String purge(String node) {
		blockList.put(node, System.currentTimeMillis());
		if (pool.containsKey(node)) {
			pool.get(node).kill();
			pool.remove(node);
			return node;
		}

		return null;
	}
	
	public Node get(String node) {
		return pool.get(node);
	}

	public List<String> toList() {
		return pool.entrySet().stream()
				.map(keyValue -> keyValue.getValue())
				.filter(node -> node.isAlive(timeoutSeconds))
				.map(node -> node.getAddress())
				.collect(Collectors.toList());
	}
	
	
	public boolean isInBlockList(String host) {
		return blockList.containsKey(host);
	}
	
}
