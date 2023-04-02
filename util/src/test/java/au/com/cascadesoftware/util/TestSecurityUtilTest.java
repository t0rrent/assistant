package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestSecurityUtilTest {
	
	private static final String PRE_HASH = "package blah.blah.blah;\n"
			+ "\n"
			+ "import somestuff;\n"
			+ "\n"
			+ "public class asdasdasdsd {\n"
			+ "public static void main(String[] sdsada) {\n"
			+ "\n"
			+ "System.out.println();\n"
			+ "}\n"
			+ "}\n";
	
	private static final String EXPECTED_THING_TO_HASH = "public class asdasdasdsd {\n"
			+ "public static void main(String[] sdsada) {\n"
			+ "System.out.println();\n"
			+ "}\n"
			+ "}";

	@Test
	public void testHashing() {
		assertEquals(EXPECTED_THING_TO_HASH.hashCode(), TestSecurityUtil.getJavaFileHash(PRE_HASH));
	}
	
}
