package au.com.cascadesoftware.voice.service;

import au.com.cascadesoftware.voice.model.AudioSource;
import au.com.cascadesoftware.voice.model.SpeechRecognitionStream;

public interface SpeechRecognitionService {

	SpeechRecognitionStream createSpeechRecognitionStream(AudioSource source);

}
