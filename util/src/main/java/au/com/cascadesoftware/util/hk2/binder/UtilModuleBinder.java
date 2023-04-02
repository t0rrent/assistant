package au.com.cascadesoftware.util.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.util.service.AnnotationService;
import au.com.cascadesoftware.util.service.CachedTransformService;
import au.com.cascadesoftware.util.service.CachingService;
import au.com.cascadesoftware.util.service.SimpleAnnotationService;
import jakarta.inject.Singleton;

public class UtilModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(CachedTransformService.class).to(CachingService.class).in(Singleton.class);
		bind(SimpleAnnotationService.class).to(AnnotationService.class).in(Singleton.class);
	}

}
