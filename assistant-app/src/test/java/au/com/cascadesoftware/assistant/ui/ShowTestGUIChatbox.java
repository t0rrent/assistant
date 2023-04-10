package au.com.cascadesoftware.assistant.ui;

import static au.com.cascadesoftware.openai.model.Message.ROLE_ASSISTANT;
import static au.com.cascadesoftware.openai.model.Message.ROLE_USER;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import au.com.cascadesoftware.assistant.AssistantApplication;
import au.com.cascadesoftware.assistant.service.ConversationLifeCycle;
import au.com.cascadesoftware.config.hk2.binder.ConfigModuleBinder;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine4.Engine4;
import au.com.cascadesoftware.json.hk2.binder.JsonModuleBinder;
import au.com.cascadesoftware.legacyui.hk2.binder.LegacyUIModuleBinder;
import au.com.cascadesoftware.legacyui.service.UILifeCycle;
import au.com.cascadesoftware.openai.model.Conversation;
import au.com.cascadesoftware.openai.model.Message;
import au.com.cascadesoftware.util.hk2.binder.UtilModuleBinder;

public class ShowTestGUIChatbox {
	
	private static final String LORUM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
	
	public static void main(final String[] args) {
		final Engine4 engine = new Engine4(Arrays.asList(
				LegacyUIModuleBinder::new,
				ConfigModuleBinder::new,
				JsonModuleBinder::new,
				UtilModuleBinder::new
		));
		AssistantApplication.initWindowConfig();
		engine.start();
		final UILifeCycle uiLifeCycle = engine.getServiceLocator().getService(UILifeCycle.class);
		final GUI chatbox = getMockChatbox(uiLifeCycle.getWindow());
		uiLifeCycle.setTopUI(chatbox);
	}

	static GUIChatbox getMockChatbox(final Window window) {
		final ConversationLifeCycle mockConversationLifeCycle = mock(ConversationLifeCycle.class);
		when(mockConversationLifeCycle.getConversation()).thenReturn(getConversation());
		final GUIChatbox mockChatbox = new GUIChatbox(window, new Boundary(), mockConversationLifeCycle);
		return mockChatbox;
	}

	static Conversation getConversation() {
		final Conversation conversation = new Conversation();
		conversation.addMessage(createMessage(ROLE_USER, LORUM_IPSUM));
		conversation.addMessage(createMessage(ROLE_ASSISTANT, "OK. That was just some lorum ipsum."));
		conversation.addMessage(createMessage(ROLE_USER, LORUM_IPSUM));
		conversation.addMessage(createMessage(ROLE_ASSISTANT, LORUM_IPSUM));
		return conversation;
	}
	
	private static Message createMessage(final String role, final String content) {
		final Message message = new Message();
		message.setRole(role);
		message.setContent(content);
		return message;
	}
	
}
