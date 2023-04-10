package au.com.cascadesoftware.openai.service;

import java.util.function.Consumer;

import au.com.cascadesoftware.openai.model.Conversation;

public interface GptQueryService {

	Conversation message(Conversation conversation);

	void messageAsync(Conversation conversation, Consumer<Conversation> callback);

}
