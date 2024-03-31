package io.taranis.opencluster.service.impl;

import java.util.List;
import java.util.Optional;

import io.taranis.opencluster.server.transport.Transport;
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
//		if (StringUtils.isNullOrEmpty(service.getAddress()))
//			service.setAddress(transport.host());
		return serviceRepository.put(service);
	}

	@Override
	public Optional<Service> deregister(Service service) throws RuntimeException {
		return serviceRepository.remove(service);
	}

	@Override
	public String discoverMe(Transport transport) throws RuntimeException {
		return transport.host();
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


}
