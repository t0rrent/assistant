package au.com.cascadesoftware.util;

public class StringProcessing {

	public static String applyBackspaces(String string) {
		while (string.contains("\b")) {
			string = string.replaceAll("^\b+|[^\b]\b", "");
		}
		return string;
	}

	public static int commonPrefixLength(final String str1, final String str2) {
	    int n = Math.min(str1.length(), str2.length());
	    for (int i = 0; i < n; i++) {
	        if (str1.charAt(i) != str2.charAt(i)) {
	            return i;
	        }
	    }
	    return n;
	}
	
}
