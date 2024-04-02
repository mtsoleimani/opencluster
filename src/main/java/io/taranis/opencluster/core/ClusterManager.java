package io.taranis.opencluster.core;

import java.util.List;

import io.taranis.opencluster.core.model.Service;

public interface ClusterManager {

	public void join(Service service) throws RuntimeException;
	
	public void leave(Service service) throws RuntimeException;
	
	public List<Service> members() throws RuntimeException;
	
	public void add(Service service) throws RuntimeException;
	
	public void remove(Service service) throws RuntimeException;
}
