package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.graphics.Color;

public class Theme {
	
	public final Color primaryElementColor;
	public final Color secondaryElementColor;
	public final Color background;
	public final Color borderColor;
	public final Color textColor;
	public final Color subTextColor;
	
	public Theme(Color primaryElementColor, Color secondaryElementColor, Color background, Color borderColor, Color textColor, Color subTextColor){
		this.primaryElementColor = primaryElementColor;
		this.secondaryElementColor = secondaryElementColor;
		this.background = background;
		this.borderColor = borderColor;
		this.textColor = textColor;
		this.subTextColor = subTextColor;
	}

	public static final Theme TEST1 = new Theme(new Color(200, 210, 220), Color.YELLOW, Color.WHITE, Color.BLACK, Color.BLACK, Color.GRAY);

}
