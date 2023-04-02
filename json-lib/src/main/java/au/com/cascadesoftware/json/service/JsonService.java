package au.com.cascadesoftware.json.service;

import java.io.File;
import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonService {

	<T> T extract(String jsonText, Class<T> type, String... locations);

	<T> T extractFromFile(File file, Class<T> type, String... locations);

	<T> T extractFromStream(InputStream file, Class<T> type, String... locations);

	JsonNode extract(String jsonText);

	JsonNode extractFromFile(File file);

	JsonNode extractFromStream(InputStream file);

}
