package io.taranis.opencluster.cluster;

import java.util.HashSet;
import java.util.Set;

import io.taranis.opencluster.server.TcpOptionsConf;
import io.taranis.opencluster.server.transport.TransportType;

public class ClusterBuilder {

	private int port;

	private final Set<String> hosts = new HashSet<String>();

	private int heartBeatInterval;

	private int nodeTimeout;

	private TransportType transportType;
	
	private TcpOptionsConf tcpOptionsConf;
	
	public static ClusterBuilder newInstance() {
		return new ClusterBuilder();
	}
	
	public ClusterBuilder withPort(int port) {
		this.port = port;
		return this;
	}

	public ClusterBuilder addHost(String host) {
		hosts.add(host);
		return this;
	}

	public ClusterBuilder withHeartBeatInterval(int heartBeatInterval) {
		this.heartBeatInterval = heartBeatInterval;
		return this;
	}

	public ClusterBuilder withNodeTimeout(int nodeTimeout) {
		this.nodeTimeout = nodeTimeout;
		return this;
	}

	public ClusterBuilder withTransportType(TransportType transportType) {
		this.transportType = transportType;
		return this;
	}

	public ClusterBuilder withTcpOptionsConf(TcpOptionsConf tcpOptionsConf) {
		this.tcpOptionsConf = tcpOptionsConf;
		return this;
	}

	
	public Cluster build() {
		return new ClusterManager(port, transportType, tcpOptionsConf, nodeTimeout, heartBeatInterval, hosts);
	}
	

}
