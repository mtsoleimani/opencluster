package io.taranis.opencluster.service;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository {

	public Service put(Service service);
	
	public Service put(Service service, String clustername);
	
	public Optional<Service> remove(Service service);
	
	public Optional<Service> remove(String serviceId);
	
	public Optional<Service> remove(String serviceId, String clustername);
	
	public Optional<Service> get(String serviceId);
	
	public Optional<List<Service>> getByCluster(String clusterName);
	
	public void clear();
	
	public void clear(String clusterName);
	
	
	public Optional<List<Service>> getByServiceName(String name);
	
	public Optional<List<Service>> getByServiceName(String name, String clusterName);
	
	public Optional<List<Service>> getByTags(List<String> tags);
	
	public Optional<List<Service>> getByTags(List<String> tags, String clusterName);
	
}
