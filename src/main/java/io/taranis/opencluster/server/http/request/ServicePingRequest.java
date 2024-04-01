package io.taranis.opencluster.server.http.request;


import io.taranis.opencluster.common.utils.StringUtils;
import lombok.Getter;

@Getter
public class ServicePingRequest implements InputValidator {

	private String id;
	
	private String clusterName;
	
	public ServicePingRequest(String id, String clusterName) {
		this.id = id;
		this.clusterName = clusterName;
	}

	@Override
	public boolean validate() {
		return !StringUtils.isNullOrEmpty(id);
	}
}
