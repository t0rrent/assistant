package au.com.cascadesoftware.voice.hk2.extension;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

import org.glassfish.hk2.utilities.Binder;

import au.com.cascadesoftware.config.hk2.binder.ConfigModuleBinder;
import au.com.cascadesoftware.testutil.hk2.extension.AbstractHK2TestExtension;
import au.com.cascadesoftware.voice.hk2.binder.VoiceTestBinder;

public class VoiceHK2TestExtension extends AbstractHK2TestExtension {

	@Override
	protected Collection<Supplier<Binder>> getBinders() {
		return Arrays.asList(
				VoiceTestBinder::new,
				ConfigModuleBinder::new
		);
	}

}
