package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Vector2f implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public float x, y;
	
	public Vector2f(){}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public double getLength(){
		return Math.sqrt(x*x + y*y);
	}

	public Vector2f sub(Vector2f v2) {
		return new Vector2f(x - v2.x, y - v2.y);
	}

	public Vector2f mul(float scalar) {
		return new Vector2f(x*scalar, y*scalar);
	}

	public Vector2i toVector2i() {
		return new Vector2i((int) x, (int) y);
	}

	public Vector2f add(Vector2f v2) {
		return new Vector2f(x + v2.x, y + v2.y);
	}

	public Vector2f mul(float sX, float sY) {
		return new Vector2f(x*sX, y*sY);
	}

	public Vector2f mul(Vector2f v) {
		return mul(v.x, v.y);
	}

	public Vector2f mul(Vector2i v) {
		return mul(v.x, v.y);
	}
}
