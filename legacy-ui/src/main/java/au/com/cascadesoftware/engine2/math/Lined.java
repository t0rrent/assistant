package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

import au.com.cascadesoftware.engine2.math.geom.InfiniteLine;

public class Lined implements Serializable {
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public double x1, y1, x2, y2;
	
	public Lined(){}
	
	public Lined(double x1, double y1, double x2, double y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public InfiniteLine getInfiniteLine() {
		return InfiniteLine.Builder.fromTwoPoints(new Vector2d(x1, y1), new Vector2d(x2, y2));
	}

}
