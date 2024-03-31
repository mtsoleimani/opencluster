package io.taranis.opencluster.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class KeepAliveOptions {

	protected int ttl;
	
	protected int interval;
	
	protected String http;
	
	protected int deregisterAfter;
	
}
