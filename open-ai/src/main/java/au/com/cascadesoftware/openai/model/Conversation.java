package au.com.cascadesoftware.openai.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {
	
	private static final long serialVersionUID = 7794193323123854261L;
	
	private List<Message> messages;
	
	public Conversation() {
		messages = new ArrayList<>();
	}
	
	public void addMessage(final Message message) {
		messages.add(message);
	}
	
	public void addMessage(final String role, final String content) {
		final Message newMessage = new Message();
		newMessage.setRole(role);
		newMessage.setContent(content);
		messages.add(newMessage);
	}
	
	public List<Message> getMessages() {
		return messages;
	}

	@Override
	public String toString() {
		return "Conversation [messages=" + messages + "]";
	}

}
