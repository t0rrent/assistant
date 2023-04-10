package au.com.cascadesoftware.legacyui.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.glassfish.hk2.api.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.legacyui.hk2.extension.LegacyUIHK2TestInjectorServiceExtension;
import au.com.cascadesoftware.legacyui.model.GUITestChild;
import au.com.cascadesoftware.legacyui.model.GUITestParent;
import au.com.cascadesoftware.testutil.service.MockService;
import jakarta.inject.Inject;

@ExtendWith(LegacyUIHK2TestInjectorServiceExtension.class)
public class TestGUIInjectorServiceIT {

	@Inject
	private Window windowMock;

	@Inject
	private MockService mockService;
	
	@Inject
	private TestGUIInjectorService guiInjectorService;
	
	@Inject
	private ServiceLocator serviceLocator;
	
	@Test
	public void testIntercept() {
		final GUITestChild child = new GUITestChild(windowMock, new Boundary(), mockService);
		guiInjectorService.intercept(GUITestChild.class, child);
		
		final GUITestParent parent = new GUITestParent(windowMock, new Boundary());
		parent.setServiceLocator(serviceLocator);
		
		final GUITestChild resultChild = parent.addAnInjectableChild(GUITestChild.class);
		assertEquals(child, resultChild);
	}
	
	@Test
	public void testNoIntercept() {
		final GUITestChild child = new GUITestChild(windowMock, new Boundary(), mockService);
		
		final GUITestParent parent = new GUITestParent(windowMock, new Boundary());
		parent.setServiceLocator(serviceLocator);
		
		final GUITestChild resultChild = parent.addAnInjectableChild(GUITestChild.class);
		assertNotEquals(child, resultChild);
	}

}
