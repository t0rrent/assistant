package au.com.cascadesoftware.assistant.ui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import jakarta.inject.Inject;

public class GUIAssistant extends GUI {

	@Inject
	public GUIAssistant(
			final Window window, 
			final Boundary bounds
	) {
		super(window, bounds);
	}
	
	public void init() {
		final GUIMessageView messageView = createInjectable(GUIMessageView.class);
		addGUI(messageView);
		messageView.initialize();
	}

}
