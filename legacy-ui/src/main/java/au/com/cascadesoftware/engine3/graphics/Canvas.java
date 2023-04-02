package au.com.cascadesoftware.engine3.graphics;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2i;

public interface Canvas {

	Graphics getGraphics();
	
	void draw(Graphics containerGraphics);

	void resize(Recti bounds, Vector2i offset);
	
	void clear();

	void setOffset(Vector2i vector2i);

	void setRotation(Vector2d rotationCentre, double r);

	void setBounds(Recti bounds);

	Canvas createFreshCanvas();

}
