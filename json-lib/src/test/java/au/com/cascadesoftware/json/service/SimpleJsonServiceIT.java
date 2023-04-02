package au.com.cascadesoftware.json.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.json.hk2.extension.JsonHK2TestExtension;
import au.com.cascadesoftware.json.model.GenericClassExample;
import au.com.cascadesoftware.json.model.POJE;
import au.com.cascadesoftware.json.model.POJO;
import au.com.cascadesoftware.json.model.WrappedListExample;
import jakarta.inject.Inject;

@ExtendWith(JsonHK2TestExtension.class)
public class SimpleJsonServiceIT {
	
	private static final String VALID_JSON_FILE = "valid-json-test.json";
	
	private static final String INVALID_JSON_FILE = "invalid-json-test.json";
	
	private static final String VALID_JSON_TEXT = "{"
			+ "		\"complexList\": ["
			+ "			{"
			+ "				\"generic\": {"
			+ "					\"field1\": \"test1\","
			+ "					\"field2\": \"ENOOM1\""
			+ "				}"
			+ "			},"
			+ "			{"
			+ "				\"generic\": {"
			+ "					\"field1\": null,"
			+ "					\"field2\": \"ENOOM2\""
			+ "				}"
			+ "			}"
			+ "		]"
			+ "}";
	
	private static final String INVALID_JSON_TEXT =  "{"
			+ "\"thingA\": {"
			+ "\"value\": 12.34"
			+ "},";

	private static final String RESOURCE_FOLDER = "json-service-it";
	
	@Inject
	private ObjectMapper objectMapper;

	@Test
	public void testReadTree() throws JsonMappingException, JsonProcessingException {
		final JsonNode result = objectMapper.readTree(VALID_JSON_TEXT);
		assertNotNull(result);
	}

	@Test
	public void testInvalidReadTree() {
		assertThrows(JsonProcessingException.class, () -> objectMapper.readTree(INVALID_JSON_TEXT));
		final String nullString = null;
		assertThrows(IllegalArgumentException.class, () -> objectMapper.readTree(nullString));
	}
	
	@Test
	public void testTreeToValue() throws IllegalArgumentException, FileNotFoundException, IOException {
		final WrappedListExample expected = getWrappedListExample();
		assertEquals(
				expected,
				objectMapper.treeToValue(objectMapper.readTree(VALID_JSON_TEXT), WrappedListExample.class)
		);
		assertEquals(
				expected,
				objectMapper.treeToValue(objectMapper.readTree(new FileReader(new File(VALID_JSON_FILE))), WrappedListExample.class)
		);
	}

	@Test
	public void testTreeToValueInvalidType() {
		assertThrows(
				JsonProcessingException.class,
				() -> objectMapper.treeToValue(objectMapper.readTree(VALID_JSON_TEXT), String.class)
		);
	}
	
	@Test
	public void testReadFromFile() throws FileNotFoundException, IOException {
		final JsonNode result = objectMapper.readTree(new FileReader(new File(VALID_JSON_FILE)));
		assertNotNull(result);
	}

	@Test
	public void testInvalidReadFromFile() {
		assertThrows(JsonProcessingException.class, () -> objectMapper.readTree(new FileReader(new File(INVALID_JSON_FILE))));
		assertThrows(IOException.class, () -> objectMapper.readTree(new FileReader(new File("missing-file.json"))));
	}
	
	@Test
	public void testReadFromStream() throws FileNotFoundException, IOException {
		final JsonNode result1 = objectMapper.readTree(new FileInputStream(new File(VALID_JSON_FILE)));
		assertNotNull(result1);
		final JsonNode result2 = objectMapper.readTree(
				getClass().getClassLoader().getResourceAsStream(RESOURCE_FOLDER + "/" + VALID_JSON_FILE)
		);
		assertNotNull(result2);
	}

	@Test
	public void testInvalidReadFromStream() {
		assertThrows(JsonProcessingException.class, () -> objectMapper.readTree(new FileInputStream(new File(INVALID_JSON_FILE))));
		assertThrows(JsonProcessingException.class, () -> objectMapper.readTree(
				getClass().getClassLoader().getResourceAsStream(RESOURCE_FOLDER + "/" + INVALID_JSON_FILE)
		));
		assertThrows(IOException.class, () -> objectMapper.readTree(new FileInputStream(new File("missing-file.json"))));
		assertThrows(IllegalArgumentException.class, () -> objectMapper.readTree(getClass().getClassLoader().getResourceAsStream("missing-file.json")));
	}

	private WrappedListExample getWrappedListExample() {
		final WrappedListExample wrappedListExample = new WrappedListExample();
		wrappedListExample.complexList = new ArrayList<>();
		final GenericClassExample<POJO> pojo0 = new GenericClassExample<POJO>();
		final GenericClassExample<POJO> pojo1 = new GenericClassExample<POJO>();
		pojo0.generic = new POJO();
		pojo1.generic = new POJO();
		pojo0.generic.field1 = "test1";
		pojo0.generic.field2 = POJE.ENOOM1;
		pojo1.generic.field2 = POJE.ENOOM2;
		wrappedListExample.complexList.add(pojo0);
		wrappedListExample.complexList.add(pojo1);
		return wrappedListExample;
	}
	
	@BeforeAll
	public static void beforeAll() throws IOException {
		writeFile(VALID_JSON_FILE, VALID_JSON_TEXT);
		writeFile(INVALID_JSON_FILE, INVALID_JSON_TEXT);
	}
	
	@AfterAll
	public static void afterAll() throws IOException {
		deleteFile(VALID_JSON_FILE);
		deleteFile(INVALID_JSON_FILE);
	}

	private static void writeFile(final String location, final String content) throws IOException {
		final FileWriter fileWriter = new FileWriter(new File(location));
		fileWriter.write(content);
		fileWriter.close();
	}

	private static void deleteFile(final String location) throws IOException {
		Files.delete(new File(location).toPath());
	}

}
