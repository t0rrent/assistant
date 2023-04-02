package au.com.cascadesoftware.util.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import au.com.cascadesoftware.util.ThreadUtils;
import au.com.cascadesoftware.util.model.CachingStrategy;
import jakarta.inject.Inject;

public class CachedTransformService implements CachingService {
	
	private final ScheduledExecutorService scheduledExecutorService;
	
	@Inject
	public CachedTransformService(final ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}
	
	@Override
	public <T, V> Function<T, V> createCachedTransform(
			final Function<T, V> transform
	) {
		return createCachedTransform((input) -> input, transform, CachingStrategy.none());
	}
	
	@Override
	public <T, V> Function<T, V> createCachedTransform(
			final Function<T, V> transform,
			final CachingStrategy cachingStrategy
	) {
		return createCachedTransform((input) -> input, transform, cachingStrategy);
	}
	
	@Override
	public <T, K, V> Function<T, V> createCachedTransform(
			final Function<T, K> keyCreator,
			final Function<T, V> transform
	){
		return createCachedTransform(keyCreator, transform, CachingStrategy.none());
	}

	@Override
	public <T, K, V> Function<T, V> createCachedTransform(
			final Function<T, K> keyCreator,
			final Function<T, V> transform,
			final CachingStrategy cachingStrategy
	) {
		final Map<K, V> map = new HashMap<>();
		final Map<K, ScheduledFuture<?>> executorExpiryTasks = new ConcurrentHashMap<>();
		return ThreadUtils.synchronize((input) -> {
			final K key = keyCreator.apply(input);
			final V value;
			if (map.containsKey(key)) {
				value = map.get(key);
			} else {
				value = transform.apply(input);
				map.put(key, value);
			}
			final Duration expiry = cachingStrategy.getExpiry();
			if (expiry != null) {
				executorExpiryTasks.computeIfPresent(key, (k, task) -> {
					task.cancel(false);
					return task;
				});
				executorExpiryTasks.put(key, scheduledExecutorService.schedule(() -> {
					map.remove(key);
					executorExpiryTasks.remove(key);
				}, expiry.toMillis(), TimeUnit.MILLISECONDS));
			}
			return value;
		});
	}

}
