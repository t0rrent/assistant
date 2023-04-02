package au.com.cascadesoftware.voice.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.json.service.SimpleJsonService;
import au.com.cascadesoftware.voice.service.SpeechRecognitionStreamProcessor;
import jakarta.inject.Singleton;

public class VoiceTestBinder extends AbstractBinder {
	
	@Override
	protected void configure() {
		bind(new ObjectMapper()).to(ObjectMapper.class);
		bind(SimpleJsonService.class).to(JsonService.class).in(Singleton.class);
		bindAsContract(SpeechRecognitionStreamProcessor.class);
	}

}
