package io.taranis.opencluster.server.messages;

import io.taranis.opencluster.server.transport.Transport;

public interface MessageHandler {

	public void onConnected(Transport transport);

	public void onIncomingMessage(Message message, Transport transport);

	public void onFailure(Throwable throwable, Transport transport);

}
