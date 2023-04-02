package au.com.cascadesoftware.testutil.hk2.binder;

import static org.mockito.Mockito.mock;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import au.com.cascadesoftware.testutil.service.MockService;

public class TestUtilBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(mock(MockService.class)).to(MockService.class);
	}

}
