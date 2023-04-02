package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Canvas;
import au.com.cascadesoftware.engine3.graphics.Graphics;

public class GUIOven extends GUI {
	
	protected Canvas canvas;
	private boolean bakeOnResize, antiAlias;

	public GUIOven(Window window, Boundary bounds) {
		super(window, bounds);
	}
	
	public GUIOven setAntiAlias(boolean aa){
		this.antiAlias = aa;
		return this;
	}

	protected void redrawCanvas() {
		if(canvas != null) super.render(canvas.getGraphics());
	}

	protected void clearCanvas() {
		canvas.clear();
	}
	
	@Override
	protected void onResize() {
		if(bakeOnResize) bake();
	}
	
	@Override
	public void setBounds(Boundary bounds) {
		super.setBounds(bounds);
		if(canvas == null) return;
		canvas.resize(getRenderBounds(), new Vector2i());
	}
	
	@Override
	public Recti getRenderBounds() {
		Recti out = super.getRenderBounds();
		out.x = 0;
		out.y = 0;
		return out;
	}
	
	@Override
	public void render(Graphics graphics) {
		if(canvas == null){
			createCanvas(graphics);
			redrawCanvas();
		}
		if(canvas != null) canvas.draw(graphics);
	}
	
	protected void createCanvas(Graphics graphics) {
		canvas = graphics.createCanvas(super.getRenderBounds(), new Vector2i(), antiAlias);
	}

	public void bake(){
		canvas = null;
	}
	
	public GUIOven setBakeOnResize(boolean value){
		this.bakeOnResize = value;
		return this;
	}

	public void setRotation(Vector2d rotationCentre, double r) {
		if(canvas != null) {
			canvas.setRotation(rotationCentre, r);
		}
	}

}
