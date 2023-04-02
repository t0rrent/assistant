package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
//import au.com.cascadesoftware.engine3.gui.Alignment;
//import au.com.cascadesoftware.engine3.gui.Boundary;
//import au.com.cascadesoftware.engine3.gui.GUI;
//import au.com.cascadesoftware.engine3.gui.GUIText;
//import au.com.cascadesoftware.engine3.gui.Scalar;
//import au.com.cascadesoftware.engine3.gui.Theme;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

@MultipleChildGroups(dimensions = 1)
public class GUITabs extends GUI {
	
	private Color primaryColor, secondaryColor;
	private GUI[] containers;
	private GUI[] tabs;
	int focus;

	public GUITabs(Window window, Boundary bounds, int size, Color primaryColor, Color secondaryColor) {
		this(window, bounds, new String[size], Theme.TEST1);
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
	}
	
	public GUITabs(Window window, Boundary bounds, String[] tabs, Theme theme) {
		super(window, bounds);
		this.primaryColor = theme.primaryElementColor;
		this.secondaryColor = theme.secondaryElementColor;
		float divider = 0.1f;
		float gap = 0.05f;
		containers = new GUI[tabs.length];
		this.tabs = new GUI[tabs.length];
		float width = (1f - (tabs.length - 1)*gap)/tabs.length;
		GUIText[] textFields = new GUIText[tabs.length];
		for(int i = 0; i < tabs.length; i++){
			containers[i] = new GUI(window, new Boundary(new Rectf(0, 0, 1f, 1f - divider), Scalar.STRETCHED, Alignment.BOTTOM_LEFT));
			super.addGUI(containers[i]);
			GUI tab = new GUI(window, new Boundary(new Rectf((width + gap)*i, 0, width, divider), Scalar.STRETCHED, Alignment.TOP_LEFT));
			this.tabs[i] = tab;
			super.addGUI(tab);
			if(tabs[i] == null) continue;
			GUIText text = new GUIText(window, new Boundary(new Rectf(0, 0, 0.8f, 0.5f), Scalar.STRETCHED, Alignment.MIDDLE_CENTER), theme.textColor, tabs[i]);
			textFields[i] = text;
			tab.addGUI(text);
		}
		textFields[0].sync(textFields);
		setFocus(0);
	}
	
	public GUITabs(Window window, Boundary bounds, GUI[] tabs, Theme theme) {
		super(window, bounds);
		this.primaryColor = theme.primaryElementColor;
		this.secondaryColor = theme.secondaryElementColor;
		float divider = 0.1f;
		containers = new GUI[tabs.length];
		this.tabs = tabs;
		for(int i = 0; i < tabs.length; i++){
			containers[i] = new GUI(window, new Boundary(new Rectf(0, 0, 1f, 1f - divider), Scalar.STRETCHED, Alignment.BOTTOM_LEFT));
			super.addGUI(containers[i]);
			super.addGUI(this.tabs[i]);
		}
		setFocus(0);
	}
	
	@Override
	public void render(Graphics graphics){
		drawBackground(graphics);
		for(GUI gui : tabs){
			gui.render(graphics);
		}
		containers[focus].render(graphics);
	}

	@Override
	public void update() {
		this.updateInput();
		containers[focus].update();
	}

	@Override
	public void addGUI(GUI gui) {
		addGUI(gui, 0);
	}

	public void addGUI(GUI gui, int i) {
		containers[i].addGUI(gui);
	}
	
	@Override
	protected void updateInput() {
		InputHandler ih = getWindow().getInput();
		if(ih.isMouseClicked(Mouse.BUTTON1)){
			for(int i = 0; i < tabs.length; i++){
				if(tabs[i].getRenderBounds().contains(ih.getMousePos())){
					setFocus(i);
				}
			}
		}
	}
	
	public void setFocus(int i){
		focus = i;
		for(int j = 0; j < tabs.length; j++){
			if(i != j) tabs[j].setBackground(primaryColor);
			else tabs[j].setBackground(secondaryColor);
		}
	}
	
}
