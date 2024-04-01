package io.taranis.opencluster.common.configs;


import java.util.Properties;

import io.taranis.opencluster.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeneralConf extends ConfigurationBase {
	
	private String token;

	@Override
	public boolean readConfigFromEnvironmentVariables() {
		token = System.getenv(Metadata.AUTH_TOKEN);
		return !StringUtils.isNullOrEmpty(token);
	}

	
	@Override
	public boolean readConfigFromFile() {
		Properties prop = readConfigFile();
		if(prop == null)
			return false;
		
		token = prop.getProperty(Metadata.AUTH_TOKEN);
		return !StringUtils.isNullOrEmpty(token);
	}
	
}
