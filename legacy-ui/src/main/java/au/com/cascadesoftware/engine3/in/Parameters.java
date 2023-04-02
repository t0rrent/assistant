package au.com.cascadesoftware.engine3.in;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Parameters {
	
	private final Properties properties;

	public Parameters(String dir) throws IOException {
	    properties = new Properties();
	    InputStream isRes = this.getClass().getClassLoader().getResourceAsStream("assets/window/" + dir + ".properties");
	    if(isRes != null){
		    properties.load(isRes);
		    isRes.close();
	    }else{
		    FileInputStream isDir = new FileInputStream("./" + dir + ".properties");
		    properties.load(isDir);
		    if(isDir != null) isDir.close();
	    }
	}

	public int getInt(String property) {
		return Integer.parseInt(getString(property));
	}

	public String getString(String property) {
		return new TextParameter(properties.getProperty(property)).toString();
	}

	public boolean getBoolean(String property) {
		return Boolean.parseBoolean(getString(property));
	}

}
