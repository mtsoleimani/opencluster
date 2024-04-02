package io.taranis.opencluster.server.http.request.keyvalue;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.server.http.request.InputValidator;
import lombok.Getter;

@Getter
public class UpdateKeyValueTtlRequest implements InputValidator {

	private String key;

	private long ttl;

	public UpdateKeyValueTtlRequest(String key, long ttl) {
		this.key = key;
		this.ttl = ttl;
	}

	@Override
	public boolean validate() {
		return (!StringUtils.isNullOrEmpty(key) && ttl > 0);
	}
}
