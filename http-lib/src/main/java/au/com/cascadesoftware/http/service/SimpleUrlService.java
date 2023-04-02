package au.com.cascadesoftware.http.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleUrlService implements UrlService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleUrlService.class);
	
	@Override
	public String buildParameters(final Map<String, String> parameters) {
		final StringBuilder parametersBuilder = new StringBuilder();
		if (parameters != null && !parameters.isEmpty()) {
	        for (final Entry<String, String> entry : parameters.entrySet()) {
				try {
					parametersBuilder.append(entry.getKey()).append("=").append(encode(entry.getValue())).append("&");
				} catch (final UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	        }
	        parametersBuilder.setLength(parametersBuilder.length() - 1);
		}
		return parametersBuilder.toString();
	}

	private String encode(final String unencodedString) throws UnsupportedEncodingException {
		return URLEncoder.encode(unencodedString, "UTF-8").replace("+" , "%20");
	}

	@Override
	public URL getUrl(final String baseUrl, final Map<String, String> parameters) {
		try {
			final String parametersAsString = buildParameters(parameters);
			final String url = baseUrl + (parametersAsString.length() > 0 ? "?" + parametersAsString : "");
			return new URL(url);
		} catch (final MalformedURLException e) {
			LOGGER.error("Malformed URL", e);
			return null;
		}
	}

}
