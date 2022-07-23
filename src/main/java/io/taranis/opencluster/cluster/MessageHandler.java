package io.taranis.opencluster.cluster;

import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.server.transport.Transport;

public interface MessageHandler {

	public void onConnected(Transport transport);

	public void onIncomingMessage(Message message, Transport transport);

	public void onFailure(Throwable throwable, Transport transport);

}
