package au.com.cascadesoftware.json.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.json.hk2.extension.JsonHK2TestExtension;
import au.com.cascadesoftware.json.model.POJE;
import au.com.cascadesoftware.json.model.POJO;
import au.com.cascadesoftware.util.annotation.AnnotationException;
import au.com.cascadesoftware.util.service.AnnotationService;
import jakarta.inject.Inject;

@ExtendWith(JsonHK2TestExtension.class)
public class SimpleJsonServiceAnnotationIT {

	@Inject
	private ObjectMapper objectMapper;
	
	@Inject
	private AnnotationService annotationService;
	
	@Test
	public void testValidNonNull() throws IllegalArgumentException, AnnotationException, JsonMappingException, JsonProcessingException {
		final POJO expected = getNonNullValidPOJO();
		final POJO result = annotationService.processAnnotations(
				objectMapper.treeToValue(objectMapper.readTree(expected.toJson()), POJO.class)
		);
		assertEquals(expected, result);
	}
	
	@Test
	public void testInvalidNonNull() throws JsonMappingException, JsonProcessingException, IllegalArgumentException {
		final POJO expected = getNonNullInvalidPOJO();
		final POJO result = objectMapper.treeToValue(objectMapper.readTree(expected.toJson()), POJO.class);
		assertNull(result.field1);
		assertThrows(AnnotationException.class, () -> annotationService.processAnnotations(result));
	}

	private POJO getNonNullValidPOJO() {
		final POJO pojo = new POJO();
		pojo.field1 = "some text";
		pojo.field2 = null;
		return pojo;
	}

	private POJO getNonNullInvalidPOJO() {
		final POJO pojo = new POJO();
		pojo.field1 = null;
		pojo.field2 = POJE.ENOOM1;
		return pojo;
	}
}
