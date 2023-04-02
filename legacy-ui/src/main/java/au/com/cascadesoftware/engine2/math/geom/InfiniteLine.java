package au.com.cascadesoftware.engine2.math.geom;

import au.com.cascadesoftware.engine2.math.Lined;
import au.com.cascadesoftware.engine2.math.Vector2d;

public class InfiniteLine {

	public final double angle, offset;
	
	public InfiniteLine(double theta, double offset){
		while(theta < 0) theta += Math.PI;
		theta %= Math.PI;
		angle = theta;
		this.offset = offset;
	}
	
	public final double getXIntercept(){
		if(offset != 0 && angle == 0) return Double.NaN;
		return offset*Math.cos(angle)*Math.cos(angle)/Math.sin(angle) + offset*Math.sin(angle);
	}
	
	public final double getYIntercept(){
		if(offset != 00 && angle == Math.PI/2) return Double.NaN;
		return -offset*Math.sin(angle)*Math.sin(angle)/Math.cos(angle) - offset*Math.cos(angle);
	}
	
	public final double getGradient(){
		if(angle == 0) return 0;
		if(angle == Math.PI/2) return Double.POSITIVE_INFINITY;
		return Math.tan(angle);
	}	

	public Vector2d getIntersectionPoint(InfiniteLine line2) {
		if(angle == Math.PI/2){
			if(line2.angle == Math.PI/2) return null;
			double x = this.getXIntercept();
			double y = line2.getGradient()*x + line2.getYIntercept();
			return new Vector2d(x, y);
		}else{
			if(angle == 0 && line2.angle == 0) return null;
			if(line2.angle == Math.PI/2){
				double x = line2.getXIntercept();
				double y = getGradient()*x + getYIntercept();
				return new Vector2d(x, y);
			}else{
				double x = (getYIntercept() - line2.getYIntercept())/(line2.getGradient() - getGradient());
				double y = getGradient()*x + getYIntercept();
				return new Vector2d(x, y);
			}
		}
	}

	public boolean intersects(Lined line) {
		InfiniteLine infLine = line.getInfiniteLine();
		Vector2d intersectionPoint = this.getIntersectionPoint(infLine);
		return Math.abs(line.x1 - intersectionPoint.x) <= Math.abs(line.x1 - line.x2)
				&& Math.abs(line.x2 - intersectionPoint.x) <= Math.abs(line.x1 - line.x2)
				&& Math.abs(line.y1 - intersectionPoint.y) <= Math.abs(line.y1 - line.y2)
				&& Math.abs(line.y2 - intersectionPoint.y) <= Math.abs(line.y1 - line.y2);
	}
	
	
	
	public static class Builder {
		
		public static InfiniteLine fromGradientAndYIntercept(double m, double yInt){
			double angle = Math.atan(m);
			return fromAngleAndYIntercept(angle, yInt);
		}

		public static InfiniteLine fromGradientAndXIntercept(double m, double xInt){
			double angle = Math.atan(m);
			return fromAngleAndXIntercept(angle, xInt);
		}
		
		public static InfiniteLine fromTwoPoints(Vector2d p1, Vector2d p2){
			double yDiff = p2.y - p1.y;
			double xDiff = p2.x - p1.x;
			if(Math.abs(xDiff) < Math.abs(yDiff)){
				double inverseM = xDiff/yDiff;
				double xInt = p1.x - inverseM*p1.y;
				return fromGradientAndXIntercept(yDiff/xDiff, xInt);
			}
			
			
			double m = yDiff/xDiff;
			double yInt = p1.y - m*p1.x;
			return fromGradientAndYIntercept(m, yInt);
		}
		
		public static InfiniteLine fromAngleAndYIntercept(double angle, double yInt){
			while(angle < 0) angle += Math.PI;
			angle %= Math.PI;
			if(angle == Math.PI/2) return new InfiniteLine(angle, 0);
			double offset = -yInt/(Math.sin(angle)*Math.sin(angle)/Math.cos(angle) + Math.cos(angle));
			return new InfiniteLine(angle, offset);
		}
		
		public static InfiniteLine fromAngleAndXIntercept(double angle, double xInt){
			while(angle < 0) angle += Math.PI;
			angle %= Math.PI;
			if(angle == 0) return new InfiniteLine(angle, 0);
			double offset = xInt/(Math.cos(angle)*Math.cos(angle)/Math.sin(angle) + Math.sin(angle));
			return new InfiniteLine(angle, offset);
		}
		
		public static InfiniteLine fromAngleAndPoint(double angle, Vector2d point){
			Vector2d p2 = point.add(new Vector2d(Math.cos(angle), Math.sin(angle)));
			return fromTwoPoints(point, p2);
		}
		
	}
	
}
