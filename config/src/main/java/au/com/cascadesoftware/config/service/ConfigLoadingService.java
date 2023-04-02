package au.com.cascadesoftware.config.service;

import java.io.File;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.util.IOUtils;
import au.com.cascadesoftware.util.service.CachingService;
import jakarta.inject.Inject;

public class ConfigLoadingService implements ConfigService { // TODO tests
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoadingService.class);

	private final CachingService cachingService;
	
	private final JsonService jsonService;
	
	private final BiFunction<String, Class<?>, ?> configTransform;
	
	@Inject
	public ConfigLoadingService(final CachingService cachingService, final JsonService jsonService) {
		this.cachingService = cachingService;
		this.jsonService = jsonService;
		this.configTransform = createConfigTransform();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T loadConfig(final String location, final Class<T> type) {
		return (T) configTransform.apply(location, type);
	}

	private BiFunction<String, Class<?>, ?> createConfigTransform() {
		final Function<String, String> fileLoader = cachingService.createCachedTransform(this::loadFromFile);
		return (location, type) -> {
			final String jsonContent = fileLoader.apply(location);
			if (jsonContent == null) {
				return null;
			}
			return jsonService.extract(jsonContent, type);
		};
	}
	
	private String loadFromFile(final String location) {
		final File file = new File(location);
		try {
			return IOUtils.readFileAsString(file);
		} catch (final IOException e) {
			LOGGER.error("The following config file could not be read:\n" + file.getAbsolutePath());
			e.printStackTrace();
			return null;
		}
	}

}
