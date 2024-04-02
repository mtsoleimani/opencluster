package io.taranis.opencluster.server.http.request;


import io.taranis.opencluster.common.utils.StringUtils;
import lombok.Getter;

@Getter
public class GetServicesByIdRequest implements InputValidator {

	private String id;
	
	public GetServicesByIdRequest(String id) {
		this.id = id;
	}

	@Override
	public boolean validate() {
		return !StringUtils.isNullOrEmpty(id);
	}
}
