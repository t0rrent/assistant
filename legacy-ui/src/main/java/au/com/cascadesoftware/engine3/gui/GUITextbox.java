package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;

public abstract class GUITextbox extends GUI {
	
	public GUITextbox(Window window, Boundary bounds) {
		super(window, bounds);
	}

	public abstract GUI setPlaceholderText(String placeholder);

	public abstract void setInputText(String inputText);
	
	public abstract void setTextAlignment(Alignment alignment);

	public abstract String getInputText();

	public abstract void setOnEntry(Runnable r);

	public abstract void setFocus(boolean b);

	protected abstract void onTextChanged();

	protected abstract boolean validate(String keytext);
	
}
