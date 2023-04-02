package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.graphics.Paint.StrokeType;
import au.com.cascadesoftware.engine3.gui.GUIShapeRegularPolygon.Side;

public class GUIShapeRectangle extends GUIShape {

	private int[] widths;
	private Color borderColor;
	private boolean legacyBorderRender;
	private StrokeType strokeType;

	public GUIShapeRectangle(Window window, Boundary bounds, Color shapeColor) {
		super(window, bounds, shapeColor);
		widths = new int[4];
		strokeType = StrokeType.SOLID;
	}

	@Override
	protected void draw(Graphics graphics) {
		Paint paint = new Paint();
		
		if(this.getFill()){
			paint.setFill(true);
			paint.setColor(this.getShapeColor());
			graphics.setPaint(paint);
			graphics.drawRect(getRenderBounds());
		}
		if(getBorderWidth() > 0){
			if(!legacyBorderRender){
				paint.setFill(true);
				paint.setColor(borderColor);
				int top = getRenderBounds().y;
				int left = getRenderBounds().x;
				int bottom = getRenderBounds().y + getRenderBounds().height;
				int right = getRenderBounds().x + getRenderBounds().width;
				int width = getRenderBounds().width;
				int height = getRenderBounds().height;
				graphics.setPaint(paint);
				if(widths[Side.TOP] > 0) graphics.drawRect(new Recti(left, top, width, widths[Side.TOP]));
				if(widths[Side.BOTTOM] > 0) graphics.drawRect(new Recti(left, bottom - widths[Side.BOTTOM], width, widths[Side.BOTTOM]));
				if(widths[Side.LEFT] > 0) graphics.drawRect(new Recti(left, top, widths[Side.LEFT], height));
				if(widths[Side.RIGHT] > 0) graphics.drawRect(new Recti(right - widths[Side.RIGHT], top, widths[Side.RIGHT], height));
			}else{
				paint.setFill(false);
				paint.setColor(borderColor);
				paint.setStrokeWidth(widths[0]);
				paint.setStrokeType(strokeType);
				graphics.setPaint(paint);
				graphics.drawRect(getOnScreenBounds());
				//graphics.drawRect(getOnScreenBounds().expand(widths[0], widths[0]));
			}
		}
	}

	public GUIShapeRectangle setBorderSide(int side, float width) {
		if(width < 0) width = 0;
		widths[side] = (int) width;
		return this;
	}

	@Override
	public GUIShapeRectangle setBorder(float width, Color color) {
		if(width < 0) width = 0;
		for(int i = 0; i < 4; i++) widths[i] = (int) width;
		borderColor = color;
		super.setBorder(width, color);
		return this;
	}

	public GUIShapeRectangle setBorderColor(Color color) {
		borderColor = color;
		return this;
	}

	public GUIShapeRectangle setStrokeType(StrokeType st) {
		strokeType = st;
		return this;
	}
	
	public GUIShapeRectangle setLegacyBorderRender(boolean b){
		legacyBorderRender = b;
		return this;
	}

}
