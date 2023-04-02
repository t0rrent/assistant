package au.com.cascadesoftware.http.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.http.exception.HttpException;
import au.com.cascadesoftware.http.hk2.extension.HttpHK2TestExtension;
import au.com.cascadesoftware.http.model.HttpRequest;
import au.com.cascadesoftware.http.model.HttpRequest.RequestMethod;
import jakarta.inject.Inject;

@ExtendWith(HttpHK2TestExtension.class)
public class SimpleHttpServiceIT {
	
	//private static final String EXAMPLE_JSON = "{\n    \"text\": \"just some nicely formatted json\"\n}";
	
	@Inject
	public HttpService httpService;
	
	@Inject
	public ObjectMapper objectMapper;
	
	@Test
	public void testInvokeHttpGet() throws IOException, HttpException {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("symbol", "000020");

		final JsonNode result = httpService.invokeHttp(
				HttpRequest.builder("https://api.twelvedata.com/stocks", RequestMethod.GET)
						.setParameter("symbol", "000020")
						.build()
		);
				
		assertTrue(result.toString().length() < 500);
		assertTrue(result.toString().contains("Dongwha Pharm.Co.,Ltd"));
		
		assertTrue(httpService.invokeHttp(HttpRequest.simpleGet("https://api.twelvedata.com/technical_indicators")).toString().length() > 500);
	}
	
	@Test
	public void testInvalidInvokeHttpGet() {
		assertThrows(HttpException.class, () -> httpService.invokeHttp(HttpRequest.simpleGet("https://example.com/")));
		assertThrows(HttpException.class, () -> httpService.invokeHttp(HttpRequest.simpleGet("https://api.openweathermap.org/data")));
		assertThrows(NullPointerException.class, () -> httpService.invokeHttp(HttpRequest.simpleGet("")));
		assertThrows(NullPointerException.class, () -> httpService.invokeHttp(HttpRequest.simpleGet(null)));
	}
	
	/*
	private void testInvalidPostOrPut(
			final ThrowingTriFunction<JsonNode, String, Map<String, String>, JsonNode, Exception> transform, 
			final String methodUrl
	) throws JsonProcessingException {
		final JsonNode content = objectMapper.readTree(EXAMPLE_JSON);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("symbol", "000020");
		assertThrows(HttpException.class, () -> transform.apply(content, "https://api.twelvedata.com/technical_indicators", parameters));
		
		assertThrows(HttpException.class, () -> transform.apply(content, "https://api.openweathermap.org/data", null));
		
		assertThrows(NullPointerException.class, () -> transform.apply(null, "http://httpbin.org/" + methodUrl, null));
		assertThrows(NullPointerException.class, () -> transform.apply(content, "", null));
		assertThrows(NullPointerException.class, () -> transform.apply(content, null, null));
	}
	
	@Test
	public void testPost() throws IOException, HttpException {
		testPostOrPut(httpService::post, "post");
	}

	@Test
	public void testInvalidPost() throws JsonProcessingException {
		testInvalidPostOrPut(httpService::post, "post");
		assertThrows(JsonParseException.class, () -> httpService.post(objectMapper.readTree(EXAMPLE_JSON), "https://example.com/"));
	}

	@Test
	public void testPut() throws IOException, HttpException  {
		testPostOrPut(httpService::put, "put");
	}

	@Test
	public void testInvalidPut() throws JsonProcessingException {
		testInvalidPostOrPut(httpService::put, "put");
	}*/
	

}
