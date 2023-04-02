package au.com.cascadesoftware.engine3.gui;

public enum Alignment {
	
	TOP_LEFT(0,0), TOP_CENTER(1,0), TOP_RIGHT(2,0),
	MIDDLE_LEFT(0,1), MIDDLE_CENTER(1,1), MIDDLE_RIGHT(2,1),
	BOTTOM_LEFT(0,2), BOTTOM_CENTER(1,2), BOTTOM_RIGHT(2,2);

	public boolean TOP,MIDDLE,BOTTOM,LEFT,CENTER,RIGHT;
	public int x, y;
	
	private Alignment(int x, int y){
		this.x = x;
		this.y = y;
		if(x == 0) LEFT = true;
		if(x == 1) CENTER = true;
		if(x == 2) RIGHT = true;
		if(y == 2) BOTTOM = true;
		if(y == 1) MIDDLE = true;
		if(y == 0) TOP = true;
	}
	
}
