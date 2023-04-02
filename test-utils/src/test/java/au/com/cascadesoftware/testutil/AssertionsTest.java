package au.com.cascadesoftware.testutil;

import static au.com.cascadesoftware.testutil.Assertions.assertFails;
import static au.com.cascadesoftware.testutil.Assertions.assertUnorderedEquals;
import static au.com.cascadesoftware.testutil.Assertions.rectangleEquals;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AssertionsTest {

	@Test
	public void rectangleEqualsCorrect() {
		final Rectangle2D r1 = new Rectangle2D.Double(1, 2, 3, 4);
		final Rectangle2D r2 = new Rectangle2D.Double(1.001, 2.002, 3.003, 4.002);
		rectangleEquals(r1, r2, 0.01);
	}

	@Test
	public void rectangleEqualsFail() {
		final Rectangle2D r1 = new Rectangle2D.Double(1, 2, 3, 4);
		final Rectangle2D r2 = new Rectangle2D.Double(1.001, 2.002, 3.003, 4.002);
		final Rectangle2D r3 = new Rectangle2D.Double(90, 202, 13, 4.002);
		assertFails(() -> rectangleEquals(r1, r2, 0.001));
		assertFails(() -> rectangleEquals(r1, r3, 0.01));
	}
	
	@Test
	public void assertUnorderedEqualsCorrect() {
		final Collection<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
		final Collection<Integer> list2 = Arrays.asList(1, 2, 3, 5, 4);
		final List<Integer> list3 = Arrays.asList(5, 4, 3, 1, 2);
		assertUnorderedEquals(list1, list2);
		assertUnorderedEquals(list2, list3);
		assertUnorderedEquals(list1, list3);
	}

	@Test
	public void assertUnorderedEqualsFail() {
		final Collection<Integer> list1 = Arrays.asList(1, 2, 3, 4, 5);
		final Collection<Integer> list2 = Arrays.asList(1, 2, 3, 4);
		final Collection<Integer> list3 = Arrays.asList(1, 6, 3, 4, 2);
		final List<Integer> list4 = Arrays.asList(5, 4, 3, 4, 2);
		assertFails(() -> assertUnorderedEquals(list1, list2));
		assertFails(() -> assertUnorderedEquals(list2, list3));
		assertFails(() -> assertUnorderedEquals(list1, list3));
		assertFails(() -> assertUnorderedEquals(list1, list4));
		assertFails(() -> assertUnorderedEquals(list2, list4));
		assertFails(() -> assertUnorderedEquals(list3, list4));
	}
	
}
