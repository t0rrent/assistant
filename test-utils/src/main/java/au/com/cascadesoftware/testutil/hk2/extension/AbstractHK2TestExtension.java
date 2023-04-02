package au.com.cascadesoftware.testutil.hk2.extension;

import java.util.Collection;
import java.util.function.Supplier;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import au.com.cascadesoftware.engine4.hk2.binder.MainBinder;

public abstract class AbstractHK2TestExtension implements BeforeEachCallback, AfterEachCallback {

    private ServiceLocator locator;

	@Override
	public void beforeEach(final ExtensionContext context) throws Exception {
		locator = ServiceLocatorUtilities.bind();
        ServiceLocatorUtilities.bind(locator, new MainBinder(getBinders(), locator));
		ServiceLocatorUtilities.enableImmediateScope(locator);
        locator.inject(context.getTestInstance().get());
	}

	protected abstract Collection<Supplier<Binder>> getBinders();

	@Override
	public void afterEach(final ExtensionContext context) throws Exception {
		locator.shutdown();
	}
}
