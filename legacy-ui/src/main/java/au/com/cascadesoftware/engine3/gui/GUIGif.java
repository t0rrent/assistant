package au.com.cascadesoftware.engine3.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.com.cascadesoftware.engine3.desktop.graphics.GraphicsDesktop;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Graphics;

public class GUIGif extends GUITimer {

	private List<BufferedImage> images;
	
	private int gifIndex;

	public GUIGif(final Window window, final Boundary bounds, final String resource) {
		super(window, bounds);
		try {
			this.images = getFrames(resource);
		} catch (final IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void draw(final Graphics graphics) {
		final GraphicsDesktop graphicsDesktop = (GraphicsDesktop) graphics;
		graphicsDesktop.getNativeGraphics().drawImage(
				images.get(gifIndex),
				getRenderBounds().x, 
				getRenderBounds().y,
				getRenderBounds().width,
				getRenderBounds().height,
				null
		);
	}
	
	@Override
	public void updateInput() {
		gifIndex = ((int) (getTimeSinceInstantiated() * 100)) % images.size();
	}
	
	private List<BufferedImage> getFrames(final String resource) throws IOException, URISyntaxException {
		final List<BufferedImage> out = new ArrayList<>();
		try {
		    String[] imageatt = new String[]{
		            "imageLeftPosition",
		            "imageTopPosition",
		            "imageWidth",
		            "imageHeight"
		    };    
		    
		    
		    ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream ciis = ImageIO.createImageInputStream(getClass().getResourceAsStream("/assets/img/" + resource));
		    reader.setInput(ciis, false);

		    int noi = reader.getNumImages(true);
		    BufferedImage master = null;

		    for (int i = 0; i < noi; i++) { 
		        BufferedImage image = reader.read(i);
		        IIOMetadata metadata = reader.getImageMetadata(i);

		        Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
		        NodeList children = tree.getChildNodes();

		        for (int j = 0; j < children.getLength(); j++) {
		            Node nodeItem = children.item(j);

		            if(nodeItem.getNodeName().equals("ImageDescriptor")){
		                Map<String, Integer> imageAttr = new HashMap<String, Integer>();

		                for (int k = 0; k < imageatt.length; k++) {
		                    NamedNodeMap attr = nodeItem.getAttributes();
		                    Node attnode = attr.getNamedItem(imageatt[k]);
		                    imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
		                }
		                if(i==0){
		                    master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
		                }
		                master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
		            }
		        }
		        try {
			        BufferedImage frame = new BufferedImage(master.getWidth(), master.getHeight(), master.getType());
			        Graphics2D g = (Graphics2D) frame.createGraphics();
			        g.drawImage(master, 0, 0, null);
			        g.dispose();
			        out.add(frame);
		        }catch(RuntimeException e ) {
		        	e.printStackTrace();
		        }
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return out;
	}

}
