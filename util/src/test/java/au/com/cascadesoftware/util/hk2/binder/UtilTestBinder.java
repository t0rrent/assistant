package au.com.cascadesoftware.util.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.util.service.CachedTransformService;
import au.com.cascadesoftware.util.service.CachingService;
import jakarta.inject.Singleton;

public class UtilTestBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(CachedTransformService.class).to(CachingService.class).in(Singleton.class);
	}

}
