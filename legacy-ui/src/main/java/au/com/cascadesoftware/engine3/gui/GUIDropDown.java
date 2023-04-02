package au.com.cascadesoftware.engine3.gui;

//import java.util.ArrayList;
//import java.util.List;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Vector2f;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
//import au.com.cascadesoftware.engine3.gui.web.Style;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

@MultipleChildGroups(dimensions = 1)
public class GUIDropDown extends GUITimer {
	
	private GUI[] containers;
	private GUI mainContainer;
	//private float borderWidth, insideBorderWidth;
	//private Color borderColor, insideBorderColor;
	private boolean open;
	private Mode mode;
	private Vector2f phaseTime;
	private float phase;
	private boolean toggleLock; //true to stop menu from reopening until mouse is not longer hovering

	public GUIDropDown(Window window, Boundary bounds, Mode mode, int lines) {
		super(window, bounds);
		phaseTime = new Vector2f();
		this.containers = new GUI[lines];
		for(int i = 0; i < containers.length; i++){
			containers[i] = new GUI(window, new Boundary()).setBackground(Color.INVISIBLE);
			super.addGUI(containers[i]);
		}
		mainContainer = new GUI(window, new Boundary()).setBackground(Color.INVISIBLE);
		this.mode = mode;
		super.addGUI(mainContainer);
	}
	
	@Override
	public void addGUI(GUI gui) {
		addGUI(gui, 0);
	}

	@Override
	public GUI setBackground(Color bgColor) {
		mainContainer.setBackground(bgColor);
		return this;
	}

	public void addGUI(GUI gui, int line) {
		/*boolean top = line == 0;
		boolean bottom = line == containers.length;
		boolean left = true;
		boolean right = true;
		float borderSizeLeft = left ? borderWidth : insideBorderWidth;
		Color borderColorLeft = left ? borderColor : insideBorderColor;
		float borderSizeBottom = bottom ? borderWidth : insideBorderWidth;
		Color borderColorBottom = bottom ? borderColor : insideBorderColor;
		float borderSizeRight = right ? borderWidth : insideBorderWidth;
		Color borderColorRight = right ? borderColor : insideBorderColor;
		float borderSizeTop = top ? borderWidth : insideBorderWidth;
		Color borderColorTop = top ? borderColor : insideBorderColor;
		List<Style> styles = new ArrayList<Style>();
		if(borderColorLeft != null) styles.add(new Style("border-left", borderSizeLeft + "px solid " + borderColorLeft.getHTMLCode()));
		if(borderColorRight != null) styles.add(new Style("border-right", borderSizeRight + "px solid " + borderColorRight.getHTMLCode()));
		if(borderColorTop != null) styles.add(new Style("border-top", borderSizeTop + "px solid " + borderColorTop.getHTMLCode()));
		if(borderColorBottom != null) styles.add(new Style("border-bottom", borderSizeBottom + "px solid " + borderColorBottom.getHTMLCode()));
		float borderVert = borderSizeTop + borderSizeBottom;
		float borderHor = borderSizeLeft + borderSizeRight;
		styles.add(new Style("@height-subtractor", borderVert + "px"));
		styles.add(new Style("@width-subtractor", borderHor + "px"));
		gui.injectCSS(styles.toArray(new Style[styles.size()]));*/
		if(line == 0){
			mainContainer.addGUI(gui);
		}else{
			containers[line - 1].addGUI(gui);
		}
	}

	public void setInsideBorder(float width, Color color) {
		//this.insideBorderWidth = width;
		//this.insideBorderColor = color;
	}
	
	@Override
	protected void updateInput() {
		InputHandler ih = getWindow().getInput();
		if(doControlTest()) return;
		boolean prevOpen = open;
		if(mode == Mode.OPEN_ON_HOVER){
			open = false;
			if(getOnScreenBounds().contains(ih.getMousePos())) open = true;
			if(prevOpen && !open){
				for(GUI g : containers){
					if(g.getOnScreenBounds().contains(ih.getMousePos())){
						open = true;
						break;
					}
				}
			}
			if(!open) toggleLock = false;
			else if(toggleLock) open = prevOpen;
		}
		if(mode == Mode.OPEN_ON_CLICK){
			open = false;
			if(prevOpen){
				if(getOnScreenBounds().contains(ih.getMousePos())) open = true;
				for(GUI g : containers){
					if(g.getOnScreenBounds().contains(ih.getMousePos())){
						open = true;
						break;
					}
				}
			}
		}
		if(ih.isMouseClicked(Mouse.BUTTON1) || ih.isScreenTouching()){
			if(getOnScreenBounds().contains(ih.getMousePos())){
				toggleLock = true;
				open = !open;
			}else{
				open = false;
			}
		}
		/*if(prevOpen && !open){
			for(int i = 0; i < containers.length; i++){
				containers[i].setBounds(new Boundary());
			}
		}else if(!prevOpen && open){
			for(int i = 0; i < containers.length; i++){
				containers[i].setBounds(new Boundary(new Rectf(0, 1 + i, 1, 1), Scalar.STRETCHED, Alignment.TOP_CENTER));
			}
		}*/
	}
	
	@Override
	protected void updateInputOverride() {
		float prevPhase = phase;
		if(open){
			if(phaseTime.x > 0) phase += getDeltaTime()/phaseTime.x;
			else phase = 1;
		}else{
			if(phaseTime.y > 0) phase -= getDeltaTime()/phaseTime.y;
			else phase = 0;
		}
		if(prevPhase > 0 != phase > 0) onOpenStateChanged(open);
		
		if(phase < 0) phase = 0;
		if(phase > 1) phase = 1;
		for(int i = 0; i < containers.length; i++){
			float y = 1 + i - (1 - phase)*containers.length;
			if(y < 0) y = 0;
			containers[i].setBounds(new Boundary(new Rectf(0, y, 1, 1), Scalar.STRETCHED, Alignment.TOP_CENTER));
		}
		if(phase != prevPhase) onPhaseChange();
	}
	
	protected void onOpenStateChanged(boolean open){};
	
	protected void onPhaseChange(){};
	
	public enum Mode {
		OPEN_ON_HOVER, OPEN_ON_CLICK, TOGGLE_ON_CLICK
	}public void setBorder(float width, Color color){
		//this.borderWidth = width;
		//this.borderColor = color;
	}

	public void setPhaseTime(Vector2f phaseTime) {
		this.phaseTime = phaseTime;
	}

	public GUIDropDown setOpenState(boolean b) {
		toggleLock = !b;
		open = b;
		return this;
	}

}
