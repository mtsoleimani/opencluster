package io.taranis.opencluster.core;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.core.model.Service;
import io.taranis.opencluster.core.model.ServiceStatus;

public class InMemoryServiceRepository implements ServiceRepository {

	private static final String DEFAULT_CLUSTER_NAME = "_default"; 
	
	private final Map<String, Service> services;
	
	private final Map<String, Map<String, LocalDateTime>> clusters;
	
	public InMemoryServiceRepository() {
		this.services = new ConcurrentHashMap<>();
		this.clusters = new ConcurrentHashMap<>();
		this.clusters.put(DEFAULT_CLUSTER_NAME, new ConcurrentHashMap<>());
	}
	
	
	@Override
	public Service put(Service service) {
		if(service.getClusterOptions() == null 
		|| StringUtils.isNullOrEmpty(service.getClusterOptions().getClusterName()))
			return put(service, DEFAULT_CLUSTER_NAME);
		
		return put(service, service.getClusterOptions().getClusterName());
	}
	
	private LocalDateTime now() {
		return LocalDateTime.now(ZoneId.of("UTC"));
	}

	@Override
	public Service put(Service service, String clustername) {
		if(StringUtils.isNullOrEmpty(service.getId()))
			service.setId(UUID.randomUUID().toString());
		
		service.setLastSeen(now());
		services.put(service.getId(), service);
		
		Map<String, LocalDateTime> cluster = clusters.putIfAbsent(clustername, new ConcurrentHashMap<>());
		if(cluster == null)
			cluster = clusters.get(clustername);
		cluster.put(service.getId(), now());
		
		return service;
	}


	@Override
	public Optional<Service> remove(Service service) {
		if(service.getClusterOptions() == null 
		|| StringUtils.isNullOrEmpty(service.getClusterOptions().getClusterName()))
			return remove(service, DEFAULT_CLUSTER_NAME);
		
		return remove(service, service.getClusterOptions().getClusterName());
	}


	private Optional<Service> remove(Service service, String clustername) {
		return remove(service.getId(), service.getClusterOptions().getClusterName());
	}


	@Override
	public Optional<Service> remove(String serviceId) {
		return remove(serviceId, DEFAULT_CLUSTER_NAME);
	}


	private void leaveFromCluster(String serviceId, String clustername) {
		Map<String, LocalDateTime> cluster = clusters.get(clustername);
		if(cluster != null)
			cluster.remove(serviceId);
	}
	
	@Override
	public Optional<Service> remove(String serviceId, String clustername) {
		if(StringUtils.isNullOrEmpty(serviceId))
			return Optional.empty();
		
		leaveFromCluster(serviceId, clustername);
		Service entry = this.services.remove(serviceId);
		return (entry == null) ?  Optional.empty() : Optional.of(entry);
	}
	
	@Override
	public Optional<Service> get(String serviceId) {
		Service entry = this.services.get(serviceId);
		return (entry == null) ?  Optional.empty() : Optional.of(entry);
	}


	@Override
	public Optional<List<Service>> getByCluster(String clusterName) {
		if(StringUtils.isNullOrEmpty(clusterName))
			clusterName = DEFAULT_CLUSTER_NAME;
		
		Map<String, LocalDateTime> cluster = this.clusters.get(clusterName);
		if(cluster == null)
			return Optional.empty();
		
		List<Service> list = new ArrayList<>();
		cluster.entrySet().forEach(i -> {
			Service service = this.services.get(i.getKey());
			if(service != null)
				list.add(service);
		});
		
		return Optional.of(list);
	}

	@Override
	public Optional<List<Service>> getByServiceName(String name) {
		if(StringUtils.isNullOrEmpty(name))
			return Optional.empty();
		
		List<Service> list = this.services.entrySet().stream().filter(i -> i.getValue().getName().equals(name)).map(Entry::getValue).toList();
		return (list == null || list.isEmpty()) ? Optional.empty() : Optional.of(list);
	}


	@Override
	public Optional<List<Service>> getByServiceName(String name, String clusterName) {
		if(StringUtils.isNullOrEmpty(name))
			return Optional.empty();
		
		List<Service> list = this.services.entrySet()
				.stream()
				.filter(i -> i.getValue().getClusterOptions() != null &&  clusterName.equals(i.getValue().getClusterOptions().getClusterName()))
				.filter(i -> i.getValue().getName().equals(name))
				.map(Entry::getValue).toList();
		return (list == null || list.isEmpty()) ? Optional.empty() : Optional.of(list);
	}


	private boolean isMember(Set<String> set, List<String> list) {
		for(String str : list)
			if(set.contains(str))
				return true;
		
		return false;
	}
	
	@Override
	public Optional<List<Service>> getByTags(List<String> tags) {
		if(tags == null || tags.isEmpty())
			return Optional.empty();
		
		Set<String> tagSet = tags.stream().collect(Collectors.toSet());
		List<Service> list = this.services.entrySet()
				.stream()
				.filter(i -> isMember(tagSet, i.getValue().getTags()))
				.map(Entry::getValue).toList();
		return (list == null || list.isEmpty()) ? Optional.empty() : Optional.of(list);
	}


	@Override
	public Optional<List<Service>> getByTags(List<String> tags, String clusterName) {
		if(tags == null || tags.isEmpty())
			return Optional.empty();
		
		Set<String> tagSet = tags.stream().collect(Collectors.toSet());
		List<Service> list = this.services.entrySet()
				.stream()
				.filter(i -> i.getValue().getClusterOptions() != null &&  clusterName.equals(i.getValue().getClusterOptions().getClusterName()))
				.filter(i -> isMember(tagSet, i.getValue().getTags()))
				.map(Entry::getValue).toList();
		return (list == null || list.isEmpty()) ? Optional.empty() : Optional.of(list);
	}


	@Override
	public Optional<List<Service>> filter(String serviceName, List<String> tags, String clusterName) {
		Stream<Entry<String, Service>> stream = this.services.entrySet().stream();
		
		if(tags != null && !tags.isEmpty()) {
			Set<String> tagSet = tags.stream().collect(Collectors.toSet());
			stream = stream.filter(i -> isMember(tagSet, i.getValue().getTags()));
		}
		
		if(!StringUtils.isNullOrEmpty(clusterName))
			stream = stream.filter(i -> i.getValue().getClusterOptions() != null &&  clusterName.equals(i.getValue().getClusterOptions().getClusterName()));
		
		if(!StringUtils.isNullOrEmpty(serviceName))
			stream = stream.filter(i -> i.getValue().getName().equals(serviceName));
		
		List<Service> list = stream.map(Entry::getValue).toList();
		return (list == null || list.isEmpty()) ? Optional.empty() : Optional.of(list);
	}
	
	@Override
	public void clear() {
		this.clusters.clear();
		this.services.clear();
	}

	@Override
	public void clear(String clusterName) {
		Map<String, LocalDateTime> cluster = this.clusters.get(clusterName);
		if(cluster == null) 
			return;
		
		cluster.entrySet().forEach(i -> {
			this.services.remove(i.getKey());
		});
		cluster.clear();
	}

	@Override
	public void updateStatus(String serviceId, String clustername, ServiceStatus status) {
		Service service = services.get(serviceId);
		if(service == null)
			return;
		
		service.setLastSeen(now());
		service.setServiceStatus(status);
	}

	@Override
	public void refreshService(String serviceId, String clustername) {
		updateStatus(serviceId, clustername, ServiceStatus.HEALTHY);
		
		if(clustername == null)
			clustername = DEFAULT_CLUSTER_NAME;
		
		Map<String, LocalDateTime> cluster = clusters.get(clustername);
		if(cluster == null)
			return;
		
		cluster.computeIfPresent(clustername, (k,v) -> now()); 
	}


}
