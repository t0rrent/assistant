package au.com.cascadesoftware.legacyui.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.glassfish.hk2.api.MultiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.GUIText;
import au.com.cascadesoftware.legacyui.hk2.extension.LegacyUIHK2TestExtension;
import jakarta.inject.Inject;

@ExtendWith(LegacyUIHK2TestExtension.class)
public class SimpleGUIInjectorServiceIT {
	
	@Inject
	private GUIInjectorService simpleGUIInjectorService;
	
	@Test
	public void testInjection() {
		final GUI test = simpleGUIInjectorService.createInjectable(GUI.class);
		assertNotNull(test);
	}
	
	@Test
	public void testUninjectable() {
		assertThrows(MultiException.class, () -> simpleGUIInjectorService.createInjectable(GUIText.class));
	}

}
