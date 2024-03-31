package io.taranis.opencluster.common.configs;


public abstract class ServerConf implements IConfiguration {
	
	protected String host = Metadata.ALL_HOSTS;
	protected int port = defaultPort();
	
	protected boolean tlsEnabled = false;
	protected String serverKeyPath;
	protected String serverCertPath;

	protected abstract int defaultPort();

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getServerKeyPath() {
		return serverKeyPath;
	}

	public String getServerCertPath() {
		return serverCertPath;
	}

	public boolean isTlsEnabled() {
		return tlsEnabled;
	}
}
