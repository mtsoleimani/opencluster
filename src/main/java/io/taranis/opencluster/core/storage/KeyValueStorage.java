package io.taranis.opencluster.core.storage;

public interface KeyValueStorage {

	public void set(String key, String value);
	
	public void set(String key, String value, long ttl);
	
	public boolean ttl(String key, long ttl);
	
	public String get(String key);
	
	public String remove(String key);
	
	public void clear();
	
	public void shutdown();
}
