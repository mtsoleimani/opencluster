package io.taranis.opencluster.messages;


public class AckMessage extends MessageBase {

	public AckMessage(String referenceId) {
		this.value = referenceId;
	}
	
	@Override
	public MessageType type() {
		return MessageType.ACK;
	}

}
