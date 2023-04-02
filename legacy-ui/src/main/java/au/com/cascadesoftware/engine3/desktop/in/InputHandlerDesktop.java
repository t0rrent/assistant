package au.com.cascadesoftware.engine3.desktop.in;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Keyboard;

public class InputHandlerDesktop implements InputHandler {
	
	private final int someArbitraryNumber = 200;
	
	private boolean[][] mouse;
	private boolean[][] keyboard;
	private Vector2i mousePos;
	private String typedText, typedTextBuf;
	private int scrollValue, scrollValueBuf;
	
	public InputHandlerDesktop(){
		mouse = new boolean[MouseEvent.MOUSE_LAST][3];
		keyboard = new boolean[KeyEvent.KEY_LAST + someArbitraryNumber][3];
		mousePos = new Vector2i();
		typedText = "";
		scrollValue = 0;
	}
	
	public void setNoInput() {
		for(int i = 0; i < mouse.length; i++){
			for(int j = 0; j < mouse[0].length; j++){
				mouse[i][j] = false;
			}
		}
		for(int i = 0; i < keyboard.length; i++){
			for(int j = 0; j < keyboard[0].length; j++){
				keyboard[i][j] = false;
			}
		}
	}
	
	public void update(){
		for(int i = 0; i < MouseEvent.MOUSE_LAST; i++){
			mouse[i][2] = mouse[i][1];
			mouse[i][1] = mouse[i][0];
		}
		for(int i = 0; i < KeyEvent.KEY_LAST + someArbitraryNumber; i++){
			keyboard[i][2] = keyboard[i][1];
			keyboard[i][1] = keyboard[i][0];
		}
		typedTextBuf = typedText;
		typedText = "";
		scrollValueBuf = scrollValue;
		scrollValue = 0;
	}

	public void mouseDown(int button){
		mouse[button][0] = true;
	}

	public void mouseUp(int button){
		mouse[button][0] = false;
	}

	public void keyDown(int button){
		keyboard[button][0] = true;
		int key = Util.translateKeyWin(button);
		String c = InputHandler.Util.getCharFromKeyText(KeyEvent.getKeyText(key), isKeyDown(Keyboard.SHIFT), this.isCapsLocked());
		typedText = typedText + c;
	}

	public void keyUp(int button){
		keyboard[button][0] = false;
	}

	public void setMousePos(Vector2i mousePos){
		this.mousePos = mousePos;
	}

	public void setScrollValue(int sv){
		this.scrollValue = sv;
	}
	
	@Override
	public boolean isMouseClicked(int button){
		button = Util.translateMouseWin(button);
		return !mouse[button][2] && mouse[button][1];
	}

	@Override
	public boolean isMouseDown(int button){
		button = Util.translateMouseWin(button);
		return mouse[button][1];
	}

	@Override
	public boolean isKeyPressed(int button){
		button = Util.translateKeyWin(button);
		return !keyboard[button][2] && keyboard[button][1];
	}

	@Override
	public boolean isKeyDown(int button){
		button = Util.translateKeyWin(button);
		return keyboard[button][1];
	}

	@Override
	public boolean isScreenTouching() {
		// screen touch event
		return false;
	}

	@Override
	public boolean isScreenTapped() {
		// screen touch event
		return false;
	}

	public boolean isShifted() {
		return keyboard[KeyEvent.VK_SHIFT][1] != this.isCapsLocked();
	}
	
	public int[] getKeysPressed() {
		int n = 0;
		for(int i = 0; i < keyboard.length; i++){
			if(isKeyPressed(i)) n++;
		}
		int[] keysPressed = new int[n];
		n = 0;
		for(int i = 0; i < keyboard.length; i++){
			if(isKeyPressed(i)){
				keysPressed[n] = i;
				n++;
			}
		}
		return keysPressed;
	}

	@Override
	public Vector2i getMousePos() {
		return mousePos;
	}

	@Override
	public String getTypedText() {
		return typedTextBuf;
	}
	
	@Override
	public boolean isCapsLocked() {
		return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
	}

	@Override
	public int getScrollValue() {
		return scrollValueBuf;
	}
	
	
	
	public static class Util{

		public static int translateKeyWin(int button) {
			//KeyEvent button values will be the standard for this engine
			return button;
		}
		
		public static int translateMouseWin(int button) {
			//MouseEvent button values will be the standard for this engine
			return button;
		}
		
	}

}
