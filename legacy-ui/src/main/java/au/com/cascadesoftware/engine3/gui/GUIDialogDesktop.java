package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2i;
//import au.com.cascadesoftware.engine3.Platform;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.exception.ClosedWindowException;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.legacyui.config.WindowConfig;


//@NativeUI(platform = Platform.DESKTOP)
public class GUIDialogDesktop extends GUIDialog {

	private String export;

	/*public GUIDialogDesktop(Window window, Boundary bounds, String parameters, Runnable onCloseRequest) {
		super(window, bounds, parameters, onCloseRequest);
	}

	//@Deprecated
	public GUIDialogDesktop(Window window, Boundary bounds, String parameters) {
		super(window, bounds, parameters);
	}*/
	
	public GUIDialogDesktop(final Window window, final Boundary bounds, final WindowConfig windowConfig) {
		super(window, bounds, windowConfig);
	}

	@Override
	protected void launchDialog() {
		new Thread(){
			private boolean running;
			
			private void render() throws ClosedWindowException {
				Graphics graphics = dialog.getGraphics();
				if(graphics == null) return;
				graphics.preRender(dialog.getSize());
				topUI.render(graphics);
				if (graphics.postRender(export)) {
					export = null;
				}
				graphics.dispose();
			}
			
			public void run() {
				Recti bounds = GUIDialogDesktop.this.getRenderBounds();
				if(bounds.width == 0 || bounds.height == 0) dialog.setBounds(bounds.translate(GUIDialogDesktop.this.getWindow().getPosition()));
				dialog.open();
				running = true;
				Vector2i lastSize = dialog.getSize();
				topUI.resize();
				while(running){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					dialog.update();
					Vector2i currentSize = dialog.getSize();
					if(!lastSize.equals(currentSize)){
						topUI.resize();
						lastSize = new Vector2i(currentSize.x, currentSize.y);
					}
					topUI.update();
					//System.out.println(window.isOpen());
					try {
						render();
						dialog.show();
					} catch (ClosedWindowException e) {}
					if(!dialog.isOpen()) running = false;
				}
				topUI.close();
				onClosed();
			}			
		}.start();
	}

	@Override
	protected void onClosed() {}

	@Override
	protected void closeDialog() {
		dialog.close();
	}
	
	public void export(String exportLocation) {
		this.export = exportLocation;
	}
	
}
