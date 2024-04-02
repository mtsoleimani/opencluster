package io.taranis.opencluster.common.configs;

import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TcpOptionsConf extends ConfigurationBase {

	private boolean tcpOptionEnabled = true;
	private int tcpBacklog = 1024;
	private int tcpTxBuffer = 4096;
	private int tcpRxBuffer = 4096;
	private boolean tcpFastOpen = true;
	private boolean tcpNoDelay = true;
	private boolean tcpQuickAck = true;
	
	@Override
	public boolean readConfigFromEnvironmentVariables() {
		
		if(!NumberUtils.isDigits(System.getenv(Metadata.TCP_OPTIONS))) {
			tcpOptionEnabled = false;
			return false;
		}	
		
		tcpOptionEnabled = (Integer.parseInt(System.getenv(Metadata.TCP_OPTIONS)) == 1);
		
		try {
			this.tcpFastOpen = (Integer.parseInt(System.getenv(Metadata.FAST_OPEN)) == 1);
			this.tcpNoDelay = (Integer.parseInt(System.getenv(Metadata.NO_DELAY)) == 1);
			this.tcpQuickAck = (Integer.parseInt(System.getenv(Metadata.QUICK_ACK)) == 1);
			this.tcpBacklog = Integer.parseInt(System.getenv(Metadata.BACKLOG));
			this.tcpTxBuffer = Integer.parseInt(System.getenv(Metadata.TX_BUFFER));
			this.tcpRxBuffer = Integer.parseInt(System.getenv(Metadata.RX_BUFFER));
			return true;
		} catch (Exception e) {
			tcpOptionEnabled = false;
			return false;
		}
	}

	
	@Override
	public boolean readConfigFromFile() {
		Properties prop = readConfigFile();
		if(prop == null)
			return false;
		
		
		if(!NumberUtils.isDigits(prop.getProperty(Metadata.TCP_OPTIONS))) {
			tcpOptionEnabled = false;
			return false;
		}	
		
		tcpOptionEnabled = (Integer.parseInt(prop.getProperty(Metadata.TCP_OPTIONS)) == 1);
		
		try {
			this.tcpFastOpen = (Integer.parseInt(prop.getProperty(Metadata.FAST_OPEN)) == 1);
			this.tcpNoDelay = (Integer.parseInt(prop.getProperty(Metadata.NO_DELAY)) == 1);
			this.tcpQuickAck = (Integer.parseInt(prop.getProperty(Metadata.QUICK_ACK)) == 1);
			this.tcpBacklog = Integer.parseInt(prop.getProperty(Metadata.BACKLOG));
			this.tcpTxBuffer = Integer.parseInt(prop.getProperty(Metadata.TX_BUFFER));
			this.tcpRxBuffer = Integer.parseInt(prop.getProperty(Metadata.RX_BUFFER));
			return true;
		} catch (Exception e) {
			tcpOptionEnabled = false;
			return false;
		}
	}
	
}
