package au.com.cascadesoftware.http.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

import au.com.cascadesoftware.http.exception.HttpException;
import au.com.cascadesoftware.http.model.HttpRequest;

public interface HttpService {
	
	JsonNode invokeHttp(HttpRequest httpRequest) throws IOException, HttpException;
	
}
