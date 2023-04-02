package au.com.cascadesoftware.util;

import java.math.BigInteger;

public class TypeCheck {

	public static boolean isInteger(final String string) {
	    if (string == null || string.isEmpty()) {
	    	return false;
	    }
	    for(int i = 0; i < string.length(); i++) {
	        if(i == 0 && string.charAt(i) == '-') {
	            if (string.length() == 1) {
	            	return false;
	            } else {
	            	continue;
	            }
	        }
	        if(Character.digit(string.charAt(i), 10) < 0) {
	        	return false;
	        }
	    }
	    try {
	    	new BigInteger(string).intValueExact();
	    } catch (final ArithmeticException e) {
	    	return false;
	    }
	    return true;
	}

}
