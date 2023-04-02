package au.com.cascadesoftware.voice.hk2.factory;

import java.io.IOException;

import org.glassfish.hk2.api.Factory;

import au.com.cascadesoftware.voice.model.VoskConfig;
import au.com.cascadesoftware.voice.model.recognition.EnglishStandardModel;
import jakarta.inject.Inject;

public class EnglishStandardModelFactory implements Factory<EnglishStandardModel> {
	
	private final VoskConfig voskConfig;
	
	@Inject
	public EnglishStandardModelFactory(final VoskConfig voskConfig) {
		this.voskConfig = voskConfig;
	}

	@Override
	public EnglishStandardModel provide() {
		try {
			return new EnglishStandardModel(voskConfig.getEnglishModelLocation());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void dispose(final EnglishStandardModel instance) {
		instance.close();
	}

}
