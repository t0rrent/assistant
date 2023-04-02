package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;
import java.util.Objects;

public class Vector2i implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public int x, y;
	
	public Vector2i(){}
	
	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(Vector2i v2) {
		this(v2.x, v2.y);
	}

	public double getLength(){
		return Math.sqrt(x*x + y*y);
	}

	public Vector2i add(Vector2i v2) {
		return new Vector2i(x + v2.x, y + v2.y);
	}

	public Vector2i sub(Vector2i v2) {
		return new Vector2i(x - v2.x, y - v2.y);
	}

	public Vector2i mul(float x2, float y2) {
		return new Vector2i((int) (x*x2), (int) (y*y2));
	}

	public Vector2i mul(float f) {
		return mul(f, f);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vector2i){
			Vector2i v2 = (Vector2i) obj;
			return v2.x == x && v2.y == y;
		}
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Vector2d toVector2d() {
		return new Vector2d(x, y);
	}

	public double getAngle() {
		if(x == 0 && y == 0) return 0;
		if(x == 0) return (y > 0) ? Math.PI/2 : 3*Math.PI/2;
		double m = y/x;
		if(x > 0){
			if(y > 0) return Math.atan(m);
			else return Math.atan(m) + 2*Math.PI;
		}else{
			return Math.PI + Math.atan(m);
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	public Vector3i toVector3i(final int z) {
		return new Vector3i(x, y, z);
	}
	
}
