package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;
import java.util.Objects;

public class Linei implements Serializable {
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
	
	public int x1, y1, x2, y2;
	
	public Linei(){}
	
	public Linei(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x1, x2, y1, y2);
	}

}
