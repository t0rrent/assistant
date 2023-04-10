package au.com.cascadesoftware.assistant.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.assistant.service.AssistantLifeCycle;
import au.com.cascadesoftware.assistant.service.ConversationLifeCycle;
import au.com.cascadesoftware.assistant.service.TextboxAccessorService;
import au.com.cascadesoftware.assistant.service.TextboxService;
import au.com.cascadesoftware.assistant.ui.GUIAssistant;
import au.com.cascadesoftware.engine4.service.LifeCycle;
import jakarta.inject.Singleton;

public class AssistantModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bindAsContract(GUIAssistant.class).in(Singleton.class);
		bindAsContract(AssistantLifeCycle.class).to(LifeCycle.class).in(Singleton.class);
		bindAsContract(ConversationLifeCycle.class).to(LifeCycle.class).in(Singleton.class);
		
		bindAsContract(TextboxAccessorService.class).to(TextboxService.class).in(Singleton.class);
	}

}
