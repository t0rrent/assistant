package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Recti;

public interface Scale {

	Orientation getOrientation();

	void addRescaleMethod(Runnable runnable);

	int getSetLength();

	double getMidpoint();

	double getRange();

	void setShow(double midPoint, double range);

	int[] getLines();

	Recti getRenderBounds();

}
