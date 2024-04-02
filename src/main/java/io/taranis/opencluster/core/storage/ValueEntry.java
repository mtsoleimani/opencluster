package io.taranis.opencluster.core.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class ValueEntry {
	
	public static final long INFINITY_TTL = -1;
	
	public ValueEntry() {
		value(null, INFINITY_TTL);
	}
	
	public ValueEntry(String value) {
		value(value, INFINITY_TTL);
	}
	
	public ValueEntry(String value, long ttl) {
		value(value, ttl);
	}
	
	public void value(String value) {
		value(value, INFINITY_TTL);
	}
	
	public void value(String value, long ttl) {
		this.value = value;
		this.ttl = ttl;
		this.createdAt = System.currentTimeMillis();
	}
	
	public void ttl(long ttl) {
		this.ttl = ttl;
	}
	
	
	public boolean isFresh() {
		return (System.currentTimeMillis() - createdAt < ttl);
	}
	
	@Override
	public String toString() {
		return this.value;
	}

	private long ttl;
	
	private String value;
	
	private long createdAt;
	
}
