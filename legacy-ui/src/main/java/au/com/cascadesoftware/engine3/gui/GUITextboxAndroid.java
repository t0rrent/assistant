package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Recti;
//import au.com.cascadesoftware.engine3.Platform;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.gui.android.AndroidTextbox;
import au.com.cascadesoftware.engine3.in.InputHandler;

//@NativeUI(platform = Platform.ANDROID)
public class GUITextboxAndroid extends GUITextbox {

	private boolean invoked;
	private int lines;
	private String placeholder, inputText;
	private Color textColor, placeholderColor, borderColor, boxColor;
	private Alignment alignment;
	private AndroidTextbox textbox;

	public GUITextboxAndroid(Window window, Boundary bounds, int lines, Color textColor, Color placeholderColor, Color boxColor, Color borderColor) {
		super(window, bounds);
		this.textColor = textColor;
		this.placeholderColor = placeholderColor;
		this.borderColor = borderColor;
		this.boxColor = boxColor;
		inputText = "";
		this.lines = lines;
		alignment = Alignment.MIDDLE_CENTER;
	}
	
	public GUITextboxAndroid(Window window, Boundary bounds, int lines, Theme theme){
		this(window, bounds, lines, theme.textColor, theme.subTextColor, theme.primaryElementColor, theme.borderColor);
	}
	
	@Override
	protected void updateInput() {
		InputHandler input = this.getWindow().getInput();
		if(doControlTest()) return;
		Recti boxBounds = getOnScreenBounds();
		if(input.isScreenTapped()){
			textbox.setFocus(boxBounds.contains(input.getMousePos()));
			return;
		}
		return;
	}
	
	@Override
	protected void draw(Graphics graphics) {
		if(!invoked){
			textbox = graphics.invokeAndroidTextbox(getRenderBounds(), lines, textColor, placeholderColor, boxColor, borderColor);
			textbox.setInputText(inputText);
			textbox.setPlaceholderText(placeholder);
			textbox.setTextAlignment(alignment);
			invoked = true;
		}else{
			textbox.update();
		}
	}

	@Override
	protected void dispose() {
		if(invoked) textbox.dispose();
	}
	
	@Override
	public GUI setPlaceholderText(String placeholder){
		this.placeholder = placeholder;
		if(invoked) textbox.setPlaceholderText(placeholder);
		return this;
	}

	@Override
	public void setInputText(String inputText){
		this.inputText = inputText;
		if(invoked) textbox.setInputText(placeholder);
	}

	@Override
	public void setTextAlignment(Alignment alignment){
		this.alignment = alignment;
		if(invoked) textbox.setTextAlignment(alignment);
	}

	@Override
	public String getInputText() {
		return this.inputText;
	}

	@Override
	public void setOnEntry(Runnable r) {
		
	}

	@Override
	public void setFocus(boolean b) {
		textbox.setFocus(b);
	}

	@Override
	protected void onTextChanged() {
		
	}

	@Override
	protected boolean validate(String keytext) {
		return true;
	}

}
