package io.taranis.opencluster.core;

import java.util.List;
import java.util.Optional;

import io.taranis.opencluster.core.model.Service;
import io.taranis.opencluster.core.storage.InMemoryKeyValueStorage;
import io.taranis.opencluster.core.storage.KeyValueStorage;

public class SystemCoordinator {

	private final ServiceRepository serviceRepository;

	private final ServiceRegistry serviceRegistry;

	private final HealthChecker healthChecker;

	private KeyValueStorage keyValueStorage;

	public SystemCoordinator() {
		this.serviceRepository = new InMemoryServiceRepository();
		this.serviceRegistry = new ServiceRegistry(serviceRepository);
		this.healthChecker = new HealthChecker(serviceRepository);
		this.keyValueStorage = new InMemoryKeyValueStorage();
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

	public void setKeyValue(String key, String value, long ttl) {
		if (ttl <= 0)
			this.keyValueStorage.set(key, value);
		else
			this.keyValueStorage.set(key, value, ttl);
	}

	public String removeKeyValue(String key) {
		return this.keyValueStorage.remove(key);
	}

	public String getKeyValue(String key) {
		return this.keyValueStorage.get(key);
	}

	public boolean updateKeyValueTtl(String key, long ttl) {
		return this.keyValueStorage.ttl(key, ttl);
	}

	public void shutdown() {
		healthChecker.shutdown();
		keyValueStorage.shutdown();
	}
}
