package io.taranis.opencluster.server.http.request;

import io.taranis.opencluster.common.utils.StringUtils;
import lombok.Getter;

@Getter
public class ServicesDiscoveryRequest implements InputValidator {

	private String serviceName;

	private String clusterName;
	
	public ServicesDiscoveryRequest(String serviceName, String clusterName) {
		this.serviceName = serviceName;
		this.clusterName = clusterName;
	}
	
	@Override
	public boolean validate() {
		return (!StringUtils.isNullOrEmpty(serviceName));
	}

}
