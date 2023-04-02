package au.com.cascadesoftware.openai.hk2.factory;

import au.com.cascadesoftware.config.hk2.factory.AbstractConfigFactory;
import au.com.cascadesoftware.config.service.ConfigService;
import au.com.cascadesoftware.openai.model.OpenAIConfig;
import jakarta.inject.Inject;

public class OpenAIConfigFactory extends AbstractConfigFactory<OpenAIConfig> {

	private static final String OPEN_AI_CONFIG_LOCATION = "openai-config.json";

	@Inject
	protected OpenAIConfigFactory(final ConfigService configService) {
		super(configService);
	}

	@Override
	protected String getLocation() {
		return OPEN_AI_CONFIG_LOCATION;
	}

	@Override
	protected Class<OpenAIConfig> getType() {
		return OpenAIConfig.class;
	}

}
