package io.taranis.opencluster.core;

import java.util.List;
import java.util.Optional;

import io.taranis.opencluster.core.model.Service;
import io.taranis.opencluster.core.model.ServiceStatus;

public interface ServiceRepository {

	public Service put(Service service);

	public Service put(Service service, String clustername);

	public Optional<Service> remove(Service service);

	public Optional<Service> remove(String serviceId);

	public Optional<Service> remove(String serviceId, String clustername);

	public Optional<Service> get(String serviceId);

	public Optional<List<Service>> getByCluster(String clusterName);

	public Optional<List<Service>> getByServiceName(String name);

	public Optional<List<Service>> getByServiceName(String name, String clusterName);

	public Optional<List<Service>> getByTags(List<String> tags);

	public Optional<List<Service>> getByTags(List<String> tags, String clusterName);

	public Optional<List<Service>> filter(String serviceName, List<String> tags, String clusterName);

	public void clear();

	public void clear(String clusterName);
	
	public void updateStatus(String serviceId, String clustername, ServiceStatus status);
	
	public void refreshService(String serviceId, String clustername);
}
