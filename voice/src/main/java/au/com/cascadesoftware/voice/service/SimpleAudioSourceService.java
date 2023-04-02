package au.com.cascadesoftware.voice.service;

import javax.sound.sampled.TargetDataLine;

import au.com.cascadesoftware.voice.model.AudioSource;

public class SimpleAudioSourceService implements AudioSourceService {

	public AudioSource getDefaultMicrophone() {
		final AudioSource audioSource = new AudioSource();
		audioSource.setDataLine(TargetDataLine.class);
		return audioSource;
	}
	
}
