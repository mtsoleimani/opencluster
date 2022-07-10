package io.taranis.opencluster.cluster.membership;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MemebrPool {

	private final Map<String, Member> pool = new ConcurrentHashMap<>();
	
	private int timeoutSeconds;
	
	public MemebrPool(int timeoutMinutes) {
		this.timeoutSeconds = timeoutMinutes * 60;
	}
	
	public String put(String node) {
		Member member = pool.putIfAbsent(node, new Member(node));
		if (member != null) {
			if(!pool.get(node).isAlive(timeoutSeconds)) {
				pool.get(node).refresh();
				return node;
			}
			
			pool.get(node).refresh();
			return null;
		}
		
		return node;
	}

	public List<String> put(List<String> nodes) {
		
		List<String> newNodes = new ArrayList<>();
		
		nodes.stream().forEach(node -> {
			String res = put(node);
			if(res != null)
				newNodes.add(res);
		});
		
		return newNodes;
	}

	public String purge(String node) {
		if (pool.containsKey(node)) {
			pool.get(node).kill();
			return node;
		}
		
		return null;
	}

	public List<String> toList() {
		return pool.entrySet().stream()
				.map(keyValue -> keyValue.getValue())
				.filter(node -> node.isAlive(timeoutSeconds))
				.map(node -> node.getAddress())
				.collect(Collectors.toList());
	}
	
}
