package au.com.cascadesoftware.util.collection;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class MapBuilderTest {

	@Test
	public void testMapBuilderInitialization() {
		final Map<String, Integer> map = MapBuilder.<String, Integer>build()
				.put("test1", 1)
				.put("test2", 2)
				.map();
		assertEquals(1, map.get("test1"));
		assertEquals(2, map.get("test2"));
		assertEquals(null, map.get("test3"));
		assertDoesNotThrow(() -> map.put("test3", 3));
		assertEquals(3, map.get("test3"));
		map.put(null, 4);
	}
	
	@Test
	public void testMapBuilderErrors() {
		assertThrows(NullPointerException.class, () -> MapBuilder.<String, Integer>build(null).map());
	}
	
}
