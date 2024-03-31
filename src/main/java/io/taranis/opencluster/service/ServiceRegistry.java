package io.taranis.opencluster.service;

import java.util.List;
import java.util.Optional;

public interface ServiceRegistry {
	
	public Service register(Service service) throws RuntimeException;
	
	public Optional<Service> deregister(Service service) throws RuntimeException;
	
	public Optional<List<Service>> getByTags(List<String> tags) throws RuntimeException;
	
	public Optional<List<Service>> getByTags(String clusterName, List<String> tags) throws RuntimeException;
	
	public Optional<Service> getByServiceId(String serviceId) throws RuntimeException;
	
	public Optional<List<Service>> getByServiceName(String serviceName) throws RuntimeException;
	
	public Optional<List<Service>> getByServiceName(String clusterName, String serviceName) throws RuntimeException;
	
	public Optional<List<Service>> filter(String clusterName, List<String> tags, String serviceName) throws RuntimeException;

}
