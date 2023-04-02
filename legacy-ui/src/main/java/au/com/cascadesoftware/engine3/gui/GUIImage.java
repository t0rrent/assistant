package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.in.ResourceLoader;

public class GUIImage extends GUI {
	
	private ResourceLoader img;
	private String web;
	private double rotation;

	public GUIImage(Window window, Boundary bounds, ResourceLoader image) {
		super(window, bounds);
		img = image;
		onResize();
	}
	
	@Override
	protected void draw(Graphics graphics) {
		graphics.rotate(rotation, getRenderBounds().center());
		graphics.drawImage(img, getRenderBounds());
		graphics.rotate(-rotation, getRenderBounds().center());
	}

	public GUI setRotation(double rotation) {
		this.rotation = rotation;
		return this;
	}

	public GUI setWebImage(String web) {
		this.web = web;
		return this;
	}
	
	public String getResourceLocation(){
		return img.getLocation();
	}

	protected String[] getHTML() {
		return new String[]{"<img src=\"/" + web + "\"></img>"};
	}
	
	@Override
	protected void onResize() {
		img.resize(getRenderBounds().width, getRenderBounds().height);
	}

}
