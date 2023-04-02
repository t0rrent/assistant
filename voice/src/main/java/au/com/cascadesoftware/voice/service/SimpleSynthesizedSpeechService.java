package au.com.cascadesoftware.voice.service;

import au.com.cascadesoftware.voice.model.AudioDestination;
import jakarta.inject.Inject;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class SimpleSynthesizedSpeechService implements SynthesizedSpeechService {
	
	private final VoiceManager voiceManager;
	
	@Inject
	public SimpleSynthesizedSpeechService(
			final VoiceManager voiceManager
	) {
		System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
		this.voiceManager = voiceManager;
	}

	@Override
	public void writeText(final String text, final AudioDestination audioDestination) {
		final Voice voice = voiceManager.getVoice("kevin16");
		voice.allocate();
		voice.speak(text);
		voice.deallocate();
	}

}
