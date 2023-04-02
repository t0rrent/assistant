package au.com.cascadesoftware.engine3.gui.android;

import au.com.cascadesoftware.engine3.gui.Alignment;

public interface AndroidTextbox {

	public void setTextAlignment(Alignment alignment);

	public void setPlaceholderText(String placeholder);

	public void setInputText(String inputText);

	public void dispose();

	public void setFocus(boolean focus);
	
	public void update();

}
