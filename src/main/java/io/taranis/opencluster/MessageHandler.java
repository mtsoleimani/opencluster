package io.taranis.opencluster;

import io.taranis.opencluster.messages.Message;
import io.taranis.opencluster.server.transport.Transport;

public interface MessageHandler {

	public void onIncomingMessage(Message message, Transport transport);

	public void onFailure(Throwable throwable, Transport transport);

}
