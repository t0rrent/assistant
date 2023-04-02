package au.com.cascadesoftware.engine3.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {
	
	public static void shuffleArray(Object[] array) {
		int n = array.length;
		for(int i = 0; i < n; i++) {
		     int r = i + new Random().nextInt(n - i);
		     Object t = array[r];
		     array[r] = array[i];
		     array[i] = t;
		}
	}

	public static void write(String location, Object p) {
		location = location.replace("\\", "/");
		String[] locationSplit = location.split("/");
	    File directory = new File(location.replace("/" + locationSplit[locationSplit.length - 1], ""));
		if(!directory.exists()) directory.mkdir();
		try{
			FileOutputStream f = new FileOutputStream(new File(location));
			ObjectOutputStream o = new ObjectOutputStream(new BufferedOutputStream(f));
			o.writeObject(p);
			o.close();
			f.close();
		} catch(IOException e) {
			System.err.println("write to " + location + " failed:");
			e.printStackTrace();
        }
	}
	
	public static Object read(String location){
		boolean exists = false;
		if(new File(location).exists()) exists = true;
		if(exists){
			try{
				FileInputStream fi = new FileInputStream(new File(location));
				ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(fi));
				Object out = oi.readObject();
				oi.close();
				fi.close();
				return out;
			}catch(IOException | ClassNotFoundException e){
				e.printStackTrace();
	        }
		}
		return null;
	}
	
	public static List<String> readText(String location){
		boolean exists = false;
		if(new File(location).exists()) exists = true;
		if(exists){
			try{
				List<String> out = new ArrayList<>();
				BufferedReader reader = new BufferedReader(new FileReader(location));
				String line = reader.readLine();
				while (line != null) {
					out.add(line);
					line = reader.readLine();
				}
				reader.close();
				return out;
			}catch(IOException e){
				e.printStackTrace();
	        }
		}
		return null;
	}

	public final static String rootDirectory(Class<?> clazz) {
		try {
			return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public static List<File> getAllFiles(final String dir) {
		final List<File> list = new ArrayList<>();
		getAllFiles(dir, list);
		return list;
	}

	private static void getAllFiles(final String dir, final List<File> files) {
	    final File directory = new File(dir);
	    final File[] fileList = directory.listFiles();
	    if(fileList != null) {
	        for (File file : fileList) {      
	            if (file.isFile()) {
	                files.add(file);
	            } else if (file.isDirectory()) {
	            	getAllFiles(file.getAbsolutePath(), files);
	            }
	        }
	    }
	}
	
}
