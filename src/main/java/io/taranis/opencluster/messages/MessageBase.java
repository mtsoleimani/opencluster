package io.taranis.opencluster.messages;

import java.util.UUID;

import io.vertx.core.json.JsonObject;

public abstract class MessageBase implements Message {

	protected String id = UUID.randomUUID().toString();

	protected String key;
	
	protected String value;
	
	@Override
	public String id() {
		return id;
	}
	
	@Override
	public String key() {
		return key;
	}

	@Override
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		JsonObject json = new JsonObject()
				.put(Metadata.TYPE, type().ordinal());
		
		if(key != null)
			json.put(Metadata.KEY, key);
		
		if(value != null)
			json.put(Metadata.VALUE, value);
		
		return json.toString();
	}
	
}
