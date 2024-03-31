package io.taranis.opencluster.common.configs;

import org.apache.commons.lang3.math.NumberUtils;


public class TcpOptionsConf implements IConfiguration {

	
	private boolean tcpOptionEnabled = true;
	private int tcpBacklog = 1024;
	private int tcpTxBuffer = 4096;
	private int tcpRxBuffer = 4096;
	private boolean tcpFastOpen = true;
	private boolean tcpNoDelay = true;
	private boolean tcpQuickAck = true;
	
	@Override
	public boolean readConfig() {
		
		if(!NumberUtils.isDigits(System.getenv(Metadata.TCP_OPTIONS))) {
			tcpOptionEnabled = false;
			return true;
		}	
		
		tcpOptionEnabled = (Integer.parseInt(System.getenv(Metadata.TCP_OPTIONS)) == 1);
		
		try {
			this.tcpFastOpen = (Integer.parseInt(System.getenv(Metadata.FAST_OPEN)) == 1);
			this.tcpNoDelay = (Integer.parseInt(System.getenv(Metadata.NO_DELAY)) == 1);
			this.tcpQuickAck = (Integer.parseInt(System.getenv(Metadata.QUICK_ACK)) == 1);
			this.tcpBacklog = Integer.parseInt(System.getenv(Metadata.BACKLOG));
			this.tcpTxBuffer = Integer.parseInt(System.getenv(Metadata.TX_BUFFER));
			this.tcpRxBuffer = Integer.parseInt(System.getenv(Metadata.RX_BUFFER));
		} catch (Exception e) {
			tcpOptionEnabled = false;
		}
		
		return true;
	}


	public boolean isTcpOptionEnabled() {
		return tcpOptionEnabled;
	}


	public int getTcpBacklog() {
		return tcpBacklog;
	}


	public int getTcpTxBuffer() {
		return tcpTxBuffer;
	}


	public int getTcpRxBuffer() {
		return tcpRxBuffer;
	}


	public boolean isTcpFastOpen() {
		return tcpFastOpen;
	}


	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}


	public boolean isTcpQuickAck() {
		return tcpQuickAck;
	}

	@Override
	public String toString() {
		return "tcpOptionEnabled =" + tcpOptionEnabled + "\n"
				+ "tcpBacklog =" + tcpBacklog + "\n"
				+ "tcpTxBuffer =" + tcpTxBuffer + "\n"
				+ "tcpRxBuffer =" + tcpRxBuffer + "\n"
				+ "tcpFastOpen =" + tcpFastOpen + "\n"
				+ "tcpNoDelay =" + tcpNoDelay + "\n"
				+ "tcpQuickAck =" + tcpQuickAck + "\n";
	}
	
}
