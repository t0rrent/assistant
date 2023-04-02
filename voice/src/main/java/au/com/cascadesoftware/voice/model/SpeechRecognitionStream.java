package au.com.cascadesoftware.voice.model;

import java.io.IOException;
import java.util.function.Consumer;

import javax.sound.sampled.AudioInputStream;

import org.vosk.Model;
import org.vosk.Recognizer;

import au.com.cascadesoftware.util.StringProcessing;

public class SpeechRecognitionStream {
	
	private final AudioInputStream audioInputStream;
	private final Consumer<SpeechRecognitionStream> closeHandler;
	private final Model speechModel;
	private Recognizer recognizer;
	
	private StringBuffer buffer;
	private String partial;
	
	public SpeechRecognitionStream(
			final AudioInputStream audioInputStream,
			final Model speechModel,
			final Consumer<SpeechRecognitionStream> closeHandler
	) {
		this.audioInputStream = audioInputStream;
		this.closeHandler = closeHandler;
		this.speechModel = speechModel;
		buffer = new StringBuffer();
		partial = "";
	}
	
	public void load() {
		try {
			this.recognizer = new Recognizer(speechModel, 120000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writePartial(final String newPartial) {
		if (newPartial.startsWith(this.partial)) {
			buffer.append(newPartial.substring(this.partial.length()));
		} else {
			final int commonPrefixLength = StringProcessing.commonPrefixLength(newPartial, this.partial);
			for (int i = 0; i < this.partial.length() - commonPrefixLength; i++) {
				buffer.append('\b');
			}
			buffer.append(newPartial.substring(commonPrefixLength));
		}
		this.partial = newPartial;
	}
	
	public void writeFull(final String full) {
		if (this.partial.length() == 0) {
			return;
		} else {
			writePartial(full + (full.length() > 0 ? '\n' : ""));
			this.partial = "";
		}
	}

	public Character read() {
		if (buffer.length() > 0) {
			final char out = buffer.charAt(0);
			buffer.deleteCharAt(0);
			return out;
		} else {
			return null;
		}
	}

	public void read(final Consumer<Character> handler) {
		final Character character = read();
		if (character != null) {
			handler.accept(character);
		}
	}
	
	public void close() {
		closeHandler.accept(this);
        recognizer.close();
	}

	public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}

	public Recognizer getRecognizer() {
		return recognizer;
	}

}
