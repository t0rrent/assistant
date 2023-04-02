package au.com.cascadesoftware.legacyui.model;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;

public class GUITestParent extends GUI {

	public  GUITestParent(final Window window, final Boundary bounds) {
		super(window, bounds);
	}
	
	public <C extends GUI> void addAnInjectableChild(final Class<C> injectableChild) {
		final C child = createInjectable(injectableChild);
		addGUI(child);
	}

}
