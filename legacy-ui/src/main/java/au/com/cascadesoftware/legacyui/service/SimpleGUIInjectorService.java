package au.com.cascadesoftware.legacyui.service;

import org.glassfish.hk2.api.ServiceLocator;

import jakarta.inject.Inject;

public class SimpleGUIInjectorService implements GUIInjectorService {
	
	private final ServiceLocator serviceLocator;
	
	@Inject
	public SimpleGUIInjectorService(final ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}

	@Override
	public <C> C createInjectable(Class<C> type) {
		return this.serviceLocator.create(type);
	}

}
