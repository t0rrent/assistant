package au.com.cascadesoftware.http.model;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	
	private final String baseUrl;
	
	private final Map<String, String> parameters;
	
	private final String authorization;
	
	private final String body;
	
	private final RequestMethod requestMethod;
	
	private final int connectTimeoutMs;
	
	private final int readTimeoutMs;
	
	private HttpRequest(
			final String baseUrl,
			final Map<String, String> parameters,
			final String authorization,
			final String body,
			final RequestMethod requestMethod,
			final int connectTimeoutMs,
			final int readTimeoutMs
	) {
		this.baseUrl = baseUrl;
		this.parameters = parameters;
		this.authorization = authorization;
		this.body = body;
		this.requestMethod = requestMethod;
		this.connectTimeoutMs = connectTimeoutMs;
		this.readTimeoutMs = readTimeoutMs;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String getAuthorization() {
		return authorization;
	}

	public String getBody() {
		return body;
	}

	public RequestMethod getRequestMethod() {
		return requestMethod;
	}

	public int getConnectTimeoutMs() {
		return connectTimeoutMs;
	}

	public int getReadTimeoutMs() {
		return readTimeoutMs;
	}
	
	public static Builder builder(final String baseUrl, final RequestMethod requestMethod) {
		return new Builder(baseUrl, requestMethod);
	}
	
	public static HttpRequest simpleGet(final String url) {
		return HttpRequest.builder(url, RequestMethod.GET).build();
	}

	public enum RequestMethod {
		GET, POST, PUT
	}
	
	public static class Builder {
		
		private final String baseUrl;
		
		private Map<String, String> parameters;
		
		private String authorization;
		
		private String body;
		
		private final RequestMethod requestMethod;
		
		private int connectTimeoutMs;
		
		private int readTimeoutMs;
		
		private Builder(final String baseUrl, final RequestMethod requestMethod) {
			if (baseUrl == null || requestMethod == null || baseUrl.isEmpty()) {
				throw new NullPointerException();
			}
			this.baseUrl = baseUrl;
			this.requestMethod = requestMethod;
			parameters = new HashMap<>();
			connectTimeoutMs = 10000;
			readTimeoutMs = 10000;
		}
		
		public Builder setParameter(final String key, final String value) {
			parameters.put(key, value);
			return this;
		}
		
		public Builder setBody(final String body) {
			this.body = body;
			return this;
		}
		
		public Builder setAuthorization(final String authorization) {
			this.authorization = authorization;
			return this;
		}
		
		public Builder setConnectTimeoutMs(final int connectTimeoutMs) {
			this.connectTimeoutMs = connectTimeoutMs;
			return this;
		}
		
		public Builder setReadTimeoutMs(final int readTimeoutMs) {
			this.readTimeoutMs = readTimeoutMs;
			return this;
		}
		
		public HttpRequest build() {
			return new HttpRequest(baseUrl, parameters, authorization, body, requestMethod, connectTimeoutMs, readTimeoutMs);
		}
		
	}

}
