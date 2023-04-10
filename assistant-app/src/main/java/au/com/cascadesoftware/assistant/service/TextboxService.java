package au.com.cascadesoftware.assistant.service;

public interface TextboxService {

	void backspace();

	void append(char c);
	
	String getContent();

	void clear();

}
