package io.taranis.opencluster.server.http.handlers.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.core.SystemCoordinator;
import io.taranis.opencluster.exception.InvalidMessageException;
import io.taranis.opencluster.server.http.HttpErrors;
import io.taranis.opencluster.server.http.HttpPostman;
import io.taranis.opencluster.server.http.handlers.HttpStorageHandler;
import io.taranis.opencluster.server.http.request.keyvalue.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class HttpStorageHandlerImpl implements HttpStorageHandler {

private final Logger logger = LoggerFactory.getLogger(HttpStorageHandlerImpl.class);
	
	private final SystemCoordinator systemCoordinator;
	
	public HttpStorageHandlerImpl(SystemCoordinator systemCoordinator) {
		this.systemCoordinator = systemCoordinator;
	}
	
	@Override
	public void handleSetValueInStorageRequest(RoutingContext routingContext) {
		Optional<SetKeyValueRequest> result = KeyValueInputValidator.parseAndValidateSetKeyValueRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}

		try {
			systemCoordinator.setKeyValue(result.get().getKey(), result.get().getValue(), result.get().getTtl());
			HttpPostman.sendOK(routingContext);
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleGetValueFromStorageRequest(RoutingContext routingContext) {
		Optional<GetKeyValueRequest> result = KeyValueInputValidator.parseAndValidateGetKeyValueRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}

		try {
			String value = systemCoordinator.getKeyValue(result.get().getKey());
			if(value == null)
				HttpPostman.send(routingContext, HttpErrors.ERROR_NOTFOUND_CODE);
			else
				HttpPostman.sendJson(routingContext, new JsonObject().put("key", result.get().getKey()).put("value", value));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleRemoveValueFromStorageRequest(RoutingContext routingContext) {
		Optional<RemoveKeyValueRequest> result = KeyValueInputValidator.parseAndValidateRemoveKeyValueRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}

		try {
			String value = systemCoordinator.removeKeyValue(result.get().getKey());
			if(value == null)
				HttpPostman.send(routingContext, HttpErrors.ERROR_NOTFOUND_CODE);
			else
				HttpPostman.sendJson(routingContext, new JsonObject().put("key", result.get().getKey()).put("value", value));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleUpdateTtlInStorageRequest(RoutingContext routingContext) {
		Optional<UpdateKeyValueTtlRequest> result = KeyValueInputValidator.parseAndValidateUpdateKeyValueTtlRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}

		try {
			if(systemCoordinator.updateKeyValueTtl(result.get().getKey(), result.get().getTtl()))
				HttpPostman.sendOK(routingContext);
			else
				HttpPostman.send(routingContext, HttpErrors.ERROR_NOTFOUND_CODE);
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

}
