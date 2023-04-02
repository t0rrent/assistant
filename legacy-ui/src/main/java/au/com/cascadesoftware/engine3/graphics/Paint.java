package au.com.cascadesoftware.engine3.graphics;

public class Paint implements Painter {
	
	private float strokeWidth;
	private boolean fill = true, bold;
	private Color color = Color.WHITE;
	private Gradient gradient;
	private String font = "";
	private float fontSize = 12;
	private StrokeType strokeType = StrokeType.SOLID;
	
	public Paint(){
	}
	
	@Override
	public float getStrokeWidth(){
		return strokeWidth;
	}
	
	@Override
	public boolean getFill(){
		return fill;
	}
	
	@Override
	public Color getColor(){
		return color;
	}
	
	@Override
	public Gradient getGradient(){
		return gradient;
	}
	
	@Override
	public String getFont(){
		return font;
	}
	
	@Override
	public float getFontSize(){
		return fontSize;
	}

	@Override
	public StrokeType getStrokeType() {
		return strokeType;
	}

	@Override
	public boolean getBold() {
		return bold;
	}
	
	public void setStrokeWidth(float strokeWidth){
		this.strokeWidth = strokeWidth;
	}
	
	public void setFill(boolean fill){
		this.fill = fill;
	}
	
	public void setColor(Color c){
		gradient = null;
		color = c;
	}

	public void setFont(String string) {
		font = string;
	}

	public void setFontSize(float f) {
		fontSize = f;
	}

	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
	}

	public void setStrokeType(StrokeType st) {
		this.strokeType = st;
	}
	
	public enum StrokeType {
		SOLID, DASHED
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
}
