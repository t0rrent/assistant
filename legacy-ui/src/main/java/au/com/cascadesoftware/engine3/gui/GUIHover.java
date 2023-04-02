package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.in.InputHandler;

public abstract class GUIHover extends GUITimer {
	
	private float statePhaseTimeIn, statePhaseTimeOut;
	private float phase;

	protected abstract void onStateChange(float phase);
	
	public GUIHover(Window window, Boundary bounds) {
		super(window, bounds);
		onStateChange(0);
	}
	
	public GUIHover setStatePhaseTime(float time){
		return setStatePhaseTime(time, time);
	}
	
	public GUIHover setStatePhaseTime(float timeIn, float timeOut){
		statePhaseTimeIn = timeIn;
		statePhaseTimeOut = timeOut;
		return this;
	}
	
	@Override
	public void update() {
		hoverStateUpdate();
		super.update();
	}

	private void hoverStateUpdate() {
		InputHandler ih = getWindow().getInput();
		float lastPhase = phase;
		if(getOnScreenBounds().contains(ih.getMousePos())){
			if(statePhaseTimeIn == 0){
				phase = 1;
			}else{
				phase += getDeltaTime() / statePhaseTimeIn;
				if(phase > 1) phase = 1;
			}
		}else{
			if(statePhaseTimeOut == 0){
				phase = 0;
			}else{
				phase -= getDeltaTime() / statePhaseTimeOut;
				if(phase < 0) phase = 0;
			}
		}
		if(phase != lastPhase) onStateChange(phase);
	}

	public void pushUpdate() {
		onStateChange(phase);
	}

	public void setPhase(float phase) {
		this.phase = phase;
	}

}
