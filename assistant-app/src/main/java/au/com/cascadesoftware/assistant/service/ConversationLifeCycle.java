package au.com.cascadesoftware.assistant.service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import au.com.cascadesoftware.assistant.BufferedConversation;
import au.com.cascadesoftware.engine4.service.LifeCycle;
import au.com.cascadesoftware.openai.model.Conversation;
import au.com.cascadesoftware.openai.service.GptQueryService;
import au.com.cascadesoftware.voice.model.AudioSource;
import au.com.cascadesoftware.voice.model.SpeechRecognitionStream;
import au.com.cascadesoftware.voice.service.AudioSourceService;
import au.com.cascadesoftware.voice.service.SpeechRecognitionService;
import jakarta.inject.Inject;

public class ConversationLifeCycle implements LifeCycle {

	public static final String ROLE_USER = "user";
	public static final String ROLE_ASSISTANT = "assistant";
	
	private final AudioSourceService audioSourceService;
	private final SpeechRecognitionService speechRecognitionService;
	private final ScheduledExecutorService scheduledExecutorService;
	private final GptQueryService gptQueryService;

	private final StringBuilder lineBuilder;
	
	private AudioSource microphone;
	private SpeechRecognitionStream stream;
	private ScheduledFuture<?> handleStreamSchedule;
	private BufferedConversation conversation;
	
	@Inject
	public ConversationLifeCycle(
			final AudioSourceService audioSourceService,
			final SpeechRecognitionService speechRecognitionService,
			final ScheduledExecutorService scheduledExecutorService,
			final GptQueryService gptQueryService
	) {
		this.audioSourceService = audioSourceService;
		this.speechRecognitionService = speechRecognitionService;
		this.scheduledExecutorService = scheduledExecutorService;
		this.gptQueryService = gptQueryService;
		lineBuilder = new StringBuilder();
	}

	@Override
	public void start() {
		newConversation();
		microphone = audioSourceService.getDefaultMicrophone();
		microphone.open();
		stream = speechRecognitionService.createSpeechRecognitionStream(microphone);
		handleStreamSchedule = scheduledExecutorService.scheduleAtFixedRate(this::handleUserInputStream, 0, 1, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop() {
		handleStreamSchedule.cancel(true);
		stream.close();
		microphone.close();
	}

	public void newConversation() {
		conversation = new BufferedConversation();
	}
	
	public Conversation getConversation() {
		return this.conversation;
	}
	
	private void handleUserInputStream() {
		try {
		final Character c = stream.read();
		if (c == null) {
			return;
		}
		if (c == '\n') {
			finishUserInput();
		} else if (c == '\b') {
			lineBuilder.setLength(lineBuilder.length() - 1);
			updateUserBuffer();
		} else {
			lineBuilder.append(c);
			updateUserBuffer();
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUserBuffer() {
		conversation.setBuffer(ROLE_USER, lineBuilder.toString());
	}

	private void finishUserInput() {
		conversation.pushBuffer(ROLE_USER);
		lineBuilder.setLength(0);
		gptQueryService.message(conversation);
	}

}
