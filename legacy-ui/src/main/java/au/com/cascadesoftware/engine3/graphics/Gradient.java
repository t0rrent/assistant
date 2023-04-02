package au.com.cascadesoftware.engine3.graphics;

import java.util.Objects;

import au.com.cascadesoftware.engine3.gui.Orientation;

public class Gradient {
	
	private final Color c1, c2;
	private final Color cornerTopLeft;
	private final Color cornerTopRight;
	private final Color cornerBottomRight;
	private final Color cornerBottomLeft;
	private final Orientation alignment;
	private final int width, height;
	
	public Gradient(final Color c1, final Color c2, final Orientation alignment){
		this.c1 = c1;
		this.c2 = c2;
		cornerTopLeft = cornerTopRight = cornerBottomRight = cornerBottomLeft = null;
		this.alignment = alignment;
		width = height = -1;
	}

	public Gradient(final Color cornerTopLeft, final Color cornerTopRight, final Color cornerBottomRight, final Color cornerBottomLeft, final int width, final int height) {
		this.alignment = null;
		c1 = c2 = null;
		this.cornerTopLeft = cornerTopLeft;
		this.cornerTopRight = cornerTopRight;
		this.cornerBottomRight = cornerBottomRight;
		this.cornerBottomLeft = cornerBottomLeft;
		this.width = width;
		this.height = height;
	}

	public Orientation getAlignment() {
		return alignment;
	}

	public Color getColor1() {
		return c1;
	}

	public Color getColor2() {
		return c2;
	}

	public Color getCornerTopLeft() {
		return cornerTopLeft;
	}

	public Color getCornerTopRight() {
		return cornerTopRight;
	}

	public Color getCornerBottomRight() {
		return cornerBottomRight;
	}

	public Color getCornerBottomLeft() {
		return cornerBottomLeft;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	@Override
	public int hashCode() {
		return alignment == null
				? Objects.hash(cornerTopLeft, cornerTopRight, cornerBottomRight, cornerBottomLeft, width, height)
				:Objects.hash(c1, c2, alignment);
	}
	
}
