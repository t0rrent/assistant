package au.com.cascadesoftware.assistant.ui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Alignment;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.Scalar;
import jakarta.inject.Inject;

public class GUIMessageView extends GUI {
	
	private static final int TEXTBOX_HEIGHT = 60;

	private GUI chatBox;
	
	private GUIConversationTextbox textBox;
	
	@Inject
	public GUIMessageView(final Window window, final Boundary boundary) {
		super(window, boundary);
	}
	
	public void initialize() {
		chatBox = createInjectable(GUIChatbox.class);
		textBox = createInjectable(GUIConversationTextbox.class);
		addGUI(chatBox);
		addGUI(textBox);
		onResize();
	}
	
	@Override
	protected void onResize() {
		if (chatBox == null) {
			return;
		}
		final float textboxHeight = TEXTBOX_HEIGHT * 1f / getOnScreenBounds().height;
		chatBox.setBounds(new Boundary(
				new Rectf(0, 0, 1, 1 - textboxHeight),
				Scalar.STRETCHED,
				Alignment.TOP_CENTER
		));
		textBox.setBounds(new Boundary(
				new Rectf(0, 0, 1, textboxHeight),
				Scalar.STRETCHED,
				Alignment.BOTTOM_CENTER
		));
		textBox.resizeElements();
	}
	
}
