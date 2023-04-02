package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FloatUtilsTest {

	@Test
	public void testPrecision() {
		final double sample1 = 123.456;
		assertEquals(123.46, FloatUtils.precision(sample1, 2));
		assertEquals(123.456, FloatUtils.precision(sample1, 4));
		assertEquals(123.0, FloatUtils.precision(sample1, 0));
		assertEquals(120.0, FloatUtils.precision(sample1, -1));
		assertEquals(0.0, FloatUtils.precision(sample1, -3));
	}
	
}
