package io.taranis.opencluster.messages;

public class LeaveMessage extends MessageBase {

	@Override
	public MessageType type() {
		return MessageType.LEAVE;
	}

}
