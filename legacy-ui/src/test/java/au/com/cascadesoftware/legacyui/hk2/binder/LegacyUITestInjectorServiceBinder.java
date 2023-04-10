package au.com.cascadesoftware.legacyui.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.legacyui.hk2.factory.WindowMockFactory;
import au.com.cascadesoftware.legacyui.service.GUIInjectorService;
import au.com.cascadesoftware.legacyui.service.TestGUIInjectorService;
import jakarta.inject.Singleton;

public class LegacyUITestInjectorServiceBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new Boundary()).to(Boundary.class);
		bindFactory(WindowMockFactory.class).to(Window.class).in(Singleton.class);
		bindAsContract(TestGUIInjectorService.class).to(GUIInjectorService.class).in(Singleton.class);
	}

}
