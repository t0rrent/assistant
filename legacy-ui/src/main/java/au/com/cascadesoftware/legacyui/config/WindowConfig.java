package au.com.cascadesoftware.legacyui.config;

import au.com.cascadesoftware.util.annotation.NonNull;

public class WindowConfig {

	public static String MAIN_WINDOW_CONFIG = "main-window.config";

	@NonNull
	public Integer width;
	
	@NonNull
	public Integer height;
	
	public boolean antiAliasing = true;
	
	public String icon;

	public boolean resizable = true;

	@NonNull
	public String title;

	public boolean startMaximized = true;

}
