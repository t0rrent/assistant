package au.com.cascadesoftware.util.model;

import java.io.IOException;
import java.io.InputStream;

public class SimpleInputStreamTest extends InputStream {
	
	private String exampleText;

	public SimpleInputStreamTest(final String exampleText) {
		this.exampleText = exampleText;
	}

	@Override
	public int read() throws IOException {
		if (exampleText.length() == 0) {
			return -1;
		}
		final char c = exampleText.charAt(0);
		exampleText = exampleText.substring(1);
		return c;
	}

}
