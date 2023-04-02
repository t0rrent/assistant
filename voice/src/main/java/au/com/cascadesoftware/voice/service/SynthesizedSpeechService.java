package au.com.cascadesoftware.voice.service;

import au.com.cascadesoftware.voice.model.AudioDestination;

public interface SynthesizedSpeechService {

	void writeText(String text, AudioDestination audioDestination);

}
