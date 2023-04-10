package au.com.cascadesoftware.legacyui.service;

public interface GUIInjectorService {

	<C> C createInjectable(Class<C> type);

}
