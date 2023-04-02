package au.com.cascadesoftware.voice.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SimpleAudioSourceServiceIT {
	
	@Test
	public void getDefaultMicrophoneTest() {
		final SimpleAudioSourceService simpleAudioSourceService = new SimpleAudioSourceService();
		assertNotNull(simpleAudioSourceService.getDefaultMicrophone());
	}

}
