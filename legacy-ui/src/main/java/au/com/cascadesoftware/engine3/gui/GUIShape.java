package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;

public abstract class GUIShape extends GUI {

	private boolean fill;
	private float borderWidth;
	private Color shapeColor, borderColor;

	public GUIShape(Window window, Boundary bounds, Color shapeColor) {
		super(window, bounds);
		this.shapeColor = shapeColor;
		fill = true;
		borderWidth = -1;
	}

	public GUIShape setBorder(float width, Color color) {
		if(width < 0) width = 0;
		this.borderColor = color;
		this.borderWidth = width;
		return this;
	}

	public GUIShape setFill(boolean fill){
		this.fill = fill;
		if(!fill && getBorderWidth() < 0) this.setBorder(1, getShapeColor());
		return this;
	}

	public void setShapeColor(Color shapeColor){
		this.shapeColor = shapeColor;
	}
	
	protected boolean getFill(){
		return fill;
	}
	
	protected Color getShapeColor(){
		return shapeColor;
	}
	
	protected Color getBorderColor(){
		return borderColor;
	}
	
	protected float getBorderWidth(){
		return borderWidth;
	}
	
}
