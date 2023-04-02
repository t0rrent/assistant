package au.com.cascadesoftware.http.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.http.hk2.extension.HttpHK2TestExtension;
import jakarta.inject.Inject;

@ExtendWith(HttpHK2TestExtension.class)
public class UrlServiceTest {
	
	@Inject
	public UrlService urlService;
	
	@Test
	public void encodingTest() throws UnsupportedEncodingException {
		final String test = "abcd?&=/\\+";
		final String result = URLEncoder.encode(test, "UTF-8");
		assertEquals("abcd%3F%26%3D%2F%5C%2B", result);
	}
	
	@Test
	public void parameterBuilderTest() {
		final Map<String, String> map = new HashMap<>();
		map.put("par1", "te st1");
		map.put("par2", "test2");
		assertEquals("par1=te%20st1&par2=test2", urlService.buildParameters(map));
		assertEquals("", urlService.buildParameters(null));
	}
	
	@Test
	public void urlBuildingTest() {
		final Map<String, String> map = new HashMap<>();
		map.put("par1", "te st1");
		map.put("par2", "te>st2");
		assertEquals(
				"http://test-site.biz/asd?par1=te%20st1&par2=te%3Est2",
				urlService.getUrl("http://test-site.biz/asd", map).toString()
		);
		assertEquals("http://test-site.biz", urlService.getUrl("http://test-site.biz", null).toString());
	}
	
	@Test
	public void urlBuildingTestEmptyParameters() {
		final Map<String, String> map = new HashMap<>();
		assertEquals(
				"http://test-site.biz/asd",
				urlService.getUrl("http://test-site.biz/asd", map).toString()
		);
	}


}
