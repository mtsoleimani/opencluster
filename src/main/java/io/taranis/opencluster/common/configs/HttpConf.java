package io.taranis.opencluster.common.configs;

import org.apache.commons.lang3.math.NumberUtils;

import io.taranis.opencluster.common.utils.StringUtils;


public class HttpConf extends ServerConf  {


	@Override
	public int defaultPort() {
		return 3000;
	}
	
	
	public boolean readConfig() {

		host =  System.getenv(Metadata.HTTP_HOST);
		if(StringUtils.isNullOrEmpty(host))
			host = Metadata.ALL_HOSTS;
		
		if(!NumberUtils.isDigits(System.getenv(Metadata.HTTP_PORT)))
			port = defaultPort();
		else
			port = Integer.parseInt(System.getenv(Metadata.HTTP_PORT));
		
		if (port <= 0 || port >= 65535)
			port = defaultPort();
		
		if(!NumberUtils.isDigits(System.getenv(Metadata.HTTP_TLS)))
			tlsEnabled = false;
		else {
			tlsEnabled = (Integer.parseInt(System.getenv(Metadata.HTTP_TLS)) == 1);
			this.serverCertPath = System.getenv(Metadata.HTTP_PEM_CERT);
			this.serverKeyPath = System.getenv(Metadata.HTTP_PEM_KEY);
		}
		
		return true;
	}
	
		
}
