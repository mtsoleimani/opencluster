package io.taranis.opencluster;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import io.taranis.opencluster.cluster.Cluster;
import io.taranis.opencluster.cluster.ClusterBuilder;
import io.taranis.opencluster.server.TcpOptionsConf;
import io.taranis.opencluster.server.transport.TransportType;

public class App {
	
	public static void main(String[] args) throws Exception {
		
		ClusterBuilder builder = ClusterBuilder.newInstance();
		
		try(InputStream inputStream = new FileInputStream("./config.properties")) {
			Properties properties = new Properties();
			properties.load(inputStream);
			
			
			builder.withMyHost(properties.getProperty("myhost"))
			.withHeartBeatInterval(Integer.parseInt(properties.getProperty("heartbeat")))
			.withNodeTimeout(Integer.parseInt(properties.getProperty("timeout")))
			.withPort(Integer.parseInt(properties.getProperty("port")))
			.withTransportType(TransportType.WEBSOCKET_CLIENT)
			.withTcpOptionsConf(new TcpOptionsConf().setDefault());
			
			
			Arrays.stream(properties.getProperty("seeds").trim().split(","))
			.filter(host -> !host.trim().isEmpty())
			.forEach(host -> {
				builder.addHost(host);
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Cluster cluster = builder.build();
		cluster.start();
		
		
		
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				cluster.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		
	}
	
}
