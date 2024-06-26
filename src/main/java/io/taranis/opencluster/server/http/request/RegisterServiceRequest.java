package io.taranis.opencluster.server.http.request;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.core.model.ClusterOptions;
import io.taranis.opencluster.core.model.HealthCheckOptions;
import io.taranis.opencluster.core.model.KeepAliveOptions;
import io.taranis.opencluster.core.model.Service;

public class RegisterServiceRequest extends Service implements InputValidator {

	@Override
	public boolean validate() {
		if(StringUtils.isNullOrEmpty(name))
			return false;
			
		if(StringUtils.isNullOrEmpty(id))
			id = null;
		
		if(StringUtils.isNullOrEmpty(address))
			address = null;
		
		if(tags != null)
			tags = tags.stream().filter(i -> !StringUtils.isNullOrEmpty(i)).toList();
		
		registeredAt = null;
		serviceStatus = null;
		lastSeen = null;
		
		
		if(purgeIfPending == null)
			purgeIfPending = false;
		
		if(healthCheckOptions != null 
		&& StringUtils.isNullOrEmpty(healthCheckOptions.getHttp())
		&& StringUtils.isNullOrEmpty(healthCheckOptions.getTcp()))
			healthCheckOptions = null;
		else if(!validate(healthCheckOptions))
			return false;
		
		
		if(keepAliveOptions != null && !validate(keepAliveOptions))
			return false;
		
		
		if(clusterOptions != null 
		&& StringUtils.isNullOrEmpty(clusterOptions.getClusterName()))
			clusterOptions = null;
		else if(!validate(clusterOptions))
			return false;
		
		return true;
	}
	
	
	private boolean validate(HealthCheckOptions healthCheckOptions) {
		if(healthCheckOptions == null)
			return true;
		
		if(StringUtils.isNullOrEmpty(healthCheckOptions.getHttp())
		&& StringUtils.isNullOrEmpty(healthCheckOptions.getTcp()))
			return true;
				
		if(!StringUtils.isNullOrEmpty(healthCheckOptions.getTcp())) {
			if(healthCheckOptions.getPort() <= 0 || healthCheckOptions.getPort() > 65535)
				return false;
		}
		
		return (healthCheckOptions.getTtl() > 0 || healthCheckOptions.getInterval() > 0);
	}

	private boolean validate(KeepAliveOptions keepAliveOptions) {
		if(keepAliveOptions == null)
			return true;
		
		return (keepAliveOptions.getTtl() > 0);
	}

	private boolean validate(ClusterOptions clusterOptions) {
		if(clusterOptions == null)
			return true;
		
		return !StringUtils.isNullOrEmpty(clusterOptions.getClusterName());
	}

}
