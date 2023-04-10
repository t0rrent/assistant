package au.com.cascadesoftware.legacyui.hk2.extension;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.glassfish.hk2.utilities.Binder;

import au.com.cascadesoftware.legacyui.hk2.binder.LegacyUITestInjectorServiceBinder;
import au.com.cascadesoftware.testutil.hk2.binder.TestUtilBinder;
import au.com.cascadesoftware.testutil.hk2.extension.AbstractHK2TestExtension;

public class LegacyUIHK2TestInjectorServiceExtension extends AbstractHK2TestExtension {

	@Override
	protected Collection<Supplier<Binder>> getBinders() {
		return Arrays.asList(
				LegacyUITestInjectorServiceBinder::new,
				TestUtilBinder::new
		);
	}

}
