package au.com.cascadesoftware.assistant.service;

import au.com.cascadesoftware.assistant.ui.GUIAssistant;
import au.com.cascadesoftware.engine4.service.LifeCycle;
import au.com.cascadesoftware.legacyui.service.UILifeCycle;
import jakarta.inject.Inject;

public class AssistantLifeCycle implements LifeCycle {
	
	private final GUIAssistant userInterface;
	private final UILifeCycle uiLifeCycle;

	@Inject
	public AssistantLifeCycle(
			final GUIAssistant userInterface,
			final UILifeCycle uiLifeCycle
	) {
		this.userInterface = userInterface;
		this.uiLifeCycle = uiLifeCycle;
	}

	@Override
	public void start() {
		uiLifeCycle.setTopUI(userInterface);
		userInterface.init();
	}

	@Override
	public void stop() {
		
	}

}
