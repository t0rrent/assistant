package au.com.cascadesoftware.engine3.gui;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.function.Function;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Recti;
//import au.com.cascadesoftware.engine3.Platform;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.display.Window.Cursor;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Keyboard;
import au.com.cascadesoftware.engine3.in.Mouse;

//@NativeUI(platform = Platform.DESKTOP)
public class GUITextboxDesktop extends GUITextbox  {
	
	private String placeholder, inputText;
	private boolean enteringInput;
	private Color textColor, placeholderColor;
	private GUIMultilineText textField;
	private boolean controlTextLast;
	private int enteringIndex;
	private Cursor oldCursor;
	
	private GUIShapeRectangle box;
	private Runnable onEntry;
	private long tickTime;
	private boolean cursorBlack;
			
	private Function<Recti, Recti> inputListenerZone;
	private boolean lastHover;

	public GUITextboxDesktop(Window window, Boundary bounds, int lines, Color textColor, Color placeholderColor, Color boxColor, Color borderColor) {
		super(window, bounds);
		this.inputListenerZone = (onScreenBounds) -> onScreenBounds;
		oldCursor = Cursor.DEFAULT;
		this.textColor = textColor;
		this.placeholderColor = placeholderColor;
		inputText = "";
		placeholder = "";
		box = new GUIShapeRectangle(window, new Boundary(new Rectf(0,0,1,1), Scalar.STRETCHED, Alignment.MIDDLE_CENTER), boxColor);
		this.addGUI(box);
		textField = new GUIMultilineText(window, new Boundary(new Rectf(0,0,1,1), Scalar.STRETCHED, Alignment.MIDDLE_CENTER), lines, textColor, "");
		this.addGUI(textField);
	}
	
	public GUITextboxDesktop(Window window, Boundary bounds, int lines, Theme theme){
		this(window, bounds, lines, theme.textColor, theme.subTextColor, theme.primaryElementColor, theme.borderColor);
	}
	
	@Override
	protected void updateInput() {
		try{
		
		InputHandler input = getWindow().getInput();
		boolean controlTest = doControlTest();
		if(controlTest || controlTextLast) return;
		controlTextLast = controlTest;
		Recti boxBounds = inputListenerZone.apply(getOnScreenBounds());
		final boolean currentHover = boxBounds.contains(input.getMousePos());
		if (currentHover != lastHover) {
			if (currentHover) {
				oldCursor = getWindow().getCursor();
				getWindow().setCursor(cursorBlack ? Cursor.TEXT_BLACK : Cursor.TEXT);
			} else {
				getWindow().setCursor(oldCursor);
			}
		}
		lastHover = currentHover;
		if(input.isMouseClicked(Mouse.BUTTON1)){
			enteringInput = boxBounds.contains(input.getMousePos());
			if(!enteringInput) inputText = inputText.replace(TICK, "");
			textField.setText(inputText);
		}
		if(enteringInput){
			String oldInput = inputText.replace(TICK, "");
			String oldRawInput = inputText;
			String keytext = input.getTypedText();
			boolean update = keytext.length() > 0;
			boolean tick = (((System.nanoTime() - tickTime)/1000000000f) % 1f) <= 0.5f;
			if(tick){
				if(!inputText.contains(TICK)){
					inputText = inputText.substring(0, inputText.length() - enteringIndex) + TICK + inputText.substring(inputText.length() - enteringIndex);
					update = true;
				}else if(update){
					inputText = inputText.replace(TICK, "");
					inputText = inputText.substring(0, inputText.length() - enteringIndex) + TICK + inputText.substring(inputText.length() - enteringIndex);
				}
			}else{
				if(inputText.contains(TICK)){
					inputText = inputText.replace(TICK, "");
					update = true;
				}
			}
			//String keychar = InputHandler.Util.getCharFromKeyText(keytext, input.isKeyDown(Keyboard.SHIFT));
			if(keytext.contains("[bksp]")){
				update = true;
				keytext.replace("[bksp]", "");
				if(inputText.length() != 0){
					if(tick){
						inputText = inputText.replace(TICK, "");
						if(inputText.length() != 0){
							backspace();
						}
						inputText = inputText.substring(0, inputText.length() - enteringIndex) + TICK + inputText.substring(inputText.length() - enteringIndex);
					}else{
						backspace();
					}
				}
			}
			if(input.isKeyDown(Keyboard.CONTROL) && input.isKeyPressed(Keyboard.V)){
	            try {
	            	inputText = inputText.substring(0, inputText.length() - enteringIndex) + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor) + inputText.substring(inputText.length() - enteringIndex);
				} catch (HeadlessException | UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
				}
			}else if(keytext.length() == 1 && validate(keytext)){
				inputText = inputText.replace(TICK, "");
				inputText = inputText.substring(0, inputText.length() - enteringIndex) + keytext + inputText.substring(inputText.length() - enteringIndex);
			}
			else{
				if(keytext.equals("[entr]")){
					if(onEntry != null) onEntry.run();
					enter();
				}
			}
			boolean updateTick = false;
			if(input.isKeyPressed(Keyboard.LEFT)){
				enteringIndex++;
				if(enteringIndex > inputText.replace(TICK, "").length()) enteringIndex = inputText.replace(TICK, "").length();
				updateTick = true;
			}
			if(input.isKeyPressed(Keyboard.RIGHT)){
				enteringIndex--;
				if(enteringIndex < 0) enteringIndex = 0;
				updateTick = true;
			}
			if(updateTick){
				tickTime = System.nanoTime();
				if(inputText.contains(TICK)){
					inputText = inputText.replace(TICK, "");
					//inputText = inputText.substring(0, inputText.length() - enteringIndex) + TICK + inputText.substring(inputText.length() - enteringIndex);
				}
			}
			textField.setTextColor(textColor);
			
			if(update){
				textField.setText(inputText);
			}
			if(!inputText.replace(TICK, "").equals(oldInput)){
				onTextChanged();
			}
			if(!inputText.equals(oldRawInput)){
				onVisualChanged();
			}
		}else if(inputText.length() == 0 && !textField.getText().equals(placeholder)){
			textField.setTextColor(placeholderColor);
			textField.setText(placeholder);
		}
		
		}catch(java.lang.StringIndexOutOfBoundsException e){
			//happens sometimes with multithreading
			enteringIndex = 0;
			inputText = "";
		}
	}
	
	protected void onBackspace(){}
	
	private void backspace() {
		if(enteringIndex < inputText.length()){
			inputText = inputText.substring(0, inputText.length() - enteringIndex - 1) + inputText.substring(inputText.length() - enteringIndex);
			onBackspace();
		}
	}

	@Override
	protected boolean validate(String keytext) {
		return true;
	}

	@Override
	protected void updateInputOverride() {
		if(doControlTest()){
			enteringInput = false;
			if(inputText.contains(TICK)){
				inputText = inputText.replace(TICK, "");
				textField.setText(inputText);
			}
		}
	}
	
	public void enter() {}
	
	@Override
	public GUI setPlaceholderText(String placeholder){
		this.placeholder = placeholder;
		return this;
	}

	@Override
	public void setInputText(String inputText){
		this.inputText = inputText;
		textField.setText(inputText);
	}

	@Override
	public void setTextAlignment(Alignment alignment){
		textField.setTextAlignment(alignment);
	}
	
	public void setBorder(float width, Color color){
		box.setBorder(width, color);
	}
	
	@Override
	public String getInputText(){
		return inputText.replace(TICK, "");
	}
	
	private static final String TICK = "[Ìtick]";

	@Override
	public void setOnEntry(Runnable r) {
		this.onEntry = r;
	}

	@Override
	public void setFocus(boolean b) {
		if(!b) inputText = inputText.replace(TICK, "");
		enteringInput = b;
	}
	
	protected void onVisualChanged(){}

	@Override
	protected void onTextChanged() {}

	public void setCursorBlack(boolean b) {
		this.cursorBlack = b;
	}
	
	public void setInputListenerZone(final Function<Recti, Recti> inputListenerZone) {
		this.inputListenerZone = inputListenerZone;
	}

}
