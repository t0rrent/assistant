package au.com.cascadesoftware.assistant.service;

import static au.com.cascadesoftware.openai.model.Message.ROLE_USER;

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
	
	private final AudioSourceService audioSourceService;
	private final SpeechRecognitionService speechRecognitionService;
	private final ScheduledExecutorService scheduledExecutorService;
	private final GptQueryService gptQueryService;
	private final TextboxService textboxService;
	
	private AudioSource microphone;
	private SpeechRecognitionStream stream;
	private ScheduledFuture<?> handleStreamSchedule;
	private BufferedConversation conversation;
	
	@Inject
	public ConversationLifeCycle(
			final AudioSourceService audioSourceService,
			final SpeechRecognitionService speechRecognitionService,
			final ScheduledExecutorService scheduledExecutorService,
			final GptQueryService gptQueryService,
			final TextboxService textboxService
	) {
		this.audioSourceService = audioSourceService;
		this.speechRecognitionService = speechRecognitionService;
		this.scheduledExecutorService = scheduledExecutorService;
		this.gptQueryService = gptQueryService;
		this.textboxService = textboxService;
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
		final Character c = stream.read();
		if (c != null) {
			if (c == '\b') {
				textboxService.backspace();
			} else if (c != '\n') {
				textboxService.append(c);
			}
		}
	}
	
	public void send() {
		conversation.addMessage(ROLE_USER, textboxService.getContent());
		textboxService.clear();
		gptQueryService.message(conversation);
	}

}
