package au.com.cascadesoftware.voice.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.vosk.Recognizer;

import au.com.cascadesoftware.voice.hk2.extension.VoiceHK2TestExtension;
import au.com.cascadesoftware.voice.model.SpeechRecognitionStream;
import jakarta.inject.Inject;

@ExtendWith(VoiceHK2TestExtension.class)
public class SpeechRecognitionStreamProcessorIT {
	
	@Inject
	private SpeechRecognitionStreamProcessor speechRecognitionStreamProcessor;
	
	private AudioInputStream mockAudioInputStream;
	private Recognizer mockRecognizer;
	private SpeechRecognitionStream mockStream;
	
	@BeforeEach
	public void beforeEach() throws IOException {
		speechRecognitionStreamProcessor.start();
		
		mockAudioInputStream = mock(AudioInputStream.class);
		when(mockAudioInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(200);
		
		mockRecognizer = mock(Recognizer.class);
		
		mockStream = mock(SpeechRecognitionStream.class);
		
		when(mockStream.getAudioInputStream()).thenReturn(mockAudioInputStream);
		when(mockStream.getRecognizer()).thenReturn(mockRecognizer);
		
		speechRecognitionStreamProcessor.addStream(mockStream);
	}
	
	@AfterEach
	public void afterEach() {
		speechRecognitionStreamProcessor.stop();
	}
	
	@Test
	public void testRecognizerAcceptWaveForm() {
		verify(mockRecognizer, timeout(2000).atLeast(5)).acceptWaveForm(any(byte[].class), anyInt());
	}
	
	@Test
	public void testGetResult() {
		when(mockRecognizer.acceptWaveForm(any(byte[].class), anyInt())).thenReturn(true);
		verify(mockRecognizer, timeout(2000).atLeast(5)).getResult();
		
	}
	
	@Test
	public void testWriteResult() {
		when(mockRecognizer.acceptWaveForm(any(byte[].class), anyInt())).thenReturn(true);
		when(mockRecognizer.getResult()).thenReturn("{\"text\": \"test1\"}");
		verify(mockStream, timeout(2000).atLeast(5)).writeFull("test1");
	}
	
	@Test
	public void testGetPartial() {
		when(mockRecognizer.acceptWaveForm(any(byte[].class), anyInt())).thenReturn(false);
		verify(mockRecognizer, timeout(2000).atLeast(5)).getPartialResult();
	}
	
	@Test
	public void testWritePartial() {
		when(mockRecognizer.acceptWaveForm(any(byte[].class), anyInt())).thenReturn(false);
		when(mockRecognizer.getPartialResult()).thenReturn("{\"partial\": \"test2\"}");
		verify(mockStream, timeout(2000).atLeast(5)).writePartial("test2");
	}
	
	@Test
	public void testRemoveStream() {
		speechRecognitionStreamProcessor.removeStream(mockStream);
		verify(mockRecognizer, after(2000).never()).acceptWaveForm(any(byte[].class), anyInt());
	}

}
