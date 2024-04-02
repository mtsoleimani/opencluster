package io.taranis.opencluster.server.http.request.keyvalue;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.taranis.opencluster.common.utils.HttpUtils;
import io.vertx.ext.web.RoutingContext;

public class KeyValueInputValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(KeyValueInputValidator.class);
	
	private KeyValueInputValidator() {
		
	}
	
	public static Optional<SetKeyValueRequest> parseAndValidateSetKeyValueRequest(RoutingContext routingContext) {
		try {
			SetKeyValueRequest obj = HttpUtils.getJson(routingContext).mapTo(SetKeyValueRequest.class);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}

	public static Optional<UpdateKeyValueTtlRequest> parseAndValidateUpdateKeyValueTtlRequest(RoutingContext routingContext) {
		try {
			UpdateKeyValueTtlRequest obj = HttpUtils.getJson(routingContext).mapTo(UpdateKeyValueTtlRequest.class);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}

	
	public static Optional<GetKeyValueRequest> parseAndValidateGetKeyValueRequest(RoutingContext routingContext) {
		try {
			String key = routingContext.request().getParam("key");
			GetKeyValueRequest obj = new GetKeyValueRequest(key);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}
	
	public static Optional<RemoveKeyValueRequest> parseAndValidateRemoveKeyValueRequest(RoutingContext routingContext) {
		try {
			String key = routingContext.request().getParam("key");
			RemoveKeyValueRequest obj = new RemoveKeyValueRequest(key);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}
	
}
