package au.com.cascadesoftware.engine3.gui;

import static au.com.cascadesoftware.engine3.gui.Orientation.LANDSCAPE;
import static au.com.cascadesoftware.engine3.gui.Orientation.PORTRAIT;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

@ExcludeFromWeb
public class GUIScrollBar extends GUI {

	private final int minLength;
	private final Orientation orientation;
	private int barLength;
	private float scroll;
	private GUIOven barContainer;
	private GUIShapeRoundedRectangle bar;

	private boolean holding, noArrows;
	private int holdingPoint;
	private float reference;
	private int contentLength;
	private boolean canJump = true;
	
	public GUIScrollBar(Window window, Boundary bounds, Orientation orientation, Color barColor) {
		this(window, bounds, orientation, barColor, 20);
	}
	
	public GUIScrollBar(Window window, Boundary bounds, Orientation orientation, Color barColor, int minLength) {
		this(window, bounds, orientation, barColor, minLength, defaultArrows(window, orientation));
	}

	public GUIScrollBar(Window window, Boundary bounds, Orientation orientation, Color barColor, int minLength, GUI[] arrows) {
		super(window, bounds);
		if(arrows == null){
			arrows = new GUI[]{};
		}
		if(arrows.length == 0) noArrows = true;
		this.minLength = minLength;
		this.orientation = orientation;
		barLength = minLength;
		contentLength = 1000;
		//do arrow buttons
		
		barContainer = (GUIOven) new GUIOven(window, new Boundary());
		bar = new GUIShapeRoundedRectangle(window, new Boundary(), barColor, 0);
		barContainer.addGUI(bar);
		barContainer.setBakeOnResize(true);
		addGUI(barContainer);
		for(GUI g : arrows){
			addGUI(g);
		}
		if(orientation == Orientation.PORTRAIT){
			GUI up = new GUIButton(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.HORIZONTAL, Alignment.TOP_CENTER)){
				@Override
				public void onClick() {
					bump(-1);
				}
			};
			GUI down = new GUIButton(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.HORIZONTAL, Alignment.BOTTOM_CENTER)){
				@Override
				public void onClick() {
					bump(1);
				}
			};
			addGUI(up);
			addGUI(down);
		}else{
			GUI left = new GUIButton(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.VERTICAL, Alignment.BOTTOM_LEFT)){
				@Override
				public void onClick() {
					bump(-1);
				}
			};
			GUI right = new GUIButton(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.VERTICAL, Alignment.BOTTOM_RIGHT)){
				@Override
				public void onClick() {
					bump(1);
				}
			};
			addGUI(left);
			addGUI(right);
		}
	}

	private void bump(int direction) {
		scroll += 20f/contentLength*direction;
		if(scroll < 0) scroll = 0;
		if(scroll > 1) scroll = 1;
		resize();
	}

	@Override
	protected void onResize() {
		this.setBarLength(contentLength);
		if(orientation == Orientation.PORTRAIT){
			int arrowButtonHeight = getRenderBounds().width;
			if(noArrows) arrowButtonHeight = 0;
			int b = getRenderBounds().height - arrowButtonHeight*2;
			float y = arrowButtonHeight + scroll*(b - barLength);
			barContainer.setBounds(new Boundary(new Rectf(0, y*1f/getRenderBounds().height, 1, barLength*1f/getRenderBounds().height), Scalar.STRETCHED, Alignment.TOP_LEFT));
		}else{
			int arrowButtonWidth = getRenderBounds().height;
			if(noArrows) arrowButtonWidth = 0;
			int b = getRenderBounds().width - arrowButtonWidth*2;
			float x = arrowButtonWidth + scroll*(b - barLength);
			//System.out.println(x);
			barContainer.setBounds(new Boundary(new Rectf(x*1f/getRenderBounds().width, 0, barLength*1f/getRenderBounds().width, 1), Scalar.STRETCHED, Alignment.TOP_LEFT));
		}
		barContainer.resize();
	}
	
	@Override
	protected void updateInputOverride() {
		if(doControlTest()) return;
		InputHandler ih = getWindow().getInput();
		boolean focus = barContainer.getOnScreenBounds().contains(ih.getMousePos());
		if(ih.isMouseClicked(Mouse.BUTTON1)){
			if(focus){
				holding = true;
				holdingPoint = orientation == LANDSCAPE ? ih.getMousePos().x : ih.getMousePos().y;
				reference = scroll;
			}else if(getOnScreenBounds().contains(ih.getMousePos()) && canJump ){
				int clickPoint = orientation == LANDSCAPE ? ih.getMousePos().x : ih.getMousePos().y;
				if(orientation == PORTRAIT){
					int arrowButtonHeight = getRenderBounds().width;
					if(noArrows) arrowButtonHeight = 0;
					int b = getRenderBounds().height - arrowButtonHeight*2;
					if(clickPoint > getRenderBounds().y + arrowButtonHeight && clickPoint < getRenderBounds().y + getRenderBounds().height - arrowButtonHeight) scroll = (clickPoint - getRenderBounds().y - arrowButtonHeight - barLength/2f)/(b - barLength);
				}else{
					int arrowButtonWidth = getRenderBounds().height;
					if(noArrows) arrowButtonWidth = 0;
					int b = getRenderBounds().width - arrowButtonWidth*2;
					if(clickPoint > getRenderBounds().x + arrowButtonWidth && clickPoint < getRenderBounds().x + getRenderBounds().width - arrowButtonWidth) scroll = (clickPoint - getRenderBounds().x - arrowButtonWidth - barLength/2f)/(b - barLength);
				}
				if(scroll < 0) scroll = 0;
				if(scroll > 1) scroll = 1;
				resize();
			}
		}else if(holding && !ih.isMouseDown(Mouse.BUTTON1)){
			holding = false;
			reference = 0;
			holdingPoint = 0;
		}else if(holding){
			float difference = (orientation == LANDSCAPE ? ih.getMousePos().x : ih.getMousePos().y) - holdingPoint;
			int b = getRenderBounds().height - getRenderBounds().width*2;
			if(orientation == Orientation.LANDSCAPE) b = getRenderBounds().width - getRenderBounds().height*2;
			scroll = reference + difference/(b - barLength);
			if(scroll < 0) scroll = 0;
			if(scroll > 1) scroll = 1;
			onScrollChanged(scroll);
			resize();
		}
	}
	
	protected void onScrollChanged(float scroll){}
	
	public void setRoundedCorners(int r){
		bar.setRoundedCorners(r);
	}

	public void setBarLength(int contentLength){
		this.contentLength = contentLength;
		if(orientation == Orientation.PORTRAIT){
			int arrowButtonHeight = getRenderBounds().width;
			if(noArrows) arrowButtonHeight = 0;
			int a = getRenderBounds().height;
			int b = getRenderBounds().height - arrowButtonHeight*2;
			barLength = a*a/contentLength - arrowButtonHeight*2;
			if(barLength > b) barLength = b;
		}else{
			int arrowButtonWidth = getRenderBounds().height;
			if(noArrows) arrowButtonWidth = 0;
			int a = getRenderBounds().width;
			int b = getRenderBounds().width - arrowButtonWidth*2;
			barLength = a*a/contentLength - arrowButtonWidth*2;
			if(barLength > b) barLength = b;
		}
		if(barLength < minLength) barLength = minLength;
	}
	
	public void addGUIToBar(GUI gui){
		barContainer.addGUI(gui);
	}
	
	public void setScrollValue(float scroll){
		this.scroll = scroll;
	}
	
	public void setCanJump(boolean b){
		this.canJump = b;
	}
	
	public boolean isHolding() {
		return holding;
	}
	
	public float getScrollValue(){
		return scroll;
	}
	public GUIShapeRoundedRectangle getBar() {
		return bar;
	}
	
	private static GUI[] defaultArrows(Window window, Orientation orientation) {
		GUI up = new GUIImage(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.HORIZONTAL, Alignment.TOP_CENTER), window.getNewResourceLoader().change("ui/scrollbar_arrow.png"));
		GUI down = new GUIImage(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.HORIZONTAL, Alignment.BOTTOM_CENTER), window.getNewResourceLoader().change("ui/scrollbar_arrow.png")).setRotation(Math.PI);
		if(orientation == Orientation.LANDSCAPE){
			up = new GUIImage(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.VERTICAL, Alignment.BOTTOM_LEFT), window.getNewResourceLoader().change("ui/scrollbar_arrow.png")).setRotation(3*Math.PI/2);
			down = new GUIImage(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.VERTICAL, Alignment.BOTTOM_RIGHT), window.getNewResourceLoader().change("ui/scrollbar_arrow.png")).setRotation(Math.PI/2);
		}
		return new GUI[]{up, down};
	}
	
	
	@ExcludeFromWeb
	public static class GUIScrollBarFancy extends GUI {

		private final Color barColor;
		private final Orientation orientation;
		
		private GUIScrollBar scrollBar;
		private GUI[] arrowContainers;
		private boolean holdingLast;
		private GUIHover scrollBarHover;
		private boolean show;

		public GUIScrollBarFancy(Window window, Boundary bounds, Orientation orientation, Color barColor) {
			super(window, bounds);
			this.orientation = orientation;
			show = true;
			GUI[] arrows = defaultArrows(window, orientation);
			GUI[] arrowContainers = new GUI[arrows.length];
			this.barColor = barColor;
			for(int i = 0; i < arrows.length; i++){
				GUIHover g = (GUIHover) new GUIHover(window, arrows[i].getBoundary()){
					@Override
					protected void onStateChange(float phase) {
						setOpacity(phase);
					}
				}.setStatePhaseTime(0.1f);
				g.addGUI(arrows[i]);
				arrowContainers[i] = g;
			}
			this.arrowContainers = arrowContainers;
			GUIScrollBar scrollBar = new GUIScrollBar(window, new Boundary(), orientation, barColor, 40, arrowContainers){
				protected void onScrollChanged(float scroll){
					GUIScrollBarFancy.this.onScrollChanged(scroll);
				}
			};
			scrollBarHover = (GUIHover) new GUIHover(window, new Boundary()){
				@Override
				protected void onStateChange(float phase) {
					setOpacity(scrollBar.holding ? 0 : phase);
					scrollBar.resize();
				}
			}.setStatePhaseTime(0.1f).setBackground(barColor.darker());
			GUIImage grooves = new GUIImage(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), window.getNewResourceLoader().change("ui/scrollbar_fancy_grooves.png"));
			if(orientation == Orientation.LANDSCAPE){
				grooves = (GUIImage) new GUIImage(window, new Boundary(new Rectf(0, 0, 1, 1), Scalar.VERTICAL, Alignment.MIDDLE_CENTER), window.getNewResourceLoader().change("ui/scrollbar_fancy_grooves.png")).setRotation(Math.PI/2);
			}
			scrollBar.addGUIToBar(scrollBarHover);
			scrollBar.addGUIToBar(grooves);
			this.scrollBar = scrollBar;
			addGUI(scrollBar);
		}

		protected void onScrollChanged(float scroll){}

		public void setBarLength(int i) {
			scrollBar.setBarLength(i);
		}
		
		@Override
		protected void updateInput() {
			if(!show) return;
			if(scrollBar.holding){
				scrollBar.barContainer.setBackground(barColor.darker().darker());
			}else{
				scrollBar.barContainer.setBackground(barColor);
			}
			if(holdingLast != scrollBar.holding){
				scrollBar.resize();
				scrollBarHover.pushUpdate();
				if(holdingLast) scrollBarHover.setPhase(1);
			}
			holdingLast = scrollBar.holding;
		}
		
		@Override
		public GUI setBackground(Color bgColor) {
			Color hoverColor = bgColor.getMiddleColor(barColor);
			for(GUI g : arrowContainers){
				g.setBackground(hoverColor);
			}
			return super.setBackground(bgColor);
		}
		
		public void setScrollValue(float scroll){
			scrollBar.setScrollValue(scroll);
		}
		
		public float getScrollValue(){
			return scrollBar.getScrollValue();
		}

		@Override
		protected void onResize() {
			float width = getBoundary().coordinates.width * 20 / getRenderBounds().width;
			float height = getBoundary().coordinates.height;
			float x = getBoundary().coordinates.x;
			float y = getBoundary().coordinates.y;
			if(orientation == Orientation.LANDSCAPE){
				width = getBoundary().coordinates.width;
				height = getBoundary().coordinates.height * 20 / getRenderBounds().height;
			}
			Rectf b = new Rectf(x, y, width, height);
			Boundary bounds = new Boundary(b, getBoundary().scalar, getBoundary().alignment);
			setBounds(bounds);
		}

		public void setShow(boolean b) {
			show = b;
			setOpacity(show ? 1f : 0f);
		}
		
	}

}
