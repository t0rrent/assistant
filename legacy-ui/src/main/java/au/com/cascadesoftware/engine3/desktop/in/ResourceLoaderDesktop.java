package au.com.cascadesoftware.engine3.desktop.in;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import au.com.cascadesoftware.engine3.exception.ResourceTypeException;
import au.com.cascadesoftware.engine3.in.ResourceLoader;
import au.com.cascadesoftware.engine3.util.OrderedHashMap;

public class ResourceLoaderDesktop implements ResourceLoader {
	
	private static OrderedHashMap<String, BufferedImage> IMAGES = new OrderedHashMap<String, BufferedImage>();
	private static final int MAX_IMAGE_MEMORY = 512; //in MB
	
	private String resource;
	private Object buffer, original;
	private int width, height;
	private ResourceLoaderDesktop combined;
	
	public ResourceLoaderDesktop(String resource){
		this.resource = resource;
	}

	@Override
	public ResourceLoader change(String resource){
		buffer = null;
		this.resource = resource;
		return this;
	}

	@Override
	public void resize(int w, int h){
		if(width == w && height == h) return;
		if(original == null) loadImage();
		if(!(original instanceof Image)) return;
		width = w;
		height = h;
		new Thread(){
			public void run() {
				Image i = ((Image) original).getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
				BufferedImage b = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			    Graphics2D bGr = b.createGraphics();
			    bGr.drawImage(i, 0, 0, null);
			    bGr.dispose();
			    buffer = b;
			}
		}.start();
	}
	
	public Image loadImage() throws ResourceTypeException {
		if(buffer != null) return (Image) buffer;
		BufferedImage image = null;
		if(IMAGES.containsKey(resource)){
			image = IMAGES.get(resource);
		}else{
			try {
				image = transform(ImageIO.read(getClass().getResourceAsStream("/assets/img/" + resource)));
				if (combined != null) {
					image = combine(image, (BufferedImage) combined.loadImage());
				}
				IMAGES.put(getMapCode(resource) + (combined == null ? "" : combined.getMapCode(combined.resource)), image);
				memoryCheck();
			} catch (IOException | java.lang.IllegalArgumentException e) {}
			if(image == null){
				String err = "for " + "/assets/img/" + resource;
				System.err.println(err);
				throw new ResourceTypeException();
			}
		}
		buffer = image;
		if(original == null) original = image;
		return image;
	}

	private BufferedImage combine(final BufferedImage baseImage, final BufferedImage overlay) {
		final BufferedImage combined = new BufferedImage(baseImage.getWidth(), baseImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D graphics = (Graphics2D) combined.getGraphics();
		graphics.drawImage(baseImage, 0, 0, null);
		graphics.drawImage(overlay, 0, 0, null);
		graphics.dispose();
		return combined;
	}

	protected String getMapCode(final String resource) {
		return resource;
	}

	protected BufferedImage transform(final BufferedImage in) {
		return in;
	}

	private void memoryCheck() {
		int memoryInBytes = 0;
		for(BufferedImage img : IMAGES.values()){
			memoryInBytes += Math.ceil(img.getWidth() * img.getHeight() * img.getColorModel().getPixelSize() / 8.0);
		}
		if(memoryInBytes > MAX_IMAGE_MEMORY * 1024 * 1024){
			IMAGES.remove(IMAGES.getFirst());
			memoryCheck();
		}
	}

	@Override
	public String getLocation() {
		return resource;
	}

	@Override
	public ResourceLoader combine(final ResourceLoader resourceLoader) {
		if (resourceLoader instanceof ResourceLoaderDesktop) {
			combined = (ResourceLoaderDesktop) resourceLoader;
			buffer = null;
		}
		return this;
	}

}
