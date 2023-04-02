package au.com.cascadesoftware.voice.model;

import javax.sound.sampled.AudioFormat;

public abstract class AbstractAudioIO<T> {

	public static final int AUDIO_FRAME_RATE = 44100;
	
	private AudioFormat format = getDefaultFormat();

	public AudioFormat getFormat() {
		return format;
	}

	public void setFormat(final AudioFormat format) {
		this.format = format;
	}

	private static AudioFormat getDefaultFormat() {
		return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 60000, 16, 2, 4, AUDIO_FRAME_RATE, false);
	}
	
	public abstract T getDataLine();

	public abstract void setDataLine(Class<T> type);
	
}
