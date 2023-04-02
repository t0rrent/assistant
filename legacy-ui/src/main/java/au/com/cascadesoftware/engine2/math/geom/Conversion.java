package au.com.cascadesoftware.engine2.math.geom;

import au.com.cascadesoftware.engine2.math.Linei;
import au.com.cascadesoftware.engine2.math.Polygon;

public class Conversion {

	public static Linei[] getLines(Polygon outline) {
		Linei[] out = new Linei[outline.npoints];
		for(int i = 0; i < outline.npoints - 1; i++){
			out[i] = new Linei(outline.xpoints[i], outline.ypoints[i], outline.xpoints[i + 1], outline.ypoints[i + 1]);
		}
		out[outline.npoints - 1] = new Linei(outline.xpoints[outline.npoints - 1], outline.ypoints[outline.npoints - 1], outline.xpoints[0], outline.ypoints[0]);
		return out;
	}

}
