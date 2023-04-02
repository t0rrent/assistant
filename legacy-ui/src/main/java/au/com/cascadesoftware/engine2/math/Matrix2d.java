package au.com.cascadesoftware.engine2.math;

public class Matrix2d {
	
	private final double a00, a01, a10, a11;

	public Matrix2d(){
		this(1, 0, 0, 1);
	}

	public Matrix2d(double a00, double a10, double a01, double a11) {
		this.a00 = a00;
		this.a01 = a01;
		this.a10 = a10;
		this.a11 = a11;
	}

	public static Matrix2d rotate(double r) {
		return new Matrix2d(Math.cos(r), -Math.sin(r), Math.sin(r), Math.cos(r));
	}
	
	public Vector2d transform(Vector2d input){
		return new Vector2d(input.x*a00 + input.y*a10, input.x*a01 + input.y*a11);
	}

}
