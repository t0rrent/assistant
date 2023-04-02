package au.com.cascadesoftware.http.service;

import java.net.URL;
import java.util.Map;

import org.eclipse.jdt.annotation.Nullable;

public interface UrlService {

	 String buildParameters(Map<String, String> parameters);
	 
	 URL getUrl(String baseUrl, @Nullable Map<String, String> parameters);
	 
}
