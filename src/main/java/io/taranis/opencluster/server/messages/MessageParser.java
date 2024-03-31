package io.taranis.opencluster.server.messages;

import io.taranis.opencluster.exception.InvalidMessageException;

public interface MessageParser {
	
	public Message parse(String text) throws InvalidMessageException;

}
