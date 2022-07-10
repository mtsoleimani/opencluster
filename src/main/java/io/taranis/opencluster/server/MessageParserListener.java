package io.taranis.opencluster.server;

import io.taranis.opencluster.Message;
import io.taranis.opencluster.server.transport.Transport;


public interface MessageParserListener {

	public void onMessage(Transport transport, Message message);
	
	public void onFailure(Transport transport, Exception e);
}
