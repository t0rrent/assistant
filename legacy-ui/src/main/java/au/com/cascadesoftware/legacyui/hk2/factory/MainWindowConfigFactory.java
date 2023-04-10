package au.com.cascadesoftware.legacyui.hk2.factory;

import org.glassfish.hk2.api.Factory;

import au.com.cascadesoftware.config.service.ConfigService;
import au.com.cascadesoftware.legacyui.config.WindowConfig;
import jakarta.inject.Inject;

public class MainWindowConfigFactory implements Factory<WindowConfig> {
	
	private final ConfigService configService;
	
	@Inject
	public MainWindowConfigFactory(final ConfigService configService) {
		this.configService = configService;
	}

	@Override
	public WindowConfig provide() {
		return configService.loadConfig(WindowConfig.MAIN_WINDOW_CONFIG, WindowConfig.class);
	}

	@Override
	public void dispose(WindowConfig instance) {
		
	}

}
