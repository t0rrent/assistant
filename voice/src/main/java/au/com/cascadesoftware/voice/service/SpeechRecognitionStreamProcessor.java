package au.com.cascadesoftware.voice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;

import org.vosk.Recognizer;

import au.com.cascadesoftware.engine4.service.LifeCycle;
import au.com.cascadesoftware.json.service.JsonService;
import au.com.cascadesoftware.voice.model.SpeechRecognitionStream;
import jakarta.inject.Inject;

public class SpeechRecognitionStreamProcessor implements LifeCycle {

    private static final int STREAM_PROCESS_PERIOD_MILLIS = 10;
    private static final int CHUNK_SIZE = 2048;

	private final ScheduledExecutorService scheduledExecutorService;
	private final JsonService jsonService;
	
	private final CopyOnWriteArrayList<SpeechRecognitionStream> streams;
	private final ByteArrayOutputStream byteArrayOutputStream;
	private final byte[] buffer = new byte[8192];
	
	private ScheduledFuture<?> processSchedule;
	
	@Inject
	public SpeechRecognitionStreamProcessor(
			final ScheduledExecutorService scheduledExecutorService,
			final JsonService jsonService
	) {
		this.scheduledExecutorService = scheduledExecutorService;
		this.jsonService = jsonService;
		this.streams = new CopyOnWriteArrayList<>();
		this.byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public void start() {
		this.processSchedule = scheduledExecutorService.scheduleAtFixedRate(() -> runAndCatchException(this::processStreams), 0, STREAM_PROCESS_PERIOD_MILLIS, TimeUnit.MILLISECONDS);
	}

	private void runAndCatchException(Runnable object) {
		try {
			object.run();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		processSchedule.cancel(true);
	}
	
	private void processStreams() {
        for (final SpeechRecognitionStream stream : streams) {
        	final AudioInputStream audioInputStream = stream.getAudioInputStream();
        	final Recognizer recognizer = stream.getRecognizer();
	        try {
                final int numBytesRead = audioInputStream.read(buffer, 0, CHUNK_SIZE);
                byteArrayOutputStream.write(buffer, 0, numBytesRead);
                if (recognizer.acceptWaveForm(buffer, numBytesRead)) {
                    stream.writeFull(jsonService.extract(recognizer.getResult(), String.class, "text"));
                } else {
                	stream.writePartial(jsonService.extract(recognizer.getPartialResult(), String.class, "partial"));
                }
	        } catch (final IOException e) {
	        	e.printStackTrace();
	        }
        }
	}

	public void addStream(final SpeechRecognitionStream speechRecognitionStream) {
		streams.add(speechRecognitionStream);
		System.out.println("Starting stream processing for " + speechRecognitionStream);
	}

	public void removeStream(final SpeechRecognitionStream speechRecognitionStream) {
		streams.remove(speechRecognitionStream);
	}

}
