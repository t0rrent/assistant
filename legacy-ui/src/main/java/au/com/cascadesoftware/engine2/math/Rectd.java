package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Rectd implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public double x, y, width, height;
	
	public Rectd(){}
	
	public Rectd(double x, double y, double w, double h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public Vector2d center(){
		return new Vector2d(x + width/2.0, y + height/2.0);
	}

	public Recti toRecti() {
		return new Recti((int) Math.floor(x), (int) Math.floor(y), (int) Math.ceil(width), (int) Math.ceil(height));
	}

	public Rectd mul(double w, double h) {
		return new Rectd(x*w, y*h, width*w, height*h);
	}

	public Rectd translate(double x2, double y2) {
		return new Rectd(x + x2, y + y2, width, height);
	}

	public Rectd expand(double f, double g) {
		x -= f;
		y -= g;
		width += f * 2;
		height += g * 2;
		return this;
	}

	public boolean contains(Vector2d point) {
		return point.x >= x && point.x < x + width && point.y >= y && point.y < y + height;
	}
	
	@Override
	public String toString() {
		return "Rectd{"
				+ "x: " + x + ", "
				+ "y: " + y + ", "
				+ "width: " + width  + ", "
				+ "height: " + height + "}";
	}
	
}
