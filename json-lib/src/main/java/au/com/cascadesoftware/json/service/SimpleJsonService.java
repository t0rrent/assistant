package au.com.cascadesoftware.json.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import au.com.cascadesoftware.util.TypeCheck;
import au.com.cascadesoftware.util.service.AnnotationService;
import jakarta.inject.Inject;

public class SimpleJsonService implements JsonService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJsonService.class);
	
	private final ObjectMapper objectMapper;
	private final AnnotationService annotationService;

	@Inject
	public SimpleJsonService(
			final ObjectMapper objectMapper,
			final AnnotationService annotationService
	) {
		this.objectMapper = objectMapper;
		this.annotationService = annotationService;
	}
	
	@Override
	public <T> T extract(final String jsonText, final Class<T> type, final String... locations) {
		if (type == null) {
			return null;
		}
		try {
			final JsonNode root = objectMapper.readTree(jsonText);
			return extract(root, type, locations);
		} catch (final JsonProcessingException | IllegalArgumentException e) {
			LOGGER.error("JSON value extract failed", e);
			return null;
		}
	}

	@Override
	public <T> T extractFromFile(final File file, final Class<T> type, final String... locations) {
		try {
			return extractFromStream(new FileInputStream(file), type, locations);
		} catch (final FileNotFoundException e) {
			LOGGER.error("JSON file now found", e);
			return null;
		}
	}

	@Override
	public JsonNode extract(final String jsonText) {
		return extract(jsonText, JsonNode.class);
	}

	@Override
	public JsonNode extractFromFile(final File file) {
		return extractFromFile(file, JsonNode.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T extract(final JsonNode rootNode, final Class<T> type, final String... locations) throws JsonProcessingException, IllegalArgumentException {	
		if (type == JsonNode.class) {
			return (T) rootNode;
		}
		JsonNode result = rootNode;
		for (final String location : locations) {
			if (TypeCheck.isInteger(location) && result.getNodeType() == JsonNodeType.ARRAY) {
				result = result.get(Integer.parseInt(location));
			} else {
				result = result.get(location);
			}
			if (result == null) {
				return null;
			}
		}
		final T unprocessed =  objectMapper.treeToValue(result, type);
		if (unprocessed == null) {
			return null;
		} else {
			return annotationService.processAnnotations(unprocessed);
		}
	}

	@Override
	public <T> T extractFromStream(final InputStream inputStream, final Class<T> type, final String... locations) {
		if (type == null || inputStream == null) {
			return null;
		}
		try {
			final JsonNode root = objectMapper.readTree(inputStream);
			return extract(root, type, locations);
		} catch (final IOException | IllegalArgumentException e) {
			LOGGER.error("JSON value extract failed", e);
			return null;
		}
	}

	@Override
	public JsonNode extractFromStream(final InputStream inputStream) {
		return extractFromStream(inputStream, JsonNode.class);
	}

}
