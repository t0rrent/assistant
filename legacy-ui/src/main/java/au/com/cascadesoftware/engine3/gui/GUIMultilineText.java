package au.com.cascadesoftware.engine3.gui;

import java.util.ArrayList;
import java.util.List;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.exception.BrokenAlgorithmException;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;

public class GUIMultilineText extends GUI {
	
	private GUIText[] lines, halfLines;
	private String inputText;
	private String textToDraw;
	private boolean needsFitting;
	private Alignment superAlignment;
	private boolean cutoffAtFront;

	public GUIMultilineText(Window window, Boundary bounds, int l, Color textColor, String string) {
		super(window, bounds);		
		cutoffAtFront = true;
		superAlignment = Alignment.MIDDLE_CENTER;
		lines = new GUIText[l];
		this.halfLines = new GUIText[l - 1];
		float bHeight = 1f / l;
		boolean fucked = true;
		for(int i = 0; i < l; i++){
			Rectf coords = new Rectf(0, (bHeight*(i - (fucked ? (l/2f - 0.5f) : 0))), 1, bHeight);
			Boundary newBounds = new Boundary(coords, Scalar.STRETCHED, Alignment.MIDDLE_CENTER);
			lines[i] = new GUIText(window, newBounds, textColor, "");
			lines[i].setTextSize(1);
			this.addGUI(lines[i]);
		}
		for(int i = 0; i < l - 1; i++){
			Rectf coords = new Rectf(0, bHeight*(i + 0.5f - (fucked ? (l/2f - 0.5f) : 0)), 1, bHeight);
			Boundary newBounds = new Boundary(coords, Scalar.STRETCHED, Alignment.MIDDLE_CENTER);
			halfLines[i] = new GUIText(window, newBounds, textColor, "");
			halfLines[i].setTextSize(1);
			this.addGUI(halfLines[i]);
		}
		setText(string);
	}
	
	private String getFirstWord(String phrase) {
		String word = "";
		for(int i = 0; i < phrase.length(); i++){
			String ch = phrase.substring(i, i+1);
			if(ch.equals(" ")) break;
			word += ch;
		}
		return word;
	}
	
	@Override
	protected void onResize() {
		needsFitting = true;
	}
	
	@Override
	protected void draw(Graphics graphics) {
		if(needsFitting){
			for(int i = 0; i < lines.length; i++) lines[i].setText("");
			for(int i = 0; i < lines.length - 1; i++) halfLines[i].setText("");
			//System.out.println("start fit");
			String remainingText = textToDraw;
			List<String> lineList = new ArrayList<String>();
			//System.out.println(remainingText);
			for(int i = 0; i <= lines.length || remainingText.trim().length() > 0; i++){
				boolean lineComplete = false;
				String line = "";
				while(!lineComplete){
					String word = getFirstWord(remainingText); //get the next word in the input text
					if(lines[0].doesPhraseFit(graphics, line + word)){ //if the word fits into the current line
						line += word + " "; //add the word plus a space after it
						remainingText = remainingText.substring(word.length()); //remove the word from the beginning of the remaining text
						if(remainingText.length() > 0) remainingText = remainingText.substring(1); //if its not the last word there will be a space so that is removed too
						else{
							line = line.substring(0, line.length() - 1);
							lineComplete = true;
						}
					}else if(line.length() > 0){ //if the word is not longer than the whole line
						line = line.substring(0, line.length() - 1); //there will be an artificial space at the end so remove it
						lineComplete = true;
					}else{
						String pref = "";
						for(int j = 1; lines[0].doesPhraseFit(graphics, pref); j++) pref = remainingText.substring(0, j); //get all the word that fits
						pref = pref.substring(0, pref.length() - 1); //remove the extra character
						if(pref.length() == 0) throw new BrokenAlgorithmException(); //if not even 1 character fits
						line = pref;
						remainingText = remainingText.substring(pref.length()); //remove the string from the beginning of the remaining text
						lineComplete = true;
					}
				}
				lineList.add(line);
				//System.out.println(remainingText);
				//System.out.println("line: " + line + ", remainder: " + remainingText);
			}
			for(int i = lineList.size() - 1; i >= 0; i--) if(lineList.get(i).length() == 0) lineList.remove(i);
			String[] lineArray = lineList.toArray(new String[lineList.size()]);
			if(lineArray.length > lines.length){ //when there is an overflow line
				if(cutoffAtFront){
					this.textToDraw = textToDraw.substring(1, textToDraw.length());
					draw(graphics);
					return;
				}
				String finalLine = lineArray[lines.length - 1];
				String overflow = " " + lineArray[lines.length];
				String suff = "";
				for(int j = 1; lines[0].doesPhraseFit(graphics, finalLine + suff) && j <= overflow.length(); j++) suff = overflow.substring(0, j); //get all the word that fits
				suff = suff.substring(0, suff.length() - 1); //remove the extra character
				if(suff.length() > 0){ //if there are extra characters
					lineArray[lines.length - 1] = finalLine + suff;
				}
				String[] newArr = new String[lines.length];
				for(int i = 0; i < lines.length; i++) newArr[i] = lineArray[i];
				lineArray = newArr;
			}
			if(superAlignment.y == 0){
				for(int i = 0; i < lineArray.length; i++) lines[i].setText(lineArray[i]);
			}else if(superAlignment.y == 1){
				boolean usesHalflines = (lineArray.length % 2) != (lines.length % 2);
				int start = (lines.length - lineArray.length)/2;
				//System.out.println(start + ", " + lines.length + ", " + lineArray.length);
				//System.out.println(start);
				for(int i = 0; i < lineArray.length; i++){
					if(usesHalflines) halfLines[i + start].setText(lineArray[i]);
					else lines[i + start].setText(lineArray[i]);
				}
			}else { //2
				for(int i = 0; i < lineArray.length; i++) lines[lines.length - lineArray.length + i].setText(lineArray[i]);
			}
			//System.out.println("end fit");
			//System.out.println();
			needsFitting = false;
		}
	}

	public void setTextColor(Color textColor) {
		for(int i = 0; i < lines.length; i++){
			lines[i].setTextColor(textColor);
			if(i < 1 - 1) halfLines[i].setTextColor(textColor);
		}
	}

	public void setText(String inputText) {
		inputText = inputText.replace("[Ìtick]", "|");
		this.inputText = inputText;
		this.textToDraw = inputText;
		needsFitting = true;
	}

	public void setTextAlignment(Alignment alignment) {
		for(int i = 0; i < lines.length; i++) lines[i].setTextAlignment(alignment);
		for(int i = 0; i < halfLines.length; i++) halfLines[i].setTextAlignment(alignment);
		this.superAlignment = alignment;
		needsFitting = true;
	}

	public String getText() {
		return inputText;
	}

}
