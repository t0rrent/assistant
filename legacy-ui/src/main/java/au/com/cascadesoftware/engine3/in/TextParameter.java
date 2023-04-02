package au.com.cascadesoftware.engine3.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextParameter {

	private String text;
	
	private static HashMap<String, TextParameter> texts = new HashMap<String, TextParameter>();
	
	public TextParameter(String text){
		this.text = text;
	}
	
	public static void add(String key, String text){
		add(key, new TextParameter(text));
	}
	
	public static void add(String key, TextParameter text){
		texts.put(key, text);
	}
	
	@Override
	public String toString() {
		if(text == null) return null;
		boolean param = false;
		List<Replacable> replacables = new ArrayList<Replacable>();
		String key = "";
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			if(c == '%'){
				param = !param;
				if(!param){
					String newChar = texts.get(key).toString();
					if(newChar != null) replacables.add(new Replacable("%" + key + "%", newChar));
					key = "";
				}
			}else if(param){
				key += c;
			}
		}
		String newText = text;
		for(Replacable r : replacables){
			newText = newText.replace(r.oldChar, r.newChar);
		}
		return newText;
	}
	
	private class Replacable {
		
		private final String oldChar, newChar;
		
		private Replacable(String oldChar, String newChar){
			this.oldChar = oldChar;
			this.newChar = newChar;
		}
		
	}
	
}
