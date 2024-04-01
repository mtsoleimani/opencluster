package io.taranis.opencluster.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class HealthCheckOptions extends KeepAliveOptions {

	protected int interval;

	protected String http;

	protected String tcp;
	
	protected int port;

}
