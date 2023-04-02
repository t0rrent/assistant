package au.com.cascadesoftware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import au.com.cascadesoftware.util.model.SimpleInputStreamTest;

public class IOUtilsIT {
	
	private static final String EXAMPLE_TEXT = "{\n    \"text\": \"just some nicely formatted json\"\n}";
	
	private static String testFile;

	private static List<String> foldersToDelete;
	
	@BeforeAll
	public static void beforeAll() throws IOException {
		foldersToDelete = new ArrayList<>();
		testFile = "test-" + new Random().nextInt(10000) + ".json";
		final FileWriter fileWriter = new FileWriter(new File(testFile));
		fileWriter.write(EXAMPLE_TEXT);
		fileWriter.close();
	}
	
	@AfterAll
	public static void afterAll() throws IOException {
		Files.delete(new File(testFile).toPath());
		for(String folder : foldersToDelete) {
			FileUtils.deleteDirectory(new File(folder));
		}
	}

	@Test
	public void testReadLinesAsString() throws IOException {
		final SimpleInputStreamTest inputStream = new SimpleInputStreamTest(EXAMPLE_TEXT);
		assertEquals(EXAMPLE_TEXT, IOUtils.readLinesAsString(inputStream));
		
		final SimpleInputStreamTest inputStream2 = new SimpleInputStreamTest(EXAMPLE_TEXT + '\n');
		assertEquals(EXAMPLE_TEXT, IOUtils.readLinesAsString(inputStream2));
		
		final SimpleInputStreamTest inputStream3 = new SimpleInputStreamTest("");
		assertEquals("", IOUtils.readLinesAsString(inputStream3));
	}

	@Test
	public void testReadLinesAsList() throws IOException {
		final SimpleInputStreamTest inputStream = new SimpleInputStreamTest(EXAMPLE_TEXT + "\n\n");
		final List<String> result = IOUtils.readLinesAsList(inputStream);
		assertEquals(
				Arrays.asList((EXAMPLE_TEXT + "\n ").split("\n"))
						.stream()
						.map((line) -> line.equals(" ") ? "" : line)
						.collect(Collectors.toList()),
				result
		);
		
		final SimpleInputStreamTest inputStream2 = new SimpleInputStreamTest("");
		assertEquals(Collections.emptyList(), IOUtils.readLinesAsList(inputStream2));
	}

	@Test
	public void testReadLinesInvalidInputs() throws IOException {
		assertThrows(NullPointerException.class, () -> IOUtils.readLinesAsList(null));
		assertThrows(NullPointerException.class, () -> IOUtils.readLinesAsString(null));
	}
	
	@Test
	public void testReadFromResource() throws FileNotFoundException, IOException {
		assertEquals(EXAMPLE_TEXT, IOUtils.readResourceAsString("arbitrary-folder/test.json"));
		assertEquals(
				Arrays.asList(EXAMPLE_TEXT.split("\n")),
				IOUtils.readResourceAsList("arbitrary-folder/test.json")
		);
	}
	
	@Test
	public void testReadFromFile() throws FileNotFoundException, IOException {
		assertEquals(EXAMPLE_TEXT, IOUtils.readFileAsString(new File(testFile)));
		assertEquals(
				Arrays.asList(EXAMPLE_TEXT.split("\n")),
				IOUtils.readFileAsList(new File(testFile))
		);
	}
	
	@Test
	public void testWriteToFile() throws FileNotFoundException, IOException {
		final String topFolder = "ioutils-test-" + new Random().nextInt(10000);
		foldersToDelete.add(topFolder);
		
		final File test1 = new File(topFolder + "/some/arbitrary/test1.json");
		final File test2 = new File(topFolder + "/some/arbitrary/test2.json");
		final File test3 = new File(topFolder + "/some/arbitrary/test3.json");
		
		IOUtils.writeToFile(test1, EXAMPLE_TEXT);
		IOUtils.writeToFile(test2, Arrays.asList(EXAMPLE_TEXT.split("\n")));

		assertEquals(EXAMPLE_TEXT, IOUtils.readFileAsString(test1));
		assertEquals(EXAMPLE_TEXT, IOUtils.readFileAsString(test2));
		assertFalse(test3.exists());
	}
	
}
