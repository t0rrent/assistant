package au.com.cascadesoftware.openai.model;

import java.io.Serializable;

public class Message implements Serializable {
	
	private static final long serialVersionUID = 7794193323123854261L;
	
	private String role;
	
	private String content;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Message [role=" + role + ", content=" + content + "]";
	}
	
}
