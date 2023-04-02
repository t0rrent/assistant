package au.com.cascadesoftware.legacyui.service;

import java.io.File;
import java.util.function.Function;

import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.legacyui.config.WindowConfig;
import au.com.cascadesoftware.util.service.CachingService;
import jakarta.inject.Inject;

public class WindowConfigLoadingService implements WindowConfigService {
	
	private final Function<String, WindowConfig> cachedWindowConfigTransform;
	private final JsonService jsonService;
	
	@Inject
	public WindowConfigLoadingService(
			final CachingService cachingService,
			final JsonService jsonService
	) {
		this.cachedWindowConfigTransform = cachingService.createCachedTransform(this::loadWinwdowConfigFromFile);
		this.jsonService = jsonService;
	}

	@Override
	public WindowConfig loadMainWindowConfg() {
		return loadWindowConfg(WindowConfig.MAIN_WINDOW_CONFIG);
	}

	@Override
	public WindowConfig loadWindowConfg(final String location) {
		return cachedWindowConfigTransform.apply(location);
	}
	
	private WindowConfig loadWinwdowConfigFromFile(final String location) {
		return jsonService.extractFromFile(new File(location), WindowConfig.class);
	}

}
