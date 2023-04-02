package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TypeCheckTest {

	@Test
	public void testIsInteger() {
	    assertTrue(TypeCheck.isInteger("1"));
	    assertTrue(TypeCheck.isInteger("02"));
	    assertTrue(TypeCheck.isInteger("430"));
	    assertTrue(TypeCheck.isInteger(Integer.MAX_VALUE + ""));
	    assertTrue(TypeCheck.isInteger("-1"));
	    assertTrue(TypeCheck.isInteger("-02"));
	    assertTrue(TypeCheck.isInteger("-430"));
	    assertTrue(TypeCheck.isInteger(Integer.MIN_VALUE + ""));
	}

	@Test
	public void testIsNotInteger() {
		assertFalse(TypeCheck.isInteger("text"));
		assertFalse(TypeCheck.isInteger("2.1"));
		assertFalse(TypeCheck.isInteger("1.0"));
	}

	@Test
	public void testIsIntegerInvalid() {
	    assertFalse(TypeCheck.isInteger(null));
	    assertFalse(TypeCheck.isInteger((Integer.MAX_VALUE + 1l) + ""));
	    assertFalse(TypeCheck.isInteger((Integer.MIN_VALUE - 1l) + ""));
	}

}
