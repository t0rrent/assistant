package au.com.cascadesoftware.voice.hk2.factory;

import au.com.cascadesoftware.config.hk2.factory.AbstractConfigFactory;
import au.com.cascadesoftware.config.service.ConfigService;
import au.com.cascadesoftware.voice.model.VoskConfig;
import jakarta.inject.Inject;

public class VoskConfigFactory extends AbstractConfigFactory<VoskConfig> {

	private static final String VOSK_CONFIG_LOCATION = "vosk-config.json";

	@Inject
	public VoskConfigFactory(final ConfigService configService) {
		super(configService);
	}
	
	protected String getLocation() {
		return VOSK_CONFIG_LOCATION;
	}

	@Override
	protected Class<VoskConfig> getType() {
		return VoskConfig.class;
	}

}
