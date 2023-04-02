package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.Timer;
import au.com.cascadesoftware.engine3.display.Window;

public class GUITimer extends GUI {
	
	private Timer timer;

	public GUITimer(Window window, Boundary bounds) {
		super(window, bounds);
		timer = new Timer();
	}
	
	@Override
	public void update() {
		timer.update();
		super.update();
	}
	
	protected float getDeltaTime(){
		return timer.getDeltaTime();
	}
	
	public float getTimeSinceInstantiated(){
		return timer.getTimeSinceInstantiated();
	}

}
