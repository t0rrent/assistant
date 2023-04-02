package au.com.cascadesoftware.engine3.display;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.exception.ClosedWindowException;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.ResourceLoader;
import au.com.cascadesoftware.legacyui.config.WindowConfig;

public interface Window {
	
	void update();
	
	void open();

	void close();

	void show();
	
	void setCursor(Cursor cursor);
	
	void setBounds(Recti bounds);

	void addGLENScriptsFromResource(String[] filenames);
	
	boolean isOpen();

	boolean isMaximized();
	
	void setMaximized(boolean fullscreen);
	
	Cursor getCursor();
	
	String getChooseFile(String title);

	String getChooseFile(String title, String[] fileTypes);

	String getChooseFile(String string, String[] fileTypes, boolean save);
	
	Graphics getGraphics() throws ClosedWindowException;
	
	Vector2i getSize();

	InputHandler getInput();

	ResourceLoader getNewResourceLoader();
	
	//@Deprecated
	//Window createDialog(String dialogParameters);

	//Window createDialog(String dialogParameters, Runnable onCloseAttempt);
	
	Window createDialog(WindowConfig config);
	
	Vector2i getPosition();

	String[] getGLENScripts();
	
	boolean isServer();
	
	int getServerPort();
	
	int getServerWSPort();

	boolean isOnline();
	
	public enum Cursor {
		DEFAULT, TEXT, HAND_OPEN, HAND_GRABBING, DOUBLE_ARROW_VERTICAL, DOUBLE_ARROW_HORIZONTAL, QUAD_ARROW, TEXT_BLACK, CROSSHAIR
	}

	void focus();

	String getChooseFolder(String title);

	ResourceLoader getNewSpriteLoader(int index, int maxSprites);
	
}
