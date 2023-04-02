package au.com.cascadesoftware.legacyui.service;

import java.util.ConcurrentModificationException;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.glassfish.hk2.api.ServiceLocator;

import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.exception.ClosedWindowException;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine4.service.CriticalLifeCycle;
import jakarta.inject.Inject;

public class UILifeCycle implements CriticalLifeCycle {
	
	private final ScheduledExecutorService scheduledExecutorService;
	private final ServiceLocator serviceLocator;
	private final Window window;
	
	private ScheduledFuture<?> updateSchedule;
	
	private GUI topUI;
	private Vector2i lastSize;
	private String export;
	
	@Inject
	public UILifeCycle(
			final ScheduledExecutorService scheduledExecutorService,
			final ServiceLocator serviceLocator,
			final Window window,
			final Optional<GUI> topUI
	) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.serviceLocator = serviceLocator;
		this.window = window;
		this.topUI = topUI.orElseGet(() ->  new GUI(window, new Boundary()));
	}

	@Override
	public void start() {
		lastSize = window.getSize();
		
		setTopUI(topUI);
		
		window.open();
		window.update();
		
		updateSchedule = scheduledExecutorService.scheduleAtFixedRate(this::updateWindow, 0, 1, TimeUnit.NANOSECONDS);
	}

	public void setTopUI(final GUI newTopUI) {
		if(topUI != null) topUI.close();
		topUI = newTopUI;
		topUI.setServiceLocator(serviceLocator);
		topUI.resize();
	}
	
	private void updateWindow() {
		try{
			Vector2i currentSize = window.getSize();
			if(!lastSize.equals(currentSize)){
				topUI.resize();
				lastSize = new Vector2i(currentSize.x, currentSize.y);
			}
			window.update();
			topUI.update();
			try {
				render();
				window.show();
			} catch (ClosedWindowException e) {
				System.exit(0);
			}
			if(!window.isOpen()) {
				System.exit(0);
			}
		}catch(ConcurrentModificationException e){
		}
	}

	private void render() throws ClosedWindowException {
		Graphics graphics = window.getGraphics();
		if(graphics == null) return;
		graphics.preRender(window.getSize());
		topUI.render(graphics);
		if (graphics.postRender(export)) {
			export = null;
		}
		graphics.dispose();
	}


	@Override
	public void stop() {
		updateSchedule.cancel(true);
	}

	public Window getWindow() {
		return window;
	}

}
