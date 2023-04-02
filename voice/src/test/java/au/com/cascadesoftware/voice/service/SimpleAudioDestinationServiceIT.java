package au.com.cascadesoftware.voice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SimpleAudioDestinationServiceIT {
	
	@Test
	public void getDefaultMicrophoneTest() {
		final SimpleAudioDestinationService simpleAudioSourceService = new SimpleAudioDestinationService();
		assertNotNull(simpleAudioSourceService.getDefaultSpeaker());
	}

}
