package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.display.Window.Cursor;
import au.com.cascadesoftware.engine3.graphics.Canvas;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

public class GUIGrid extends GUI {

	private Vector2i lines;
	private Color lineColor;
	private Canvas canvas;
	private float lineWidth;
	
	private Scale landscapeNS, portraitNS;
	private boolean canInput;
	private boolean holding;
	private Vector2i holdingPoint;
	private Vector2d reference;
	
	private GUIOven oven;
	private GUI container;

	public GUIGrid(Window window, Boundary bounds, Vector2i lines, Color lineColor) {
		super(window, bounds);
		this.lines = lines;
		this.lineColor = lineColor;
		lineWidth = 1;
		canInput = true;
		Boundary containerBoundary = new Boundary();
		container = new GUI(window, containerBoundary){
			@Override
			public Recti getRenderBounds(){
				return containerBoundary.getRect(getGraphBounds());
			}
		};
		oven = new GUIOven(window, new Boundary());
		oven.addGUI(container);
		addGUI(oven);
		oven.setBakeOnResize(true);
	}

	protected Recti getGraphBounds() {
		double width = oven.getRenderBounds().width;
		double x = oven.getRenderBounds().x;
		double height = oven.getRenderBounds().height;
		double y = oven.getRenderBounds().y;
		if(landscapeNS != null){
			width /= landscapeNS.getRange();
			x -= (width - oven.getRenderBounds().width)/2;
			x -= (landscapeNS.getMidpoint() - 0.5)*width;
		}
		if(portraitNS != null){
			height /= portraitNS.getRange();
			y -= (height - oven.getRenderBounds().height)/2;
			y -= (portraitNS.getMidpoint() - 0.5)*height;
		}
		Recti out = new Recti((int) x, (int) y, (int) width, (int) height);
		return out;
	}

	private void redrawCanvas() {
		if(canvas == null) return;
		canvas.resize(getRenderBounds(), new Vector2i());
		Graphics graphics = canvas.getGraphics();
		drawMethod(graphics);
	}
	
	@Override
	protected void draw(Graphics graphics) {
		if(canvas == null){
			canvas = graphics.createCanvas(getRenderBounds(), new Vector2i(), false);
			redrawCanvas();
		}
		canvas.draw(graphics);
	}
	
	private void drawMethod(Graphics graphics) {
		Paint paint = new Paint();
		paint.setColor(lineColor);
		paint.setStrokeWidth(lineWidth);
		graphics.setPaint(paint);
		float width = getRenderBounds().width/(lines.x+1);
		float height = getRenderBounds().height/(lines.y+1);
		if(landscapeNS != null && landscapeNS.getSetLength() > 0){
			int[] linePositions = landscapeNS.getLines();
			for(int lineX : linePositions){
				if(lineX < 0 || lineX >= landscapeNS.getRenderBounds().width) continue;
				graphics.drawLine((int) (lineX), 0, (int) (lineX), getRenderBounds().height);
			}
		}else{
			for(int x = 1; x <= lines.x; x++){
				graphics.drawLine((int)(x*width), 0, (int)(x*width), getRenderBounds().height);
			}
		}
		if(portraitNS != null && portraitNS.getSetLength() > 0){
			int[] linePositions = portraitNS.getLines();
			for(int lineY : linePositions){
				if(lineY < 0 || lineY >= portraitNS.getRenderBounds().height) continue;
				graphics.drawLine(0, (int)lineY, getRenderBounds().width, (int)lineY);
			}
		}else{
			for(int y = 1; y <= lines.y; y++){
				graphics.drawLine(0, (int)(y*height), getRenderBounds().width, (int)(y*height));
			}
		}
	}

	@Override
	protected void onResize() {
		redrawCanvas();
	}

	public void setLineWidth(float width){
		lineWidth = width;
	}
	
	public void bindScale(Scale scale) {
		scale.addRescaleMethod(new Runnable(){
			@Override
			public void run() {
				GUIGrid.this.resize();
			}
		});
		if(scale.getOrientation() == Orientation.LANDSCAPE){
			landscapeNS = scale;
		}else{
			portraitNS = scale;
		}
		redrawCanvas();
	}
	
	public void addGUIToGraph(GUI gui) {
		container.addGUI(gui);
	}
	
	@Override
	protected void updateInput() {
		if((landscapeNS == null && portraitNS == null) || !canInput || doControlTest()) return;
		InputHandler ih = getWindow().getInput();
		boolean focus = getOnScreenBounds().contains(ih.getMousePos());
		if(ih.isMouseClicked(Mouse.BUTTON1)){
			if(focus){
				holding = true;
				holdingPoint = new Vector2i(ih.getMousePos().x, ih.getMousePos().y);
				reference = new Vector2d();
				if(landscapeNS != null) reference.x = landscapeNS.getMidpoint();
				if(portraitNS != null) reference.y = portraitNS.getMidpoint();
				
				//new Time( new Date(24, 06, 2000, 7, 29, 0), new Date(27, 07, 2000, 4, 30, 0)).display();
			}
		}else if(holding && !ih.isMouseDown(Mouse.BUTTON1)){
			holding = false;
			getWindow().setCursor(Cursor.DEFAULT);
		}else if(holding){
			float differenceX = (ih.getMousePos().x - holdingPoint.x)*1f/getOnScreenBounds().width;
			float differenceY = (ih.getMousePos().y - holdingPoint.y)*1f/getOnScreenBounds().height;
			//((ih.getMousePos().x : ih.getMousePos().y) - holdingPoint)*1f / (orientation == LANDSCAPE ? getBounds().width : getBounds().height);
			if(landscapeNS != null) landscapeNS.setShow(reference.x - differenceX*landscapeNS.getRange(), landscapeNS.getRange());
			if(portraitNS != null) portraitNS.setShow(reference.y - differenceY*portraitNS.getRange(), portraitNS.getRange());
			
		}
		if(holding){
			getWindow().setCursor(Cursor.HAND_GRABBING);
		}
	}
	
	public void setInputControl(boolean inputControl){
		this.canInput = inputControl;
	}
	
}
