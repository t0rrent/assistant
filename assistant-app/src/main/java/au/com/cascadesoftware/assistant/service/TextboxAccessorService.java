package au.com.cascadesoftware.assistant.service;

import au.com.cascadesoftware.engine3.gui.GUITextboxDesktop;

public class TextboxAccessorService implements TextboxService {
	
	private GUITextboxDesktop textbox;

	public void setTextbox(final GUITextboxDesktop textbox) {
		this.textbox = textbox;
	}

	@Override
	public void backspace() {
		final String textboxText = textbox.getInputText();
		if (textboxText.length() > 0) {
			textbox.setInputText(textboxText.substring(0, textboxText.length() - 1));
		}
	}

	@Override
	public void append(final char c) {
		if (textbox != null) {
			textbox.setInputText(textbox.getInputText() + c);
		}
	}

	@Override
	public String getContent() {
		if (textbox != null) {
			return textbox.getInputText();
		} else {
			return null;
		}
	}

	@Override
	public void clear() {
		textbox.setInputText("");
	}

}
