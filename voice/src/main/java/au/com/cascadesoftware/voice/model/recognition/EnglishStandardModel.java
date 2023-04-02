package au.com.cascadesoftware.voice.model.recognition;

import java.io.IOException;

import org.vosk.Model;

import jakarta.inject.Inject;

public class EnglishStandardModel extends Model {
	
	@Inject
	public EnglishStandardModel(final String location) throws IOException {
		super(location);
	}
	
	//"D:\\vosk-models\\vosk-model-en-us-0.21"
	
}
