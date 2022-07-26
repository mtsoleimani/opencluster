package io.taranis.opencluster.cluster;

import java.util.List;
import java.util.Set;

import io.taranis.opencluster.cluster.node.ClientNodeFactory;
import io.taranis.opencluster.cluster.node.ClientPool;
import io.taranis.opencluster.server.TcpOptionsConf;
import io.taranis.opencluster.server.transport.TransportType;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ClusterManager implements Cluster, ClusterListener {

	private static final Vertx vertx = Vertx.vertx(new VertxOptions());
	
	private ClusterServer server;
	
	private ClientPool clientPool;
	
	private Set<String> seeds;
	
	public ClusterManager(int port, TransportType transportType, TcpOptionsConf tcpOptionsConf, 
			int nodeTimeout, int heartBeatInterval, Set<String> seeds, String myHost) {
		ClientNodeFactory.setTransportType(transportType);
		this.server = new ClusterServer(vertx, port, transportType, tcpOptionsConf, this);
		this.clientPool = new ClientPool(vertx, port, nodeTimeout, heartBeatInterval, myHost);
		this.seeds = seeds;
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
		
		
		if(seeds == null || seeds.isEmpty())
			return;
		
		seeds.stream().filter(host -> (host != null && !host.isEmpty())).forEach(host -> {
			join(host);
		});
	}

	
	@Override
	public void shutdown() throws Exception {
		server.stop();
		clientPool.shutdown();
	}


	@Override
	public void onDiscovery(String host, List<String> hosts) {
		clientPool.join(host);
		if(hosts != null && !hosts.isEmpty())
			clientPool.join(hosts);
	}


	@Override
	public void onJoinedNode(String host) {
		clientPool.join(host);
	}


	@Override
	public void onLeftNode(String host) {
		clientPool.leave(host, true);
	}


	@Override
	public void onNodeFailure(String host) {
		/* do nothing */
	}


	@Override
	public void onReceiveData(String host, String key, String value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void join(String host) {
		clientPool.join(host);
	}


	@Override
	public void join(List<String> hosts) {
		hosts.stream().forEach(host -> {
			join(host);
		});
	}


	@Override
	public void leave(String host) {
		clientPool.leave(host, false);
	}


	@Override
	public void leave(List<String> hosts) {
		hosts.stream().forEach(host -> {
			leave(host);
		});
	}


	@Override
	public void sendData(String host, String key, String value) throws Exception {
		clientPool.sendData(host, key, value);
	}
	

}
