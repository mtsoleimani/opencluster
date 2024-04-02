package io.taranis.opencluster.server.http.handlers.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.core.SystemCoordinator;
import io.taranis.opencluster.core.model.Service;
import io.taranis.opencluster.common.utils.HttpUtils;
import io.taranis.opencluster.exception.InvalidMessageException;
import io.taranis.opencluster.exception.NotFoundException;
import io.taranis.opencluster.server.http.HttpPostman;
import io.taranis.opencluster.server.http.handlers.HttpServiceHandler;
import io.taranis.opencluster.server.http.request.GetServicesByIdRequest;
import io.taranis.opencluster.server.http.request.GetServicesRequest;
import io.taranis.opencluster.server.http.request.RegisterServiceRequest;
import io.taranis.opencluster.server.http.request.ServiceInputValidator;
import io.taranis.opencluster.server.http.request.ServicePingRequest;
import io.taranis.opencluster.server.http.request.ServicesDiscoveryRequest;
import io.taranis.opencluster.server.http.request.UnregisterServiceRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class HttpServiceHandlerImpl implements HttpServiceHandler {
	
	private final Logger logger = LoggerFactory.getLogger(HttpServiceHandlerImpl.class);
	
	private final SystemCoordinator systemCoordinator;
	
	public HttpServiceHandlerImpl(SystemCoordinator systemCoordinator) {
		this.systemCoordinator = systemCoordinator;
	}

	@Override
	public void handleRegisterServiceRequest(RoutingContext routingContext) {
		Optional<RegisterServiceRequest> result = ServiceInputValidator.parseAndValidateRegisterServiceRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}


		try {
			if(StringUtils.isNullOrEmpty(result.get().getAddress()))
				result.get().setAddress(HttpUtils.getRemoteHost(routingContext));

			Service service = systemCoordinator.registerService(result.get());
			HttpPostman.sendJson(routingContext, JsonObject.mapFrom(service));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleUnregisterServiceRequest(RoutingContext routingContext) {
		Optional<UnregisterServiceRequest> result = ServiceInputValidator.parseAndValidateUnregisterServiceRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}
		
		try {
			Optional<Service> service = systemCoordinator.unregisterService(result.get());
			if(service.isEmpty())
				throw new NotFoundException();
			
			HttpPostman.sendJson(routingContext, JsonObject.mapFrom(service.get()));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleGetServiceByIdRequest(RoutingContext routingContext) {
		Optional<GetServicesByIdRequest> result = ServiceInputValidator.parseAndValidateGetServicesByIdRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}	
		
		try {
			Optional<Service> service = systemCoordinator.getService(result.get().getId());
			if(service.isEmpty())
				throw new NotFoundException();
			
			HttpPostman.sendJson(routingContext, JsonObject.mapFrom(service.get()));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleGetServicesRequest(RoutingContext routingContext) {
		Optional<GetServicesRequest> result = ServiceInputValidator.parseAndValidateGetServicesRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}	
		
		try {
			
			Optional<List<Service>> list = systemCoordinator.getServices(result.get().getClusterName(), result.get().getTags(), result.get().getServiceName());
			if(list.isEmpty())
				throw new NotFoundException();
			
			HttpPostman.sendJson(routingContext, new JsonObject().put("list", list.get()));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handleDiscoverMeRequest(RoutingContext routingContext) {
		HttpPostman.sendJson(routingContext, new JsonObject().put("ipaddress", HttpUtils.getRemoteHost(routingContext)));		
	}

	@Override
	public void handleServiceDiscoveryRequest(RoutingContext routingContext) {
		Optional<ServicesDiscoveryRequest> result = ServiceInputValidator.parseAndValidateServicesDiscoveryRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}	
		
		try {
			
			Optional<List<Service>> list = systemCoordinator.serviceDiscovery(result.get().getClusterName(), result.get().getServiceName());
			if(list.isEmpty())
				throw new NotFoundException();
			
			HttpPostman.sendJson(routingContext, new JsonObject().put("list", list.get()));
		} catch (Exception e) {
			logger.info(e.getLocalizedMessage(), e);
			HttpPostman.handleException(routingContext, e);
		}
	}

	@Override
	public void handlePingServiceRequest(RoutingContext routingContext) {
		Optional<ServicePingRequest> result = ServiceInputValidator.parseAndValidateServicePingRequest(routingContext);
		if(result.isEmpty()) {
			HttpPostman.handleException(routingContext, new InvalidMessageException());
			return;
		}	
		
		HttpPostman.sendOK(routingContext);
		systemCoordinator.onKeepAlive(result.get().getId(), result.get().getClusterName());
	}

}
