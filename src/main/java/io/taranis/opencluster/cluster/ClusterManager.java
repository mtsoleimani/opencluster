package io.taranis.opencluster.cluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.taranis.opencluster.MessageHandler;
import io.taranis.opencluster.cluster.membership.Member;
import io.taranis.opencluster.cluster.membership.MemebrPool;
import io.taranis.opencluster.messages.DataMessage;
import io.taranis.opencluster.messages.LeaveMessage;
import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.server.BasicServer;
import io.taranis.opencluster.server.TcpOptionsConf;
import io.taranis.opencluster.server.WebSocketServerBuilder;
import io.taranis.opencluster.server.transport.Transport;
import io.taranis.opencluster.server.transport.TransportType;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ClusterManager implements Cluster, ClusterListener, MessageHandler {

	private static final Vertx vertx = Vertx.vertx(new VertxOptions());
	
	private final Set<String> hosts = new HashSet<String>();

	private int heartBeatInterval;
	
	private MemebrPool memebrPool;
	
	private BasicServer server;
	
	private int nodeTimeout;
	
	public ClusterManager(int port, TransportType transportType, 
			TcpOptionsConf tcpOptionsConf, 
			int nodeTimeout, int heartBeatInterval,
			Set<String> seeds) {
		this.memebrPool = new MemebrPool(nodeTimeout);
		this.server = createServer(port, transportType, tcpOptionsConf);
		this.hosts.addAll(seeds);
		this.heartBeatInterval = heartBeatInterval;
		this.nodeTimeout = nodeTimeout;
	}
	
	
	public void start() throws Exception {
		server.start(new Promise<Void>() {
			
			@Override
			public boolean tryFail(Throwable cause) {
				cause.printStackTrace();
				return false;
			}
			
			@Override
			public boolean tryComplete(Void result) {
				return false;
			}
			
			@Override
			public Future<Void> future() {
				return null;
			}
		});
		
		join();
	}
	
	public void join() {
		hosts.stream().forEach(node -> {
			join(node);
		});
	}
	
	@Override
	public void shutdown() throws Exception {
		memebrPool.toTransportList().stream().forEach(node -> {
			leave(node);
		});
	}
	
	
	private BasicServer createServer(int port, TransportType transportType, TcpOptionsConf tcpOptionsConf) {
		return WebSocketServerBuilder.newInstance()
				.withHost("0.0.0.0")
				.withMessageHandler(this)
				.withPort(port)
				.withTcpOptionsConf(tcpOptionsConf)
				.withVertx(vertx)
				.build();
	}
	
	@Override
	public void setSeedNodes(List<String> seedNodes) {
		if(seedNodes == null || seedNodes.isEmpty())
			return;
		
		seedNodes.stream().forEach(ndoe -> {
			this.hosts.add(ndoe);	
		});
		
		join();
	}

	@Override
	public void join(String node) {
		if(node == null || node.isEmpty())
			return;
		
		Member memeber = memebrPool.get(node);
		if(memeber != null && memeber.isAlive(nodeTimeout))
			return;
		
		if(memeber != null && !memeber.isAlive(nodeTimeout))
			memebrPool.purge(node);
		
		//memeber.getTransport().write(new HeartBeatMessage(memebrPool.toList()).toString());
	}

	@Override
	public void join(List<String> nodes) {
		if(nodes == null || nodes.isEmpty())
			return;
		
		nodes.stream().forEach(node -> {
			join(node);
		});
	}

	@Override
	public void leave(String node) {
		if(node == null || node.isEmpty())
			return;
		
		Member memeber = memebrPool.get(node);
		if(memeber == null)
			return;
		leave(memeber.getTransport());
	}

	@Override
	public void leave(List<String> nodes) {
		if(nodes == null || nodes.isEmpty())
			return;
		
		nodes.stream().forEach(node -> {
			leave(node);
		});
	}
	
	private void leave(final Transport node) {
		if(node == null)
			return;
		
		try {
			node.write(new LeaveMessage().toString(), handler -> {
				memebrPool.purge(node);
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void sendData(String node, String key, String value) throws Exception {
		if(node == null || node.isEmpty())
			return;
		
		Member memeber = memebrPool.get(node);
		if(memeber == null)
			return;
		
		if(memeber.getTransport() == null)
			throw new NullPointerException();
		
		memeber.getTransport().write(new DataMessage(key, value).toString());
	}

	@Override
	public List<String> toList() {
		return memebrPool.toList();
	}
	
	@Override
	public void onHearbeat(String node, List<String> neighbors) {
		// TODO Auto-generated method stub
		hosts.add(node);
		neighbors.stream().forEach(neighbor -> {
			hosts.add(neighbor);
		});
		
		join();
	}

	@Override
	public void onNodeFailure(String node) {
		// TODO Auto-generated method stub
		hosts.add(node);
		memebrPool.purge(node);
	}

	@Override
	public void onJoinedNode(String node) {
		// TODO Auto-generated method stub
		hosts.add(node);
	}

	@Override
	public void onJoinedNode(List<String> nodes) {
		// TODO Auto-generated method stub
		nodes.stream().forEach(node -> {
			hosts.add(node);
		});
	}

	@Override
	public void onLeftNode(String node) {
		// TODO Auto-generated method stub
		hosts.remove(node);
		memebrPool.purge(node);
	}

	@Override
	public void onReceiveData(String node, String key, String value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onIncomingMessage(Message message, Transport transport) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onFailure(Throwable throwable, Transport transport) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onNodeConnected(String node) {
		// TODO Auto-generated method stub
		
	}


	

}
