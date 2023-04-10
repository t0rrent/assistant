package au.com.cascadesoftware.config.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.config.service.ConfigLoadingService;
import au.com.cascadesoftware.config.service.ConfigService;
import jakarta.inject.Singleton;

public class ConfigTestBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(ConfigLoadingService.class).to(ConfigService.class).in(Singleton.class);
	}

}
