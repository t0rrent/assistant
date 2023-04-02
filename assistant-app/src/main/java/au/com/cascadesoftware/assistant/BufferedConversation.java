package au.com.cascadesoftware.assistant;

import java.util.HashMap;
import java.util.Map;

import au.com.cascadesoftware.openai.model.Conversation;

public class BufferedConversation extends Conversation {

	private static final long serialVersionUID = -7128676788405598138L;
	
	private Map<String, String> bufferMap;
	
	public BufferedConversation() {
		bufferMap = new HashMap<>();
	}
	
	public void setBuffer(final String role, final String buffer) {
		bufferMap.put(role, buffer);
	}

	public void pushBuffer(final String role) {
		addMessage(role, bufferMap.get(role));
		bufferMap.remove(role);
	}
	
	public String getBuffer(final String role) {
		return bufferMap.get(role);
	}

}
