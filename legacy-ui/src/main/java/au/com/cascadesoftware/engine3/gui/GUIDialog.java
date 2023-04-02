package au.com.cascadesoftware.engine3.gui;

import java.util.ArrayList;
import java.util.List;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.legacyui.config.WindowConfig;

public abstract class GUIDialog extends GUI {
	
	protected GUI topUI;
	protected Window dialog;
	boolean launchedDialog;

	/*public GUIDialog(Window window, Boundary bounds, String parameters, Runnable onCloseRequest) {
		super(window, bounds);
		dialog = window.createDialog(parameters, onCloseRequest);
		topUI = new GUI(dialog, new Boundary());
	}

	public GUIDialog(Window window, Boundary bounds, String parameters) {
		super(window, bounds);
		dialog = window.createDialog(parameters);
		topUI = new GUI(dialog, new Boundary());
	}*/
	
	public GUIDialog(final Window window, final Boundary bounds, final WindowConfig windowConfig) {
		super(window, bounds);
		dialog = window.createDialog(windowConfig);
		topUI = new GUI(dialog, new Boundary());
	}

	protected abstract void launchDialog();

	protected abstract void closeDialog();
	
	@Override
	protected void updateInput() {
		if(!launchedDialog){
			resize();
			launchDialog();
			launchedDialog = true;
		}
	}
	
	@Override
	public GUI setBackground(Color bgColor) {
		return this;
	}

	@Override
	protected void dispose() {
		topUI.close();
		closeDialog();
	}
	
	@Override
	public void addGUI(GUI gui) {
		topUI.addGUI(gui);
	}
	
	public Window getDialog(){
		return dialog;
	}

	protected void onClosed() {
		
	}
	
	@Override
	public void resize() {
		topUI.resize();
	}
	
	@Override
	public void clear() {
		topUI.clear();
	}
	
	public GUI[] getAllChildren() {
		List<GUI> out = new ArrayList<GUI>();
		//out.add(topUI);
		for(GUI g2 : topUI.getAllChildren()) out.add(g2);
		return out.toArray(new GUI[out.size()]);
	}
	
}
