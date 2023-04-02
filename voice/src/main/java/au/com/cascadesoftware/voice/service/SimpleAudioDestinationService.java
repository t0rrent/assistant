package au.com.cascadesoftware.voice.service;

import javax.sound.sampled.SourceDataLine;

import au.com.cascadesoftware.voice.model.AudioDestination;

public class SimpleAudioDestinationService implements AudioDestinationService {

	@Override
	public AudioDestination getDefaultSpeaker() {
		final AudioDestination audioDestination = new AudioDestination();
		audioDestination.setDataLine(SourceDataLine.class);
		return audioDestination;
	}

}
