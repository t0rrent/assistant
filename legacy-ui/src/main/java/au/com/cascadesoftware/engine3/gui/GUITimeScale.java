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
import au.com.cascadesoftware.engine3.chart.Date;
import au.com.cascadesoftware.engine3.chart.Day;
import au.com.cascadesoftware.engine3.chart.Month;
import au.com.cascadesoftware.engine3.chart.NumberScale;
import au.com.cascadesoftware.engine3.chart.NumberScale.Type;
import au.com.cascadesoftware.engine3.chart.TimeScale;
import au.com.cascadesoftware.engine3.chart.TimeScale.Interval;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.display.Window.Cursor;
import au.com.cascadesoftware.engine3.graphics.Canvas;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.gui.GUINumberScale.InputType;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;
import au.com.cascadesoftware.engine3.util.RoundingAlgorithm;

public class GUITimeScale extends GUI implements Scale{

	private TimeScale timeScale;
	private float textHeight;
	private double midpoint, range;
	private Orientation orientation;
	private Date[] set;
	private int[] lines;
	private Color color;
	private TimeScaleRoundingAlgorithm roundingAlgorithm;
	private Alignment superAlignment;
	private InputType inputType;

	private boolean scaleFromCenter;
	private boolean holding;
	private boolean focusLast;
	private int holdingPoint;
	private Vector2d reference;
	private List<Runnable> rescaleMethods;
	
	public GUITimeScale(Window window, Boundary bounds, TimeScale timeScale, Orientation orientation, Color color) {
		super(window, bounds);
		rescaleMethods = new ArrayList<Runnable>();
		this.color = color;
		this.timeScale = timeScale;
		textHeight = 24f;
		this.orientation = orientation;
		roundingAlgorithm = TimeScaleRoundingAlgorithm.ALGORITHM;
		superAlignment = Alignment.TOP_LEFT;
		inputType = InputType.NONE;
		setShow(0.5f, 1f);
	}
	
	private void gUpdate() {
		setShow(getMidpoint(), range);
		for(Runnable r : rescaleMethods){
			if(r != null) r.run();
		}
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
		if(lines == null) return new int[]{};
		return lines;
	}
	
	private void setLines() {
		if(set == null) return;

		double p1 = midpoint - this.range/2f;
		double p2 = midpoint + this.range/2f;
		int n = set.length;
		int start = (int) (p1*n) - 2;
		int end = (int) (p2*n) + 2;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;
		

		int[] out = new int[end - start];
		if(orientation == PORTRAIT){
			for(int i = start, j = 0; i < end; i++, j++){
				if(set[i] == null){
					out[j] = -1;
					continue;
				}
				double positionInTimeScale = (timeScale.getPosition(set[i]) - midpoint)/range + 0.5;
				out[j] = (int) (getRenderBounds().height*(1 - positionInTimeScale));
			}
		}else{
			for(int i = start, j = 0; i < end; i++, j++){
				if(set[i] == null){
					out[j] = -1;
					continue;
				}
				double positionInTimeScale = (timeScale.getPosition(set[i]) - midpoint)/range + 0.5;
				out[j] = (int) (getRenderBounds().width*(positionInTimeScale));
			}
		}
		
		lines = out;
		
		/* old:
		double range = this.range*(0.5f/midpoint);
		
		double p1 = midpoint - this.range/2f;
		double p2 = midpoint + this.range/2f;
		
		int n = set.length;
		int start = (int) (p1*n) - 2;
		int end = (int) (p2*n) + 2;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return new int[]{};

		int[] out = new int[end - start];
		if(orientation == PORTRAIT){
			double modY = (getBounds().height*(0.5f - (0.5f/range)));
			double k = (1f/range)/(set.length - 1 - ((set.length - 1)*(1f - midpoint*2f)));
			for(int i = start, j = 0; i < end; i++, j++){
				out[j] = (int) (0.5f + getBounds().height*i*k + modY);
			}
			return out;
		}

		double modX = (getBounds().width*(0.5f - (0.5f/range)));
		double k = (1f/range)/(set.length - 1 - ((set.length - 1)*(1f - midpoint*2f)));
		for(int i = start, j = 0; i < end; i++, j++){
			out[j] = (int) (0.001f + getBounds().width*i*k + modX);
		}
		return out;
		*/
	}
	
	private void drawPortrait(Graphics graphics) {
		if(set == null || lines == null) return;
		Paint paint = new Paint();
		paint.setColor(color);
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
		int start = (int) (p1*n) - 2;
		int end = (int) (p2*n) + 2;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;
		
		for(int i = start, j = 0; i < end; i++, j++){
			String str = getDisplayString(set[i]);
			if(str == null) continue;
			float strWidth = graphics.measureText(str).x;
			int lineY = lines[j];
			if(j < lines.length - 1 && lineY < lines[j + 1] + textHeight * 1) continue;
			int strY = lineY + (int) (textHeight*graphics.getTextHeightModifier()*2);
			if(strY < 0 || strY > getRenderBounds().height + textHeight - textHeight*graphics.getTextHeightModifier()) continue;
			int xMod = (int) (strWidth*(superAlignment.x == 2 ? 1 : 0) + getRenderBounds().width*0.1f*(superAlignment.x == 2 ? 0.5f : -0.5f));
			graphics.drawString(str, strX - xMod, strY);
			if(lineY < 0 || lineY >= getRenderBounds().height) continue;
			graphics.drawLine(lineX1, lineY, lineX2, lineY);
		}
	}

	
	
	private void drawLandscape(Graphics graphics) {
		if(set == null || lines == null) return;
		Paint paint = new Paint();
		paint.setColor(color);
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
		int start = (int) (p1*n) - 2;
		int end = (int) (p2*n) + 2;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;
		
		for(int i = start, j = 0; i < end; i++, j++){
			String str = getDisplayString(set[i]);
			if(str == null) continue;
			int lineX = lines[j];
			float strWidth = graphics.measureText(str).x;
			if(j < lines.length - 1 && lineX > lines[j + 1] - strWidth * 1.5f) continue;
			int strX = lineX - (int) (strWidth/2f + 1);
			if(strX < -strWidth || strX > getRenderBounds().width) continue;
			graphics.drawString(str, strX, (int) (strY + textHeight*(superAlignment.y == 2 ? -0.5f :  (2 + graphics.getTextHeightModifier()*2)/2f)));
			if(lineX < 0 || lineX >= getRenderBounds().width) continue;
			graphics.drawLine(lineX, lineY1, lineX, lineY2);
		}
	}
	
	private String getDisplayString(Date time) {
		if(time == null) return "null";
		//System.out.println(time.hour);
		int i = time.second;
		Interval displayType = Interval.SECOND;
		if(i == 0) displayType = Interval.MINUTE;
		if(displayType == Interval.MINUTE){
			i = time.minute;
			displayType = Interval.MINUTE;
			if(i == 0) displayType = Interval.HOUR;
		}
		if(displayType == Interval.HOUR){
			i = time.hour;
			displayType = Interval.HOUR;
			if(i == 0) displayType = Interval.DAY;
		}
		if(displayType == Interval.DAY){
			i = time.day;
			displayType = Interval.DAY;
			if(i == 1) displayType = Interval.MONTH;
		}
		if(displayType == Interval.MONTH){
			i = time.month;
			displayType = Interval.MONTH;
			if(i == 1) displayType = Interval.YEAR;
		}
		if(displayType == Interval.YEAR){
			i = time.year;
			displayType = Interval.YEAR;
		}
		String out = "" + i;
		if(displayType == Interval.MINUTE){
			String hr = time.hour + "";
			if(time.hour < 10) hr = "0" + hr;
			if(i < 10) out = "0" + out;
			out = hr + ":" + out;
		}else if(displayType == Interval.HOUR){
			if(i < 10) out = "0" + out;
			out = out + ":00";
		}else if(displayType == Interval.MONTH){
			out = Month.values()[i-1].shortDisplay;
		}
		return out;
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
				float scale = 3f;
				range = (float) (reference.y*Math.exp(difference*scale));
				float holdingFloat = (holdingPoint - getOnScreenBounds().x)*1f/getOnScreenBounds().width;
				if(orientation == PORTRAIT) holdingFloat = 1 - (holdingPoint - getOnScreenBounds().y)*1f/getOnScreenBounds().height;
				if(!scaleFromCenter) midpoint = reference.x + (0.5 + (holdingFloat - 0.5)*reference.y/range - holdingFloat)*range;
			}else{
				midpoint = (reference.x - difference*range);
				if(orientation == PORTRAIT) midpoint = (reference.x + difference*range);
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
	
	public void setScaleFromCenter(boolean value){
		scaleFromCenter = value;
	}

	@Override
	public void setShow(double midpoint, double range){
		if(orientation == PORTRAIT) midpoint = 1 - midpoint;

		if(range > 1) range = 1;
		if(midpoint < range/2f) midpoint = range/2f;
		if(midpoint > 1 - range/2f) midpoint = 1 - range/2f;
		//float stop = numberScale.getIncrement()/numberScale.getRange()*10f;
		//if(range < stop) range = stop;
		
		int drawHeight = (int) (getRenderBounds().height/range);
		int drawWidth = (int) (getRenderBounds().width/range);
		int a = 2;
		int b = 4;
		int n = (int) (drawHeight / (textHeight*a));
		if(orientation == LANDSCAPE) n = (int) (drawWidth / (textHeight*b));
	
		IntervalData intervalData = roundingAlgorithm.getN(timeScale, n);
		n = intervalData.n;
		
		if(n<2) return;
		
		
		this.midpoint = midpoint;
		this.range = range;
		
		double p1 = midpoint - range/2f;
		double p2 = midpoint + range/2f;
		if(orientation == PORTRAIT){
			//p1 = 1 - midpoint - range/2f;
			//p2 = 1 - midpoint + range/2f;
		}
		int start = (int) (p1*n) - 10;
		int end = (int) (p2*n) + 10;
		if(start < 0) start = 0;
		if(end > n) end = n;
		if(end < 0 || start >= n) return;

		set = new Date[n];
		
		for(int j = start, i = start; j < end; j++, i++){
			//System.out.println(i);
			//if(orientation == PORTRAIT) i = n - j - 1;
			Date date = timeScale.getMin().addYear((int) (i/intervalData.subInterval));
			if(intervalData.subInterval >= 1){
				if(intervalData.interval == Interval.YEAR) date = timeScale.getMin().addMonth((int) (j*12/intervalData.subInterval));
				if(intervalData.interval == Interval.MONTH) date = timeScale.getMin().addMonth(j);
				if(intervalData.interval == Interval.DAY) date = timeScale.getMin().addHour((int) (j*24/intervalData.subInterval));
				if(intervalData.interval == Interval.HOUR) date = timeScale.getMin().addMinute((int) (j*60/intervalData.subInterval));
				if(intervalData.interval == Interval.MINUTE) date = timeScale.getMin().addSecond((int) (j*60/intervalData.subInterval));
				if(intervalData.interval == Interval.SECOND) date = timeScale.getMin().addSecond(j);
			}
			if(intervalData.interval == Interval.WEEK){
				date = timeScale.getMin().startOfDay();
				if(j == 0){
					date = timeScale.getMin();
				}else if(j == n - 1){
					date = timeScale.getMax();
				}else{
					while(date.getDayOfWeek() != Day.MONDAY) date = date.nextDay();
					date = date.addDay((int) ((j-1)*7*intervalData.subInterval));
					if(intervalData.subInterval == 1 && date.day < 8 || intervalData.subInterval == 2 && date.day < 15){
						date = date.startOfMonth();
					}
				}
			}
			set[i] = date;
		}
		
		setLines();
	}
	
	@Override
	protected void onResize() {
		gUpdate();
	}

	@Override
	public double getMidpoint(){
		if(orientation == PORTRAIT) return 1 - midpoint;
		return midpoint;
	}

	@Override
	public double getRange(){
		return range;
	}
	
	public GUITimeScale setAlignment(Alignment alignment){
		this.superAlignment = alignment;
		return this;
	}
	
	public GUITimeScale setInputType(InputType inputType){
		this.inputType = inputType;
		return this;
	}

	@Override
	public void addRescaleMethod(Runnable runnable) {
		rescaleMethods.add(runnable);
	}
	
	
	
	private abstract static class TimeScaleRoundingAlgorithm {

		public abstract IntervalData getN(TimeScale ns, int n);
		
		private static final TimeScaleRoundingAlgorithm ALGORITHM = new TimeScaleRoundingAlgorithm(){
			
			@Override
			public IntervalData getN(TimeScale ns, int n) {
				int years = ns.getMax().year - ns.getMin().year;
				int months = years*12 + ns.getMax().month - ns.getMin().month;
				int days = ns.getRange().days + ns.getMax().day - ns.getMin().day;				
				int hours = days*24 + ns.getMax().hour - ns.getMin().hour;
				int minutes = days*24*60 + ns.getMax().minute - ns.getMin().minute;
				int seconds = days*24*60*60 + ns.getMax().second - ns.getMin().second;
				if(n < years){
					int roundN = RoundingAlgorithm.INCREMENTAL_TENTHS_AND_FIFTHS.getN(new NumberScale(0, years, Type.INTEGER), n);
					if(roundN > n){
						roundN = n;
					}
					return new IntervalData(roundN, Interval.YEAR, (roundN - 1f)/years);
				}
				Interval interval = Interval.YEAR;
				int subInterval = 1;
				Interval constrainingInterval = ns.getInterval();
				if(days > 10 && constrainingInterval == Interval.SECOND) constrainingInterval = Interval.MINUTE;
				if(months > 6 && constrainingInterval == Interval.MINUTE) constrainingInterval = Interval.HOUR;
				if(years > 12 && constrainingInterval == Interval.HOUR) constrainingInterval = Interval.DAY;
				if(years > 12*30 && constrainingInterval == Interval.DAY) constrainingInterval = Interval.MONTH;
				if(years > 12*30*12 && constrainingInterval == Interval.MONTH) constrainingInterval = Interval.YEAR;
				if(constrainingInterval != Interval.YEAR){
					if(n > years*2) subInterval = 2;
					if(n > years*4) subInterval = 4;
					if(n > years*6) subInterval = 6;
					if(n > years*12){
						subInterval = 1;
						interval = Interval.MONTH;
					}
					if(constrainingInterval != Interval.MONTH){
						if(n > months*7){
							subInterval = 2;
							interval = Interval.WEEK;
						}
						if(constrainingInterval != Interval.WEEK){
							if(n > months*14) subInterval = 1;
							if(n > months*28){
								subInterval = 1;
								interval = Interval.DAY;
							}
							if(constrainingInterval != Interval.DAY){
								if(n > days*2) subInterval = 2;
								if(n > days*4) subInterval = 4;
								if(n > days*6) subInterval = 6;
								if(n > days*12) subInterval = 12;
								if(n > days*24){
									subInterval = 1;
									interval = Interval.HOUR;
								}
								if(constrainingInterval != Interval.HOUR){
									if(n > hours*2) subInterval = 2;
									if(n > hours*4) subInterval = 4;
									if(n > hours*6) subInterval = 6;
									if(n > hours*12) subInterval = 12;
									if(n > hours*30) subInterval = 30;
									if(n > hours*60){
										subInterval = 1;
										interval = Interval.MINUTE;
									}
									if(constrainingInterval != Interval.MINUTE){
										if(n > minutes*2) subInterval = 2;
										if(n > minutes*4) subInterval = 4;
										if(n > minutes*6) subInterval = 6;
										if(n > minutes*12) subInterval = 12;
										if(n > minutes*30) subInterval = 30;
										if(n > minutes*60){
											interval = Interval.SECOND;
										}
									}
								}
							}
						}
					}
				}
				//interval = Interval.WEEK;subInterval = 1;
				//System.out.println(subInterval);
				int d = days;
				int weeks = 0;
				Date startOfWeek = ns.getMin();
				for(; startOfWeek.getDayOfWeek() != Day.MONDAY; weeks = 1){
					startOfWeek = startOfWeek.nextDay();
					d--;
				}
				if(d > 0) weeks += (int) Math.ceil(d/7.0);
				
				if(interval == Interval.YEAR) return new IntervalData(years*subInterval + 1, interval, subInterval);
				if(interval == Interval.MONTH) return new IntervalData(months*subInterval + 1, interval, subInterval);
				if(interval == Interval.WEEK) return new IntervalData((int)Math.ceil(weeks*1f/subInterval) + subInterval, interval, subInterval);
				if(interval == Interval.DAY) return new IntervalData(days*subInterval + 1, interval, subInterval);
				if(interval == Interval.HOUR) return new IntervalData(hours*subInterval + 1, interval, subInterval);
				if(interval == Interval.MINUTE) return new IntervalData(minutes*subInterval + 1, interval, subInterval);
				return new IntervalData(seconds + 1, interval, subInterval);
			}
			
		};
	}
	
	private static class IntervalData{

		private final int n;
		private final Interval interval;
		private final float subInterval;
		
		private IntervalData(int n, Interval interval, float subInterval){
			this.n = n;
			this.interval = interval;
			this.subInterval = subInterval;
		}
		
	}

}
