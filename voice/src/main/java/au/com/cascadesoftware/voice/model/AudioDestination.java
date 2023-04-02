package au.com.cascadesoftware.voice.model;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class AudioDestination extends AbstractAudioIO<SourceDataLine> {

	private SourceDataLine dataLine;
	
	private boolean opened;

	public SourceDataLine getDataLine() {
		return dataLine;
	}

	public void setDataLine(final Class<SourceDataLine> type) {
		try {
			this.dataLine = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(type, getFormat()));
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