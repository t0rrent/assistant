package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Vector2f;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;

public class GUIText extends GUI {

	private float textSize;
	private float textSizeAbsolute;
	private float textScale;
	private boolean needsRescale, bold;
	private String text;
	private Color color;
	private Alignment alignment = Alignment.MIDDLE_CENTER;
	
	private Vector2f tm = new Vector2f();
	private Vector2i offset;

	public GUIText(Window window, Boundary bounds, Color color, String text) {
		super(window, bounds);
		this.text = text;
		this.color = color;
		needsRescale = true;
		offset = new Vector2i();
	}
	
	private void rescale(Graphics graphics){
		Paint paint = new Paint();
		paint.setFontSize(1000f);
		paint.setBold(bold);
		graphics.setPaint(paint);
		Vector2f tm = graphics.measureText(text);
		textScale = tm.y/tm.x;
	}
	
	private float getHeight() {
		if(textSizeAbsolute > 0){
			return textSizeAbsolute;
		}
		if(textSize > 0){
			return textSize * getRenderBounds().height;
		}
		float rectScale = getRenderBounds().height*1f/getRenderBounds().width;
	
		float out = (rectScale > textScale && textSize <= 0) ? getRenderBounds().width*textScale : getRenderBounds().height;
		return out*3/4;
	}
	
	public void offset(Vector2i pixels){
		this.offset = pixels;
	}

	@Override
	protected void draw(Graphics graphics) {
		if(needsRescale){
			rescale(graphics);
			needsRescale = false;
		}
		
		Paint paint = new Paint();
		paint.setFill(true);
		
		/*paint.setColor(Color.MAGENTA);
		graphics.setPaint(paint);
		graphics.drawRect(getBounds());*/
		
		float height = getHeight();
		
		/*paint.setColor(Color.CYAN);
		graphics.setPaint(paint);
		graphics.drawRect(getBounds().x + (int) Math.ceil((getBounds().width - height*4/3f/textScale)/2*alignment.x), getBounds().y + (int) Math.ceil((getBounds().height - height)/2*alignment.y), (int) (height*4/3f/textScale), (int)height);
		*/
		
		paint.setColor(color);
		paint.setFontSize(height);
		paint.setBold(bold);
		graphics.setPaint(paint);
		tm = graphics.measureText(text);
		//System.out.println(getBounds().width + ", " + tm.x + "; " + rectScale + ", " + (tm.y*1f/tm.x) + " : " + textScale + "; " + tm.y + ", " + height);
		//System.out.println(tm.x + ", "+text + ", " + getBounds().width);
		graphics.drawString(text, (int) Math.ceil(getRenderBounds().x + (getRenderBounds().width - tm.x)/2f*alignment.x) + offset.x, (int) Math.ceil(getRenderBounds().y + tm.y*graphics.getTextHeightModifier()*(3 - alignment.y*2) + getRenderBounds().height/2f*alignment.y) + offset.y);
	}

	public void setText(String text){
		this.text = text;
		needsRescale = true;
	}

	public GUIText setBold(boolean b){
		this.bold = b;
		needsRescale = true;
		return this;
	}
	
	public GUIText setTextSize(float size){
		this.textSize = size;
		return this;
	}
	
	public GUIText setTextAbsolute(float size){
		this.textSizeAbsolute = size;
		return this;
	}
	
	public GUIText setTextAlignment(Alignment alignment){
		this.alignment = alignment;
		return this;
	}
	
	public String getText(){
		return text;
	}
	
	public Vector2f getSize(){
		return tm;
	}

	public void setTextColor(Color textColor) {
		this.color = textColor;
	}

	public boolean doesPhraseFit(Graphics graphics, String text) {
		if(text.length() == 0) return true;
		Paint paint = new Paint();
		paint.setFontSize(getHeight());
		graphics.setPaint(paint);
		Vector2f tm = graphics.measureText(text);
		return tm.x <= getRenderBounds().width;
	}

	public void sync(GUIText[] textFields) {
		float smallest = getHeight();
		for(GUIText gui : textFields){
			if(gui.getHeight() < smallest) smallest = gui.getHeight();
		}
		for(GUIText gui : textFields){
			gui.setTextAbsolute(smallest);
		}
	}

	@Override
	protected void onResize() {
		needsRescale = true;
	}

	protected String[] getHTML() {
		int size = (int) getHeight();
		float thm = 1;
		if(textScale == 0) size = 72;
		Vector2i textLocation = new Vector2i((int) Math.ceil(getRenderBounds().x + (getRenderBounds().width - tm.x)/2f*alignment.x), (int) Math.ceil(getRenderBounds().y + tm.y*thm*(3 - alignment.y*2) + getRenderBounds().height/2f*alignment.y));
		textLocation = textLocation.sub(getRenderBounds().cornerPosition());
		String alignment = "left";
		if(this.alignment.CENTER) alignment = "center";
		if(this.alignment.RIGHT) alignment = "right";
		return new String[]{"<div style=\"font-size:" + size + "px;color:" + color.getHTMLCode() + ";text-align:" + alignment + "\">" + this.text + "</div>"};
	}
	
}
