package au.com.cascadesoftware.voice.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import au.com.cascadesoftware.util.StringProcessing;

public class SpeechRecognitionStreamTest {
	
	private SpeechRecognitionStream stream;
	
	@BeforeEach
	public void beforeEach() {
		stream = new SpeechRecognitionStream(null, null, null);
	}
	
	@Test
	public void testBasicInput() {
		stream.writePartial("te");
		stream.writePartial("test");
		stream.writeFull("test");
		final String result = getResult();
		assertEquals("test\n", result);
	}
	
	@Test
	public void testIncompletePartial() {
		stream.writePartial("test1");
		stream.writeFull("test1");
		stream.writePartial("test2");
		final String result = getResult();
		assertEquals("test1\ntest2", result);
	}
	
	@Test
	public void testMultilineInput() {
		stream.writePartial("test1");
		stream.writeFull("test1");
		stream.writePartial("test2");
		stream.writeFull("test2");
		final String result = getResult();
		assertEquals("test1\ntest2\n", result);
	}
	
	@Test
	public void testCorrectionInPartial() {
		stream.writePartial("test1");
		stream.writePartial("test2");
		stream.writeFull("test2");
		final String result = getResult();
		assertEquals("test1\b2\n", result);
		assertEquals("test2\n", StringProcessing.applyBackspaces(result));
	}
	
	@Test
	public void testCorrectionInFull() {
		stream.writePartial("test1");
		stream.writeFull("test2");
		final String result = getResult();
		assertEquals("test1\b2\n", result);
		assertEquals("test2\n", StringProcessing.applyBackspaces(result));
	}
	
	@Test
	public void testMultiCorrection() {
		stream.writePartial("test1");
		stream.writeFull("test1");
		stream.writePartial("alpha");
		stream.writePartial("alpha beta");
		stream.writePartial("alphabet words");
		stream.writeFull("alpha beta birds");
		final String result = getResult();
		assertEquals("test1\n"
				+ "alpha beta"
				+ "\b\b\b\b\b"
				+ "bet words"
				+ "\b\b\b\b\b\b\b\b\b"
				+ " beta birds\n", result);
		assertEquals("test1\nalpha beta birds\n", StringProcessing.applyBackspaces(result));
	}
	
	@Test
	public void testEnitreCorrectionInPartial() {
		stream.writePartial("test1");
		stream.writePartial("");
		stream.writeFull("");
		final String result = getResult();
		assertEquals("test1\b\b\b\b\b", result);
		assertEquals("", StringProcessing.applyBackspaces(result));
	}
	
	@Test
	public void testEnitreCorrectionInFull() {
		stream.writePartial("test1");
		stream.writeFull("");
		final String result = getResult();
		assertEquals("test1\b\b\b\b\b", result);
		assertEquals("", StringProcessing.applyBackspaces(result));
	}

	private String getResult() {
		String result = "";
		Character character;
		while ((character = stream.read()) != null) {
			result += character;
		}
		return result;
	}

}
