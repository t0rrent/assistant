package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Polygon;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
//import au.com.cascadesoftware.engine3.gui.web.Style;
//import au.com.cascadesoftware.engine3.gui.web.Styles;

public class GUIShapeRegularPolygon extends GUIShape {

	private int sides;
	private double rotation;

	public GUIShapeRegularPolygon(Window window, Boundary bounds, Color shapeColor, int sides) {
		super(window, bounds, shapeColor);
		this.sides = sides;
	}
	
	private Polygon getPolygon() {
		if(sides < 3) return null;
		int[] xPoints = new int[sides];
		int[] yPoints = new int[sides];
		int midX = getRenderBounds().x + getRenderBounds().width/2;
		int midY = getRenderBounds().y + getRenderBounds().height/2;
		float radius = getRenderBounds().width/2f;
		if(getRenderBounds().height/2 < radius) radius = getRenderBounds().height/2f;
		for(int i = 0; i < sides; i++){
			double rotation = (this.rotation + i*2*Math.PI/sides) % (2*Math.PI);
			double xF = Math.sin(rotation);
			double yF = -Math.cos(rotation);
			xPoints[i] = (int) (midX + xF*radius);
			yPoints[i] = (int) (midY + yF*radius);
		}
		Polygon p = new Polygon(xPoints, yPoints, sides);
		return p;
	}

	@Override
	protected void draw(Graphics graphics) {
		Polygon p = getPolygon();
		if(p == null) return;
		Paint paint = new Paint();
		if(getFill()){
			paint.setFill(true);
			paint.setColor(this.getShapeColor());
			graphics.setPaint(paint);
			graphics.drawPolygon(getPolygon());
		}
		if(this.getBorderWidth() > 0){
			paint.setFill(false);
			paint.setColor(this.getBorderColor());
			paint.setStrokeWidth(getBorderWidth());
			graphics.setPaint(paint);
			graphics.drawPolygon(getPolygon());
		}
	}

	public void setRotation(double d){
		this.rotation = d;
	}

	public void rotate(float rotation){
		this.rotation += rotation;
	}
	
	/*@Override
	protected String[] getHTML() {
		String points = "";
		Polygon p = getPolygon();
		for(int i = 0; i < p.npoints; i++){
			int x = (int) (p.xpoints[i] - getRenderBounds().x + getBorderWidth()/2 + 1);
			int y = (int) (p.ypoints[i] - getRenderBounds().y + getBorderWidth()/2 + 1);
			points += x + "," + y + " ";
		}
		float borderThickness = this.getBorderWidth();
		String border = "";
		if(borderThickness > 0) border = ";stroke:" + getBorderColor().getHTMLCode() + ";stroke-width:" + borderThickness;
		return new String[]{
				"<svg height=\"" + (getRenderBounds().height + getBorderWidth() + 1) + "\" width=\"" + (getRenderBounds().width + getBorderWidth() + 1) + "\">",
				"<polygon points=\"" + points + "\" style=\"fill:" + getShapeColor().getHTMLCode() + border + "\" />",
				"</svg>"
		};
	}
	
	@Override
	public Styles getCSS() {
		return super.getCSS().inject(new Style[]{
				new Style("width", getRenderBounds().width + "px")
		});
	}*/
	
	public static class Side {
		public static int RIGHT = 0;
		public static int TOP = 1;
		public static int LEFT = 2;
		public static int BOTTOM = 3;
	}

}
