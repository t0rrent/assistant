package au.com.cascadesoftware.legacyui.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.engine3.desktop.display.WindowDesktop;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine4.service.CriticalLifeCycle;
import au.com.cascadesoftware.legacyui.config.WindowConfig;
import au.com.cascadesoftware.legacyui.hk2.factory.MainWindowConfigFactory;
import au.com.cascadesoftware.legacyui.service.GUICalculationsService;
import au.com.cascadesoftware.legacyui.service.GUIInjectorService;
import au.com.cascadesoftware.legacyui.service.SimpleGUIInjectorService;
import au.com.cascadesoftware.legacyui.service.UILifeCycle;
import au.com.cascadesoftware.legacyui.ui.GUILoading;
import jakarta.inject.Singleton;

public class LegacyUIModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new Boundary()).to(Boundary.class);
		bindFactory(MainWindowConfigFactory.class).to(WindowConfig.class).in(Singleton.class);
		bind(WindowDesktop.class).to(Window.class).in(Singleton.class);
		bind(GUILoading.class).to(GUI.class).in(Singleton.class).ranked(GUI.DEFAULT_BIND_RANK);
		
		bindAsContract(UILifeCycle.class).to(CriticalLifeCycle.class).in(Singleton.class);
		bindAsContract(GUICalculationsService.class).in(Singleton.class);
		bind(SimpleGUIInjectorService.class).to(GUIInjectorService.class).in(Singleton.class);
	}

}
