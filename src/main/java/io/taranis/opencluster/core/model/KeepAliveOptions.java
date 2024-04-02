package io.taranis.opencluster.core.model;

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
	
	protected int deregisterAfter;
	
}
