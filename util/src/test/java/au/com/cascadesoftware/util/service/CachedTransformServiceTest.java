package au.com.cascadesoftware.util.service;

import static au.com.cascadesoftware.testutil.MockitoUtils.getMockTransform;
import static au.com.cascadesoftware.testutil.Executions.multiThreadedRepetitionTest;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.util.hk2.extension.UtilHK2TestExtension;
import au.com.cascadesoftware.util.model.CachingStrategy;
import jakarta.inject.Inject;

@ExtendWith(UtilHK2TestExtension.class)
public class CachedTransformServiceTest {

	@Inject
	private CachingService cachingService;

	@Test
	public void testCaching() {
		final Function<String, Integer> mockTransform = getMockTransform();
		final Function<String, Integer> exampleCacheTransform = cachingService.createCachedTransform(mockTransform);
		applyTest(mockTransform, exampleCacheTransform);
	}

	@Test
	public void testCachingWithKeyTransform() {
		final Function<String, Integer> mockTransform = getMockTransform();
		final Function<String, Integer> exampleCacheTransform = cachingService.createCachedTransform(
				(input) -> "key:" + input,
				mockTransform
		);
		applyTest(mockTransform, exampleCacheTransform);
	}

	@Test
	public void testCachingExpiry() throws InterruptedException {
		final Function<String, Integer> mockTransform = getMockTransform();
		final CachingStrategy cachingStrategy = CachingStrategy.builder().setExpiry(Duration.of(1, ChronoUnit.SECONDS)).build();
		final Function<String, Integer> exampleCacheTransform = cachingService.createCachedTransform(mockTransform, cachingStrategy);
		applyTest(mockTransform, exampleCacheTransform);
		Thread.sleep(2000);
		exampleCacheTransform.apply("a");
		exampleCacheTransform.apply("ab");
		verify(mockTransform, times(4)).apply(anyString());
	}

	private void applyTest(final Function<String, Integer> mockTransform, final Function<String, Integer> exampleCacheTransform) {
		exampleCacheTransform.apply("a");
		exampleCacheTransform.apply("ab");
		exampleCacheTransform.apply("a");
		verify(mockTransform, times(2)).apply(anyString());
	}

	@Test
	public void testCachingExpiryReset() throws InterruptedException {
		final Function<String, Integer> mockTransform = getMockTransform();
		final CachingStrategy cachingStrategy = CachingStrategy.builder().setExpiry(Duration.of(1, ChronoUnit.SECONDS)).build();
		final Function<String, Integer> exampleCacheTransform = cachingService.createCachedTransform(mockTransform, cachingStrategy);
		exampleCacheTransform.apply("a");
		Thread.sleep(800);
		exampleCacheTransform.apply("a");
		Thread.sleep(800);
		exampleCacheTransform.apply("a");
		Thread.sleep(1200);
		exampleCacheTransform.apply("a");
		verify(mockTransform, times(2)).apply(anyString());
	}

	@Test
	public void testCachingThreadSafety() throws InterruptedException {
		final Function<String, Integer> mockTransform = getMockTransform();
		final CachingStrategy cachingStrategy = CachingStrategy.builder().setExpiry(Duration.of(1, ChronoUnit.SECONDS)).build();
		final Function<String, Integer> exampleCacheTransform = cachingService.createCachedTransform(mockTransform, cachingStrategy);
		multiThreadedRepetitionTest(8, 1000, () -> exampleCacheTransform.apply("a"));
		verify(mockTransform, times(1)).apply(anyString());
	}
	
}
