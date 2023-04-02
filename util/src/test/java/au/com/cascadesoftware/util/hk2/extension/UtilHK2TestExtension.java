package au.com.cascadesoftware.util.hk2.extension;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.glassfish.hk2.utilities.Binder;

import au.com.cascadesoftware.testutil.hk2.extension.AbstractHK2TestExtension;
import au.com.cascadesoftware.util.hk2.binder.UtilTestBinder;

public class UtilHK2TestExtension extends AbstractHK2TestExtension {

	@Override
	protected Collection<Supplier<Binder>> getBinders() {
		return Arrays.asList(UtilTestBinder::new);
	}

}
