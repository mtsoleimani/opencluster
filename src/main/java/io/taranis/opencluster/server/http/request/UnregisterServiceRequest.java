package io.taranis.opencluster.server.http.request;

import io.taranis.opencluster.common.utils.StringUtils;
import io.taranis.opencluster.service.ClusterOptions;
import io.taranis.opencluster.service.Service;

public class UnregisterServiceRequest extends Service implements InputValidator {

	@Override
	public boolean validate() {
		
		if(clusterOptions != null 
		&& StringUtils.isNullOrEmpty(clusterOptions.getClusterName()))
			clusterOptions = null;
		else if(!validate(clusterOptions))
			return false;
		
		return(!StringUtils.isNullOrEmpty(id));
	}
	
	private boolean validate(ClusterOptions clusterOptions) {
		if(clusterOptions == null)
			return true;
		return !StringUtils.isNullOrEmpty(clusterOptions.getClusterName());
	}

}
