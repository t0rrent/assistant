package au.com.cascadesoftware.util.collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MapBuilder<K, V> {
	
	private final Map<K, V> map;
	
	public MapBuilder(final Supplier<Map<K, V>> mapSupplier) {
		this.map = (Map<K, V>) mapSupplier.get();
	}
	
	public MapBuilder<K, V> put(final K key, final V value) {
		map.put(key, value);
		return this;
	}
	
	public Map<K, V> map() {
		return map;
	}
	
	public Map<K, V> unmodifiableMap() {
		return Collections.unmodifiableMap(map());
	}
	
	public static <K, V> MapBuilder<K, V> build() {
		return build(HashMap::new);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <K, V> MapBuilder<K, V> build(final Supplier<Map<K, V>> mapSupplier) {
		return new MapBuilder(mapSupplier);
	}

}
