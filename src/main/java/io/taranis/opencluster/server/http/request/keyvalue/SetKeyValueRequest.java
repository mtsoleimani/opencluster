package io.taranis.opencluster.server.http.request.keyvalue;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.server.http.request.InputValidator;
import lombok.Getter;

@Getter
public class SetKeyValueRequest implements InputValidator {

	private String key;

	private String value;

	private long ttl;

	public SetKeyValueRequest(String key, String value, long ttl) {
		this.key = key;
		this.value = value;
		this.ttl = ttl;
	}

	@Override
	public boolean validate() {
		return (!StringUtils.isNullOrEmpty(key) && !StringUtils.isNullOrEmpty(value));
	}
}
