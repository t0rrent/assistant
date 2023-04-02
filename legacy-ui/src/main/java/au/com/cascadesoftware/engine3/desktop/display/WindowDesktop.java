package au.com.cascadesoftware.engine3.desktop.display;

import java.awt.Canvas;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.desktop.graphics.GraphicsDesktop;
import au.com.cascadesoftware.engine3.desktop.in.InputHandlerDesktop;
import au.com.cascadesoftware.engine3.desktop.in.InputListener;
import au.com.cascadesoftware.engine3.desktop.in.ResourceLoaderDesktop;
import au.com.cascadesoftware.engine3.desktop.in.SpriteLoaderDesktop;
import au.com.cascadesoftware.engine3.display.Window;
//import au.com.cascadesoftware.engine3.display.Window.Cursor;
import au.com.cascadesoftware.engine3.exception.ClosedWindowException;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.in.InputHandler;
//import au.com.cascadesoftware.engine3.in.Parameters;
import au.com.cascadesoftware.engine3.in.ResourceLoader;
import au.com.cascadesoftware.legacyui.config.WindowConfig;
import jakarta.inject.Inject;

public class WindowDesktop implements Window {
	
	private boolean open, maximized, antiAlias;
	
	private JFrame frame;
	private Canvas canvas;
	private Container contentPane;
	private BufferStrategy bufferStrategy;
	private InputHandlerDesktop inputHandler;
	private HashMap<Cursor, java.awt.Cursor> customCursors;
	private Cursor nextCursorForUpdate;
	private int width, height;
	private String[] filesFromResource;
	private String fdContext;
	private boolean isServer, isOnline;
	private int port, portWS;
	private Cursor cursor;
	
	/*public WindowDesktop(){
		this("window_parameters", null);
	}
	
	public WindowDesktop(Runnable onCloseAttempt){
		this("window_parameters", onCloseAttempt);
	}*/
	/*
	 public WindowDesktop(String parameters, Runnable onCloseAttempt){
		cursor = Cursor.DEFAULT;
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		filesFromResource = new String[]{};
		Parameters properties = null;
		try {
			properties = new Parameters(parameters);
		}catch(java.io.FileNotFoundException e){
			//e.printStackTrace();
			System.err.println("Fatal error: " + parameters + ".properties could not be found");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		width = properties.getInt("WIDTH");
		height = properties.getInt("HEIGHT");
		isServer = properties.getBoolean("SERVER");
		isOnline = properties.getBoolean("ONLINE");
		if(isServer){
			port = properties.getInt("PORT");
			portWS = properties.getInt("PORT_WS");
		}
		boolean resizable = properties.getBoolean("RESIZABLE");
		antiAlias = properties.getBoolean("ANTIALIAS");
		
		frame = new JFrame(properties.getString("TITLE"));
		frame.setResizable(resizable);
		canvas = new Canvas();
		contentPane = frame.getContentPane();
		contentPane.add(canvas);
		
		if (onCloseAttempt == null) {
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		} else {
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
					onCloseAttempt.run();
			    }
			});
		}
		if(properties.getString("ICON") != null) frame.setIconImage(getImageFromResource(properties.getString("ICON")));
		
		frame.pack();
		
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setBounds((screenSize.width - properties.getInt("WIDTH"))/2, (screenSize.height - properties.getInt("HEIGHT"))/2, properties.getInt("WIDTH"), properties.getInt("HEIGHT"));
		if(properties.getBoolean("START_MAXIMIZED")) frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		inputHandler = new InputHandlerDesktop();
		
		addCustomCursors();
		addListeners();
	}
	 */
	
	@Inject
	public WindowDesktop(final WindowConfig windowConfig) {
		cursor = Cursor.DEFAULT;
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		filesFromResource = new String[]{};
		
		width = windowConfig.width;
		height = windowConfig.height;
		
		antiAlias = windowConfig.antiAliasing;
		
		frame = new JFrame(windowConfig.title);
		frame.setResizable(windowConfig.resizable);
		canvas = new Canvas();
		contentPane = frame.getContentPane();
		contentPane.add(canvas);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setIconImage(getImageFromResource(windowConfig.icon == null ? "/assets/window/icon64.png" : windowConfig.icon));
		
		frame.pack();
		
		canvas.createBufferStrategy(2);
		bufferStrategy = canvas.getBufferStrategy();
		canvas.requestFocus();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		frame.setBounds((screenSize.width - width)/2, (screenSize.height - height)/2, width, height);
		if(windowConfig.startMaximized) {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}

		inputHandler = new InputHandlerDesktop();
		
		addCustomCursors();
		addListeners();
	}

	private void addCustomCursors() {
		customCursors = new HashMap<Cursor, java.awt.Cursor>();
		addCursor(Cursor.HAND_GRABBING, "hand_grab.png", new Vector2i(16, 16));
		addCursor(Cursor.HAND_OPEN, "hand_open.png", new Vector2i(16, 16));
		addCursor(Cursor.TEXT, "text.png", new Vector2i(3, 8));
		addCursor(Cursor.TEXT_BLACK, "text_black.png", new Vector2i(3, 8));
		addCursor(Cursor.DOUBLE_ARROW_HORIZONTAL, "double_arrow_horizontal.png", new Vector2i(11, 4));
		addCursor(Cursor.DOUBLE_ARROW_VERTICAL, "double_arrow_vertical.png", new Vector2i(4, 11));
		addCursor(Cursor.QUAD_ARROW, "quad_arrow.png", new Vector2i(11, 11));
		addCursor(Cursor.CROSSHAIR, "crosshair.png", new Vector2i(12, 12));
	}

	private void addCursor(Cursor key, String file, Vector2i hotSpot) {
		String dir = "/assets/cursor/" + file;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		customCursors.put(key, toolkit.createCustomCursor(getImageFromResource(dir), new Point(hotSpot.x, hotSpot.y), "img"));
	}

	private void addListeners() {
		canvas.addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e) {
		        width = e.getComponent().getWidth();
		        height = e.getComponent().getHeight();
		        onResized();
		    }
		});
		frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e) {
            	forceClosed();
            }
		});
		frame.addWindowStateListener(new WindowStateListener() {
	        public void windowStateChanged(WindowEvent event) {
	            maximized = (event.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
	        }
	    });
		canvas.addMouseListener(new InputListener(inputHandler));
		canvas.addMouseMotionListener(new InputListener(inputHandler));
		canvas.addKeyListener(new InputListener(inputHandler));
		canvas.addMouseWheelListener(new InputListener(inputHandler));
	}

	protected void forceClosed() {
		open = false;
	}

	protected void onResized() {
		//empty atm
	}

	private BufferedImage getImageFromResource(String dir) {
		try {
			return ImageIO.read(getClass().getResourceAsStream(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void open(){
		frame.setVisible(true);
		open = true;
        width = canvas.getWidth();
        height = canvas.getHeight();
	}

	@Override
	public void close(){
		frame.dispose();
		open = false;
	}

	@Override
	public void show(){
		if(!open) return;
		try{
			bufferStrategy.show();
		}catch(java.lang.IllegalStateException e){
			
		}
	}

	@Override
	public void update() {
		inputHandler.update();
		if(nextCursorForUpdate != null){
			if(nextCursorForUpdate == Cursor.DEFAULT) frame.setCursor(java.awt.Cursor.getDefaultCursor());
			else if(customCursors.containsKey(nextCursorForUpdate)) frame.setCursor(customCursors.get(nextCursorForUpdate));
			nextCursorForUpdate = null;
		}
	}
	
	@Override
	public boolean isOpen(){
		return open;
	}
	
	@Override
	public Vector2i getSize(){
		return new Vector2i(width, height);
	}
	
	@Override
	public InputHandler getInput(){
		return inputHandler;
	}

	@Override
	public Graphics getGraphics() throws ClosedWindowException {
		try {
			if(!frame.isShowing()) throw new ClosedWindowException();
			Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
			g.clearRect(0, 0, width, height);
			return new GraphicsDesktop(g, antiAlias);
		} catch (IllegalStateException e) {
			System.err.println("Rare getGraphics bug");
			e.printStackTrace();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return getGraphics();
		}
	}

	@Override
	public ResourceLoader getNewResourceLoader() {
		return new ResourceLoaderDesktop("default");
	}

	@Override
	public ResourceLoader getNewSpriteLoader(int index, int maxSprites) {
		return new SpriteLoaderDesktop("default", index, maxSprites);
	}

	@Override
	public void setCursor(Cursor cursor) {
		nextCursorForUpdate = cursor;
		this.cursor = cursor;
	}

	@Override
	public Cursor getCursor() {
		return this.cursor;
	}

	public Window createDialog(WindowConfig windowConfig) {
		return new WindowDesktop(windowConfig);
	}
	
	/*@Override
	public Window createDialog(String dialogParameters) {
		return new WindowDesktop(dialogParameters, null);
	}

	@Override
	public Window createDialog(String dialogParameters, Runnable onCloseAttempt) {
		return new WindowDesktop(dialogParameters, onCloseAttempt);
	}*/
	
	public void setBounds(Recti bounds){
		int windowHeightDifference = frame.getHeight() - contentPane.getHeight() - frame.getInsets().top;
		int windowWidthDifference = frame.getWidth() - contentPane.getWidth();
		frame.setBounds(bounds.x + 1, bounds.y + frame.getInsets().top, bounds.width + windowWidthDifference - 1, bounds.height + windowHeightDifference);
	}

	@Override
	public Vector2i getPosition() {
		return new Vector2i(frame.getX(), frame.getY());
	}

	@Override
	public String[] getGLENScripts() {
		List<File> scripts = new ArrayList<File>();
		try {
			URI uri = getClass().getResource("/assets/glen/ui").toURI();
	        Path myPath;
	        if (uri.getScheme().equals("jar")) {
	            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
	            myPath = fileSystem.getPath("/assets/glen/ui");
	        } else {
	            myPath = Paths.get(uri);
	        }
	        Stream<Path> walk = Files.walk(myPath, 1);
	        for (Iterator<Path> it = walk.iterator(); it.hasNext();){
	        	Path path = it.next();
	        	File file = new File(path.toUri());
	        	String[] s = file.getName().split("\\.");
				if(s.length > 1 && s[s.length - 1].equalsIgnoreCase("glen")){
					scripts.add(file);
				}
	        }
	        walk.close();
		} catch(URISyntaxException | IOException | NullPointerException e){
			return new String[]{};
		} catch(IllegalArgumentException e){
			//could not find glen scripts from resources in jar file
		}

		File fis = new File("./GLEN/");
		if(fis.listFiles() != null) for(File f : fis.listFiles()){
			String[] s = f.getName().split("\\.");
			if(s.length > 1 && s[s.length - 1].equalsIgnoreCase("glen")){
				scripts.add(f);
			}
		}
		//insert 
		for(String r : filesFromResource ){
			for(int i = scripts.size() - 1; i >= 0; i--){
				if(scripts.get(i).getName().replace(".glen", "").equals(r.replace(".glen", ""))){
					scripts.remove(i);
					break;
				}
			}
		}
		String[] out = new String[scripts.size() + filesFromResource.length];
		try {
			BufferedReader[] readers = new BufferedReader[scripts.size() + filesFromResource.length];
			String[] name = new String[scripts.size() + filesFromResource.length];
			for(int i = 0; i < scripts.size(); i++){
				File file = scripts.get(i);
				readers[i] = new BufferedReader(new FileReader(file));
				name[i] = file.getName();
			}
			for(int i = 0; i < filesFromResource.length; i++){
				readers[i + scripts.size()] = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/assets/glen/ui/" + filesFromResource[i])));
				name[i + scripts.size()] = filesFromResource[i];
			}
			
			for(int i = 0; i < readers.length; i++){
				BufferedReader reader = readers[i];
				
				StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				String ls = System.getProperty("line.separator");
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
					stringBuilder.append(ls);
				}
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				reader.close();
				
				String code = stringBuilder.toString();
				List<String> strings = new ArrayList<String>();
				
				boolean writingString = false;
				String str = "";
				String newCode = code;
				for(int j = 0; j < code.length(); j++){
					boolean escape = false;
					if(code.substring(j, j + 1).equals("\\")){
						j++;
						escape = true;
						
					}
					if(!escape && code.substring(j, j + 1).equals("\"")){
						writingString = !writingString;
						if(!writingString){
							newCode = newCode.replace("\"" + str + "\"", "~str");
							strings.add(str);
							str = "";
						}
					}else if(writingString){
						str += code.substring(j, j + 1);
					}
					escape = false;
				}
				String content = name[i] + "~" + newCode.replaceAll("\\s+", "").toLowerCase();
				for(int j = 0; j < strings.size(); j++){
					for(int k = 0; k < content.length(); k++){
						if(content.substring(k, k + 4).equals("~str")){
							content = content.substring(0, k) + "\"" + strings.get(j) + "\"" +  content.substring(k + 4);
							break;
						}
					}
				}
				out[i] = content;
			}
		} catch (IOException e) {
			return new String[]{};
		}
		
		return out;
	}

	@Override
	public void addGLENScriptsFromResource(String[] filenames) {
		filesFromResource = filenames;
	}

	@Override
	public String getChooseFile(String title, String[] fileTypes, boolean save) {
		inputHandler.setNoInput();
		FileDialog fd = new FileDialog(this.frame, title, 0);
		String documents = "C:\\";
		if(this.fdContext == null && System.getProperty("os.name").equals("Windows 10")){
			try {
				Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
				p.waitFor();
			    InputStream in = p.getInputStream();
			    byte[] b = new byte[in.available()];
			    in.read(b);
			    in.close();
			    documents = new String(b);
			    documents = documents.split("\\s\\s+")[4];
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
        fd.setDirectory((this.fdContext == null) ? documents : this.fdContext);
        /*FilenameFilter fnf = new FilenameFilter(){
			@Override
			public boolean accept(File file, String name) {
				if(fileTypes == null) return true;
				for(String s : fileTypes) if(name.endsWith("." + s)) return true;
				return false;
			}
     
        };
        fd.setFilenameFilter(fnf);*/
        String f = "";
        if(fileTypes != null){
        	for(String type : fileTypes) f += "*." + type + ";";
        	f = f.substring(0, f.length() - 1);
        }
        fd.setFile(f);
        if(save) fd.setMode(FileDialog.SAVE);
        fd.setVisible(true);
        String filename = fd.getFile();
        this.fdContext = fd.getDirectory();
        return fd.getDirectory() == null || filename == null ? null : fd.getDirectory() + filename;
	}

	@Override
	public String getChooseFile(String title) {
		return getChooseFile(title, null, false);
	}

	@Override
	public String getChooseFile(String title, String[] fileTypes) {
		return getChooseFile(title, fileTypes, false);
	}
	
	@Override
	public String getChooseFolder(String title) {
		inputHandler.setNoInput();
		FileDialog fd = new FileDialog(this.frame, title, 0);
		String documents = "C:\\";
		if(this.fdContext == null && System.getProperty("os.name").equals("Windows 10")){
			try {
				Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
				p.waitFor();
			    InputStream in = p.getInputStream();
			    byte[] b = new byte[in.available()];
			    in.read(b);
			    in.close();
			    documents = new String(b);
			    documents = documents.split("\\s\\s+")[4];
			} catch (IOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
        fd.setDirectory((this.fdContext == null) ? documents : this.fdContext);
        /*FilenameFilter fnf = new FilenameFilter(){
			@Override
			public boolean accept(File file, String name) {
				if(fileTypes == null) return true;
				for(String s : fileTypes) if(name.endsWith("." + s)) return true;
				return false;
			}
     
        };
        fd.setFilenameFilter(fnf);*/
        fd.setMode(FileDialog.LOAD);
        fd.setFilenameFilter(new FolderFilter());
        fd.setVisible(true);
        this.fdContext = fd.getDirectory();
        return fd.getDirectory() == null ? null : fd.getDirectory();
	}

	@Override
	public boolean isMaximized() {
		return maximized;
	}

	@Override
	public void setMaximized(boolean b) {
		if(b) frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		else frame.setExtendedState(JFrame.NORMAL);
	}

	@Override
	public boolean isServer() {
		return isServer;
	}

	@Override
	public int getServerPort() {
		return port;
	}

	@Override
	public boolean isOnline() {
		return isOnline;
	}

	@Override
	public void focus() {
		frame.toFront();
		frame.repaint();
	}

	@Override
	public int getServerWSPort() {
		return portWS;
	}
	
	class FolderFilter implements FilenameFilter
	{
	    public boolean accept(File dir, String name)
	    {
	        return new File(dir,name).isDirectory();
	    }
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}
	
}
