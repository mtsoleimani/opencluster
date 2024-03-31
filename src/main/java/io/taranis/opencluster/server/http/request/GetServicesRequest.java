package io.taranis.opencluster.server.http.request;

import java.util.Arrays;
import java.util.List;

import io.taranis.opencluster.common.utils.StringUtils;
import lombok.Getter;

@Getter
public class GetServicesRequest implements InputValidator {

	private String serviceName;

	private String clusterName;
	
	private List<String> tags;

	public GetServicesRequest(String serviceName, String tags, String clusterName) {
		this.serviceName = serviceName;
		this.clusterName = clusterName;
		if(tags != null)
			this.tags = Arrays.asList(tags.split(",")).stream().map(String::trim).filter( i -> !StringUtils.isNullOrEmpty(i)).toList();
	}
	
	public GetServicesRequest(String serviceName, List<String> tags, String clusterName) {
		this.serviceName = serviceName;
		this.clusterName = clusterName;
		this.tags = tags;
	}

	@Override
	public boolean validate() {
		return (!StringUtils.isNullOrEmpty(serviceName) 
		|| !StringUtils.isNullOrEmpty(clusterName)
		|| ( tags != null && !tags.isEmpty()));
	}

}
