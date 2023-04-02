package au.com.cascadesoftware.voice.service;

import javax.sound.sampled.AudioInputStream;

import org.vosk.Model;

import au.com.cascadesoftware.voice.model.AudioSource;
import au.com.cascadesoftware.voice.model.SpeechRecognitionStream;
import jakarta.inject.Inject;

public class SimpleSpeechRecognitionService implements SpeechRecognitionService {
	
	private final Model speechModel;
	private final SpeechRecognitionStreamProcessor speechRecognitionStreamProcessor;

	@Inject
	public SimpleSpeechRecognitionService(
			final Model speechModel,
			final SpeechRecognitionStreamProcessor speechRecognitionStreamProcessor
	) {
		this.speechModel = speechModel;
		this.speechRecognitionStreamProcessor = speechRecognitionStreamProcessor;
	}
	
	@Override
	public SpeechRecognitionStream createSpeechRecognitionStream(final AudioSource source) {
		final SpeechRecognitionStream newSpeechRecognitionStream = new SpeechRecognitionStream(
				new AudioInputStream(source.getDataLine()), 
				speechModel,
				speechRecognitionStreamProcessor::removeStream
		);
		newSpeechRecognitionStream.load();
		speechRecognitionStreamProcessor.addStream(newSpeechRecognitionStream);
		return newSpeechRecognitionStream;
	}

}
