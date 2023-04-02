package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StringProcessingTest {
	
	@Test
	public void testBackspaces() {
		assertEquals("abc", StringProcessing.applyBackspaces("\bad\bef\b\bbc"));
	}
	
	@Test
	public void testCommonPrefixLength() {
        assertEquals(0, StringProcessing.commonPrefixLength("", ""));
        assertEquals(0, StringProcessing.commonPrefixLength("zxc", ""));
        assertEquals(0, StringProcessing.commonPrefixLength("", "zxc"));
        assertEquals(0, StringProcessing.commonPrefixLength("abc", "def"));
        assertEquals(3, StringProcessing.commonPrefixLength("abc", "abc"));
        assertEquals(2, StringProcessing.commonPrefixLength("ab", "abc"));
        assertEquals(3, StringProcessing.commonPrefixLength("abc", "abcd"));
	}

}
