package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Recti;

import static au.com.cascadesoftware.engine3.gui.Scalar.*;

public class Boundary {

	public final Rectf coordinates;
	public final Scalar scalar;
	public final Alignment alignment;

	private int xPixels = -1;
	private int yPixels = -1;
	private int widthPixels = -1;
	private int heightPixels = -1;
	
	public Boundary(Rectf bounds, Scalar scalar, Alignment alignment){
		this.coordinates = bounds;
		this.scalar = scalar;
		this.alignment = alignment;
	}
	
	public Boundary(Rectf bounds){
		this.coordinates = bounds;
		this.scalar = STRETCHED;
		this.alignment = Alignment.MIDDLE_CENTER;
	}
	
	public Boundary() {
		this.coordinates = new Rectf(0, 0, 1, 1);
		this.scalar = STRETCHED;
		this.alignment = Alignment.MIDDLE_CENTER;
	}
	
	public Boundary(Recti rect) {
		this();
		fixWidth(rect.width);
		fixHeight(rect.height);
		fixX(rect.x);
		fixY(rect.y);
	}

	public Boundary clone(){
		return new Boundary(new Rectf(coordinates.x, coordinates.y, coordinates.width, coordinates.height), scalar, alignment);
	}

	public Recti getRect(Recti container){
		float scale = container.height*1f/container.width;
		float bx = coordinates.x;
		float by = coordinates.y;
		float bwidth = coordinates.width;
		float bheight = coordinates.height;
		Scalar effectiveScalar = scalar;
		if(scalar == SMALLEST){
			if(scale > 1) effectiveScalar = HORIZONTAL;
			else effectiveScalar = VERTICAL;
		}else if(scalar == LARGEST){
			if(scale < 1) effectiveScalar = HORIZONTAL;
			else effectiveScalar = VERTICAL;
		}
		if(effectiveScalar == HORIZONTAL){
			by /= scale;
			bheight /= scale;
		}
		if(effectiveScalar == VERTICAL){
			bx *= scale;
			bwidth *= scale;
		}
		float x = bx*(alignment.x == 2 ? -1 : 1) + (1 - bwidth)*alignment.x/2f;
		float y = by*(alignment.y == 2 ? -1 : 1) + (1 - bheight)*alignment.y/2f;
		Recti out = new Rectf(container.x + x*container.width, container.y + y*container.height, bwidth*container.width, bheight*container.height).toRecti();
		if(heightPixels != -1) out.height = heightPixels;
		if(widthPixels != -1) out.width = widthPixels;
		if(xPixels != -1) out.x = xPixels;
		if(yPixels != -1) out.y = yPixels;
		return out;
	}

	public Boundary fixHeight(int h) {
		this.heightPixels = h;
		return this;
	}

	public Boundary fixWidth(int w) {
		this.widthPixels = w;
		return this;
	}

	public Boundary fixX(int x) {
		this.xPixels = x;
		return this;
	}

	public Boundary fixY(int y) {
		this.yPixels = y;
		return this;
	}
	
}
