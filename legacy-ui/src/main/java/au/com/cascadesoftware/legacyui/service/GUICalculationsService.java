package au.com.cascadesoftware.legacyui.service;

import java.util.ArrayList;
import java.util.List;

import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;

public class GUICalculationsService {

	public List<String> getFittedParagraph(
			final Graphics graphics,
			final String content,
			final float fontSize,
			final int width
	) {
		final Paint paint = new Paint();
		paint.setFontSize(fontSize);
		graphics.setPaint(paint);
		final List<String> fittedParagraph = new ArrayList<>();
		String currentLine = "";
		for (final char c : replaceTabsWithSpaces(content).toCharArray()) {
			final String proposedLine = currentLine + c;
			if (graphics.measureText(proposedLine).x > width) {
				if (currentLine.length() == 1) {
					fittedParagraph.add("" + c);
				} else if (c == ' ') {
					fittedParagraph.add(currentLine);
					currentLine = "";
				} else {
					if (currentLine.contains(" ") && !currentLine.endsWith(" ")) {
						final String[] words = currentLine.split(" ");
						final String lastWord = words[words.length - 1];
						final int spillOverSize = lastWord.length();
						fittedParagraph.add(currentLine.substring(0, currentLine.length() - spillOverSize));
						currentLine = lastWord + c;
					} else {
						fittedParagraph.add(currentLine);
						currentLine = "" + c;
					}
				}
			} else {
				currentLine = proposedLine;
			}
		}
		if (currentLine.replace(" ", "").length() > 0) {
			fittedParagraph.add(currentLine);
		}
		return fittedParagraph;
	}

	private String replaceTabsWithSpaces(final String string) {
		return string.replace("\t", "    ");
	}

}
