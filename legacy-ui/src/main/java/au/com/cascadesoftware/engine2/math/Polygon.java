package au.com.cascadesoftware.engine2.math;

import java.io.Serializable;

public class Polygon implements Serializable{
	
	private static final long serialVersionUID = Engine2Math.SERIAL_VERSION_UID;
    
	public int npoints;
    public int xpoints[];
    public int ypoints[];
    
    public Polygon(Vector2i[] points){
    	this(getXPoints(points), getYPoints(points), points.length);
    }
    
    private static int[] getXPoints(Vector2i[] points) {
		int[] out = new int[points.length];
		for(int i = 0; i < points.length; i++) out[i] = points[i].x;
		return out;
	}
    
    private static int[] getYPoints(Vector2i[] points) {
		int[] out = new int[points.length];
		for(int i = 0; i < points.length; i++) out[i] = points[i].y;
		return out;
	}

	public Polygon(int xpoints[], int ypoints[], int npoints){
        if(npoints > xpoints.length || npoints > ypoints.length) throw new IndexOutOfBoundsException();
        if(npoints < 0) throw new NegativeArraySizeException();
        this.npoints = npoints;
        this.xpoints = new int[npoints];
        this.ypoints = new int[npoints];
        for(int i = 0; i < npoints; i++){
        	this.xpoints[i] = xpoints[i];
        	this.ypoints[i] = ypoints[i];
        }
    }

	public boolean contains(int x, int y){
		double x0;
		double x1;
		double y0;
		double y1;
		double epsilon = 0.0;
		int crossings = 0;
		int[] xp;
		int[] yp;
		xp = xpoints;
		yp = ypoints;
		epsilon = 1E-7;
		x0 = xp[0] - x;
		y0 = yp[0] - y;
		for(int i = 1; i < npoints; i++){
			x1 = xp[i] - x;
			y1 = yp[i] - y;
			if(y0 == 0.0) y0 -= epsilon;
			if(y1 == 0.0) y1 -= epsilon;
			if(y0 * y1 < 0) if (linesIntersect(x0, y0, x1, y1, epsilon, 0.0, java.lang.Double.MAX_VALUE / 10.0, 0.0)) ++crossings;
			x0 = xp[i] - x;
		 	y0 = yp[i] - y;
		}
		x1 = xp[0] - x;
		y1 = yp[0] - y;
		if (y0 == 0.0) y0 -= epsilon;
		if(y1 == 0.0) y1 -= epsilon;
		if (y0 * y1 < 0) if(linesIntersect(x0, y0, x1, y1, epsilon, 0.0, java.lang.Double.MAX_VALUE / 10.0, 0.0)) ++crossings;
		return (crossings & 1) != 0;
	}

	private boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		return ((relativeCCW(x1, y1, x2, y2, x3, y3) *
                relativeCCW(x1, y1, x2, y2, x4, y4) <= 0)
               && (relativeCCW(x3, y3, x4, y4, x1, y1) *
                   relativeCCW(x3, y3, x4, y4, x2, y2) <= 0));
	}

	private int relativeCCW(double x1, double y1, double x2, double y2, double px, double py) {
		x2 -= x1;
		y2 -= y1;
		px -= x1;
		py -= y1;
		double ccw = px * y2 - py * x2;
		if(ccw == 0.0) {
        	ccw = px * x2 + py * y2;
        	if (ccw > 0.0) {
        		px -= x2;
        		py -= y2;
        		ccw = px * x2 + py * y2;
        		if (ccw < 0.0) {
        			ccw = 0.0;
        		}
        	}
		}
		return (ccw < 0.0) ? -1 : ((ccw > 0.0) ? 1 : 0);
	}
    
    
}
