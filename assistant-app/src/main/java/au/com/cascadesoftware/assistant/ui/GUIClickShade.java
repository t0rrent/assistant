package au.com.cascadesoftware.assistant.ui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.util.MathUtils;

public class GUIClickShade extends GUI {
	
	private final Color fillColor;
	private final float fadeTime;
	
	private long clickTime;

	public GUIClickShade(final Window window, final Boundary bounds, final Color fillColor, final float fadeTime) {
		super(window, bounds);
		this.fillColor = fillColor;
		this.fadeTime = fadeTime;
	}

	public void click() {
		clickTime = System.nanoTime();
	}
	
	@Override
	protected void updateInput() {
		final float fillOpacity = MathUtils.clamp(1 - (System.nanoTime() - clickTime) / 1000000000f / fadeTime, 0, 1);
		final Color fillColor = getFillColor(fillOpacity);
		setBackground(fillColor);
	}

	private Color getFillColor(float fillOpacity) {
		return new Color(
				fillColor.getRedf(),
				fillColor.getGreenf(),
				fillColor.getBluef(),
				fillColor.getAlphaf() * fillOpacity
		);
	}

}
