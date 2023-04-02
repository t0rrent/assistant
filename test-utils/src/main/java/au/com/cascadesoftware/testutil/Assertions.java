package au.com.cascadesoftware.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.geom.Rectangle2D;
import java.util.Collection;

import org.opentest4j.AssertionFailedError;

public class Assertions {

	public static void rectangleEquals(final Rectangle2D expected, final Rectangle2D actual, final double delta) {
		assertEquals(expected.getX(), actual.getX(), delta);
		assertEquals(expected.getY(), actual.getY(), delta);
		assertEquals(expected.getWidth(), actual.getWidth(), delta);
		assertEquals(expected.getHeight(), actual.getHeight(), delta);
	}
	
	public static <T> void assertUnorderedEquals(final Collection<T> expected, final Collection<T> actual) {
		 assertTrue(
				 expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(expected),
				 "Unequal unordered list, expected: <" + expected + "> but was: <" + actual + '>'
		 );
	}
	
	static void assertFails(final Runnable runnable) {
		assertThrows(AssertionFailedError.class, ()  -> runnable.run());
	}
	
}
