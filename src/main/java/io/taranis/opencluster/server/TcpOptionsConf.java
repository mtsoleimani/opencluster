package io.taranis.opencluster.server;

public class TcpOptionsConf {

	protected int tcpBacklog;
	
	protected int tcpTxBuffer;
	
	protected int tcpRxBuffer;
	
	protected boolean tcpFastOpen;
	
	protected boolean tcpNoDelay;
	
	protected boolean tcpQuickAck;


	public int getTcpBacklog() {
		return tcpBacklog;
	}

	public TcpOptionsConf withTcpBacklog(int tcpBacklog) {
		this.tcpBacklog = tcpBacklog;
		return this;
	}

	public int getTcpTxBuffer() {
		return tcpTxBuffer;
	}

	public TcpOptionsConf withTcpTxBuffer(int tcpTxBuffer) {
		this.tcpTxBuffer = tcpTxBuffer;
		return this;
	}

	public int getTcpRxBuffer() {
		return tcpRxBuffer;
	}

	public TcpOptionsConf withTcpRxBuffer(int tcpRxBuffer) {
		this.tcpRxBuffer = tcpRxBuffer;
		return this;
	}

	public boolean isTcpFastOpen() {
		return tcpFastOpen;
	}

	public TcpOptionsConf withTcpFastOpen(boolean tcpFastOpen) {
		this.tcpFastOpen = tcpFastOpen;
		return this;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public TcpOptionsConf withTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
		return this;
	}

	public boolean isTcpQuickAck() {
		return tcpQuickAck;
	}

	public TcpOptionsConf withTcpQuickAck(boolean tcpQuickAck) {
		this.tcpQuickAck = tcpQuickAck;
		return this;
	}

}
