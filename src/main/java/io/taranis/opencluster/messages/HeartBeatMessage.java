package io.taranis.opencluster.messages;

import java.util.List;

public class HeartBeatMessage extends MessageBase {

	private List<String> nodes;
	
	public HeartBeatMessage() {
		
	}
	
	public HeartBeatMessage(List<String> nodes) {
		this.value = String.join(",", nodes);
		this.nodes = nodes;
	}
	
	@Override
	public MessageType type() {
		return MessageType.HEARTBEAT;
	}
	
	public List<String> getNodes() {
		return nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

}
