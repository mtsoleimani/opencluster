package io.taranis.opencluster.cluster.membership;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.taranis.opencluster.server.transport.Transport;


public class MemebrPool {

	private final Map<String, Member> pool = new ConcurrentHashMap<>();
	
	private int timeoutSeconds;
	
	public MemebrPool(int timeoutMinutes) {
		this.timeoutSeconds = timeoutMinutes * 60;
	}
	
	public String put(Transport node) {
		Member member = pool.putIfAbsent(node.host(), new Member(node));
		if (member != null) {
			if(!pool.get(node.host()).isAlive(timeoutSeconds)) {
				pool.get(node.host()).refresh();
				pool.get(node.host()).setTransport(node);
				return node.host();
			}
			
			pool.get(node.host()).refresh();
			return null;
		}
		
		return node.host();
	}

//	public List<Transport> put(List<Transport> nodes) {
//		
//		List<Transport> newNodes = new ArrayList<>();
//		
//		nodes.stream().forEach(node -> {
//			String res = put(node);
//			if(res != null)
//				newNodes.add(node);
//		});
//		
//		return newNodes;
//	}

	public String purge(String node) {
		if (pool.containsKey(node)) {
			pool.get(node).kill();
			return node;
		}

		return null;
	}

	public String purge(Transport node) {
		if (pool.containsKey(node.host())) {
			pool.get(node.host()).kill();
			return node.host();
		}

		return null;
	}
	
	public Member get(String node) {
		return pool.get(node);
	}

	public List<String> toList() {
		return pool.entrySet().stream()
				.map(keyValue -> keyValue.getValue())
				.filter(node -> node.isAlive(timeoutSeconds))
				.map(node -> node.getAddress())
				.collect(Collectors.toList());
	}
	
	public List<Transport> toTransportList() {
		return pool.entrySet().stream()
				.map(keyValue -> keyValue.getValue())
				.filter(node -> node.isAlive(timeoutSeconds))
				.map(node -> node.getTransport())
				.collect(Collectors.toList());
	}
	
}
