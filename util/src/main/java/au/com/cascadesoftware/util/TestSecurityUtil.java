package au.com.cascadesoftware.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSecurityUtil {

	public static final int getJavaFileHash(final String javaFile) {
		final List<String> lines = new ArrayList<>(Arrays.asList(javaFile.split("\n")));
		for (int i = 0; i < lines.size(); i++) {
			final String line = lines.get(i);
			if (line.trim().isEmpty() || line.contains("import") || line.contains("package")) {
				lines.remove(i--);
			}
		}
		return String.join("\n", lines).hashCode();
	}
	
}
