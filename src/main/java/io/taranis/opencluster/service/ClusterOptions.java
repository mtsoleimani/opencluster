package io.taranis.opencluster.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Setter
@Getter
@Accessors(chain = true)
public class ClusterOptions {

	private String clusterName;
	
	private boolean onChangedNotification;
	
	
}
