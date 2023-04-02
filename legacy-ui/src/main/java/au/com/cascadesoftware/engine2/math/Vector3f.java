package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Vector3f implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public float x, y, z;
	
	public Vector3f(){}
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getLength(){
		return Math.sqrt(x*x + y*y + z*z);
	}

	public Vector3i toVector3i() {
		return new Vector3i((int) x, (int) y, (int) z);
	}
}
