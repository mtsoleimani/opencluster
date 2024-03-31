package io.taranis.opencluster.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import io.taranis.opencluster.service.Service;
import io.taranis.opencluster.service.ServiceRegistry;
import io.taranis.opencluster.service.ServiceRepository;

public class ServiceRegistryImpl implements ServiceRegistry {

	private final ServiceRepository serviceRepository;

	public ServiceRegistryImpl(ServiceRepository serviceRepository) {
		this.serviceRepository = serviceRepository;
	}

	@Override
	public Service register(Service service) throws RuntimeException {
		service.setRegisteredAt(LocalDateTime.now(ZoneId.of("UTC")));
		return serviceRepository.put(service);
	}

	@Override
	public Optional<Service> deregister(Service service) throws RuntimeException {
		return serviceRepository.remove(service);
	}

	@Override
	public Optional<List<Service>> getByTags(List<String> tags) throws RuntimeException {
		return serviceRepository.getByTags(tags);
	}

	@Override
	public Optional<List<Service>> getByTags(String clusterName, List<String> tags) throws RuntimeException {
		return serviceRepository.getByTags(tags, clusterName);
	}

	@Override
	public Optional<Service> getByServiceId(String serviceId) throws RuntimeException {
		return serviceRepository.get(serviceId);
	}

	@Override
	public Optional<List<Service>> getByServiceName(String serviceName) throws RuntimeException {
		return serviceRepository.getByServiceName(serviceName);
	}

	@Override
	public Optional<List<Service>> getByServiceName(String clusterName, String serviceName) throws RuntimeException {
		return serviceRepository.getByServiceName(serviceName, clusterName);
	}

	@Override
	public Optional<List<Service>> filter(String clusterName, List<String> tags, String serviceName)
			throws RuntimeException {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


}
