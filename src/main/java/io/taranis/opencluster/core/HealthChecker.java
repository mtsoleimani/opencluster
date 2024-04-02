package io.taranis.opencluster.core;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.core.model.Service;

public class HealthChecker {
	
	private static final int CONNECTION_TIMEOUT_MILLIS = 30 * 1000;
	
	private final ServiceRepository serviceRepository;
	
	private final ScheduledExecutorService scheduler;
	
	public HealthChecker(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
		this.scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
	public void onKeepAlive(String serviceId, String clusterName) {
		serviceRepository.refreshService(serviceId, clusterName);
	}
	
	public void shutdown() {
		this.scheduler.shutdown();
	}
	
	public void schedule(Service service) {
		if(service.getHealthCheckOptions() == null)
			return;
		
		this.scheduler.schedule(() -> ping(service.getId()), service.getHealthCheckOptions().getInterval(), TimeUnit.SECONDS);
	}
	
	
	private void ping(String id) {
		
		Optional<Service> service = serviceRepository.get(id);
		
		if(service.isEmpty())
			return;
		
		boolean httpIsAlive = false;
		boolean tcpIsAlive = false;
		
		if(!StringUtils.isNullOrEmpty(service.get().getHealthCheckOptions().getHttp()))
			httpIsAlive = checkHttpServerIfAvailable(service.get().getHealthCheckOptions().getHttp());
		
		if(!StringUtils.isNullOrEmpty(service.get().getHealthCheckOptions().getTcp()))
			tcpIsAlive = checkTcpServerIfAvailable(service.get().getHealthCheckOptions().getTcp(), service.get().getHealthCheckOptions().getPort());
		
		if(httpIsAlive || tcpIsAlive)
			onKeepAlive(service.get().getId(), service.get().getClusterOptions() != null ? service.get().getClusterOptions().getClusterName() : null);
		
		schedule(service.get());
	}
	
	private boolean checkTcpServerIfAvailable(String host, int port) {
	    try(Socket socket = new Socket()) {
	    	socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT_MILLIS);
	        return true;
	    } catch (Exception e) {
	    	return false;
		}
	}
	
	private boolean checkHttpServerIfAvailable(String url) {
		try {
			
			int statusCode;
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS)
					.setConnectionRequestTimeout(CONNECTION_TIMEOUT_MILLIS)
					.setSocketTimeout(CONNECTION_TIMEOUT_MILLIS)
					.build();
				
			try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
				HttpGet httpGet = new HttpGet(url);
				httpGet.addHeader("Accept", "*/*");
				try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
					statusCode = response.getStatusLine().getStatusCode();
				}
			}
					
			return (statusCode/100 == 2);
		} catch (Exception e) {
			return false;
		}
	}
	
}
