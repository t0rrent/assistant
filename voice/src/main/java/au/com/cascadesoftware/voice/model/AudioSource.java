package au.com.cascadesoftware.voice.model;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class AudioSource extends AbstractAudioIO<TargetDataLine> {

	private TargetDataLine dataLine;
	
	private boolean opened;

	public TargetDataLine getDataLine() {
		return dataLine;
	}

	public void setDataLine(final Class<TargetDataLine> type) {
		try {
			this.dataLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(type, getFormat()));
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void open() {
		if (!opened) {
			try {
				dataLine.open(getFormat());
				dataLine.start();
				opened = true;
			} catch (final LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		if (opened) {
			dataLine.close();
			opened = true;
		}
	}

}
