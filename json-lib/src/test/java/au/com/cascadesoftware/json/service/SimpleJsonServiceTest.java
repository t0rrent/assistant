package au.com.cascadesoftware.json.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.JsonNode;

import au.com.cascadesoftware.json.hk2.extension.JsonHK2TestExtension;
import jakarta.inject.Inject;

@ExtendWith(JsonHK2TestExtension.class)
public class SimpleJsonServiceTest {
	
	private static final String VALID_JSON_TEXT = "{"
			+ "	\"thingA\": {"
			+ "		\"value\": 12.34"
			+ "	}," 
			+ "	\"thingB\": {"
			+ "		\"anArray\": ["
			+ "			{"
			+ "				\"elementName\": \"element0\""
			+ "			},"
			+ "			{"
			+ "				\"elementName\": \"element1\""
			+ "			}"
			+ "		]"
			+ "	}"
			+ "}";
	
	private static final String INVALID_JSON_TEXT =  "{"
			+ "\"thingA\": {"
			+ "\"value\": 12.34"
			+ "},";

	@Inject
	private JsonService jsonService;
	
	@Test
	public void testSimpleExtract() {
		assertEquals(
				12.34,
				this.jsonService.extract(VALID_JSON_TEXT, double.class, "thingA", "value")
		);
	}
	
	@Test
	public void testExtractJsonNode() {
		final JsonNode result = this.jsonService.extract(VALID_JSON_TEXT);
		assertEquals(result.toString(), VALID_JSON_TEXT.replaceAll("\\s+", ""));
	}
	
	@Test
	public void testDeepExtract() {
		assertEquals(
				"element1",
				this.jsonService.extract(VALID_JSON_TEXT, String.class, "thingB", "anArray", "1", "elementName")
		);
	}
	
	@Test
	public void testIncorrectLocation() {
		assertNull(this.jsonService.extract(VALID_JSON_TEXT, String.class, "thingB", "blah", "1", "elementName"));
	}
	
	@Test
	public void testInvalidJson() {
		assertNull(this.jsonService.extract(INVALID_JSON_TEXT, String.class, "thingA", "value"));
	}
	
	@Test
	public void testNullJson() {
		assertNull(this.jsonService.extract(null, String.class, "thingA", "value"));
	}
	
	@Test
	public void testNullType() {
		assertNull(this.jsonService.extract(VALID_JSON_TEXT, null, "thingA", "value"));
	}

}
