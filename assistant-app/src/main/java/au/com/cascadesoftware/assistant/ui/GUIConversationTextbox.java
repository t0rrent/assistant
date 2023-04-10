package au.com.cascadesoftware.assistant.ui;

import au.com.cascadesoftware.assistant.service.ConversationLifeCycle;
import au.com.cascadesoftware.assistant.service.TextboxAccessorService;
import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import au.com.cascadesoftware.engine3.gui.Alignment;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.GUIButton;
import au.com.cascadesoftware.engine3.gui.GUIHover;
import au.com.cascadesoftware.engine3.gui.GUIImage;
import au.com.cascadesoftware.engine3.gui.GUITextboxDesktop;
import au.com.cascadesoftware.engine3.gui.Scalar;
import jakarta.inject.Inject;

public class GUIConversationTextbox extends GUI {
	
	private static final Color TEXT_COLOR = Color.BLACK;
	private static final Color PLACEHOLDER_TEXT_COLOR = Color.GRAY;
	private static final Color BOX_COLOR = new Color("ddd");
	private static final Color SEND_BACKGROUND_COLOR = Color.LIGHT_GRAY;
	private static final Color SEND_BACKGROUND_COLOR_HOVER = new Color(186, 186, 186);
	private static final Color SEND_BACKGROUND_COLOR_CLICK = new Color("666");
	private static final Color BORDER_COLOR = new Color("444");
	
	private static final int BORDER_WIDTH = 2;
	
	private static final String SEND_IMAGE_RESOURCE = "ui/send.png";
	private static final String SEND_HOVER_IMAGE_RESOURCE = "ui/send-hover.png";
	
	private static final int TEXTBOX_HORIZONTAL_PADDING = 10;
	private static final float TEXTBOX_VERTICAL_SIZE_RATIO = 0.5f;
	
	private final ConversationLifeCycle conversationLifeCycle;

	private final GUITextboxDesktop textBox;
	private final GUI sendButton;

	@Inject
	public GUIConversationTextbox(
			final Window window, 
			final Boundary bounds,
			final ConversationLifeCycle conversationLifeCycle,
			final TextboxAccessorService textboxAccessorService
	) {
		super(window, bounds);
		this.conversationLifeCycle = conversationLifeCycle;
		this.sendButton = createSendButton();
		this.textBox = createTextbox();
		textboxAccessorService.setTextbox(textBox);
		addGUI(textBox);
		addGUI(sendButton);
		setBackground(BOX_COLOR);
		resizeElements();
	}
	
	private GUITextboxDesktop createTextbox() {
		final GUITextboxDesktop textbox = new GUITextboxDesktop(
				getWindow(),
				new Boundary(), 
				1,
				TEXT_COLOR, 
				PLACEHOLDER_TEXT_COLOR,
				Color.INVISIBLE, 
				Color.INVISIBLE
		);
		textbox.setCursorBlack(true);
		textbox.setTextAlignment(Alignment.MIDDLE_LEFT);
		textbox.setInputListenerZone((onScreenBounds) -> onScreenBounds.expandByFactorOf(0, 1));
		return textbox;
	}
	
	private GUI createSendButton() {
		final GUI sendImage = new GUIImage(
				getWindow(),
				new Boundary(
					new Rectf(0, 0, 1, 1),
					Scalar.VERTICAL,
					Alignment.MIDDLE_RIGHT
				),
				getWindow().getNewResourceLoader().change(SEND_IMAGE_RESOURCE)
		);
		sendImage.setBackground(SEND_BACKGROUND_COLOR);
		
		final GUI hoverImage = new GUIImage(
				getWindow(),
				new Boundary(),
				getWindow().getNewResourceLoader().change(SEND_HOVER_IMAGE_RESOURCE)
		);
		
		final GUIClickShade clickShade = new GUIClickShade(getWindow(), new Boundary(), SEND_BACKGROUND_COLOR_CLICK, 0.1f);
		
		final GUIHover hover = new GUIHover(getWindow(), new Boundary()) {
			@Override
			protected void onStateChange(float phase) {
				hoverImage.setOpacity(phase);
				setBackground(new Color(SEND_BACKGROUND_COLOR_HOVER, phase));
			}
		};
		
		final GUIButton button = new GUIButton(getWindow(), new Boundary()) {
			@Override
			public void onClick() {
				clickShade.click();
				onSendButtonClicked();
			}
		};

		sendImage.addGUI(hover);
		sendImage.addGUI(clickShade);
		sendImage.addGUI(hoverImage);
		sendImage.addGUI(button);
		return sendImage;
	}

	private void onSendButtonClicked() {
		conversationLifeCycle.send();
	}

	@Override
	protected void overlayDraw(final Graphics graphics) {
		final Paint borderPaint = getBorderPaint();
		graphics.setPaint(borderPaint);
		graphics.drawRect(getOnScreenBounds().x, getOnScreenBounds().y, getOnScreenBounds().width, BORDER_WIDTH);
		graphics.drawRect(getOnScreenBounds().x, getOnScreenBounds().y + getOnScreenBounds().height - BORDER_WIDTH, getOnScreenBounds().width, BORDER_WIDTH);
	}

	private Paint getBorderPaint() {
		final Paint paint = new Paint();
		paint.setColor(BORDER_COLOR);
		return paint;
	}
	
	public void resizeElements() {
		final int buttonWidth = getOnScreenBounds().height;
		textBox.setBounds(new Boundary(
				new Rectf(
						TEXTBOX_HORIZONTAL_PADDING * 1f / getOnScreenBounds().width, 
						0,
						1 - (buttonWidth + 2 * TEXTBOX_HORIZONTAL_PADDING) * 1f / getOnScreenBounds().width,
						TEXTBOX_VERTICAL_SIZE_RATIO
				),
				Scalar.STRETCHED,
				Alignment.MIDDLE_LEFT
		));
	}

}
