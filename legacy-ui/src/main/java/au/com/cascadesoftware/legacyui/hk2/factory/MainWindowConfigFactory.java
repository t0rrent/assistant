package au.com.cascadesoftware.legacyui.hk2.factory;

import org.glassfish.hk2.api.Factory;

import au.com.cascadesoftware.legacyui.config.WindowConfig;
import au.com.cascadesoftware.legacyui.service.WindowConfigService;
import jakarta.inject.Inject;

public class MainWindowConfigFactory implements Factory<WindowConfig> {
	
	private final WindowConfigService windowConfigService;
	
	@Inject
	public MainWindowConfigFactory(final WindowConfigService windowConfigService) {
		this.windowConfigService = windowConfigService;
	}

	@Override
	public WindowConfig provide() {
		return windowConfigService.loadMainWindowConfg();
	}

	@Override
	public void dispose(WindowConfig instance) {
		
	}

}
