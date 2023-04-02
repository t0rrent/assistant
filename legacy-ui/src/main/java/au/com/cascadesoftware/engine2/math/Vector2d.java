package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

import au.com.cascadesoftware.engine2.math.geom.InfiniteLine;

public class Vector2d implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public double x, y;
	
	public Vector2d(){}
	
	public Vector2d(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getLength(){
		return Math.sqrt(x*x + y*y);
	}

	public Vector2d sub(Vector2d v2) {
		return new Vector2d(x - v2.x, y - v2.y);
	}

	public Vector2d mul(double scalar) {
		return new Vector2d(x*scalar, y*scalar);
	}

	public Vector2i toVector2i() {
		return new Vector2i((int) x, (int) y);
	}

	public Vector2f toVector2f() {
		return new Vector2f((float) x, (float) y);
	}

	public Vector2d add(Vector2d v2) {
		return new Vector2d(x + v2.x, y + v2.y);
	}

	public Vector2d mul(double sX, double sY) {
		return new Vector2d(x*sX, y*sY);
	}

	public Vector2d mul(Vector2f v) {
		return mul(v.x, v.y);
	}

	public Vector2d mul(Vector2d v) {
		return mul(v.x, v.y);
	}

	public Vector2d mul(Vector2i v) {
		return mul(v.x, v.y);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Vector2d)) return false;
		Vector2d v2 = (Vector2d) o;
		if(v2.x == x && v2.y == y) return true;
		return super.equals(o);
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

	public static Vector2d fromAngle(double theta, double r) {
		while(theta < 0) theta += Math.PI*2;
		theta %= Math.PI*2;
		return new Vector2d(Math.cos(theta), Math.sin(theta)).mul(r);
	}

	public Vector2d normalize() {
		return fromAngle(getAngle(), 1);
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public double getDistanceFrom(Vector2d v2) {
		return Math.sqrt(Math.pow(v2.x - x, 2)  +  Math.pow(v2.y - y, 2));
	}

	public boolean isBetween(InfiniteLine lineA, InfiniteLine lineB) {
		final double inaccuracy = 0.0000000000001;
		if(Math.abs(lineA.angle - lineB.angle) > inaccuracy) throw new RuntimeException("this method only supports parrallel lines, angle 1: " + lineA.angle + ", angle 2: " + lineB.angle);
		InfiniteLine testLine = null;
		if(lineA.angle == 0){ //make test line vertical
			testLine = InfiniteLine.Builder.fromAngleAndPoint(Math.PI/2, this);
		}else{ //make test line horizontal
			testLine = InfiniteLine.Builder.fromAngleAndPoint(0, this);
		}
		Vector2d p1 = testLine.getIntersectionPoint(lineA);
		Vector2d p2 = testLine.getIntersectionPoint(lineB);
		return p1.getDistanceFrom(p2) + inaccuracy >= p1.getDistanceFrom(this) + p2.getDistanceFrom(this);
	}

	public Vector2d abs() {
		return new Vector2d(Math.abs(x), Math.abs(y));
	}

	public Vector2d floor() {
		return new Vector2d(Math.floor(x), Math.floor(y));
	}
	
}
