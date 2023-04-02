package au.com.cascadesoftware.testutil;

import static au.com.cascadesoftware.testutil.MockitoUtils.getMockTransform;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

public class MockitoUtilsTest {
	
	@Test
	public void testMockFunction() {
		final Function<String, Integer> mockTransform = getMockTransform();
		assertEquals(0, mockTransform.apply(""));
		assertEquals(1, mockTransform.apply("a"));
		assertEquals(2, mockTransform.apply("ab"));
		verify(mockTransform, times(3)).apply(anyString());
		verify(mockTransform, times(1)).apply(eq("ab"));
	}

}
