package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Graphics;

public class GUIOverlay extends GUI {

	public GUIOverlay(Window window, Boundary bounds, int zIndex) {
		super(window, bounds);
		setOverlayZIndex(zIndex);
	}

	public GUIOverlay(Window window, Boundary bounds) {
		super(window, bounds);
	}
	
	@Override
	protected void overOverlayDraw(Graphics graphics) {
		super.render(graphics);
	}
	
	
	@Override
	public void render(Graphics graphics){}

}
