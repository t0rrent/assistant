package au.com.cascadesoftware.engine3.graphics;

import java.io.Serializable;
import java.util.Objects;

public class Color implements Serializable {
	
	private static final long serialVersionUID = 2088934373043988071L;
	
	private int r, g, b, a;
	
	public Color(int value){
	    r = (value >> 16) & 0xff;
	    g = (value >>  8) & 0xff;
	    b = (value      ) & 0xff;
	    a = (value >> 24) & 0xff;
	}
	
	public Color(int r, int g, int b){
		this(r, g, b, 255);
	}
	
	public Color(float r, float g, float b){
		this(r, g, b, 1f);
	}
	
	public Color(int r, int g, int b, int a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(int r, int g, int b, float a){
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = (int) (a*255);
	}
	
	public Color(float r, float g, float b, float a){
		this.r = (int) (r*255);
		this.g = (int) (g*255);
		this.b = (int) (b*255);
		this.a = (int) (a*255);
	}
	
	
	public Color(Color color, int a){
		this(color.r, color.g, color.b, a);
	}
	
	public Color(Color color, float a){
		this(color.r, color.g, color.b, (int) (a*255));
	}
	
	public Color(Color c) {
		this(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public Color(String htmlhex) {
		htmlhex = htmlhex.replace("#", "");
		String newHex = "";
		if(htmlhex.length() == 6 || htmlhex.length() == 3){ //no alpha
			if(htmlhex.length() == 3) for(int i = 0; i < htmlhex.length(); i++) newHex += htmlhex.substring(i, i + 1) + htmlhex.substring(i, i + 1);
			else newHex = htmlhex;
			newHex += "ff";
		}else{
			if(htmlhex.length() == 4) for(int i = 0; i < htmlhex.length(); i++) newHex += htmlhex.substring(i, i + 1) + htmlhex.substring(i, i + 1);
			else newHex = htmlhex;
		}
		r = Integer.parseInt(newHex.substring(0, 2), 16);
		g = Integer.parseInt(newHex.substring(2, 4), 16);
		b = Integer.parseInt(newHex.substring(4, 6), 16);
		a = Integer.parseInt(newHex.substring(6, 8), 16);
	}

	public int getRed(){
		return r;
	}
	
	public float getRedf(){
		return r/255f;
	}
	
	public int getGreen(){
		return g;
	}
	
	public float getGreenf(){
		return g/255f;
	}
	
	public int getBlue(){
		return b;
	}
	
	public float getBluef(){
		return b/255f;
	}
	
	public int getAlpha(){
		return a;
	}
	
	public float getAlphaf(){
		return a/255f;
	}
	
	public void setAlpha(int a){
		this.a = a;
	}
	
	public void setAlpha(float a){
		this.a = (int) (a*255);
	}
	
	public void setRed(int r){
		this.r = r;
	}
	
	public void setRed(float r){
		this.r = (int) (r*255);
	}
	
	public void setGreen(int g){
		this.g = g;
	}
	
	public void setGreen(float g){
		this.g = (int) (g*255);
	}
	
	public void setBlue(int b){
		this.b = b;
	}
	
	public void setBlue(float b){
		this.b = (int) (b*255);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(r, g, b, a);
	}

	private static final float FACTOR = 0.7f;
	
	public Color brighter() {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int alpha = getAlpha();

        return new Color(Math.min((int)(r/FACTOR), 255),
                         Math.min((int)(g/FACTOR), 255),
                         Math.min((int)(b/FACTOR), 255),
                         alpha);
    }

    public Color darker() {
        return new Color(Math.max((int)(getRed()  *FACTOR), 0),
                         Math.max((int)(getGreen()*FACTOR), 0),
                         Math.max((int)(getBlue() *FACTOR), 0),
                         getAlpha());
    }
    
    public Color getMiddleColor(Color c2) {
		return new Color((r + c2.r)/2, (g + c2.g)/2, (b + c2.b)/2, (a + c2.a)/2);
	}

	public final static Color INVISIBLE = new Color(255, 255, 255, 0);
	public final static Color white     = new Color(255, 255, 255);
    public final static Color WHITE = white;
    public final static Color lightGray = new Color(192, 192, 192);
    public final static Color LIGHT_GRAY = lightGray;
    public final static Color LIGHT_GARY = LIGHT_GRAY;
    public final static Color gray      = new Color(128, 128, 128);
    public final static Color GRAY = gray;
    public final static Color GARY = GRAY;
    public final static Color darkGray  = new Color(64, 64, 64);
    public final static Color DARK_GRAY = darkGray;
    public final static Color DARK_GARY = DARK_GRAY; // <3 gary
    public final static Color black     = new Color(0, 0, 0);
    public final static Color BLACK = black;
    public final static Color red       = new Color(255, 0, 0);
    public final static Color RED = red;
    public final static Color pink      = new Color(255, 175, 175);
    public final static Color PINK = pink;
    public final static Color orange    = new Color(255, 200, 0);
    public final static Color ORANGE = orange;
    public final static Color yellow    = new Color(255, 255, 0);
    public final static Color YELLOW = yellow;
    public final static Color green     = new Color(0, 255, 0);
    public final static Color GREEN = green;
    public final static Color magenta   = new Color(255, 0, 255);
    public final static Color MAGENTA = magenta;
    public final static Color cyan      = new Color(0, 255, 255);
    public final static Color CYAN = cyan;
    public final static Color blue      = new Color(0, 0, 255);
    public final static Color BLUE = blue;

	public String getHTMLCode() {
		int code = getRed()*256*256*256 + getGreen()*256*256 + getBlue()*256 + getAlpha();
		String out = "#" + Integer.toHexString(code);
		while(out.length() < 9) out = "#0" + out.substring(1);
		return out;
	}
	
}