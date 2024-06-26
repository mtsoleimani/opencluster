package io.taranis.opencluster.server.http.request;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.taranis.opencluster.common.utils.HttpUtils;
import io.vertx.ext.web.RoutingContext;

public class ServiceInputValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceInputValidator.class);
	
	private ServiceInputValidator() {
		
	}
	
	public static Optional<RegisterServiceRequest> parseAndValidateRegisterServiceRequest(RoutingContext routingContext) {
		try {
			RegisterServiceRequest obj = HttpUtils.getJson(routingContext).mapTo(RegisterServiceRequest.class);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}

	public static Optional<UnregisterServiceRequest> parseAndValidateUnregisterServiceRequest(RoutingContext routingContext) {
		try {
			UnregisterServiceRequest obj = HttpUtils.getJson(routingContext).mapTo(UnregisterServiceRequest.class);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}

	
	public static Optional<GetServicesByIdRequest> parseAndValidateGetServicesByIdRequest(RoutingContext routingContext) {
		try {
			String id = routingContext.request().getParam("id");
			GetServicesByIdRequest obj = new GetServicesByIdRequest(id);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}
	
	public static Optional<GetServicesRequest> parseAndValidateGetServicesRequest(RoutingContext routingContext) {
		try {
			String serviceName = routingContext.request().getParam("name");
			String tags = routingContext.request().getParam("tags");
			String clusterName = routingContext.request().getParam("cluster");
			
			GetServicesRequest obj = new GetServicesRequest(serviceName, tags, clusterName);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}
	
	public static Optional<ServicesDiscoveryRequest> parseAndValidateServicesDiscoveryRequest(RoutingContext routingContext) {
		try {
			String serviceName = routingContext.request().getParam("name");
			String clusterName = routingContext.request().getParam("cluster");
			
			ServicesDiscoveryRequest obj = new ServicesDiscoveryRequest(serviceName, clusterName);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}
	
	public static Optional<ServicePingRequest> parseAndValidateServicePingRequest(RoutingContext routingContext) {
		try {
			String serviceId = routingContext.request().getParam("id");
			String clusterName = routingContext.request().getParam("cluster");
			
			ServicePingRequest obj = new ServicePingRequest(serviceId, clusterName);
			return obj.validate() ? Optional.of(obj) : Optional.empty();
		} catch(Exception e) {
			logger.warn(e.getLocalizedMessage(), e);
		}

		return Optional.empty();
	}
	
}
