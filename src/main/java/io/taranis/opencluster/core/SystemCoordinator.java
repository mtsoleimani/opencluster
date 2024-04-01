package io.taranis.opencluster.core;

import java.util.List;
import java.util.Optional;

import io.taranis.opencluster.core.model.Service;

public class SystemCoordinator {

	private final ServiceRepository serviceRepository;

	private final ServiceRegistry serviceRegistry;
	
	private final HealthChecker healthChecker;

	public SystemCoordinator() {
		serviceRepository = new InMemoryServiceRepository();
		serviceRegistry = new ServiceRegistry(serviceRepository);
		healthChecker = new HealthChecker(serviceRepository);
	}
	
	public Service registerService(Service service) {
		Service res = serviceRegistry.register(service);
		healthChecker.schedule(res);
		return res;
	}

	public Optional<Service> unregisterService(Service service) {
		 return serviceRegistry.deregister(service);
	}

	public Optional<Service> getService(String id) {
		return serviceRegistry.getByServiceId(id);
	}

	public Optional<List<Service>> getServices(String clusterName, List<String> tags, String serviceName) {
		 return serviceRegistry.filter(clusterName, tags, serviceName);
	}

	public Optional<List<Service>> serviceDiscovery(String clusterName, String serviceName) {
		 return serviceRegistry.getByServiceName(clusterName, serviceName);
	}
	
	public void onKeepAlive(String serviceId, String clusterName) {
		healthChecker.onKeepAlive(serviceId, clusterName);
	}
	
	
	public void shutdown() {
		/* do nothing */ 
	}
}
