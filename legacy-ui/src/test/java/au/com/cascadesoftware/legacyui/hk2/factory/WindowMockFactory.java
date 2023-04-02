package au.com.cascadesoftware.legacyui.hk2.factory;

import static org.mockito.Mockito.mock;

import org.glassfish.hk2.api.Factory;

import au.com.cascadesoftware.engine3.display.Window;

public class WindowMockFactory implements Factory<Window> {

	@Override
	public Window provide() {
		return mock(Window.class);
	}

	@Override
	public void dispose(Window instance) {
		
	}

}
