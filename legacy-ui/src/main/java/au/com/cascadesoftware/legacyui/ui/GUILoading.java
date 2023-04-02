package au.com.cascadesoftware.legacyui.ui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.gui.Alignment;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.GUIGif;
import au.com.cascadesoftware.engine3.gui.GUIText;
import au.com.cascadesoftware.engine3.gui.Scalar;
import jakarta.inject.Inject;

public class GUILoading extends GUI {
	
	private final GUI gears;
	private final GUI loading;

	@Inject
	public GUILoading(final Window window, final Boundary bounds) {
		super(window, bounds);
		gears = new GUIGif(window, new Boundary(), "loading-gears.gif");
		loading = new GUIText(window, new Boundary(), Color.DARK_GARY, "Loading...");
		setBackground(Color.WHITE);
		addGUI(gears);
		addGUI(loading);
		onResize();
	}
	
	@Override
	protected void onResize() {
		final float h = 300f / getOnScreenBounds().height;
		gears.setBounds(new Boundary(new Rectf(0, 0, h, h), Scalar.VERTICAL, Alignment.MIDDLE_CENTER));
		loading.setBounds(new Boundary(new Rectf(0, -h * 2 / 3, h / 3 , 1)));
	}

}
