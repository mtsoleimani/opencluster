package io.taranis.opencluster.cluster.node;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.vertx.core.Vertx;


public class ClientPool implements NodeListener {

	private final String myHost;
	
	private final NodePool nodePool;
	
	private final int heartBeatInterval; 
	
	private final int port;
	
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	
	
	private final Map<String, Long> lastHeartBeat = new ConcurrentHashMap<>();
	
	
	public ClientPool(Vertx vertx, int port, int timeoutMinutes, int heartBeatInterval, String myHost) {
		nodePool = new NodePool(vertx, timeoutMinutes);
		this.port = port;
		this.heartBeatInterval = heartBeatInterval;
		this.myHost = myHost;
	}
	
	public void join(String host) {
		if(host == null || host.isEmpty() || host.equals(myHost))
			return;
		
		
		try {
			
			Node node = nodePool.newIfExistsGet(host);
			node.getNodeClient().setListener(this);
			
			if(!node.isConnected()) {
				node.getNodeClient().connect(host, port);
				return;
			}
			
			if(!canPing(host))
				return;
			
			node.getNodeClient().heartbeat(nodePool.toList());
			nodePool.refresh(node.getNodeClient());
			lastHeartBeat.put(host, System.currentTimeMillis());
			
		} catch (Exception e) {
			e.printStackTrace();
			nodePool.reset(host);
			//reconnect(host);
		}
	}
	
	public void join(List<String> hosts) {
		if(hosts == null || hosts.isEmpty())
			return;
		
		hosts.stream().forEach(host -> {
			join(host);
		});
	}
	
	public void leave(String host, boolean silent) {
		if(host == null || host.isEmpty())
			return;
		
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
		
		if(neighbors == null || neighbors.isEmpty())
			return;
		
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
	
	
	private boolean canPing(String host) {
		if(lastHeartBeat.containsKey(host))
			return ( ( System.currentTimeMillis() - lastHeartBeat.get(host))/1000 >= heartBeatInterval );
		return true;
	}
	
	
	private void heatbeat(NodeClient node) {
		
		if(!canPing(node.host()))
			return;
		
		try {
			if(!nodePool.get(node.host()).isConnected())
				return;
			
			node.heartbeat(nodePool.toList());
			nodePool.refresh(node);
			schedule(node);
			lastHeartBeat.put(node.host(), System.currentTimeMillis());
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
