package io.taranis.opencluster.core;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import io.taranis.opencluster.core.model.Service;
import io.taranis.opencluster.core.model.ServiceStatus;

public class ServiceRegistry {
	
	private final ServiceRepository serviceRepository;

	public ServiceRegistry(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
	}

	
	public Service register(Service service) throws RuntimeException {
		service.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
		service.setLastSeen(LocalDateTime.now(ZoneId.of("UTC")));
		service.setServiceStatus(ServiceStatus.HEALTHY);
		return serviceRepository.put(service);
	}

	
	public Optional<Service> deregister(Service service) throws RuntimeException {
		return serviceRepository.remove(service);
	}

	
	public Optional<List<Service>> getByTags(List<String> tags) throws RuntimeException {
		return serviceRepository.getByTags(tags);
	}

	
	public Optional<List<Service>> getByTags(String clusterName, List<String> tags) throws RuntimeException {
		return serviceRepository.getByTags(tags, clusterName);
	}

	
	public Optional<Service> getByServiceId(String serviceId) throws RuntimeException {
		return serviceRepository.get(serviceId);
	}

	
	public Optional<List<Service>> getByServiceName(String serviceName) throws RuntimeException {
		return serviceRepository.getByServiceName(serviceName);
	}

	
	public Optional<List<Service>> getByServiceName(String clusterName, String serviceName) throws RuntimeException {
		return serviceRepository.getByServiceName(serviceName, clusterName);
	}

	
	public Optional<List<Service>> filter(String clusterName, List<String> tags, String serviceName) throws RuntimeException {
		return serviceRepository.filter(serviceName, tags, clusterName);
	}

}
