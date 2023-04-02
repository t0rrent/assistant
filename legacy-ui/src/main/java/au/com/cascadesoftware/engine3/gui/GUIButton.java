package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

public abstract class GUIButton extends GUI {
	
	private ClickType clickType;
	private boolean[] hold;
	private SpecialClickBounds specialClickBounds;
	
	public abstract void onClick();

	public GUIButton(Window window, Boundary bounds) {
		super(window, bounds);
		specialClickBounds = getSpecialClickBounds();
		hold = new boolean[2];
		clickType = ClickType.ON_MOUSE_DOWN;
	}
	
	@Override
	protected void updateInputOverride() {
		InputHandler ih = getWindow().getInput();
		if(clickType == ClickType.ON_MOUSE_DOWN && specialClickBounds.contains(ih.getMousePos())){
			if(ih.isMouseClicked(Mouse.BUTTON1) || ih.isScreenTapped()) onClick();
			else if(ih.isMouseClicked(Mouse.BUTTON2)) onRightClick();
		}
		if(clickType == ClickType.ON_MOUSE_UP){
			if(specialClickBounds.contains(ih.getMousePos())){
				if(ih.isMouseClicked(Mouse.BUTTON1) || ih.isScreenTapped()) hold[0] = true;
				if(ih.isMouseClicked(Mouse.BUTTON2)) hold[1] = true;
			}
			if(!(ih.isMouseDown(Mouse.BUTTON1) || ih.isScreenTouching())){
				if(hold[0] && specialClickBounds.contains(ih.getMousePos())) onClick();
				hold[0] = false;
			}
			if(!ih.isMouseDown(Mouse.BUTTON2)){
				if(hold[1] && specialClickBounds.contains(ih.getMousePos())) onClick();
				hold[1] = false;
			}
		}
	}
	
	public void onRightClick(){}
	
	public GUIButton setClickType(ClickType ct){
		this.clickType = ct;
		return this;
	}
	
	public enum ClickType {
		ON_MOUSE_DOWN, ON_MOUSE_UP
	}

	public void setSpecialClickBounds(SpecialClickBounds specialClickBounds) {
		this.specialClickBounds = specialClickBounds;
	}

}
