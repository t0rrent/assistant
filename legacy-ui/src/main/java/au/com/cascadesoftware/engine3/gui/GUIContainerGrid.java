package au.com.cascadesoftware.engine3.gui;

import java.util.ArrayList;
import java.util.List;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.display.Window.Cursor;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.graphics.Paint;
//import au.com.cascadesoftware.engine3.gui.web.Style;
import au.com.cascadesoftware.engine3.in.InputHandler;
import au.com.cascadesoftware.engine3.in.Mouse;

@MultipleChildGroups(dimensions = 2)
public class GUIContainerGrid extends GUI {
	
	private GUI[][] containers, containersAbove;
	private float borderWidth, insideBorderWidth;
	private boolean canResize;
	private boolean focusLast;
	private Color borderColor, insideBorderColor;

	private Vector2i holding;
	private Vector2i holdingPoint;
	private Vector2i reference;

	public GUIContainerGrid(Window window, Boundary bounds, Vector2i gridSize) {
		super(window, bounds);
		this.containers = new GUI[gridSize.x][gridSize.y];
		this.containersAbove = new GUI[gridSize.x][gridSize.y];
		canResize = true;
		holding = new Vector2i(-1, -1);
		float width = 1f/gridSize.x;
		float height = 1f/gridSize.y;
		for(int x = 0; x < containers.length; x++){
			for(int y = 0; y < containers[0].length; y++){
				containers[x][y] = new GUI(window, new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT)).setBackground(Color.INVISIBLE);
				super.addGUI(containers[x][y]);
			}
		}
		for(int x = 0; x < containersAbove.length; x++){
			for(int y = 0; y < containersAbove[0].length; y++){
				containersAbove[x][y] = new GUI(window, new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT)).setBackground(Color.INVISIBLE);
				containersAbove[x][y] .setParent(this);
			}
		}
	}

	@Override
	public void render(Graphics graphics){
		super.render(graphics);
		for(int x = 0; x < containersAbove.length; x++){
			for(int y = containersAbove[0].length - 1; y >= 0; y--){
				containersAbove[x][y].render(graphics);
			}
		}
	}
	
	@Override
	public void update() {
		super.update();
		for(int x = 0; x < containersAbove.length; x++){
			for(int y = 0; y < containersAbove[0].length; y++){
				containersAbove[x][y].update();
			}
		}
	}
	
	@Override
	protected void overlayDraw(Graphics graphics) {
		if(borderWidth > 0){
			Paint paint = new Paint();
			paint.setStrokeWidth(borderWidth);
			paint.setFill(false);
			paint.setColor(borderColor);
			graphics.setPaint(paint);
			graphics.drawRect(getRenderBounds());
		
		}
		if(insideBorderWidth > 0){
			Paint paint = new Paint();
			paint.setStrokeWidth(insideBorderWidth);
			paint.setColor(insideBorderColor);
			graphics.setPaint(paint);
			for(int x = 1; x < containers.length; x++){
				int a = (int) (containers[x][0].getRenderBounds().x);
				int y = (int) (getRenderBounds().y + insideBorderWidth/2);
				graphics.drawLine(a, y, a, (int) (y + getRenderBounds().height - insideBorderWidth/2));
			}
			for(int y = 1; y < containers[0].length; y++){
				int a = (int) (containers[0][y].getRenderBounds().y);
				int x = (int) (getRenderBounds().x + insideBorderWidth/2);
				graphics.drawLine(x, a, (int) (x + getRenderBounds().width - insideBorderWidth/2), a);
			}
		}
	}
	
	@Override
	protected void updateInputOverride() {
		InputHandler ih = getWindow().getInput();
		if(!canResize || doControlTest()) return;

		int overlap = 10;
		List<Vector2i> containerList = new ArrayList<Vector2i>();
		for(int x = 0; x < containers.length; x++){
			for(int y = 0; y < containers[0].length; y++){
				boolean grabbed = false;
				if(containers[x][y].getOnScreenBounds().contains(ih.getMousePos().add(new Vector2i((int)(-overlap-borderWidth/2),(int)(-overlap-borderWidth/2))))) grabbed = true;
				if(containers[x][y].getOnScreenBounds().contains(ih.getMousePos().add(new Vector2i((int)(overlap+borderWidth/2),(int)(-overlap-borderWidth/2))))) grabbed = true;
				if(containers[x][y].getOnScreenBounds().contains(ih.getMousePos().add(new Vector2i((int)(overlap+borderWidth/2),(int)(overlap+borderWidth/2))))) grabbed = true;
				if(containers[x][y].getOnScreenBounds().contains(ih.getMousePos().add(new Vector2i((int)(-overlap-borderWidth/2),(int)(overlap+borderWidth/2))))) grabbed = true;
				if(grabbed){
					boolean alreadyAdded = false;
					for(Vector2i v : containerList){
						if(v.x == x && v.y == y) alreadyAdded = true;
					}
					if(!alreadyAdded) containerList.add(new Vector2i(x, y));
				}
			}
		}
		Vector2i target = new Vector2i(-1, -1);
		if(containerList.size() == 2){
			if(containerList.get(0).x == containerList.get(1).x){
				int smallest = containerList.get(0).y;
				if(containerList.get(1).y < smallest) smallest = containerList.get(1).y;
				target.y = smallest;
			}else{
				int smallest = containerList.get(0).x;
				if(containerList.get(1).x < smallest) smallest = containerList.get(1).x;
				target.x = smallest;
			}
		}
		if(containerList.size() == 4){
			int smallestX = containerList.get(0).x;
			int smallestY = containerList.get(0).y;
			for(Vector2i v : containerList){
				if(smallestX > v.x) smallestX = v.x;
				if(smallestY > v.y) smallestY = v.y;
			}
			target.x = smallestX;
			target.y = smallestY;
		}
		
		if(ih.isMouseClicked(Mouse.BUTTON1) || ih.isScreenTapped()){
			holding = target;
			int hX = holding.x;
			int hY = holding.y;
			if(hX < 0) hX = 0;
			if(hY < 0) hY = 0;
			holdingPoint = new Vector2i(containers[hX][hY].getOnScreenBounds().width, containers[hX][hY].getOnScreenBounds().height);
			reference = new Vector2i(ih.getMousePos().x, ih.getMousePos().y);
		}else if(isHolding() && !(ih.isMouseDown(Mouse.BUTTON1) || ih.isScreenTouching())){
			holding = new Vector2i(-1, -1);
			resize();
			getWindow().setCursor(Cursor.DEFAULT);
		}else if(isHolding()){
			if(holding.x >= 0){
				int difference = ih.getMousePos().x - reference.x;
				float newWidth = (holdingPoint.x + difference)*1f/getOnScreenBounds().width;
				float x1 = (containers[holding.x][0].getOnScreenBounds().x - getOnScreenBounds().x)*1f/getOnScreenBounds().width;
				float x2 = x1 + newWidth;
				setColumn(holding.x, x2);
			}
			if(holding.y >= 0){
				int difference = ih.getMousePos().y - reference.y;
				float newHeight = (holdingPoint.y + difference)*1f/getOnScreenBounds().height;
				float y1 = (containers[0][holding.y].getOnScreenBounds().y - getOnScreenBounds().y)*1f/getOnScreenBounds().height;
				float y2 = y1 + newHeight;
				setRow(holding.y, y2);
			}
			resize();
		}
		boolean focus = containerList.size()>1;
		if(isHolding()){
			Cursor c = Cursor.QUAD_ARROW;
			if(holding.x < 0) c = Cursor.DOUBLE_ARROW_VERTICAL;
			if(holding.y < 0) c = Cursor.DOUBLE_ARROW_HORIZONTAL;
			getWindow().setCursor(c);
		}else if(focus){
			Cursor c = Cursor.QUAD_ARROW;
			if(target.x < 0) c = Cursor.DOUBLE_ARROW_VERTICAL;
			if(target.y < 0) c = Cursor.DOUBLE_ARROW_HORIZONTAL;
			getWindow().setCursor(c);
		}else if(focusLast){
			getWindow().setCursor(Cursor.DEFAULT);
		}
		focusLast = focus;
	}

	public boolean isHolding() {
		return holding.x >= 0 || holding.y >= 0;
	}

	public void setRow(int row, float position){
		float y1 = (containers[0][row].getOnScreenBounds().y - getOnScreenBounds().y)*1f/getOnScreenBounds().height;
		float y2 = position;
		float y3 = 1f;
		if(row < containers[0].length - 2){
			y3 = (containers[0][row + 2].getOnScreenBounds().y - getOnScreenBounds().y)*1f/getOnScreenBounds().height;
		}
		float newHeight = y2 - y1;
		float newHeight2 = y3 - y2;
		if(newHeight > 0.05f && newHeight2 > 0.05f){
			for(int x = 0; x < containers.length; x++){
				float xF = (containers[x][row].getOnScreenBounds().x - getOnScreenBounds().x)*1f/getOnScreenBounds().width;
				float width = containers[x][row].getOnScreenBounds().width*1f/getOnScreenBounds().width;
				containers[x][row].setBounds(new Boundary(new Rectf(xF, y1, width, newHeight), Scalar.STRETCHED, Alignment.TOP_LEFT));
				containers[x][row + 1].setBounds(new Boundary(new Rectf(xF, y2, width, newHeight2), Scalar.STRETCHED, Alignment.TOP_LEFT));
				containersAbove[x][row].setBounds(new Boundary(new Rectf(xF, y1, width, newHeight), Scalar.STRETCHED, Alignment.TOP_LEFT));
				containersAbove[x][row + 1].setBounds(new Boundary(new Rectf(xF, y2, width, newHeight2), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
	}

	public void setColumn(int column, float position){
		float x1 = (containers[column][0].getOnScreenBounds().x - getOnScreenBounds().x)*1f/getOnScreenBounds().width;
		float x2 = position;
		float x3 = 1f;
		if(column < containers.length - 2){
			x3 = (containers[column + 2][0].getOnScreenBounds().x - getOnScreenBounds().x)*1f/getOnScreenBounds().width;
		}
		float newWidth = x2 - x1;
		float newWidth2 = x3 - x2;
		if(newWidth > 0.05f && newWidth2 > 0.05f){
			//System.out.println(difference);
			for(int y = 0; y < containers[0].length; y++){
				float yF = (containers[column][y].getOnScreenBounds().y - getOnScreenBounds().y)*1f/getOnScreenBounds().height;
				float height = containers[column][y].getOnScreenBounds().height*1f/getOnScreenBounds().height;
				//System.out.println();
				containers[column][y].setBounds(new Boundary(new Rectf(x1, yF, newWidth, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
				containers[column + 1][y].setBounds(new Boundary(new Rectf(x2, yF, newWidth2, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
				containersAbove[column][y].setBounds(new Boundary(new Rectf(x1, yF, newWidth, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
				containersAbove[column + 1][y].setBounds(new Boundary(new Rectf(x2, yF, newWidth2, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
	}
	
	@Override
	public GUI[] getAllChildren() {
		List<GUI> out = new ArrayList<GUI>();
		List<GUI> guis = new ArrayList<GUI>();
		for(int x = 0; x < containers.length; x++){
			for(int y = 0; y < containers[0].length; y++){
				guis.add(containers[x][y]);
			}
		}
		for(int x = 0; x < containers.length; x++){
			for(int y = 0; y < containers[0].length; y++){
				guis.add(containersAbove[x][y]);
			}
		}
		out.addAll(guis);
		for(GUI g : guis){
			if (g != null) {
				for(GUI g2 : g.getAllChildren()) out.add(g2);
			}
		}
		return out.toArray(new GUI[out.size()]);
	}
	
	@Override
	protected void onResize() {
		for(int x = 0; x < containersAbove.length; x++){
			for(int y = 0; y < containersAbove[0].length; y++){
				containersAbove[x][y].resize();
			}
		}
	}
	
	public void setBorder(float width, Color color){
		this.borderWidth = width;
		this.borderColor = color;
	}
	
	public void setResizable(boolean r){
		this.canResize = r;
	}

	@Override
	public void addGUI(GUI gui) {
		addGUI(gui, new Vector2i());
	}

	public void addGUI(GUI gui, Vector2i gridLocation) {
		//modify(gui, gridLocation);
		containers[gridLocation.x][gridLocation.y].addGUI(gui);
	}

	public void addGUIAboveBorder(GUI gui, Vector2i gridLocation) {
		//modify(gui, gridLocation);
		containersAbove[gridLocation.x][gridLocation.y].addGUI(gui);
	}

	/*private void modify(GUI gui, Vector2i gridLocation) {
		boolean top = gridLocation.y == 0;
		boolean bottom = gridLocation.y == containers[0].length - 1;
		boolean left = gridLocation.x == 0;
		boolean right = gridLocation.x == containers.length - 1;
		float borderSizeLeft = left ? borderWidth : insideBorderWidth;
		Color borderColorLeft = left ? borderColor : insideBorderColor;
		float borderSizeBottom = bottom ? borderWidth : insideBorderWidth;
		Color borderColorBottom = bottom ? borderColor : insideBorderColor;
		float borderSizeRight = right ? borderWidth : insideBorderWidth;
		Color borderColorRight = right ? borderColor : insideBorderColor;
		float borderSizeTop = top ? borderWidth : insideBorderWidth;
		Color borderColorTop = top ? borderColor : insideBorderColor;
		List<Style> styles = new ArrayList<Style>();
		if(borderColorLeft != null) styles.add(new Style("border-left", borderSizeLeft + "px solid " + borderColorLeft.getHTMLCode()));
		if(borderColorRight != null) styles.add(new Style("border-right", borderSizeRight + "px solid " + borderColorRight.getHTMLCode()));
		if(borderColorTop != null) styles.add(new Style("border-top", borderSizeTop + "px solid " + borderColorTop.getHTMLCode()));
		if(borderColorBottom != null) styles.add(new Style("border-bottom", borderSizeBottom + "px solid " + borderColorBottom.getHTMLCode()));
		float borderVert = borderSizeTop + borderSizeBottom;
		float borderHor = borderSizeLeft + borderSizeRight;
		styles.add(new Style("@height-subtractor", borderVert + "px"));
		styles.add(new Style("@width-subtractor", borderHor + "px"));
		gui.injectCSS(styles.toArray(new Style[styles.size()]));
	}*/

	public void setInsideBorder(float width, Color color){
		this.insideBorderWidth = width;
		this.insideBorderColor = color;
	}

	public void addRow() {
		addRow(containers[0].length);
	}

	public void addRow(int insert){
		Vector2i gridSize = new Vector2i(containers.length, containers[0].length + 1);
		float width = 1f/gridSize.x;
		float height = 1f/gridSize.y;
		GUI[][] containersNew = new GUI[gridSize.x][gridSize.y], containersAboveNew = new GUI[gridSize.x][gridSize.y];
		for(int x = 0; x < containers.length; x++){
			for(int y = 0; y < insert; y++){
				containersNew[x][y] = containers[x][y];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			containersNew[x][insert] = new GUI(getWindow(), new Boundary(new Rectf(x*width, insert*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT)).setBackground(Color.INVISIBLE);
			super.addGUI(containersNew[x][insert]);
			for(int y = insert + 1; y < gridSize.y; y++){
				containersNew[x][y] = containers[x][y - 1];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		for(int x = 0; x < containersAbove.length; x++){
			for(int y = 0; y < insert; y++){
				containersAboveNew[x][y] = containersAbove[x][y];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			containersAboveNew[x][insert] = new GUI(getWindow(), new Boundary(new Rectf(x*width, insert*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT)).setBackground(Color.INVISIBLE);
			containersAboveNew[x][insert] .setParent(this);
			for(int y = insert + 1; y < gridSize.y; y++){
				containersAboveNew[x][y] = containersAbove[x][y - 1];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		containers = containersNew;
		containersAbove = containersAboveNew;
	}

	public void addColumn() {
		addColumn(containers.length);
	}

	public void addColumn(int insert){
		Vector2i gridSize = new Vector2i(containers.length + 1, containers[0].length);
		float width = 1f/gridSize.x;
		float height = 1f/gridSize.y;
		GUI[][] containersNew = new GUI[gridSize.x][gridSize.y], containersAboveNew = new GUI[gridSize.x][gridSize.y];
		for(int y = 0; y < containers[0].length; y++){
			for(int x = 0; x < insert; x++){
				containersNew[x][y] = containers[x][y];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			containersNew[insert][y] = new GUI(getWindow(), new Boundary(new Rectf(insert*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT)).setBackground(Color.INVISIBLE);
			super.addGUI(containersNew[insert][y]);
			for(int x = insert + 1; x < gridSize.x; x++){
				containersNew[x][y] = containers[x - 1][y];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		for(int y = 0; y < containersAbove[0].length; y++){
			for(int x = 0; x < insert; x++){
				containersAboveNew[x][y] = containersAbove[x][y];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			containersAboveNew[insert][y] = new GUI(getWindow(), new Boundary(new Rectf(insert*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT)).setBackground(Color.INVISIBLE);
			containersAboveNew[insert][y] .setParent(this);
			for(int x = insert + 1; x < gridSize.x; x++){
				containersAboveNew[x][y] = containersAbove[x - 1][y];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		containers = containersNew;
		containersAbove = containersAboveNew;
	}

	public void removeRow() {
		removeRow(containers[0].length - 1);
	}

	public void removeRow(int i) {
		Vector2i gridSize = new Vector2i(containers.length, containers[0].length - 1);
		float width = 1f/gridSize.x;
		float height = 1f/gridSize.y;
		GUI[][] containersNew = new GUI[gridSize.x][gridSize.y], containersAboveNew = new GUI[gridSize.x][gridSize.y];
		for(int x = 0; x < containers.length; x++){
			for(int y = 0; y < i; y++){
				containersNew[x][y] = containers[x][y];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			super.removeGUI(containers[x][i]);
			for(int y = i; y < gridSize.y; y++){
				containersNew[x][y] = containers[x][y + 1];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		for(int x = 0; x < containersAbove.length; x++){
			for(int y = 0; y < i; y++){
				containersAboveNew[x][y] = containersAbove[x][y];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			for(int y = i; y < gridSize.y; y++){
				containersAboveNew[x][y] = containersAbove[x][y + 1];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		containers = containersNew;
		containersAbove = containersAboveNew;
	}

	public void removeColumn() {
		removeColumn(containers.length - 1);
	}

	public void removeColumn(int i) {
		Vector2i gridSize = new Vector2i(containers.length - 1, containers[0].length);
		float width = 1f/gridSize.x;
		float height = 1f/gridSize.y;
		GUI[][] containersNew = new GUI[gridSize.x][gridSize.y], containersAboveNew = new GUI[gridSize.x][gridSize.y];
		for(int y = 0; y < containers[0].length; y++){
			for(int x = 0; x < i; x++){
				containersNew[x][y] = containers[x][y];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			super.removeGUI(containers[i][y]);
			for(int x = i; x < gridSize.x; x++){
				containersNew[x][y] = containers[x + 1][y];
				containersNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		for(int y = 0; y < containersAbove[0].length; y++){
			for(int x = 0; x < i; x++){
				containersAboveNew[x][y] = containersAbove[x][y];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
			for(int x = i; x < gridSize.x; x++){
				containersAboveNew[x][y] = containersAbove[x + 1][y];
				containersAboveNew[x][y].setBounds(new Boundary(new Rectf(x*width, y*height, width, height), Scalar.STRETCHED, Alignment.TOP_LEFT));
			}
		}
		containers = containersNew;
		containersAbove = containersAboveNew;
	}

	public void clear(Vector2i vector2i) {
		containers[vector2i.x][vector2i.y].clear();
		containersAbove[vector2i.x][vector2i.y].clear();
	}
	
	public Vector2i getSize(){
		return new Vector2i(containers.length, containers[0].length);
	}
	
}