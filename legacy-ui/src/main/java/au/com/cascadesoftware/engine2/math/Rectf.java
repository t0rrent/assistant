package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Rectf implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public float x, y, width, height;
	
	public Rectf(){}
	
	public Rectf(float x, float y, float w, float h){
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

	public Rectf mul(float w, float h) {
		return new Rectf(x*w, y*h, width*w, height*h);
	}

	public Rectf translate(float x2, float y2) {
		return new Rectf(x + x2, y + y2, width, height);
	}
	
}
