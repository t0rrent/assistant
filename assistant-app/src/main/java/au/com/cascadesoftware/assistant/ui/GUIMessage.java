package au.com.cascadesoftware.assistant.ui;

import static au.com.cascadesoftware.openai.model.Message.ROLE_USER;

import java.util.List;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.gui.Alignment;
import au.com.cascadesoftware.engine3.gui.Boundary;
import au.com.cascadesoftware.engine3.gui.GUI;
import au.com.cascadesoftware.engine3.gui.GUIShapeRoundedRectangle;
import au.com.cascadesoftware.engine3.gui.GUIText;
import au.com.cascadesoftware.engine3.gui.Scalar;
import au.com.cascadesoftware.legacyui.service.GUICalculationsService;
import au.com.cascadesoftware.openai.model.Message;

public class GUIMessage extends GUI {
	
	private static final int BUBBLE_RADIUS = 15;

	private static final float FONT_SIZE = 18f;
	private static final int LINE_SPACING = 5;
	private static final int PADDING = 10;
	
	private final static Color USER_COLOR = new Color(114, 141, 214);
	private final static Color ASSISTANT_COLOR = new Color(211, 211, 211);

	private final Message message;

	private boolean needsResize;
	
	private GUI canvas;

	private Boundary initialBoundary;
	
	public GUIMessage(final Window window, final Boundary boundary, final Message message) {
		super(window, boundary);
		final Color chatColor = getColor(message.getRole());
		final GUIShapeRoundedRectangle bubble = new GUIShapeRoundedRectangle(window, new Boundary(), chatColor, BUBBLE_RADIUS);
		this.message = message;
		this.initialBoundary = boundary;
		this.needsResize = true;
		canvas = new GUI(window, new Boundary());
		addGUI(bubble);
		addGUI(canvas);
	}
	
	@Override
	protected void onResize() {
		needsResize = true;
	}
	
	@Override
	protected void draw(final Graphics graphics) {
		if (needsResize) {
			setBounds(initialBoundary);
			final int width = getOnScreenBounds().width - PADDING * 2;
			final List<String> lines = getServiceLocator().getService(GUICalculationsService.class)
					.getFittedParagraph(graphics, message.getContent(), FONT_SIZE, width);
			setBounds(new Boundary(
					new Rectf(
							initialBoundary.coordinates.x,
							initialBoundary.coordinates.y,
							initialBoundary.coordinates.width,
							getCoordinateHeight(lines.size())
					),
					Scalar.STRETCHED,
					getAlignment(message.getRole())
			));
			setLines(lines);
			needsResize = false;
		}
	}

	private void setLines(final List<String> lines) {
		canvas.clear();
		final float paddingX = PADDING * 1f / getOnScreenBounds().width;
		final float paddingY = PADDING * 1f / getOnScreenBounds().height;
		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			float div = (getOnScreenBounds().height * 1f / (getOnScreenBounds().height + PADDING * 2)) 
					/ lines.size();
			final GUIText text = new GUIText(
					getWindow(),
					new Boundary(new Rectf(paddingX, paddingY + i * div, 1, div), Scalar.STRETCHED, Alignment.TOP_LEFT),
					getTextColor(message.getRole()),
					line
			);
			text.setTextAlignment(Alignment.MIDDLE_LEFT);
			text.setTextAbsolute(FONT_SIZE);
			canvas.addGUI(text);
		}
	}

	private float getCoordinateHeight(final int lineCount) {
		if (getParent() == null) {
			return 1;
		}
		final float heightPixels = lineCount * FONT_SIZE + (lineCount - 1) * LINE_SPACING + PADDING * 2;	
		return heightPixels / getParent().getOnScreenBounds().height;
	}

	private static Color getColor(final String role) {
		if (role.equals(ROLE_USER)) {
			return USER_COLOR;
		} else {
			return ASSISTANT_COLOR;
		}
	}

	private static Color getTextColor(final String role) {
		if (role.equals(ROLE_USER)) {
			return Color.WHITE;
		} else {
			return Color.BLACK;
		}
	}
	
	private static Alignment getAlignment(final String role) {
		if (role.equals(ROLE_USER)) {
			return Alignment.TOP_RIGHT;
		} else {
			return Alignment.TOP_LEFT;
		}
	}

}
