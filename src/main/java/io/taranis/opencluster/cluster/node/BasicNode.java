package io.taranis.opencluster.cluster.node;

import java.util.concurrent.atomic.AtomicBoolean;

public class BasicNode {
	
	public BasicNode() {
		
	}
	
	protected AtomicBoolean connected = new AtomicBoolean(false);
	
	protected String address;

	protected NodeState state = NodeState.DEAD;

	protected long lastPing;
	

	public String getAddress() {
		return address;
	}

	public BasicNode setAddress(String address) {
		this.address = address;
		return this;
	}

	public NodeState getState() {
		return state;
	}
	
	public boolean isAlive(long timeout) {
		state = ((((System.currentTimeMillis() - lastPing) / 1000) > timeout)) 
				? NodeState.DEAD 
				: NodeState.ALIVE;
		return (state == NodeState.ALIVE);
	}

	public BasicNode kill() {
		connected.set(false);
		lastPing = 0;
		this.state = NodeState.DEAD;
		return this;
	}

	public long getLastPing() {
		return lastPing;
	}

	public BasicNode refresh() {
		this.lastPing = System.currentTimeMillis();
		this.state = NodeState.ALIVE;
		return this;
	}

	public void setConnected() {
		connected.set(true);
	}
	
	public boolean isConnected() {
		return connected.get();
	}
	
}
