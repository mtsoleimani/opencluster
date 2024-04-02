package io.taranis.opencluster.common.configs;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ServerConf extends ConfigurationBase {
	
	protected String host = Metadata.ALL_HOSTS;
	protected int port = defaultPort();
	
	protected boolean tlsEnabled = false;
	protected String serverKeyPath;
	protected String serverCertPath;

	protected abstract int defaultPort();
}
