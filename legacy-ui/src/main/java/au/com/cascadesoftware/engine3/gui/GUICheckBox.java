package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

public class GUICheckBox extends GUI {
	
	private boolean ticked;
	private GUI check;
	private Color checkColor;

	public GUICheckBox(Window window, Boundary bounds, boolean defaultState, float checkWidth) {
		super(window, bounds);
		ticked = defaultState;
		checkColor = Color.BLACK;
		check = new GUI(window, new Boundary(new Rectf(0, 0, checkWidth, checkWidth))).setBackground(defaultState ? checkColor : Color.INVISIBLE);
		addGUI(check);
	}
	
	public GUICheckBox setCheckColor(Color c){
		checkColor = c;
		check.setBackground(ticked ? checkColor : Color.INVISIBLE);
		return this;
	}
	
	public boolean isTicked(){
		return ticked;
	}
	
	@Override
	protected void updateInput() {
		InputHandler ih = getWindow().getInput();
		if(ih.isMouseClicked(Mouse.BUTTON1) && getOnScreenBounds().contains(ih.getMousePos())){
			ticked = !ticked;
			check.setBackground(ticked ? checkColor : Color.INVISIBLE);
			onTicked(ticked);
		}
	}
	
	protected void onTicked(boolean state){};

}
