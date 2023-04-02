package au.com.cascadesoftware.engine3.desktop.in;

import java.awt.image.BufferedImage;

public class SpriteLoaderDesktop extends ResourceLoaderDesktop {
	
	private final int spriteIndex, totalSprites;
	
	public SpriteLoaderDesktop(final String resource, final int spriteIndex, final int totalSprites){
		super(resource);
		this.spriteIndex = spriteIndex;
		this.totalSprites = totalSprites;
	}

	@Override
	protected BufferedImage transform(BufferedImage in) {
		final BufferedImage out = in.getSubimage(in.getWidth() * spriteIndex / totalSprites, 0, in.getWidth() / totalSprites, in.getHeight());
		return out;
	}

	@Override
	protected String getMapCode(final String resource) {
		return resource + spriteIndex;
	}

}
