package au.com.cascadesoftware.legacyui.service;

import au.com.cascadesoftware.legacyui.config.WindowConfig;

public interface WindowConfigService {

	WindowConfig loadMainWindowConfg();

	WindowConfig loadWindowConfg(String location);

}
