package au.com.cascadesoftware.json.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.json.service.SimpleJsonService;
import au.com.cascadesoftware.util.service.AnnotationService;
import au.com.cascadesoftware.util.service.SimpleAnnotationService;
import jakarta.inject.Singleton;

public class JsonTestBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new ObjectMapper()).to(ObjectMapper.class);
		bind(SimpleJsonService.class).to(JsonService.class).in(Singleton.class);
		bind(SimpleAnnotationService.class).to(AnnotationService.class).in(Singleton.class);
	}

}
