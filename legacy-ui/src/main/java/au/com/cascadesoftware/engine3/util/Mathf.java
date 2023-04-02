package au.com.cascadesoftware.engine3.util;

public class Mathf {

	public static final float PI = 3.14159265359f;

	public static float sigmoid(float x) {
		return (float) (1.0/(1 + Math.exp(-x)));
	}
	
	public static float inverseSigmoid(float x){
		return (float) Math.log(x/(1-x));
	}

	public static float divSigmoid(float x) {
		return sigmoid(x)*(1 - sigmoid(x));
	}

	public static float cos(float angle) {
		return (float) Math.cos(angle);
	}

	public static float sin(float angle) {
		return (float) Math.sin(angle);
	}
	
}
