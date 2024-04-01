package io.taranis.opencluster.common.configs;

public interface IConfiguration {

	public boolean readConfig();
	
	public boolean readConfigFromFile();
	
	public boolean readConfigFromEnvironmentVariables();
}
