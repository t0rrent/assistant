package au.com.cascadesoftware.json.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.json.hk2.factory.ObjectMapperFactory;
import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.json.service.SimpleJsonService;
import jakarta.inject.Singleton;

public class JsonModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bindFactory(ObjectMapperFactory.class).to(ObjectMapper.class).in(Singleton.class);
		bind(SimpleJsonService.class).to(JsonService.class).in(Singleton.class);
	}

}
