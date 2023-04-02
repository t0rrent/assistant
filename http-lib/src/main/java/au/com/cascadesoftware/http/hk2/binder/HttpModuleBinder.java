package au.com.cascadesoftware.http.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.http.service.HttpService;
import au.com.cascadesoftware.http.service.SimpleHttpService;
import au.com.cascadesoftware.http.service.SimpleUrlService;
import au.com.cascadesoftware.http.service.UrlService;
import jakarta.inject.Singleton;

public class HttpModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(SimpleHttpService.class).to(HttpService.class).in(Singleton.class);
		bind(SimpleUrlService.class).to(UrlService.class).in(Singleton.class);
	}

}
