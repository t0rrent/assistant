package au.com.cascadesoftware.assistant.ui;

import static au.com.cascadesoftware.openai.model.Message.ROLE_ASSISTANT;
import static au.com.cascadesoftware.openai.model.Message.ROLE_USER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import au.com.cascadesoftware.assistant.service.ConversationLifeCycle;
import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.gui.Alignment;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.Scalar;
import au.com.cascadesoftware.openai.model.Conversation;
import au.com.cascadesoftware.openai.model.Message;
import jakarta.inject.Inject;
public class GUIChatbox extends GUI {
	
	private final int MESSAGE_PADDING = 20;
	
	private final int MESSAGE_WIDTH = 500;

	private static final float SCROLLBAR_WIDTH = 20;
	
	private static final Collection<String> DISPLAYED_ROLES = Arrays.asList(
			ROLE_USER,
			ROLE_ASSISTANT
	);
	
	private final GUI canvas;
	
	private final GUI scrollbarContainer;
	
	private final ConversationLifeCycle conversationLifeCycle;
	
	private final List<GUIMessage> messageGUIs;

	private boolean needsResize;

	private boolean needsResetting;
	
	private int lastMessageSize;
	
	@Inject
	public GUIChatbox(
			final Window window,
			final Boundary bounds,
			final ConversationLifeCycle conversationLifeCycle
	) {
		super(window, bounds);
		this.conversationLifeCycle = conversationLifeCycle;
		this.canvas = new GUI(window, new Boundary());
		this.scrollbarContainer = new GUI(window, new Boundary());
		addGUI(canvas);
		addGUI(scrollbarContainer);
		needsResetting = true;
		this.messageGUIs = new ArrayList<>();
	}
	
	@Override
	protected void onResize() {
		needsResetting = true;
	}
	
	@Override
	protected void updateInput() {
		final Conversation conversation = conversationLifeCycle.getConversation();
		if (!needsResize && lastMessageSize != conversation.getMessages().size()) {
			needsResetting = true;
		}
		lastMessageSize = conversation.getMessages().size();
	}
	
	@Override
	protected void overlayDraw(final Graphics graphics) {
		if (needsResize) {
			resizeMessages();
			needsResize = false;
		} else if (needsResetting) {
			resetMessages(conversationLifeCycle.getConversation()
					.getMessages()
					.stream()
					.filter(this::isMessageDisplayed)
					.toList());
			needsResetting = false;
			needsResize = true;
		}
	}

	private void resetMessages(final List<Message> messages) {
		canvas.clear();
		messageGUIs.clear();
		for (final Message message : messages) {
			final GUIMessage guiMessage = new GUIMessage(getWindow(), getMessageTemplateBoundary(), message);
			canvas.addGUI(guiMessage);
			messageGUIs.add(guiMessage);
		}
	}
	
	private void resizeMessages() {
		final int canvasHeightPixels = (messageGUIs.size() + 1) * MESSAGE_PADDING + messageGUIs.stream()
				.map(GUI::getOnScreenBounds)
				.map((coordinates) -> coordinates.height)
				.mapToInt(Integer::intValue)
				.sum();
		final float newCanvasWidth = canvasHeightPixels > getOnScreenBounds().height ? 1 - getScrollbarWidth() : 1;
		final float newCanvasHeight = canvasHeightPixels * 1f / getOnScreenBounds().height;
		final float canvasHeightChangeRatio = canvas.getBoundary().coordinates.height / newCanvasHeight;
		canvas.setBounds(new Boundary(
				new Rectf(0, 0, newCanvasWidth, newCanvasHeight),
				Scalar.STRETCHED,
				Alignment.TOP_LEFT
		));
		scrollbarContainer.clear();
		if (canvasHeightPixels > getOnScreenBounds().height) {
			addScrollbar();
		}
		if (canvas.getOnScreenBounds().height > getOnScreenBounds().height) {
			final float scroll = (canvas.getOnScreenBounds().height - getOnScreenBounds().height) * 1f / canvas.getOnScreenBounds().height;
			updateMessageBounds(scroll, canvasHeightChangeRatio);
		} else {
			updateMessageBounds(0, canvasHeightChangeRatio);
		}
		
	}
	
	private float getScrollbarWidth() {
		return SCROLLBAR_WIDTH * 1f / getOnScreenBounds().width;
	}

	private void addScrollbar() {
		final Boundary scrollbarBoundary = new Boundary(
				new Rectf(0, 0, getScrollbarWidth(), 1),
				Scalar.STRETCHED,
				Alignment.MIDDLE_RIGHT
		);
		final GUICustomScrollBar scrollbar = new GUICustomScrollBar(
				getWindow(),
				scrollbarBoundary,
				this::updateScroll
		);
		scrollbar.setScrollValue(1);
		scrollbarContainer.addGUI(scrollbar);
	}

	private void updateScroll(final float scrollValue) {
		final float scroll = (canvas.getOnScreenBounds().height - getOnScreenBounds().height) * 1f / canvas.getOnScreenBounds().height * scrollValue;
		updateMessageBounds(scroll, 1);
	}
	
	private void updateMessageBounds(final float scroll, final float heightChangeRatio) {
		int progressiveY = MESSAGE_PADDING;
		final float messageX = MESSAGE_PADDING * 1f / canvas.getOnScreenBounds().width;
		final float messageWidth = MESSAGE_WIDTH * 1f / canvas.getOnScreenBounds().width;
		for (final GUIMessage message : messageGUIs) {
			final Boundary currentBounds = message.getBoundary();
			final float y = progressiveY * 1f / canvas.getOnScreenBounds().height;
			message.setBounds(new Boundary(
					new Rectf(
							messageX,
							y - scroll,
							messageWidth,
							currentBounds.coordinates.height * heightChangeRatio
					),
					Scalar.STRETCHED,
					currentBounds.alignment
			));
			progressiveY += message.getOnScreenBounds().height + MESSAGE_PADDING;
		}
	}

	private Boundary getMessageTemplateBoundary() {
		return new Boundary(new Rectf(
				0,
				-2,
				MESSAGE_WIDTH * 1f / getOnScreenBounds().width,
				1
		));
	}

	private boolean isMessageDisplayed(final Message message) {
		return DISPLAYED_ROLES.contains(message.getRole());
	}

}
