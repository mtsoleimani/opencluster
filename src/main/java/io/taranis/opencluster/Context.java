package io.taranis.opencluster;

import io.taranis.opencluster.service.ServiceRegistry;
import io.taranis.opencluster.service.ServiceRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class Context {

	private ServiceRepository serviceRepository;
	
	private ServiceRegistry serviceRegistry;
}
