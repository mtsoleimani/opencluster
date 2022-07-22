package io.taranis.opencluster.cluster.node;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import io.vertx.core.Vertx;


public class ClientPool implements NodeListener, Runnable {

	private final NodePool nodePool;
	
	private int heartBeatInterval; //TODO needs timer 
	
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	
	public ClientPool(Vertx vertx, int timeoutMinutes, int heartBeatInterval) {
		nodePool = new NodePool(vertx, timeoutMinutes);
		this.heartBeatInterval = heartBeatInterval;
	}
	
	public void join(String host) {
		Node node = nodePool.newIfExistsGet(host);
		try {
			node.getNodeClient().heartbeat(nodePool.toList());
			nodePool.refresh(node.getNodeClient());
		} catch (Exception e) {
			nodePool.reset(host);
			e.printStackTrace();
		}
	}
	
	public void join(List<String> hosts) {
		hosts.stream().forEach(host -> {
			join(host);
		});
	}
	
	public void leave(String host, boolean silent) {
		nodePool.purge(host);
		Node node = nodePool.get(host);
		if(node == null)
			return;
		
		if(silent)
			return;
		
		try {
			node.getNodeClient().leave();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendData(String host, String key, String value) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void onHearbeat(NodeClient node, List<String> neighbors) {
		neighbors.stream().filter(host -> !nodePool.isInBlockList(host)).forEach(host -> {
			join(host);			
		});
	}

	@Override
	public void onNodeConnected(NodeClient node) {
		try {
			node.heartbeat(nodePool.toList());
		} catch (Exception e) {
			onNodeFailure(node);
			e.printStackTrace();
		}
	}

	@Override
	public void onNodeFailure(NodeClient node) {
		nodePool.reset(node.host());
	}


	@Override
	public void onReceiveData(NodeClient node, String key, String value) {
		// TODO Auto-generated method stub
		System.out.println( String.format("incoming data from(%s): key:%s value:%s",  node.host(), key, value));
	}

	@Override
	public void onLeftNode(String host) {
		nodePool.purge(host);
	}

	@Override
	public void onAck(NodeClient node) {
		if(!nodePool.isInBlockList(node.host()))
			nodePool.refresh(node);
	}

	public void shutdown() throws Exception {
		scheduler.shutdown();
		nodePool.toList().stream().filter(host -> !nodePool.isInBlockList(host)).forEach(host -> {
			leave(host, false);
		});
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
