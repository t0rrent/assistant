package au.com.cascadesoftware.legacyui;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.glassfish.hk2.api.MultiException;
import org.glassfish.hk2.api.ServiceLocator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.legacyui.hk2.extension.LegacyUIHK2TestExtension;
import au.com.cascadesoftware.legacyui.model.GUITestChild;
import au.com.cascadesoftware.legacyui.model.GUITestChildFail;
import au.com.cascadesoftware.legacyui.model.GUITestParent;
import au.com.cascadesoftware.testutil.service.MockService;
import jakarta.inject.Inject;

@ExtendWith(LegacyUIHK2TestExtension.class)
public class GuiIT {

	@Inject
	private Window windowMock;

	@Inject
	private MockService mockService;
	
	@Inject
	private ServiceLocator serviceLocator;

	@Test
	public void testSeviceBinding() {
		final GUITestParent parent = new GUITestParent(windowMock, new Boundary());
		parent.setServiceLocator(serviceLocator);
		parent.addAnInjectableChild(GUITestChild.class);
		verify(mockService, times(1)).exampleCall();
	}

	@Test
	public void testSeviceBindingFail() {
		final GUITestParent parent = new GUITestParent(windowMock, new Boundary());
		parent.setServiceLocator(serviceLocator);
		assertThrows(MultiException.class, () -> parent.addAnInjectableChild(GUITestChildFail.class));
		verify(mockService, times(0)).exampleCall();
	}
	
}
