package io.taranis.opencluster.messages;

public interface Message {
	
	public MessageType type();
	
	public String key();
	
	public String value();
}