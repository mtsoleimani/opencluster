package io.taranis.opencluster.core.request;

import io.taranis.opencluster.common.utils.StringUtils;
import lombok.Getter;

@Getter
public class GetServicesByClusterRequest implements InputValidator {

	private String clusterName;

	public GetServicesByClusterRequest(String clusterName) {
		this.clusterName = clusterName;
	}

	@Override
	public boolean validate() {
		return !StringUtils.isNullOrEmpty(clusterName);
	}
}
