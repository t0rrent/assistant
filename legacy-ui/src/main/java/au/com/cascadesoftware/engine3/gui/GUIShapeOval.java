package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.graphics.Paint.StrokeType;

public class GUIShapeOval extends GUIShape {

	private StrokeType strokeType;

	public GUIShapeOval(Window window, Boundary bounds, Color shapeColor) {
		super(window, bounds, shapeColor);
		strokeType = StrokeType.SOLID;
	}

	@Override
	protected void draw(Graphics graphics) {
		Paint paint = new Paint();
		if(this.getFill()){
			paint.setFill(true);
			paint.setColor(this.getShapeColor());
			graphics.setPaint(paint);
			graphics.drawArc(getRenderBounds().x, getRenderBounds().y, getRenderBounds().width, getRenderBounds().height, 0, 360);
		}
		if(getBorderWidth() > 0){
			paint.setFill(false);
			paint.setColor(getBorderColor());
			paint.setStrokeWidth(getBorderWidth());
			paint.setStrokeType(strokeType);
			graphics.setPaint(paint);
			graphics.drawArc(getRenderBounds().x, getRenderBounds().y, getRenderBounds().width, getRenderBounds().height, 0, 360);
		}
	}

	public GUIShapeOval setStrokeType(StrokeType st) {
		strokeType = st;
		return this;
	}
	
	@Override
	public SpecialClickBounds getSpecialClickBounds() {
		return new SpecialClickBounds(){
			@Override
			public boolean contains(Vector2i p) {
				int h = getOnScreenBounds().x + getOnScreenBounds().width/2;
				int k = getOnScreenBounds().y + getOnScreenBounds().height/2;
				int rx = getOnScreenBounds().width/2;
				int ry = getOnScreenBounds().height/2;
				//System.out.println(rx);
				return (p.x - h)*(p.x - h)*1.0/rx/rx + (p.y - k)*(p.y - k)*1.0/ry/ry <= 1;
			}
		};
	}
	
}
