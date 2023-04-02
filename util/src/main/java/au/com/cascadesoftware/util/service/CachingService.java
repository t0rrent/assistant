package au.com.cascadesoftware.util.service;

import java.util.function.Function;

import au.com.cascadesoftware.util.model.CachingStrategy;

public interface CachingService {

	<T, K, V> Function<T, V> createCachedTransform(Function<T, K> keyCreator, Function<T, V> transform, CachingStrategy cachingStrategy);

	<T, K, V> Function<T, V> createCachedTransform(Function<T, K> keyCreator, Function<T, V> transform);
	
	<T, V> Function<T, V> createCachedTransform(Function<T, V> transform, CachingStrategy cachingStrategy);

	<T, V> Function<T, V> createCachedTransform(Function<T, V> transform);

}
