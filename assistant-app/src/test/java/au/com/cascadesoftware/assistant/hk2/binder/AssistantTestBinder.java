package au.com.cascadesoftware.assistant.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.assistant.service.TextboxAccessorService;
import au.com.cascadesoftware.assistant.service.TextboxService;
import jakarta.inject.Singleton;

public class AssistantTestBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bindAsContract(TextboxAccessorService.class).to(TextboxService.class).in(Singleton.class);
	}

}
