package au.com.cascadesoftware.engine3.gui;

import static au.com.cascadesoftware.engine3.gui.GUINumberScale.InputType.MIDPOINT;
import static au.com.cascadesoftware.engine3.gui.GUINumberScale.InputType.NONE;
import static au.com.cascadesoftware.engine3.gui.GUINumberScale.InputType.RANGE;
import static au.com.cascadesoftware.engine3.gui.Orientation.LANDSCAPE;
import static au.com.cascadesoftware.engine3.gui.Orientation.PORTRAIT;

import java.util.ArrayList;
import java.util.List;

import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.chart.NumberScale;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.display.Window.Cursor;
import au.com.cascadesoftware.engine3.graphics.Canvas;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;
import au.com.cascadesoftware.engine3.util.RoundingAlgorithm;

public class GUINumberScale extends GUI implements Scale {

	private NumberScale numberScale;
	private float textHeight;
	private double midpoint, range;
	private Orientation orientation;
	private String[] set;
	private Color color;
	private RoundingAlgorithm roundingAlgorithm;
	private Alignment superAlignment;
	private List<Runnable> rescaleMethods;

	private boolean scaleFromCenter;
	private InputType inputType;
	private boolean holding;
	private boolean focusLast;
	private int holdingPoint;
	private Vector2d reference;
	private boolean restrictRange;
	
	public GUINumberScale(Window window, Boundary bounds, NumberScale numberScale, Orientation orientation, Color color) {
		super(window, bounds);
		rescaleMethods = new ArrayList<Runnable>();
		this.color = color;
		this.numberScale = numberScale;
		textHeight = 24f;
		this.orientation = orientation;
		restrictRange = true;
		roundingAlgorithm = RoundingAlgorithm.INCREMENTAL_TENTHS_AND_FIFTHS;
		superAlignment = Alignment.TOP_LEFT;
		inputType = InputType.NONE;
		setShow(0.5f, 1f);
	}
	
	private void gUpdate() {
		setShow(midpoint, range);
	}
	
	@Override
	protected void draw(Graphics graphics) {
		//Vector2i winSize = getWindow().getSize();
		Canvas canvas = graphics.createCanvas(getRenderBounds(), new Vector2i(), true);
		Graphics g = canvas.getGraphics();
		if(orientation == LANDSCAPE) drawLandscape(g);
		else drawPortrait(g);
		canvas.draw(graphics);
		
	}
	
	@Override
	public int[] getLines() {
		if(set == null) return new int[]{};
		double range = this.range*(0.5f/midpoint);
		
		double p1 = midpoint - this.range/2f;
		double p2 = midpoint + this.range/2f;
		int n = set.length;
		int start = (int) (p1*n) - OVERFLOW;
		int end = (int) (p2*n) + OVERFLOW;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return new int[]{};

		int[] out = new int[end-start];
		if(orientation == PORTRAIT){
			double modY = (getRenderBounds().height*(0.5f - (0.5f/range)));
			double k = (1f/range)/(set.length - 1 - ((set.length - 1)*(1f - midpoint*2f)));
			for(int i = start, j = 0; i < end; i++, j++){
				out[j] = (int) (0.5f + getRenderBounds().height*i*k + modY);
			}
			return out;
		}
		
		double modX = (getRenderBounds().width*(0.5f - (0.5f/range)));
		double k = (1f/range)/(set.length - 1 - ((set.length - 1)*(1f - midpoint*2f)));
		for(int i = start, j = 0; i < end; i++, j++){
			out[j] = (int) (0.001f + getRenderBounds().width*i*k + modX);
		}
		return out;
	}
	
	private void drawPortrait(Graphics graphics) {
		if(set == null) return;
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStrokeWidth(1);
		paint.setFontSize(textHeight);
		graphics.setPaint(paint);
		
		int lineX1 = (int) (getRenderBounds().width*0.1f);
		int lineX2 = 0;
		if(superAlignment.x == 2){
			lineX1 = (int) (getRenderBounds().width*0.9f);
			lineX2 = getRenderBounds().width - 1;
		}
		int strX = lineX1;
		
		double p1 = midpoint - this.range/2f;
		double p2 = midpoint + this.range/2f;
		
		int n = set.length;
		int start = (int) (p1*n) - OVERFLOW;
		int end = (int) (p2*n) + OVERFLOW;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;
		
		int[] linePositions = getLines();
		
		for(int i = start, j = 0; i < end; i++, j++){
			String str = set[set.length - i - 1];
			if(str == null) continue;
			int lineY = linePositions[j];
			int strY = lineY + (int) (textHeight*graphics.getTextHeightModifier()*2);
			if(strY < 0 || strY > getRenderBounds().height + textHeight - textHeight*graphics.getTextHeightModifier()) continue;
			int xMod = (int) (graphics.measureText(str).x*(superAlignment.x == 2 ? 1 : 0) + getRenderBounds().width*0.1f*(superAlignment.x == 2 ? 0.5f : -0.5f));
			graphics.drawString(str, strX - xMod, strY);
			if(lineY < 0 || lineY >= getRenderBounds().height) continue;
			graphics.drawLine(lineX1, lineY, lineX2, lineY);
		}
	}

	private void drawLandscape(Graphics graphics) {
		if(set == null) return;
		Paint paint = new Paint();
		paint.setColor(color);
		paint.setStrokeWidth(1);
		paint.setFontSize(textHeight);
		graphics.setPaint(paint);
		
		int lineY1 = (int) (0 + getRenderBounds().height * 0.1f);
		int lineY2 = 0;
		if(superAlignment.y == 2){
			lineY1 = (int) (0 + getRenderBounds().height * 0.9f);
			lineY2 = getRenderBounds().height - 1;
		}
		int strY = lineY1;
		
		double p1 = midpoint - this.range/2f;
		double p2 = midpoint + this.range/2f;
		
		int n = set.length;
		int start = (int) (p1*n) - OVERFLOW;
		int end = (int) (p2*n) + OVERFLOW;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;

		int[] linePositions = getLines();
		
		for(int i = start, j = 0; i < end; i++, j++){
			String str = set[i];
			if(str == null) continue;
			int lineX = linePositions[j];
			double strWidth = graphics.measureText(set[i]).x;
			int strX = lineX - (int) (strWidth/2f + 1);
			if(strX < -strWidth || strX > getRenderBounds().width) continue;
			graphics.drawString(set[i], strX, (int) (strY + textHeight*(superAlignment.y == 2 ? -0.5f :  (2 + graphics.getTextHeightModifier()*2)/2f)));
			if(lineX < 0 || lineX >= getRenderBounds().width) continue;
			graphics.drawLine(lineX, lineY1, lineX, lineY2);
		}
	}
	
	@Override
	public Orientation getOrientation(){
		return this.orientation;
	}

	@Override
	public int getSetLength(){
		if(set == null) return 0;
		return set.length;
	}
	
	@Override
	protected void updateInput() {
		if(inputType == NONE || doControlTest()) return;
		InputHandler ih = getWindow().getInput();
		boolean focus = getOnScreenBounds().contains(ih.getMousePos());
		if(ih.isMouseClicked(Mouse.BUTTON1)){
			if(focus){
				holding = true;
				holdingPoint = orientation == LANDSCAPE ? ih.getMousePos().x : ih.getMousePos().y;
				reference = new Vector2d(midpoint, range);
			}
		}else if(holding && !ih.isMouseDown(Mouse.BUTTON1)){
			holding = false;
			getWindow().setCursor(Cursor.DEFAULT);
			reference = new Vector2d();
			holdingPoint = 0;
		}else if(holding){
			float difference = ((orientation == LANDSCAPE ? ih.getMousePos().x : ih.getMousePos().y) - holdingPoint)*1f / (orientation == LANDSCAPE ? getOnScreenBounds().width : getOnScreenBounds().height);
			if(inputType == RANGE){
				float scale = 2f;
				range = (float) (reference.y*Math.exp(difference*scale));
				float holdingFloat = (holdingPoint - getOnScreenBounds().x)*1f/getOnScreenBounds().width;
				if(orientation == PORTRAIT){
					holdingFloat = (holdingPoint - getOnScreenBounds().y)*1f/getOnScreenBounds().height;
				}
				if(!scaleFromCenter) midpoint = reference.x + (0.5 + (holdingFloat - 0.5)*reference.y/range - holdingFloat)*range;
			}else{
				midpoint = (reference.x - difference*range);
			}
			gUpdate();
		}
		if(inputType == MIDPOINT){
			if(holding){
				getWindow().setCursor(Cursor.HAND_GRABBING);
			}else if(focus){
				getWindow().setCursor(Cursor.HAND_OPEN);
			}else if(focusLast){
				getWindow().setCursor(Cursor.DEFAULT);
			}
		}
		if(inputType == RANGE){
			Cursor c = orientation == PORTRAIT ? Cursor.DOUBLE_ARROW_VERTICAL : Cursor.DOUBLE_ARROW_HORIZONTAL;
			if(holding){
				getWindow().setCursor(c);
			}else if(focus){
				getWindow().setCursor(c);
			}else if(focusLast){
				getWindow().setCursor(Cursor.DEFAULT);
			}
		}
		focusLast = focus;
	}

	public void setTextHeight(float textHeight){
		this.textHeight = textHeight;
		gUpdate();
	}

	public void setRangeRestricted(boolean r){
		this.restrictRange = r;
	}
	
	public void setScaleFromCenter(boolean value){
		scaleFromCenter = value;
	}

	@Override
	public void setShow(double midpoint, double range){
		if(restrictRange){
			if(range > 1) range = 1;
			if(midpoint < range/2f) midpoint = range/2f;
			if(midpoint > 1 - range/2f) midpoint = 1 - range/2f;
		}
		
		int a = 2;
		int b = 6;
		float stop = numberScale.getIncrement()/numberScale.getRange()*((getRenderBounds().height)/textHeight/(a * 1.5f));
		if(orientation == LANDSCAPE) stop = numberScale.getIncrement()/numberScale.getRange()*((getRenderBounds().width)/textHeight/(b * 1.5f));
		if(range < stop) range = stop;
		
		int drawHeight = (int) (getRenderBounds().height/range);
		int drawWidth = (int) (getRenderBounds().width/range);
		int n = (int) (drawHeight / (textHeight*a));
		if(orientation == LANDSCAPE) n = (int) (drawWidth / (textHeight*b));
		
		boolean roundFit = true;
		if(roundFit){
			n = roundingAlgorithm.getN(numberScale, n);
		}
		if(n<2) return;
		
		this.midpoint = midpoint;
		this.range = range;
		float[] floatSet = numberScale.getSetByAmount(n, orientation == PORTRAIT ? 1 - midpoint : midpoint, range);
		
		double p1 = midpoint - range/2f;
		double p2 = midpoint + range/2f;
		if(orientation == PORTRAIT){
			p1 = 1 - midpoint - range/2f;
			p2 = 1 - midpoint + range/2f;
		}
		int start = (int) (p1*n) - OVERFLOW;
		int end = (int) (p2*n) + OVERFLOW;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;
		set = new String[n];
		for(int i = start; i < end; i++){
			String e = floatSet[i] + "";
			int decimalOffset = -numberScale.getType().decimalPosition;
			if(decimalOffset <= 0){
				set[i] = e.split("\\.")[0];
			}else{
				int l = 0;
				if(e.split("\\.").length > 1){ l = e.split("\\.")[1].length();
				}
				for(int j = 0; j < decimalOffset - l; j++) e += "0";
				set[i] = e;
			}
		}
		for(Runnable r : rescaleMethods){
			if(r != null) r.run();
		}
	}
	
	@Override
	protected void onResize() {
		gUpdate();
	}

	@Override
	public double getMidpoint(){
		return midpoint;
	}

	@Override
	public double getRange(){
		return range;
	}
	
	public GUINumberScale setRoundingAlgorithm(RoundingAlgorithm ar){
		this.roundingAlgorithm = ar;
		return this;
	}
	
	public GUINumberScale setAlignment(Alignment alignment){
		this.superAlignment = alignment;
		return this;
	}
	
	public GUINumberScale setInputType(InputType inputType){
		this.inputType = inputType;
		return this;
	}

	@Override
	public void addRescaleMethod(Runnable runnable) {
		rescaleMethods.add(runnable);
	}
	
	private static final int OVERFLOW = 10;
	
	
	
	public enum InputType {
		NONE, RANGE, MIDPOINT
	}

}
