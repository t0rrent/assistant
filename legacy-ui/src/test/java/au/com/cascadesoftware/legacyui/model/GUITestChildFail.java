package au.com.cascadesoftware.legacyui.model;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.legacyui.GuiIT;
import au.com.cascadesoftware.testutil.service.MockService;
import jakarta.inject.Inject;

public class GUITestChildFail extends GUI {

	@Inject
	public GUITestChildFail(final Window window, final Boundary bounds, final MockService mockService, final GuiIT anUninjectableClass) {
		super(window, bounds);
		mockService.exampleCall();
	}

}
