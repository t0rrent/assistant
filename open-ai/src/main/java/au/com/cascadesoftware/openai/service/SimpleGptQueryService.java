package au.com.cascadesoftware.openai.service;

import static au.com.cascadesoftware.openai.model.Message.ROLE_ASSISTANT;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import au.com.cascadesoftware.http.exception.HttpException;
import au.com.cascadesoftware.http.model.HttpRequest;
import au.com.cascadesoftware.http.model.HttpRequest.RequestMethod;
import au.com.cascadesoftware.http.service.HttpService;
import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.openai.model.Conversation;
import au.com.cascadesoftware.openai.model.Message;
import au.com.cascadesoftware.openai.model.OpenAIConfig;
import jakarta.inject.Inject;

public class SimpleGptQueryService implements GptQueryService {
	
	private static final int READ_TIMEOUT_MILLISECONDS = 30000;
	private static final String API_URL = "https://api.openai.com/v1/chat/completions";
	
	private final HttpService httpService;
	private final ObjectMapper objectMapper;
	private final JsonService jsonService;
	private final OpenAIConfig config;
	private final ScheduledExecutorService scheduledExecutorService;
	
	@Inject
	public SimpleGptQueryService(
			final HttpService httpService, 
			final ObjectMapper objectMapper,
			final JsonService jsonService,
			final OpenAIConfig config,
			final ScheduledExecutorService scheduledExecutorService
	) {
		this.httpService = httpService;
		this.objectMapper = objectMapper;
		this.jsonService = jsonService;
		this.config = config;
		this.scheduledExecutorService = scheduledExecutorService;
	}

	@Override
	public Conversation message(final Conversation conversation) {
		try {
			final JsonNode response = httpService.invokeHttp(
					HttpRequest.builder(API_URL, RequestMethod.POST)
							.setBody(getQueryBody(conversation).toString())
							.setAuthorization("Bearer " + config.getApiKey())
							.setReadTimeoutMs(READ_TIMEOUT_MILLISECONDS)
							.build()
			);
			final Message newMessage = jsonService.extract(response.toString(), Message.class, "choices", "0", "message");
			conversation.addMessage(newMessage);
			return conversation;
		} catch (final HttpException e) {
			final Message errorMessage = new Message();
			errorMessage.setRole(ROLE_ASSISTANT);
			errorMessage.setContent(jsonService.extract(e.getResponseMessage(), String.class, "error", "message"));
			conversation.addMessage(errorMessage);
			return conversation;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void messageAsync(final Conversation conversation, final Consumer<Conversation> callback) {
		scheduledExecutorService.execute(() -> callback.accept(message(conversation)));
	}

	private JsonNode getQueryBody(final Conversation conversation) throws JsonMappingException, JsonProcessingException {
		final ObjectNode body = objectMapper.createObjectNode();
		final JsonNode messages = objectMapper.convertValue(conversation.getMessages(), JsonNode.class);
		body.set("model", objectMapper.readValue("\"gpt-3.5-turbo\"", JsonNode.class));
		body.set("messages", messages);
		body.set("temperature", objectMapper.readValue("0.7", JsonNode.class));
		body.set("max_tokens", objectMapper.readValue("2000", JsonNode.class));
		return body;
	}

}
