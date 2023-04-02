package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Vector3i implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public int x, y, z;
	
	public Vector3i(Vector3i v2){
		this.x = v2.x;
		this.y = v2.y;
		this.z = v2.z;
	}
	
	public Vector3i(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double getLength(){
		return Math.sqrt(x*x + y*y + z*z);
	}

	public Vector3i add(Vector3i vec2) {
		return new Vector3i(vec2.x + x, vec2.y + y, vec2.z + z);
	}

	public Vector3i sub(Vector3i vec2) {
		return new Vector3i(vec2.x - x, vec2.y - y, vec2.z - z);
	}

	public Vector3i mul(int i, int j, int k) {
		return new Vector3i(x*i, y*j, z*k);
	}
}
