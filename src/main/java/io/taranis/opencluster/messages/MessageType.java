package io.taranis.opencluster.messages;

public enum MessageType {

	HEARTBEAT,
	
	DATA
	
	;
	
	public static MessageType valueOf(int ordinal) {
		for(MessageType item : MessageType.values())
			if(ordinal == item.ordinal())
				return item;
		return null;
	}
	
}