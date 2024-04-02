package io.taranis.opencluster.core.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKeyValueStorage implements KeyValueStorage {

	private final Map<String, ValueEntry> storage;

	public InMemoryKeyValueStorage() {
		this.storage = new ConcurrentHashMap<>();
	}

	@Override
	public void set(String key, String value) {
		ValueEntry entry = this.storage.putIfAbsent(key, new ValueEntry(value));
		if (entry != null)
			entry.value(value);
	}

	@Override
	public void set(String key, String value, long ttl) {
		ValueEntry entry = this.storage.putIfAbsent(key, new ValueEntry(value, ttl));
		if (entry != null)
			entry.value(value, ttl);
	}

	@Override
	public boolean ttl(String key, long ttl) {
		ValueEntry entry = this.storage.get(key);
		if (entry != null) {
			entry.ttl(ttl);
			return true;
		}

		return false;
	}

	@Override
	public String get(String key) {
		ValueEntry entry = this.storage.get(key);
		if (entry == null)
			return null;

		if (entry.isFresh())
			return entry.value();

		this.storage.remove(key);
		return null;
	}

	@Override
	public String remove(String key) {
		ValueEntry entry = this.storage.get(key);
		if (entry == null)
			return null;
		
		return entry.value();
	}

	@Override
	public void clear() {
		this.storage.clear();
	}

	@Override
	public void shutdown() {
		clear();
	}

}
