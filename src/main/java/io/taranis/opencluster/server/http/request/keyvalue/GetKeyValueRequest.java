package io.taranis.opencluster.server.http.request.keyvalue;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.server.http.request.InputValidator;
import lombok.Getter;

@Getter
public class GetKeyValueRequest implements InputValidator {

	private String key;

	public GetKeyValueRequest(String key) {
		this.key = key;
	}

	@Override
	public boolean validate() {
		return !StringUtils.isNullOrEmpty(key);
	}
}
