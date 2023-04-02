package au.com.cascadesoftware.engine3.graphics;

import au.com.cascadesoftware.engine3.graphics.Paint.StrokeType;

public interface Painter{
	
	float getStrokeWidth();
	
	boolean getFill();
	
	Color getColor();
	
	String getFont();
	
	float getFontSize();

	Gradient getGradient();

	StrokeType getStrokeType();

	boolean getBold();

}