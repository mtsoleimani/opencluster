package io.taranis.opencluster.service;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class Service {
	
	protected String id;
	
	protected String name;
	
	protected List<String> tags;
	
	protected String address;
	
	protected int port;
	
	protected LocalDateTime registeredAt;
	
	protected LocalDateTime lastSeen;
	
	protected HealthCheckOptions healthCheckOptions;
	
	protected KeepAliveOptions keepAliveOptions;
	
	protected ClusterOptions clusterOptions;
	
	protected Boolean purgeIfPending;
	
	protected ServiceStatus serviceStatus;
}
