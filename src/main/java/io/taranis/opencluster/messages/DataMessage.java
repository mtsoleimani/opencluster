package io.taranis.opencluster.messages;

public class DataMessage extends MessageBase {

	public DataMessage() {
		
	}
	
	public DataMessage(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public MessageType type() {
		return MessageType.DATA;
	}

}
