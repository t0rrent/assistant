package au.com.cascadesoftware.legacyui.model;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.testutil.service.MockService;
import jakarta.inject.Inject;

public class GUITestChild extends GUI {

	@Inject
	public GUITestChild(final Window window, final Boundary bounds, final MockService mockService) {
		super(window, bounds);
		mockService.exampleCall();
	}

}
