package io.taranis.opencluster.messages;

public abstract class MessageBase implements Message {

	protected String key;
	
	protected String value;
	
	@Override
	public String key() {
		return key;
	}

	@Override
	public String value() {
		return value;
	}
	
}
