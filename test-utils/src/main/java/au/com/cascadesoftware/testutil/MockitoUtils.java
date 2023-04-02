package au.com.cascadesoftware.testutil;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

public class MockitoUtils {

	public static Function<String, Integer> getMockTransform() {
		@SuppressWarnings("unchecked")
		final Function<String, Integer> mockTransform = mock(Function.class);
		when(mockTransform.apply(anyString()))
				.then((invocation) -> ((String) invocation.getArgument(0)).length());
		return mockTransform;
	}
	
}
