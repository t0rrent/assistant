package au.com.cascadesoftware.engine3.in;

import au.com.cascadesoftware.engine2.math.Vector2i;

public interface InputHandler {
	
	boolean isMouseClicked(int button);
	
	boolean isMouseDown(int button);
	
	boolean isKeyPressed(int button);

	boolean isKeyDown(int button);
	
	Vector2i getMousePos();

	boolean isScreenTouching();
	
	boolean isScreenTapped();

	String getTypedText();
	
	boolean isCapsLocked();

	int getScrollValue();
	
	
	
	public static class Util{
		
		public static char shift(char c) {
			if(c == '1') return '!';
			if(c == '2') return '@';
			if(c == '3') return '#';
			if(c == '4') return '$';
			if(c == '5') return '%';
			if(c == '6') return '^';
			if(c == '7') return '&';
			if(c == '8') return '*';
			if(c == '9') return '(';
			if(c == '0') return ')';
			if(c == '-') return '_';
			if(c == '=') return '+';
			if(c == '`') return '~';
			if(c == '[') return '{';
			if(c == ']') return '}';
			if(c == '\\') return '|';
			if(c == ';') return ':';
			if(c == '\'') return '"';
			if(c == ',') return '<';
			if(c == '.') return '>';
			if(c == '/') return '?';
			return (c + "").toUpperCase().charAt(0);
		}
		
		public static String getCharFromKeyText(String keytext, boolean shift) {
			keytext = keytext.replace("NumPad-", "").replace("NumPad ", "");
			if(keytext.length() == 1){
				if(shift) return "" + shift(keytext.charAt(0));
				else return keytext.toLowerCase();
			}
			if(shift){
				if(keytext.equals("Back Quote")) return "~";
				if(keytext.equals("Minus")) return "_";
				if(keytext.equals("Equals")) return "+";
				if(keytext.equals("Open Bracket")) return "{";
				if(keytext.equals("Close Bracket")) return "}";
				if(keytext.equals("Back Slash")) return "|";
				if(keytext.equals("Semicolon")) return ":";
				if(keytext.equals("Quote")) return "\"";
				if(keytext.equals("Comma")) return "<";
				if(keytext.equals("Period")) return ">";
				if(keytext.equals("Slash")) return "?";
			}else{
				if(keytext.equals("Back Quote")) return "`";
				if(keytext.equals("Minus")) return "-";
				if(keytext.equals("Equals")) return "=";
				if(keytext.equals("Open Bracket")) return "[";
				if(keytext.equals("Close Bracket")) return "]";
				if(keytext.equals("Back Slash")) return "\\";
				if(keytext.equals("Semicolon")) return ";";
				if(keytext.equals("Quote")) return "'";
				if(keytext.equals("Comma")) return ",";
				if(keytext.equals("Period")) return ".";
				if(keytext.equals("Slash")) return "/";
			}
			if(keytext.equals("Space")) return " ";
			if(keytext.equals("Backspace")) return "[bksp]";
			if(keytext.equals("Enter")) return "[entr]";
			return "";
		}

		public static String getCharFromKeyText(String keyText, boolean shift, boolean caps) {
			char c = keyText.charAt(0);
			if(((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) && keyText.length() == 1) shift = shift != caps;
			return getCharFromKeyText(keyText, shift);
		}
		
	}
	
}
