package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.graphics.Paint.StrokeType;

public class GUIShapeRoundedRectangle extends GUIShape {

	private StrokeType strokeType;
	private float roundedness;
	private int roundednessPixel;

	public GUIShapeRoundedRectangle(Window window, Boundary bounds, Color shapeColor, int p) {
		super(window, bounds, shapeColor);
		roundednessPixel = p;
		roundedness = -1;
		strokeType = StrokeType.SOLID;
	}

	public GUIShapeRoundedRectangle(Window window, Boundary bounds, Color shapeColor, float p) {
		super(window, bounds, shapeColor);
		roundedness = p;
		roundednessPixel = -1;
		strokeType = StrokeType.SOLID;
	}

	@Override
	protected void draw(Graphics graphics) {
		Paint paint = new Paint();
		int r = roundednessPixel;
		if(r < 0){
			if(this.getRenderBounds().width < this.getRenderBounds().height){
				r = (int) (roundedness*this.getRenderBounds().width);
			}else{
				r = (int) (roundedness*this.getRenderBounds().height);
			}
		}
		if(this.getFill()){
			paint.setFill(true);
			paint.setColor(this.getShapeColor());
			graphics.setPaint(paint);
			graphics.drawRect(getRenderBounds().x + r, getRenderBounds().y, getRenderBounds().width - r*2, getRenderBounds().height);
			graphics.drawRect(getRenderBounds().x, getRenderBounds().y + r, r, getRenderBounds().height - r*2);
			graphics.drawRect(getRenderBounds().x + getRenderBounds().width - r, getRenderBounds().y + r, r, getRenderBounds().height - r*2);
			if(r > 1){
				graphics.drawArc(getRenderBounds().x, getRenderBounds().y, r*2, r*2, 90, 90);
				graphics.drawArc(getRenderBounds().x + getRenderBounds().width - r*2, getRenderBounds().y, r*2, r*2, 0, 90);
				graphics.drawArc(getRenderBounds().x, getRenderBounds().y + getRenderBounds().height - r*2, r*2, r*2, 180, 90);
				graphics.drawArc(getRenderBounds().x + getRenderBounds().width - r*2, getRenderBounds().y + getRenderBounds().height - r*2, r*2, r*2, -90, 90);
			}
		}
		if(getBorderWidth() > 0){
			paint.setFill(false);
			paint.setColor(getBorderColor());
			paint.setStrokeWidth(getBorderWidth());
			paint.setStrokeType(strokeType);
			graphics.setPaint(paint);
			graphics.drawLine(getRenderBounds().x, getRenderBounds().y + r, getRenderBounds().x, getRenderBounds().y + getRenderBounds().height - r);
			graphics.drawLine(getRenderBounds().x + getRenderBounds().width, getRenderBounds().y + r, getRenderBounds().x + getRenderBounds().width, getRenderBounds().y + getRenderBounds().height - r);
			graphics.drawLine(getRenderBounds().x + r, getRenderBounds().y, getRenderBounds().x + getRenderBounds().width - r, getRenderBounds().y);
			graphics.drawLine(getRenderBounds().x + r, getRenderBounds().y + getRenderBounds().height, getRenderBounds().x + getRenderBounds().width - r, getRenderBounds().y + getRenderBounds().height);
			paint.setStrokeWidth(getBorderWidth()*1.05f);
			paint.setStrokeType(strokeType);
			graphics.setPaint(paint);
			graphics.drawArc(getRenderBounds().x, getRenderBounds().y, r*2, r*2, 90, 90);
			graphics.drawArc(getRenderBounds().x + getRenderBounds().width - r*2, getRenderBounds().y, r*2, r*2, 0, 90);
			graphics.drawArc(getRenderBounds().x, getRenderBounds().y + getRenderBounds().height - r*2, r*2, r*2, 180, 90);
			graphics.drawArc(getRenderBounds().x + getRenderBounds().width - r*2, getRenderBounds().y + getRenderBounds().height - r*2, r*2, r*2, -90, 90);
		}
	}

	public GUIShapeRoundedRectangle setStrokeType(StrokeType st) {
		strokeType = st;
		return this;
	}

	public void setRoundedCorners(int r) {
		roundednessPixel = r;
		roundedness = -1;
	}
	
	@Deprecated
	public void setRoundedCorners(float r){
		roundedness = r;
		roundednessPixel = -1;
	}
	
}
