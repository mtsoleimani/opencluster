package io.taranis.opencluster.common.configs;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public abstract class ConfigurationBase implements IConfiguration {

	private static final String CONFIG_FILE = "config.properties";
	
	@Override
	public boolean readConfig() {
		return Files.exists(Path.of(CONFIG_FILE)) ? readConfigFromFile() : readConfigFromEnvironmentVariables();
	}
	
	
	protected Properties readConfigFile() {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
		    prop.load(fis);
		    return prop;
		} catch (IOException ex) {
		    return null;
		}
	}
	
}
