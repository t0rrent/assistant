package au.com.cascadesoftware.engine3.gui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

import org.glassfish.hk2.api.ServiceLocator;

import au.com.cascadesoftware.engine2.math.Recti;
import au.com.cascadesoftware.engine2.math.Vector2i;
//import au.com.cascadesoftware.engine3.Engine3;
import au.com.cascadesoftware.engine3.display.Window;
//import au.com.cascadesoftware.engine3.exception.PlatformException;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
import jakarta.inject.Inject;
//import au.com.cascadesoftware.engine3.gui.web.Container;
//import au.com.cascadesoftware.engine3.gui.web.HTMLDocument;
//import au.com.cascadesoftware.engine3.gui.web.Style;
//import au.com.cascadesoftware.engine3.gui.web.Styles;

public class GUI {

	public static final int DEFAULT_BIND_RANK = 5;
	
	private Window window;
	protected final int identifier;
	
	private Boundary bounds;
	private GUI parent;
	private List<GUI> guis;

	private Color background;
	private float opacity;
	
	//private Styles styles;
	//private String specialContainerName;
	//private String href;
	private int zIndex;
	
	private ServiceLocator serviceLocator;
	
	@Inject
	public GUI(Window window, Boundary bounds){
		identifier = new Random().nextInt(Integer.MAX_VALUE);
		this.bounds = bounds;
		this.window = window;
		guis = new ArrayList<GUI>();
		opacity = 1;
		//styles = new Styles();
	}
	
	protected void setParent(GUI parent){
		this.parent = parent;
	}
	
	public Window getWindow(){
		return window;
	}
	
	protected void draw(Graphics graphics) {}

	protected void overlayDraw(Graphics graphics) {}

	protected void overOverlayDraw(Graphics graphics) {}

	protected void updateInput() {}
	
	protected void updateInputOverride() {}

	protected void dispose() {}
	
	protected void onResize() {}
	
	protected boolean doControlTest() {
		GUI parent = getParent();
		boolean parentControl = false;
		while(parent != null){
			if(parent instanceof GUIContainerGrid){
				GUIContainerGrid g = (GUIContainerGrid) parent;
				if(g.isHolding()){
					parentControl = true;
					break;
				}
			}
			parent = parent.getParent();
		}
		return parentControl;
	}
	
	public Recti getOnScreenBounds(){
		Recti container = new Recti(0, 0, window.getSize().x, window.getSize().y);
		if(parent != null){
			if(parent == this) throw new RuntimeException();
			container = parent.getOnScreenBounds();
		}
		return bounds.getRect(container);
	}
	
	public Recti getRenderBounds(){
		Recti container = new Recti(0, 0, window.getSize().x, window.getSize().y);
		if(parent != null){
			if(parent == this) throw new RuntimeException();
			container = parent.getRenderBounds();
		}
		return bounds.getRect(container);
	}
	
	public void setBounds(Boundary bounds){
		this.bounds = bounds;
	}
	
	public void close(){
		for(GUI gui : guis){
			gui.close();
		}
		dispose();
	}
	
	public void clear(){
		try {
			for(GUI g : guis) g.close();
			guis.clear();
		} catch (ConcurrentModificationException e) {
			clear();
		}
	}
	
	public GUI setOpacity(float opacity){
		this.opacity = opacity;
		return this;
	}

	public void addGUI(GUI gui){
		/*if(gui.getClass().isAnnotationPresent(NativeUI.class)){
			if(((NativeUI)gui.getClass().getAnnotation(NativeUI.class)).platform() != Engine3.PLATFORM) throw new PlatformException();
		}*/
		gui.setParent(this);
		guis.add(gui);
	}
	
	public GUI getParent(){
		return this.parent;
	}
	
	protected void drawBackground(Graphics graphics){
		if(background != null && background.getAlpha() > 0){
			Paint paint = new Paint();
			paint.setColor(background);
			graphics.setPaint(paint);
			graphics.drawRect(getRenderBounds());
		}
	}
	
	public void render(Graphics graphics){
		try{
			float opacity = getRealizedOpacity();
			graphics.setOpacity(opacity);
			drawBackground(graphics);
			draw(graphics);
			for(GUI gui : guis){
				gui.render(graphics);
			}
			graphics.setOpacity(opacity);
			this.overlayDraw(graphics);
			if(parent == null){
				int biggestZIndex = 0;
				for(GUI gui : getAllChildren()){
					if(gui == null || gui.getWindow() != getWindow()) continue;
					if(gui.zIndex > biggestZIndex) biggestZIndex = gui.zIndex;
				}
				try{
					for(int i = 0; i <= biggestZIndex; i++){
						for(GUI gui : getAllChildren()){
							if(gui.getWindow() != getWindow()) continue;
							if(gui.zIndex == i) gui.overOverlayDraw(graphics);
						}
					}
				}catch(NullPointerException e){}
			}
		}catch(ConcurrentModificationException e){}
		catch(RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	private float getRealizedOpacity() {
		float parentOpacity = 1;
		if(parent != null) parentOpacity = parent.getRealizedOpacity();
		return parentOpacity*opacity;
	}

	public void update() {
		this.updateInput();
		final int n = guis.size();
		for(int i = 0; i < n; i++){
			if(n != guis.size()){
				update();
				return;
			}
			GUI gui = guis.get(i);
			if(gui != null) gui.update();
		}
		this.updateInputOverride();
	}

	public void resize() {
		this.onResize();
		try{
			for(GUI gui : guis){
				gui.resize();
			}
		}catch(ConcurrentModificationException e){
			resize();
		}
	}
	
	public GUI setBackground(Color bgColor) {
		this.background = bgColor;
		return this;
	}

	public Boundary getBoundary() {
		return bounds;
	}
	
	/*public String[] generateHTML(){
		//String[] getHTML();
		List<String> doc = new ArrayList<String>();
		String name = this.specialContainerName;
		if(name == null) name = this.identifier + "";
		String spacer = "";
		if(href != null){
			doc.add("<a href=\"" + href + "\">");
			spacer += "  ";
		}
		String clazz = getCSSClasses();
		if(clazz.length() > 0){
			clazz = "\" class=\"" + clazz;
		}
		
		doc.add(spacer + "<div id=\"container" + name + clazz + "\">");
		doc.add(spacer + "  <div id=\"content" + name + clazz + "\">");
		String[] content = getHTML();
		for(int i = 0; i < content.length; i++){
			doc.add(spacer + "    " + content[i]);
		}
		doc.add(spacer + "  </div>");
		for(GUI child : guis){
			if(child.getClass().isAnnotationPresent(ExcludeFromWeb.class)) continue;
			String[] childhtml = child.generateHTML();
			for(int i = 0; i < childhtml.length; i++){
				doc.add(spacer + "  " + childhtml[i]);
			}
		}
		doc.add(spacer + "</div>");
		if(href != null) doc.add("</a>");
		return doc.toArray(new String[doc.size()]);
	}

	public String getCSSClasses() {
		return "";
	}

	protected String[] getHTML() {return new String[]{};}

	public String getCSSContainerName() {
		String name = this.specialContainerName;
		if(name == null) name = this.identifier + "";
		return name;
	}

	public Color getBackgroundColor() {
		return background;
	}

	public HTMLDocument generateHTMLDoc() {
		HTMLDocument htmldoc = new HTMLDocument(generateHTML(), new Container(this).getCSS());
		return htmldoc;
	}*/

	public GUI[] getChildren() {
		return guis.toArray(new GUI[guis.size()]);
	}

	public GUI[] getAllChildren() {
		try{
			List<GUI> out = new ArrayList<GUI>();
			out.addAll(guis);
			for(GUI g : guis){
				GUI[] allChildren = g.getAllChildren();
				if(allChildren == null) return null;
				for(GUI g2 : allChildren){
					out.add(g2);
				}
			}
			return out.toArray(new GUI[out.size()]);
		}catch(java.util.NoSuchElementException e){
			return null;
		}catch(java.util.ConcurrentModificationException | java.lang.NullPointerException e){
			return getAllChildren();
		}
	}

	/*public Styles getCSS() {
		return styles;
	}
	
	public void injectCSS(Style[] styles){
		this.styles.inject(styles);
	}

	public void setCSSContainerName(String name) {
		this.specialContainerName = name;
	}
	
	public void setHref(String s){
		href = s;
	}*/

	public void setWindow(Window window) {
		this.window = window;
		for(GUI g : guis) g.setWindow(window);
	}
	
	public float getOpacity() {
		return opacity;
	}

	public void removeGUI(GUI g) {
		for(int i = 0; i < guis.size(); i++){
			if(g == guis.get(i)) guis.remove(i);
		}
	}

	public GUI setOverlayZIndex(int zIndex) {
		this.zIndex = zIndex;
		return this;
	}

	public SpecialClickBounds getSpecialClickBounds() {
		return new SpecialClickBounds(){
			@Override
			public boolean contains(Vector2i pos){
				return getOnScreenBounds().contains(pos);
			}
		};
	}

	public GUI setBackground(Object colorEnum) {
		Field[] fields = colorEnum.getClass().getFields();
		int i = 0;
		Field theField = null;
		for(Field f : fields){
			if(f.getType().equals(Color.class)){
				theField = f;
				i++;
			}
		}
		if(i == 1){
			try {
				setBackground((Color) theField.get(colorEnum));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return this;
	}
	
	protected ServiceLocator getServiceLocator() {
		if (this.serviceLocator == null && parent != null) {
			return parent.getServiceLocator();
		} else {
			return this.serviceLocator;
		}
	}
	
	public void setServiceLocator(final ServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
	
	protected <C> C createInjectable(final Class<C> type) {
		return getServiceLocator().create(type);
	}
	
}
