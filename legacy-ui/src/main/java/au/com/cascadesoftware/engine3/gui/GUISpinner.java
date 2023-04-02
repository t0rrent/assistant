package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;

public class GUISpinner extends GUI {
	
	private float loadPhase;
	private long lastTime;
	private GUIOven oven;
	private Color spinnerColor;
	private GUI drawableGUI;
	private int fixedThickness;
	
	public GUISpinner(Window window, Boundary bounds) {
		this(window, bounds, new Color("#222"));
	}

	public GUISpinner(Window window, Boundary bounds, Color color) {
		super(window, bounds);
		this.spinnerColor = color;
		lastTime = System.nanoTime();
		fixedThickness = -1;
		oven = new GUIOven(window, new Boundary()).setAntiAlias(true);
		addGUI(oven);
		drawableGUI = new GUI(window, new Boundary()) {
			@Override
			protected void draw(Graphics graphics) {
				GUISpinner.this.drawSpinner(graphics);
			}
		};
		oven.addGUI(drawableGUI);
	}
	
	public GUISpinner setFixedThickness(int fixedThickness) {
		this.fixedThickness = fixedThickness;
		return this;
	}
	
	@Override
	protected void updateInput() {
		long currentTime = System.nanoTime();
		loadPhase += (currentTime - lastTime) / 1000000000f * 1;
		double x = drawableGUI.getOnScreenBounds().x + drawableGUI.getOnScreenBounds().width/2.0;
		double y = drawableGUI.getOnScreenBounds().y + drawableGUI.getOnScreenBounds().height/2.0;
		oven.setRotation(new Vector2d(x, y), (loadPhase % 1) * 2* Math.PI);
		lastTime = currentTime;
	}
	
	protected void drawSpinner(Graphics graphics) {
		int x = drawableGUI.getRenderBounds().x + drawableGUI.getRenderBounds().width/2;
		int y = drawableGUI.getRenderBounds().y + drawableGUI.getRenderBounds().height/2;
		int w = drawableGUI.getRenderBounds().width > drawableGUI.getRenderBounds().height ? drawableGUI.getRenderBounds().height/2 : drawableGUI.getRenderBounds().width/2;
		int thickness = w/10;
		if (fixedThickness > 0) {
			thickness = fixedThickness;
		}
		int l = 2*thickness;
		w -= thickness + l;

		Paint paint = new Paint();
		paint.setColor(spinnerColor);
		paint.setStrokeWidth(thickness);
		paint.setFill(false);
		graphics.setPaint(paint);
		
		for(int r : new int[]{0, 180}){
			graphics.drawArc(x - w, y - w, w*2, w*2, (int)(r + w/250), 140);
			double p1x = w, p4x = w, p1y = 0, p2x = w - l, p2y = l, p3x = w + l, p3y = l;
			double theta = r*(Math.PI/180.0);
			
			double tp1x = p1x*Math.cos(theta) - p1y*Math.sin(theta);
			double tp4x = p4x*Math.cos(theta) - p1y*Math.sin(theta);
			double tp1y = p1x*Math.sin(theta) + p1y*Math.cos(theta);
	
			double tp2x = p2x*Math.cos(theta) - p2y*Math.sin(theta);
			double tp2y = p2x*Math.sin(theta) + p2y*Math.cos(theta);
			
			double tp3x = p3x*Math.cos(theta) - p3y*Math.sin(theta);
			double tp3y = p3x*Math.sin(theta) + p3y*Math.cos(theta);
			
			graphics.drawLine((int)(x + tp4x), (int)(y - tp1y), (int)(x + tp2x), (int)(y - tp2y));
			graphics.drawLine((int)(x + tp1x), (int)(y - tp1y), (int)(x + tp3x), (int)(y - tp3y));
		}
	}
	
	@Override
	protected void onResize() {
		oven.bake();
	}

}
