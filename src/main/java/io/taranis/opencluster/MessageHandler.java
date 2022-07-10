package io.taranis.opencluster;

import io.taranis.opencluster.server.transport.Transport;

public interface MessageHandler {

	public void onIncomingMessage(Message message, Transport transport);

	default public void onFailure(Transport transport) {
		/* in default mode: nothing */
	}

}
