package au.com.cascadesoftware.http.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.http.exception.HttpException;
import au.com.cascadesoftware.http.model.HttpRequest;
import au.com.cascadesoftware.http.model.HttpRequest.RequestMethod;
import au.com.cascadesoftware.util.IOUtils;
import jakarta.inject.Inject;

public class SimpleHttpService implements HttpService {

	public final UrlService urlService;
	public final ObjectMapper objectMapper;
	
	@Inject
	public SimpleHttpService(
			final UrlService urlService,
			final ObjectMapper objectMapper
	) {
		this.urlService = urlService;
		this.objectMapper = objectMapper;
	}

	@Override
	public JsonNode invokeHttp(final HttpRequest httpRequest) throws IOException, HttpException {
		final URL url = urlService.getUrl(httpRequest.getBaseUrl(), httpRequest.getParameters());
		final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
        connection.setDoOutput(httpRequest.getRequestMethod() != RequestMethod.GET);
        connection.setRequestMethod(httpRequest.getRequestMethod().toString());
        connection.setRequestProperty("Content-Type", "application/json");
        
        if (httpRequest.getAuthorization() != null) {
        	connection.setRequestProperty("Authorization", httpRequest.getAuthorization());
        }

        connection.setConnectTimeout(httpRequest.getConnectTimeoutMs());
        connection.setReadTimeout(httpRequest.getReadTimeoutMs());

        if (httpRequest.getRequestMethod() != RequestMethod.GET) {
	        final OutputStream outputStream = connection.getOutputStream();
	        outputStream.write(httpRequest.getBody().getBytes());
	        outputStream.flush();
        }

        try (
        		InputStream inputStream = connection.getInputStream();
		) {
            final JsonNode response = objectMapper.readTree(inputStream);
        	return response;
        } catch (final SocketTimeoutException e) {
        	throw new HttpException(HttpException.TIMEOUT_CODE, e.getMessage());
        } catch (final IOException e) {
        	final InputStream errorStream = connection.getErrorStream();
        	throw new HttpException(
        			connection.getResponseCode(), 
        			errorStream == null ? "No error message from remote" : IOUtils.readLinesAsString(errorStream)
			);
        } finally {
        	connection.disconnect();
        }
	}

}
