package au.com.cascadesoftware.engine3.graphics;

import au.com.cascadesoftware.engine2.math.Polygon;
import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2f;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.gui.android.AndroidTextbox;
import au.com.cascadesoftware.engine3.in.ResourceLoader;

public interface Graphics {
	
	void setPaint(Painter paint);

	void drawImage(ResourceLoader resourceLoader, Recti bounds);
	
	void drawImage(ResourceLoader resourceLoader, int x, int y, int width, int height);
	
	void drawLine(int x, int y, int x2, int y2);

	void drawRect(int x, int y, int width, int height);
	
	void drawRect(Recti bounds);
	
	void drawElipse(int x, int y, int width, int height);
	
	void drawElipse(Recti bounds);

	void clearRect(int x, int y, int width, int height);
	
	void clearRect(Recti bounds);
	
	void drawString(String text, int x, int y);
	
	void drawPolygon(Polygon p);
	
	void rotate(double rotation, Vector2d center);
	
	void setOpacity(float opacity);

	Vector2f measureText(String text);

	void dispose();

	float getTextHeightModifier();

	AndroidTextbox invokeAndroidTextbox(Recti bounds, int lines, Color textColor, Color placeholderColor, Color boxColor, Color borderColor);

	Canvas createCanvas(Recti bounds, Vector2i offset, boolean antiAlias);

	void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle);

	void drawArc(Recti bounds, int startAngle, int arcAngle); //degrees

	void drawElipse(Recti bounds, double rotation);

	void drawElipse(int x, int y, int width, int height, double rotation);

	void preRender(Vector2i size);

	boolean postRender(String export);

	
}
