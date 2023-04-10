package au.com.cascadesoftware.util;

public class MathUtils {

	public static float clamp(final float value, final int min, final int max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		} else {
			return value;
		}
	}

}
