package au.com.cascadesoftware.openai.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.openai.hk2.factory.OpenAIConfigFactory;
import au.com.cascadesoftware.openai.model.OpenAIConfig;
import au.com.cascadesoftware.openai.service.GptQueryService;
import au.com.cascadesoftware.openai.service.SimpleGptQueryService;
import jakarta.inject.Singleton;

public class OpenAiModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bindFactory(OpenAIConfigFactory.class).to(OpenAIConfig.class).in(Singleton.class);
		bind(SimpleGptQueryService.class).to(GptQueryService.class).in(Singleton.class);
	}

}
