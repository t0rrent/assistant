package au.com.cascadesoftware.openai.service;

import au.com.cascadesoftware.openai.model.Conversation;

public interface GptQueryService {

	Conversation message(Conversation conversation);

}
