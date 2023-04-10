package au.com.cascadesoftware.legacyui.hk2.binder;

import au.com.cascadesoftware.legacyui.service.GUIInjectorService;
import au.com.cascadesoftware.legacyui.service.TestGUIInjectorService;
import jakarta.inject.Singleton;

public class LegacyUIShowTestBinder extends LegacyUIModuleBinder {
	
	private static final int HIGH_RANK = 100;

	@Override
	protected void configure() {
		super.configure();
		bindAsContract(TestGUIInjectorService.class).to(GUIInjectorService.class).in(Singleton.class).ranked(HIGH_RANK);
	}

}
