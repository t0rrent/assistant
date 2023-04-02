package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Recti implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public int x, y, width, height;
	
	public Recti(){}
	
	public Recti(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public Recti(Recti clone){
		this.x = clone.x;
		this.y = clone.y;
		this.width = clone.width;
		this.height = clone.height;
	}

	public Recti(Vector2i topLeft, Vector2i bottomRight) {
		this(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
	}

	public boolean contains(Vector2i point) {
		return point.x >= x && point.x < x + width && point.y >= y && point.y < y + height;
	}
	
	public Vector2d center(){
		return new Vector2d(x + width/2.0, y + height/2.0);
	}

	public Recti expandByFactorOf(float f) {
		x -= width*f*0.5f;
		y -= height*f*0.5f;
		width += width*f;
		height += height*f;
		return this;
	}

	public Recti expandByFactorOf(double f, double g) {
		x -= width*f*0.5f;
		y -= height*g*0.5f;
		width += width*f;
		height += height*g;
		return this;
	}

	public Recti translate(Vector2i v) {
		return new Recti(x + v.x, y + v.y, width, height);
	}

	public Vector2i cornerPosition() {
		return new Vector2i(x, y);
	}

	public Rectd toRectd() {
		return new Rectd(x, y, width, height);
	}

	public Recti expand(int f, int g) {
		x -= f;
		y -= g;
		width += f * 2;
		height += g * 2;
		return this;
	}

	public boolean intersects(Recti r2) {	
		for(int x = 0; x < Math.floor(width*1.0/r2.width) + 1; x++){
			for(int y = 0; y < Math.floor(height*1.0/r2.height) + 1; y++){
				if(r2.contains(new Vector2i(this.x + r2.width*x, this.y + r2.height*y))) return true;
			}
			if(r2.contains(new Vector2i(this.x + r2.width*x, this.y + height))) return true;
		}
		for(int y = 0; y < Math.floor(height*1.0/r2.height) + 1; y++) if(r2.contains(new Vector2i(this.x + width, this.y + r2.height*y))) return true;
		if(r2.contains(new Vector2i(this.x + width, this.y + height))) return true;
		
		for(int x = 0; x < Math.floor(r2.width*1.0/width) + 1; x++){
			for(int y = 0; y < Math.floor(r2.height*1.0/height) + 1; y++){
				if(contains(new Vector2i(r2.x + width*x, r2.y + height*y))) return true;
			}
			if(contains(new Vector2i(r2.x + width*x, r2.y + r2.height))) return true;
		}
		for(int y = 0; y < Math.floor(r2.height*1.0/height) + 1; y++) if(contains(new Vector2i(r2.x + r2.width, r2.y + height*y))) return true;
		if(contains(new Vector2i(r2.x + r2.width, r2.y + r2.height))) return true;
		if(r2.x + r2.width == x && r2.y == y + height) return true;
		if(x + width == r2.x && y == r2.y + r2.height) return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "Recti{"
				+ "x: " + x + ", "
				+ "y: " + y + ", "
				+ "width: " + width  + ", "
				+ "height: " + height + "}";
	}
}
