package au.com.cascadesoftware.assistant.ui;

import java.util.function.Consumer;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.gui.Alignment;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.GUIShapeRoundedRectangle;
import au.com.cascadesoftware.engine3.gui.Scalar;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

public class GUICustomScrollBar extends GUI {	
	
	private static final float STATIC_BAR_RATIO = 0.2f;
	
	private static final Color SCROLLBAR_COLOR = Color.LIGHT_GRAY;

	private static final Color SCROLLBAR_BACKGROUND_COLOR = new Color("ddd");

	private final Consumer<Float> onScrollChange;
	
	private final GUIShapeRoundedRectangle bar;
	
	private float barRatio = STATIC_BAR_RATIO;
	
	private boolean holding;
	
	private int holdReference;
	
	private float scrollValue, heldValue;

	private boolean previousMouseDown;

	public GUICustomScrollBar(final Window window, final Boundary bounds, final Consumer<Float> onScrollChange) {
		super(window, bounds);
		int rounded = 6;
		this.onScrollChange = onScrollChange;
		this.bar = new GUIShapeRoundedRectangle(window, new Boundary(), SCROLLBAR_COLOR, rounded);
		addGUI(this.bar);
		setBackground(SCROLLBAR_BACKGROUND_COLOR);
		onResize();
	}
	
	@Override
	protected void updateInput() {
		final InputHandler input = getWindow().getInput();
		float range = (1 - barRatio) * getOnScreenBounds().height;
		if (!holding) {
			if (input.isMouseDown(Mouse.BUTTON1) && !previousMouseDown && getOnScreenBounds().contains(input.getMousePos())) {
				if (!bar.getOnScreenBounds().contains(input.getMousePos())) {
					int remoteY = input.getMousePos().y - (int) (getOnScreenBounds().height * (scrollValue * (1 - barRatio) + barRatio));
					if (remoteY < 0) {
						remoteY += (int) (getOnScreenBounds().height * barRatio);
					}
					scrollValue += remoteY / range;
					correct();
				}
				holding = true;
				holdReference = input.getMousePos().y; // x because this is for a horizontal scroll bar
			}
		} else {
			heldValue = (input.getMousePos().y - holdReference) / range;
			if (scrollValue + heldValue < 0) {
				 heldValue = -scrollValue;
			} else if (scrollValue + heldValue > 1) {
				heldValue = 1 - scrollValue;
			}
			if (!input.isMouseDown(Mouse.BUTTON1)) {
				holding = false;
				scrollValue += heldValue;
				heldValue = 0;
			}
			onResize();
		}
		previousMouseDown = input.isMouseDown(Mouse.BUTTON1);
	}
	
	public void setScrollValue(final float newValue){
		holding = false;
		scrollValue = newValue;
		correct();
		onResize();
	}
	
	private void correct() {
		if (scrollValue < 0) {
			scrollValue = 0;
		} else if (scrollValue > 1) {
			scrollValue = 1;
		}
	}

	public float getScrollValue(){
		return scrollValue + heldValue;
	}
	
	public boolean isBeingHeld() {
		return holding;
	}
	
	@Override
	protected void onResize() {
		final float viewedValue = scrollValue + heldValue;
		onScrollChange.accept(viewedValue);
		bar.setBounds(new Boundary(new Rectf(0, viewedValue * (1 - barRatio), 1, barRatio), Scalar.STRETCHED, Alignment.TOP_CENTER));
	}
	
}
