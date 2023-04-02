package au.com.cascadesoftware.engine3;

import java.io.Serializable;

public class Timer implements Serializable {

	private static final long serialVersionUID = -7702361849443521673L;
	
	private final long timeOnInstantiation;
	private long resetTime;
	
	private float deltaTime;
	private long last;

	public Timer(){
		timeOnInstantiation = System.nanoTime();
		resetTime = System.nanoTime();
	}
	
	public void update(){
		long current = System.nanoTime();
		deltaTime = (current - last)/1000000000f;
		if(last == 0) deltaTime = 0;
		last = current;
	}
	
	public float getDeltaTime(){
		return deltaTime;
	}
	
	public float getTimeSinceInstantiated(){
		return (System.nanoTime() - timeOnInstantiation)/1000000000f;
	}
	
	public float getTimeSinceReset(){
		return (System.nanoTime() - resetTime)/1000000000f;
	}

	public void reset() {
		resetTime = System.nanoTime();
		last = 0;
	}
	
}
