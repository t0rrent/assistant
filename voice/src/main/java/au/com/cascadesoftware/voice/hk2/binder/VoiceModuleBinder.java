package au.com.cascadesoftware.voice.hk2.binder;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.vosk.Model;

import com.sun.speech.freetts.VoiceManager;

import au.com.cascadesoftware.engine4.service.LifeCycle;
import au.com.cascadesoftware.voice.hk2.factory.EnglishStandardModelFactory;
import au.com.cascadesoftware.voice.hk2.factory.VoskConfigFactory;
import au.com.cascadesoftware.voice.model.VoskConfig;
import au.com.cascadesoftware.voice.service.AudioDestinationService;
import au.com.cascadesoftware.voice.service.AudioSourceService;
import au.com.cascadesoftware.voice.service.SimpleAudioDestinationService;
import au.com.cascadesoftware.voice.service.SimpleAudioSourceService;
import au.com.cascadesoftware.voice.service.SimpleSpeechRecognitionService;
import au.com.cascadesoftware.voice.service.SimpleSynthesizedSpeechService;
import au.com.cascadesoftware.voice.service.SpeechRecognitionService;
import au.com.cascadesoftware.voice.service.SpeechRecognitionStreamProcessor;
import au.com.cascadesoftware.voice.service.SynthesizedSpeechService;
import jakarta.inject.Singleton;

public class VoiceModuleBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bindFactory(VoskConfigFactory.class).to(VoskConfig.class).in(Singleton.class);
		bindFactory(EnglishStandardModelFactory.class).to(Model.class).in(Singleton.class);

		bind(VoiceManager.getInstance()).to(VoiceManager.class);
		
		bindAsContract(SpeechRecognitionStreamProcessor.class).to(LifeCycle.class).in(Singleton.class);
		
		bind(SimpleAudioSourceService.class).to(AudioSourceService.class).in(Singleton.class);
		bind(SimpleAudioDestinationService.class).to(AudioDestinationService.class).in(Singleton.class);
		bind(SimpleSpeechRecognitionService.class).to(SpeechRecognitionService.class).in(Singleton.class);
		bind(SimpleSynthesizedSpeechService.class).to(SynthesizedSpeechService.class).in(Singleton.class);
	}

}
