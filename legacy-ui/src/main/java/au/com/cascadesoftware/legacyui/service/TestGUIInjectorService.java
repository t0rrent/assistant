package au.com.cascadesoftware.legacyui.service;

import java.util.HashMap;
import java.util.Map;

import org.glassfish.hk2.api.ServiceLocator;

import jakarta.inject.Inject;

public class TestGUIInjectorService extends SimpleGUIInjectorService {
	
	private final Map<Class<?>, Object> interceptions;
	
	@Inject
	public TestGUIInjectorService(final ServiceLocator serviceLocator) {
		super(serviceLocator);
		this.interceptions = new HashMap<>();
	}
	
	public <C> void intercept(final Class<C> type, final C object) {
		interceptions.put(type, object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <C> C createInjectable(final Class<C> type) {
		if (interceptions.containsKey(type)) {
			return (C) interceptions.get(type);
		} else {
			return super.createInjectable(type);
		}
	}

}
