package io.taranis.opencluster.cluster.membership;

public class Member {
	
	public Member(String address) {
		this.address = address;
		this.state = MemberState.ALIVE;
		this.lastPing = System.currentTimeMillis();
	}
	
	
	private String address;

	private MemberState state;

	private long lastPing;

	public String getAddress() {
		return address;
	}

	public Member setAddress(String address) {
		this.address = address;
		return this;
	}

	public MemberState getState() {
		return state;
	}
	
	public boolean isAlive(long timeout) {
		if((((System.currentTimeMillis() - lastPing) / 1000) < timeout))
			state = MemberState.DEAD;
		
		return (state == MemberState.ALIVE);
	}

	public Member kill() {
		lastPing = 0;
		this.state = MemberState.DEAD;
		return this;
	}

	public long getLastPing() {
		return lastPing;
	}

	public Member refresh() {
		this.lastPing = System.currentTimeMillis();
		this.state = MemberState.ALIVE;
		return this;
	}
}
