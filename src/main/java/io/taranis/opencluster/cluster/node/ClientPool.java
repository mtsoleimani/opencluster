package io.taranis.opencluster.cluster.node;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.vertx.core.Vertx;


public class ClientPool implements NodeListener {

	private final NodePool nodePool;
	
	private final int heartBeatInterval; 
	
	private final int port;
	
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	
	public ClientPool(Vertx vertx, int port, int timeoutMinutes, int heartBeatInterval) {
		nodePool = new NodePool(vertx, timeoutMinutes);
		this.port = port;
		this.heartBeatInterval = heartBeatInterval;
	}
	
	public void join(String host) {
		
		try {
			
			Node node = nodePool.newIfExistsGet(host);
			node.getNodeClient().setListener(this);
			
			if(!node.isConnected()) {
				node.getNodeClient().connect(host, port);
				return;
			}
			
			node.getNodeClient().heartbeat(nodePool.toList());
			nodePool.refresh(node.getNodeClient());
			
		} catch (Exception e) {
			e.printStackTrace();
			nodePool.reset(host);
			//reconnect(host);
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
		if(silent || node == null)
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
		if(!nodePool.isInBlockList(node.host()))
			nodePool.refresh(node);
		neighbors.stream().filter(host -> !nodePool.isInBlockList(host)).forEach(host -> {
			join(host);			
		});
	}

	@Override
	public void onNodeConnected(NodeClient node) {
		System.out.println("node connected to: " + node.host());
		nodePool.get(node.host()).setConnected();
		heatbeat(node);
	}
	
	private void heatbeat(NodeClient node) {
		try {
			if(!nodePool.get(node.host()).isConnected())
				return;
			
			node.heartbeat(nodePool.toList());
			nodePool.refresh(node);
			schedule(node);
		} catch (Exception e) {
			e.printStackTrace();
			onNodeFailure(node);
		}
	}

	private void schedule(final NodeClient node) {
		scheduler.schedule(() -> {
			heatbeat(node);
		}, heartBeatInterval, TimeUnit.SECONDS);
	}
	
	@Override
	public void onNodeFailure(NodeClient node) {
		System.out.println("connection has been failed to: " + node.host());
		nodePool.reset(node.host());
		reconnect(node.host());
	}

	
	private void reconnect(final String host) {
		System.out.println("reconnection is in schedule...: " + host);
		scheduler.schedule(() -> {
			System.out.println("reconnecting to: " + host);
			join(host);
		}, heartBeatInterval, TimeUnit.SECONDS);
	}

	@Override
	public void onReceiveData(NodeClient node, String key, String value) {
		// TODO Auto-generated method stub
		if(!nodePool.isInBlockList(node.host()))
			nodePool.refresh(node);
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

}
