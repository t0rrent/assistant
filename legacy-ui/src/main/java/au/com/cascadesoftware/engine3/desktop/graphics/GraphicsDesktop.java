package au.com.cascadesoftware.engine3.desktop.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Function;

import javax.imageio.ImageIO;

import au.com.cascadesoftware.engine2.math.Polygon;
import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2d;
import au.com.cascadesoftware.engine2.math.Vector2f;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.desktop.in.ResourceLoaderDesktop;
import au.com.cascadesoftware.engine3.graphics.Canvas;
import au.com.cascadesoftware.engine3.graphics.Gradient;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint.StrokeType;
import au.com.cascadesoftware.engine3.graphics.Painter;
import au.com.cascadesoftware.engine3.gui.Orientation;
import au.com.cascadesoftware.engine3.gui.android.AndroidTextbox;
import au.com.cascadesoftware.engine3.in.ResourceLoader;

public class GraphicsDesktop implements Graphics {

	private static final HashMap<Integer, BufferedImage> GRADIENT_MAP = new HashMap<>();
	
	private static final int MAX_GRADIENT_MAP_SIZE = 100000;

	private Graphics2D bufferGraphics;
	
	private final Graphics2D mainGraphics;
	
	private BufferedImage buffer;
	
	private Painter paint;
	private HashMap<String, BasicStroke> strokeMap;
	private HashMap<String, Font> fontMap;
	
	private float opacity;

	private boolean antiAlias;
	
	public GraphicsDesktop(Graphics2D g, boolean antiAlias){
		this.antiAlias = antiAlias;
		if(antiAlias){
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}
		opacity = 1f;
		this.mainGraphics = g;
		this.bufferGraphics = mainGraphics;
		strokeMap = new HashMap<String, BasicStroke>();
		fontMap = new HashMap<String, Font>();
	}
	

	@Override
	public void setPaint(Painter paint) {
		this.paint = paint;
		
		au.com.cascadesoftware.engine3.graphics.Color c = au.com.cascadesoftware.engine3.graphics.Color.WHITE;
		if(paint != null) c = paint.getColor();
		float a = c.getAlphaf() * opacity;
		Color color = getConvertedColor(new au.com.cascadesoftware.engine3.graphics.Color(c, a));
		if (bufferGraphics == null) {
			System.out.println("we got a problem");
		}
		bufferGraphics.setColor(color);
		if(strokeMap.containsKey(paint.getStrokeType().name() + paint.getStrokeWidth())){
			bufferGraphics.setStroke(strokeMap.get(paint.getStrokeType().name() + paint.getStrokeWidth()));
		}else{
			BasicStroke stroke = new BasicStroke(paint.getStrokeWidth());
			if(paint.getStrokeType() == StrokeType.DASHED) stroke = new BasicStroke(paint.getStrokeWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, paint.getStrokeWidth()*3, new float[]{paint.getStrokeWidth()*3}, 0.0f);
			strokeMap.put(paint.getStrokeType().name() + paint.getStrokeWidth(), stroke);
			bufferGraphics.setStroke(stroke);
		}
		if(fontMap.containsKey(paint.getFont())){
			bufferGraphics.setFont(fontMap.get(paint.getFont()).deriveFont(paint.getFontSize()));
		}else{
			Font font = new Font(paint.getFont(), Font.PLAIN, (int)paint.getFontSize());
			fontMap.put(paint.getFont(), font);
			bufferGraphics.setFont(font);
		}
	}

	@Override
	public void drawImage(ResourceLoader resourceLoader, int x, int y, int width, int height) {
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
		Composite old = bufferGraphics.getComposite();
		bufferGraphics.setComposite(ac);
		Image img = ((ResourceLoaderDesktop) resourceLoader).loadImage();
		bufferGraphics.drawImage(img, x, y, width, height, null);
		bufferGraphics.setComposite(old);
	}

	@Override
	public void drawLine(int x, int y, int x2, int y2) {
		bufferGraphics.drawLine(x, y, x2, y2);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		Paint oldPaint = bufferGraphics.getPaint();
		boolean conventionalRender = true;
		if(paint.getGradient() != null){
			final Gradient gradient = paint.getGradient();
			if (gradient.getAlignment() == null) {
				conventionalRender = false;
				BufferedImage image = get4PointGradientImage(gradient);
				bufferGraphics.drawImage(image, x, y, width, height, null);
			} else {
				au.com.cascadesoftware.engine3.graphics.Color c1 = paint.getGradient().getColor1();
				au.com.cascadesoftware.engine3.graphics.Color c2 = paint.getGradient().getColor2();
				Color c1a = new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
				Color c2a = new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
				GradientPaint grad = new GradientPaint(x, 0, c1a, x + width, 0, c2a);
				if(paint.getGradient().getAlignment() == Orientation.PORTRAIT){
					grad = new GradientPaint(0, y, c1a, 0, y+height, c2a);
				}
				bufferGraphics.setPaint(grad);
			}
		}
		if (conventionalRender) {
			if(paint.getFill()) bufferGraphics.fillRect(x, y, width, height);
			else bufferGraphics.drawRect(x, y, width, height);
		}
		bufferGraphics.setPaint(oldPaint);
	}

	private BufferedImage get4PointGradientImage(final Gradient gradient) {
		if (gradient.getAlignment() != null) {
			throw new IllegalStateException("wrong kind of gradient");
		}
		if (GRADIENT_MAP.containsKey(gradient.hashCode())) {
			return GRADIENT_MAP.get(gradient.hashCode());
		} else {
			final BufferedImage image = new BufferedImage(gradient.getWidth(), gradient.getHeight(), BufferedImage.TYPE_INT_ARGB);
			final Graphics2D graphics = (Graphics2D) image.getGraphics();
			for (int x = 0; x < gradient.getWidth(); x++) {
				for (int y = 0; y < gradient.getHeight(); y++) {
					Color color = new Color(
							bilinearInterpolation(x * 1f / (gradient.getWidth() - 1), y * 1f / (gradient.getHeight() - 1), gradient, au.com.cascadesoftware.engine3.graphics.Color::getRedf),
							bilinearInterpolation(x * 1f / (gradient.getWidth() - 1), y * 1f / (gradient.getHeight() - 1), gradient, au.com.cascadesoftware.engine3.graphics.Color::getGreenf),
							bilinearInterpolation(x * 1f / (gradient.getWidth() - 1), y * 1f / (gradient.getHeight() - 1), gradient, au.com.cascadesoftware.engine3.graphics.Color::getBluef),
							bilinearInterpolation(x * 1f / (gradient.getWidth() - 1), y * 1f / (gradient.getHeight() - 1), gradient, au.com.cascadesoftware.engine3.graphics.Color::getAlphaf)
					);
					graphics.setColor(color);
					graphics.fillRect(x, y, 1, 1);
				}
			}
			if (GRADIENT_MAP.size() > MAX_GRADIENT_MAP_SIZE) {
				System.out.println("MAX GRADIENT_MAP EXCEEDED");
			} else {
				GRADIENT_MAP.put(gradient.hashCode(), image);
			}
			return image;
		}
	}

	private float bilinearInterpolation(float x, float y, Gradient gradient, Function<au.com.cascadesoftware.engine3.graphics.Color, Float> colorComponentTransform) {
		float colorTopLeft = colorComponentTransform.apply(gradient.getCornerTopLeft());
		float colorTopRight = colorComponentTransform.apply(gradient.getCornerTopRight());
		float colorBottomLeft = colorComponentTransform.apply(gradient.getCornerBottomLeft());
		float colorBottomRight = colorComponentTransform.apply(gradient.getCornerBottomRight());
		return lerp(lerp(colorTopLeft, colorTopRight, x), lerp(colorBottomLeft, colorBottomRight, x), y);
	}

	private float lerp(float x1, float x2, float factor) {
		return x1 * (1 - factor) + x2 * factor;
	}


	@Override
	public void drawRect(Recti bounds) {
		drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void clearRect(int x, int y, int width, int height) {
		bufferGraphics.clearRect(x, y, width, height);
	}

	@Override
	public void clearRect(Recti bounds) {
		clearRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void drawString(String text, int x, int y) {
		Font old = null;
		if(paint.getBold()){
			old = bufferGraphics.getFont();
			bufferGraphics.setFont(old.deriveFont(Font.BOLD));
		}
		bufferGraphics.drawString(text, x, y);
		if(paint.getBold()) bufferGraphics.setFont(old);
	}

	@Override
	public void drawPolygon(Polygon p) {
		java.awt.Polygon polygon = new java.awt.Polygon(p.xpoints, p.ypoints, p.npoints);
		if(paint.getFill()) bufferGraphics.fillPolygon(polygon);
		else bufferGraphics.drawPolygon(polygon);
	}
	
	@Override
	public Vector2f measureText(String text){
		Font old = null;
		if(paint.getBold()){
			old = bufferGraphics.getFont();
			bufferGraphics.setFont(old.deriveFont(Font.BOLD));
		}
		Rectangle2D r = bufferGraphics.getFontMetrics().getStringBounds(text, bufferGraphics);
		if(paint.getBold()) bufferGraphics.setFont(old);
		return new Vector2f((float)r.getWidth(), (float)r.getHeight());
	}
	
	private Color getConvertedColor(au.com.cascadesoftware.engine3.graphics.Color color) {
		try{
			return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		}catch(java.lang.IllegalArgumentException e){
			System.err.println("Invalid Color(r,g,b,a): (" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ", " + color.getAlpha() + ")");
			return Color.BLACK;
		}
	}

	public Painter getPaint(){
		return paint;
	}

	@Override
	public void dispose() {
		bufferGraphics.dispose();
	}

	@Override
	public void drawImage(ResourceLoader resourceLoader, Recti bounds) {
		drawImage(resourceLoader, bounds.x, bounds.y, bounds.width, bounds.height);
	}


	@Override
	public float getTextHeightModifier() {
		return 5/24f;
	}

	@Override
	public AndroidTextbox invokeAndroidTextbox(Recti bounds, int lines, au.com.cascadesoftware.engine3.graphics.Color textColor,
			au.com.cascadesoftware.engine3.graphics.Color placeholderColor,
			au.com.cascadesoftware.engine3.graphics.Color boxColor,
			au.com.cascadesoftware.engine3.graphics.Color borderColor) {
		return null;
	}

	@Override
	public Canvas createCanvas(Recti bounds, Vector2i offset, boolean antiAlias) {
		return new CanvasDesktop(bounds, offset, antiAlias);
	}


	public Graphics2D getNativeGraphics() {
		return bufferGraphics;
	}

	@Override
	public void rotate(double rotation, Vector2d center) {
		bufferGraphics.rotate(rotation, center.x, center.y);
	}


	@Override
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	@Override
	public void drawElipse(int x, int y, int width, int height) {
		drawElipse(x, y, width, height, 0);
	}

	@Override
	public void drawElipse(int x, int y, int width, int height, double rotation) {
		bufferGraphics.rotate(rotation, x + width/2, y + height/2);
		Paint oldPaint = bufferGraphics.getPaint();
		if(paint.getGradient() != null){
			if (paint.getGradient().getAlignment() == null) {
			} else {
				au.com.cascadesoftware.engine3.graphics.Color c1 = paint.getGradient().getColor1();
				au.com.cascadesoftware.engine3.graphics.Color c2 = paint.getGradient().getColor2();
				Color c1a = new Color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha());
				Color c2a = new Color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha());
				GradientPaint grad = new GradientPaint(x, 0, c1a, x+width, 0, c2a);
				if(paint.getGradient().getAlignment() == Orientation.PORTRAIT){
					grad = new GradientPaint(0, y, c1a, 0, y+height, c2a);
				}
				bufferGraphics.setPaint(grad);
			}
		}
		if(paint.getFill()) bufferGraphics.fillOval(x, y, width, height);
		else bufferGraphics.drawOval(x, y, width, height);
		bufferGraphics.setPaint(oldPaint);
		bufferGraphics.rotate(-rotation, x + width/2, y + height/2);
	}

	@Override
	public void drawElipse(Recti bounds) {
		drawElipse(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle){
		if(paint.getFill()) bufferGraphics.fillArc(x, y, width, height, startAngle, arcAngle);
		else bufferGraphics.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawArc(Recti bounds, int startAngle, int arcAngle){
		drawArc(bounds.x, bounds.y, bounds.width, bounds.height, startAngle, arcAngle);
	}


	@Override
	public void drawElipse(Recti bounds, double rotation) {
		drawElipse(bounds.x, bounds.y, bounds.width, bounds.height, rotation);
	}
	
	@Override
	public void preRender(Vector2i size) {
		this.buffer = new BufferedImage(size.x, size.y, BufferedImage.TYPE_INT_ARGB);
		Graphics2D bufferGraphics = buffer.createGraphics();
		if (bufferGraphics != null) {
			if(antiAlias){
				bufferGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				bufferGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			}
			this.bufferGraphics = bufferGraphics;
		}
	}

	@Override
	public boolean postRender(String export) {
		this.mainGraphics.drawImage(buffer, 0, 0, buffer.getWidth(), buffer.getHeight(), null);
		if (export != null) {
			try {
	            return ImageIO.write(buffer, "png", new File(export));
		    } catch (IOException e) {
	            e.printStackTrace();
		    }
		}
		this.bufferGraphics = mainGraphics;
		return false;
	}

}
