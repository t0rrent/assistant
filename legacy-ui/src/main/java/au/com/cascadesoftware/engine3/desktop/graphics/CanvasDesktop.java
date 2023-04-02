package au.com.cascadesoftware.engine3.desktop.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.graphics.Canvas;
import au.com.cascadesoftware.engine3.graphics.Graphics;

public class CanvasDesktop implements Canvas {
	
	private BufferedImage buffer;
	private Recti bounds;
	private Vector2i offset;
	private boolean antiAlias;
	private double rotation;
	private Vector2d rotationCentre;

	public CanvasDesktop(Recti bounds, Vector2i offset, boolean antiAlias) {
		this.antiAlias = antiAlias;
		this.rotationCentre = new Vector2d();
		resize(bounds, offset);
	}

	@Override
	public Graphics getGraphics() {
		return new GraphicsDesktop((Graphics2D) buffer.getGraphics(), antiAlias);
	}

	@Override
	public void draw(Graphics containerGraphics){
		Graphics2D g = ((GraphicsDesktop) containerGraphics).getNativeGraphics();
		g.rotate(rotation, rotationCentre.x, rotationCentre.y);
		g.drawImage(buffer, bounds.x + offset.x, bounds.y + offset.y, bounds.width, bounds.height, null);
		g.rotate(-rotation, rotationCentre.x, rotationCentre.y);
	}

	@Override
	public void setBounds(Recti bounds){
		this.bounds = bounds;
	}

	@Override
	public void resize(Recti bounds, Vector2i offset) {
		this.bounds = bounds;
		this.offset = offset;
		buffer = null;
		try{
			buffer = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
		}catch(java.lang.OutOfMemoryError e){
			System.err.println("Out of memory, buffer probably too big");
		}
	}

	@Override
	public void clear() {
		int[] pixels = new int[buffer.getWidth()*buffer.getHeight()];
		Arrays.fill(pixels, 0);
		buffer.setRGB(0, 0, buffer.getWidth(), buffer.getHeight(), pixels, 0, buffer.getWidth());
	}

	@Override
	public void setOffset(Vector2i v) {
		offset = v;
	}

	@Override
	public void setRotation(Vector2d rotationCentre, double r) {
		this.rotation = r;
		this.rotationCentre = rotationCentre;
	}

	@Override
	public Canvas createFreshCanvas() {
		return new CanvasDesktop(bounds, offset, antiAlias);
	}

}
