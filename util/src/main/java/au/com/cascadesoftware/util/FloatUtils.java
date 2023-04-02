package au.com.cascadesoftware.util;

public class FloatUtils {

	public static double precision(final double value, final int precision) {
		final double multiplier = Math.pow(10, precision);
		return Math.round(value * multiplier) * 1.0 / multiplier;
	}
	
}
