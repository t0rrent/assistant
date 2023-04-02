package au.com.cascadesoftware.json.hk2.factory;

import org.glassfish.hk2.api.Factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperFactory implements Factory<ObjectMapper> {

	@Override
	public ObjectMapper provide() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper;
	}

	@Override
	public void dispose(final ObjectMapper instance) {
	}

}
