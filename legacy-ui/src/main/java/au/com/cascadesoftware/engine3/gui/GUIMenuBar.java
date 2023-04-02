package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Vector2f;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.gui.GUIDropDown.Mode;
import au.com.cascadesoftware.engine3.gui.GUIShapeRegularPolygon.Side;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

public abstract class GUIMenuBar extends GUI {
	
	private GUI[][] dropDownMenuElements;
	private GUIDropDown[] dropDownMenus;
	private int openedDropDown;
	private Color barColor, selectedColor, borderColor, hoverColor, textColor;
	
	protected abstract void menuSelection(String name, int i);


	public GUIMenuBar(Window window, Boundary bounds, String[] dropDownMenuNames, int[] dropDownMenuSizes, float relativeElementWidth, Color barColor, Color selectedColor, Color borderColor, Color hoverColor, Color textColor) {
		super(window, bounds);
		if(dropDownMenuNames.length != dropDownMenuSizes.length){
			System.err.print("dropDownMenuNames.length != dropDownMenuSizes.length");
			new Exception().printStackTrace();
			return;
		}
		this.barColor = barColor;
		this.selectedColor = selectedColor;
		this.borderColor = borderColor;
		this.hoverColor = hoverColor;
		this.textColor = textColor;
		setBackground(barColor);
		dropDownMenus = new GUIDropDown[4];
		openedDropDown = -1;
		dropDownMenuElements = new GUI[dropDownMenuNames.length][];
		for(int i = 0; i < dropDownMenuNames.length; i++){
			final int j = i;
			dropDownMenus[i] = new GUIDropDown(window, new Boundary(new Rectf(relativeElementWidth*i, 0, relativeElementWidth, 1), Scalar.VERTICAL, Alignment.MIDDLE_LEFT), Mode.TOGGLE_ON_CLICK, dropDownMenuSizes[i]){
				@Override
				protected void onOpenStateChanged(boolean open) {
					if(open){
						setBackground(selectedColor);
						openedDropDown = j;
					}
					else setBackground(barColor);
				}
			};
			dropDownMenuElements[i] = new GUIButton[dropDownMenuSizes[i]];
			prepDropDown(dropDownMenus[i], dropDownMenuElements[i], dropDownMenuSizes[i], dropDownMenuNames[i]);		
		}
	}
	
	public void addGUI(GUI gui, int dropDown, int element) {
		dropDownMenuElements[dropDown][element].addGUI(gui);
	}

	private void prepDropDown(GUIDropDown file, GUI[] menuElements, int lines, String name) {
		file.setBackground(barColor);
		file.setPhaseTime(new Vector2f(0.05f, 0.05f));
		file.addGUI(new GUIShapeRectangle(getWindow(), new Boundary(), Color.INVISIBLE).setBorder(1f, new Color("#555")));
		for(int i = 0; i < lines; i++){
			final int j = i;
			menuElements[i] = new GUIButton(getWindow(), new Boundary()){
				@Override
				public void onClick() {
					if(!file.getOnScreenBounds().contains(getWindow().getInput().getMousePos())) menuSelection(name, j);
				}
			};
			menuElements[i].setBackground(selectedColor);
			menuElements[i].addGUI(new GUIHover(getWindow(), new Boundary()){
				@Override
				protected void onStateChange(float phase) {
					if(phase == 0 || file.getOnScreenBounds().contains(getWindow().getInput().getMousePos())) setBackground(selectedColor);
					else setBackground(hoverColor);
				}
			});
			menuElements[i].addGUI(new GUIShapeRectangle(getWindow(), new Boundary(), Color.INVISIBLE).setBorder(1f, borderColor).setBorderSide(Side.TOP, 0f));
			file.addGUI(menuElements[i], i+1);
		}
		addGUI(file);
		file.addGUI(new GUIText(getWindow(), new Boundary(), textColor, name));
	}
	
	@Override
	protected void updateInput() {
		InputHandler ih = getWindow().getInput();
		if(openedDropDown >= 0 && ih.isMouseClicked(Mouse.BUTTON1)) openedDropDown = -1;
	}
	
	@Override
	protected void updateInputOverride() {
		InputHandler ih = getWindow().getInput();
		for(int i = 0; i < dropDownMenus.length; i++){
			if(openedDropDown >= 0 && dropDownMenus[i].getOnScreenBounds().contains(ih.getMousePos()) && i != openedDropDown){
				dropDownMenus[openedDropDown].setOpenState(false);
				openedDropDown = i;
				dropDownMenus[i].setOpenState(true);
			}
		}
	}

}
