package au.com.cascadesoftware.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class IOUtils {

	public static List<String> readLinesAsList(final InputStream inputStream) throws IOException {
		final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final List<String> lines = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
        	lines.add(line);
        }
        bufferedReader.close();
        return lines;
	}

	public static String readLinesAsString(final InputStream inputStream) throws IOException {
		return readLinesAsList(inputStream).stream().collect(Collectors.joining("\n"));
	}
	
	public static List<String> readFileAsList(final File file) throws FileNotFoundException, IOException {
		return readLinesAsList(new FileInputStream(file));
	}
	
	public static String readFileAsString(final File file) throws FileNotFoundException, IOException {
		return readLinesAsString(new FileInputStream(file));
	}
	
	public static List<String> readResourceAsList(final String resource) throws FileNotFoundException, IOException {
		return readLinesAsList(IOUtils.class.getClassLoader().getResourceAsStream(resource));
	}
	
	public static String readResourceAsString(final String resource) throws FileNotFoundException, IOException {
		return readLinesAsString(IOUtils.class.getClassLoader().getResourceAsStream(resource));
	}
	
	public static void writeToFile(final File target, final String content) throws IOException {
		final String filePath = target.getPath();
		final String[] paths = filePath.replace("\\", "/").split("/");
		final File directory = new File(filePath.substring(0, filePath.length() - paths[paths.length - 1].length()));
		if (!directory.exists()){
	        directory.mkdirs();
		}
		final FileWriter fileWriter = new FileWriter(target);
		fileWriter.write(content);
		fileWriter.close();
	}
	
	public static void writeToFile(final File target, final Collection<String> lines) throws IOException {
		writeToFile(target, String.join("\n", lines));
	}
	
}
