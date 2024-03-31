package io.taranis.opencluster.service;

import java.util.List;

public interface ServiceClusterHandler {

	public void join(Service service) throws RuntimeException;
	
	public void leave(Service service) throws RuntimeException;
	
	public List<Service> members() throws RuntimeException;
	
	public void add(Service service) throws RuntimeException;
	
	public void remove(Service service) throws RuntimeException;
}
