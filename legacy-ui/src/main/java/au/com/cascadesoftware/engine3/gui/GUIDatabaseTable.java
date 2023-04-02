package au.com.cascadesoftware.engine3.gui;

/*import java.util.ArrayList;
import java.util.List;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.chart.Date;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.engine3.graphics.Graphics;
import au.com.cascadesoftware.engine3.gui.GUIDropDown.Mode;
import au.com.cascadesoftware.engine3.gui.GUIScrollBar.GUIScrollBarFancy;
import au.com.cascadesoftware.engine3.in.Keyboard;
import au.com.cascadesoftware.engine3.internet.mysql.Table;
import au.com.cascadesoftware.engine3.internet.mysql.TableFormat.DataType;

public class GUIDatabaseTable extends GUI {
	
	private GUIContainerGrid container;
	private String[][] data;
	private GUI sb, oven, updateRect;
	private GUIText updateText;
	private Table table;
	private Theme theme;
	private boolean canUpdate;
	private boolean removing;
	List<GUITextboxDesktop> textboxes;
	List<GUI> removeButtons;

	public GUIDatabaseTable(Window window, Boundary bounds, Table table, Theme theme) {
		super(window, bounds);
		this.theme = theme;
		textboxes = new ArrayList<GUITextboxDesktop>();
		removeButtons = new ArrayList<GUI>();
		updateTable(table);
	}

	public void updateTable(Table table) {
		this.clear();
		this.table = table;
		
		updateRect = new GUIShapeRectangle(getWindow(), new Boundary(), Color.INVISIBLE).setBorder(1, Color.GARY).setBackground(new Color(212, 212, 212));
		updateRect.addGUI(new GUIHover(getWindow(), new Boundary()) {
			@Override
			protected void onStateChange(float phase) {
				setBackground(new Color((int) (0x22000000*(canUpdate ? phase : 0))));
			}
		});
		updateText = new GUIText(getWindow(), new Boundary(), Color.LIGHT_GARY, "Update");
		GUIButton update = new GUIButton(getWindow(), new Boundary(new Rectf(0.4f, 0, 0.1f, 0.5f))){
			@Override
			public void onClick() {
				if(!canUpdate) return;
				table.update(data);
				canUpdate = false;
				onUpdate();
				updateRect.setBackground(new Color(212, 212, 212));
				updateText.setTextColor(Color.LIGHT_GARY);
				updateRect.clear();
			}
		};
		update.addGUI(updateRect);
		update.addGUI(updateText);

		GUI addRect = new GUIShapeRectangle(getWindow(), new Boundary(), Color.LIGHT_GARY).setBorder(1, Color.GARY);
		GUI addText = new GUIText(getWindow(), new Boundary(), Color.DARK_GARY, "Add");
		GUIButton add = new GUIButton(getWindow(), new Boundary(new Rectf(-0.4f, 0, 0.1f, 0.5f))){
			@Override
			public void onClick() {
				removing = false;
				updateRemoving();
				addRow();
			}
		};
		addRect.addGUI(new GUIHover(getWindow(), new Boundary()) {
			@Override
			protected void onStateChange(float phase) {
				setBackground(new Color((int) (0x22000000*phase)));
			}
		});
		add.addGUI(addRect);
		add.addGUI(addText);

		GUI removeRect = new GUIShapeRectangle(getWindow(), new Boundary(), Color.LIGHT_GARY).setBorder(1, Color.GARY);
		GUI removeText = new GUIText(getWindow(), new Boundary(), Color.DARK_GARY, "Remove");
		GUIButton remove = new GUIButton(getWindow(), new Boundary(new Rectf(0, 0, 0.1f, 0.5f))){
			@Override
			public void onClick() {
				removing = !removing;
				updateRemoving();
			}
		};
		removeRect.addGUI(new GUIHover(getWindow(), new Boundary()) {
			@Override
			protected void onStateChange(float phase) {
				setBackground(new Color((int) (0x22000000*phase)));
			}
		});
		remove.addGUI(removeRect);
		remove.addGUI(removeText);

		int rows = table.getRepresentation().length;
		int columns = table.getFormat().columns.length;
		
		String[][] values = table.getRepresentation();
		data = new String[values.length][];
		for(int x = 0; x < values.length; x++){
			String[] r = new String[values[x].length];
			for(int y = 0; y < r.length; y++) r[y] = values[x][y];
			data[x] = r;
		}

		sb = new GUIScrollBarFancy(getWindow(), new Boundary(new Rectf(0, -0.05f, 1, 0.9f), Scalar.STRETCHED, Alignment.MIDDLE_RIGHT), Orientation.PORTRAIT, Color.GRAY){
			@Override
			protected void onScrollChanged(float scroll) {
				int diff = container.getOnScreenBounds().height - oven.getOnScreenBounds().height;
				float offset = diff*scroll/container.getOnScreenBounds().height;
				Boundary b = container.getBoundary();
				container.setBounds(new Boundary(new Rectf(b.coordinates.x, -offset*b.coordinates.height, b.coordinates.width, b.coordinates.height), b.scalar, b.alignment));
				for(int row = 0; row < removeButtons.size(); row++){
					b = container.getBoundary();
					removeButtons.get(row).setBounds(new Boundary(new Rectf(b.coordinates.x, b.coordinates.y + b.coordinates.height/data.length*row, b.coordinates.width, b.coordinates.height/data.length), b.scalar, b.alignment));
				}
				GUIDatabaseTable.this.resize();
			}
		};
		sb.setBackground(Color.LIGHT_GRAY);
		oven = new GUIOven(getWindow(), new Boundary(new Rectf(0, -0.05f, 1, 0.9f))){
			@Override
			public void render(Graphics graphics) {
				if(canvas == null) createCanvas(graphics);
				clearCanvas();
				redrawCanvas();
				canvas.draw(graphics);
			}
		};
		((GUIOven) oven).setBakeOnResize(true);
		
		if(rows == 0){
			container = new GUIContainerGrid(getWindow(), new Boundary(new Rectf(0, 0, 1, 1f/getTotalWidth()*1*2), Scalar.HORIZONTAL, Alignment.TOP_LEFT), new Vector2i(columns, 0));
			addRow();
		}else{
			container = new GUIContainerGrid(getWindow(), new Boundary(new Rectf(0, 0, 1, 1f/getTotalWidth()*rows*2), Scalar.HORIZONTAL, Alignment.TOP_LEFT), new Vector2i(columns, rows));
			for(int row = 0; row < rows; row++){
				initRow(row);
			}
		}
		resizeContainer();
		container.setResizable(true);
		container.setInsideBorder(1, Color.BLACK);
		container.setBorder(1, Color.BLACK);
		container.setBackground(theme.primaryElementColor);
		
		oven.addGUI(container);
		addGUI(oven);
		
		GUI bottomBar = new GUI(getWindow(), new Boundary(new Rectf(0, 0.45f, 1, 0.1f))).setBackground(new Color(224, 224, 224));
		bottomBar.addGUI(update);
		bottomBar.addGUI(add);
		bottomBar.addGUI(remove);
		addGUI(sb);
		addGUI(bottomBar);
		onResize();
	}

	protected void updateRemoving() {
		container.setBackground(removing ? new Color(0xFFFF7777) : theme.primaryElementColor);
		if(removing){
			for(int row = 0; row < data.length; row++){
				int rowF = row;
				Boundary b = container.getBoundary();
				GUIButton button = new GUIButton(getWindow(), new Boundary(new Rectf(b.coordinates.x, b.coordinates.y + b.coordinates.height/data.length*row, b.coordinates.width, b.coordinates.height/data.length), b.scalar, b.alignment)){
					@Override
					public void onClick() {
						removing = !removing;
						updateRemoving();
						removeRow(rowF);
					}
				};
				button.addGUI(new GUIHover(getWindow(), new Boundary()) {
					@Override
					protected void onStateChange(float phase) {
						setBackground(new Color((int) (0x22000000*phase)));
					}
				});
				oven.addGUI(button);
				removeButtons.add(button);
			}
		}else{
			for(GUI g : removeButtons){
				oven.removeGUI(g);
			}
			removeButtons.clear();
		}
	}

	private void initRow(int row) {
		int columns = table.getFormat().columns.length;float[] fixedColumns = new float[columns - 1];
		String[][] values = table.getRepresentation();
		float totalWidth = 0;
		for(int i = 0; i < fixedColumns.length; i++){
			totalWidth += 1f/getRelativeHeight(table.getFormat().types[i]);
			fixedColumns[i] = totalWidth;
		}
		totalWidth += 1f/getRelativeHeight(table.getFormat().types[fixedColumns.length]);
		for(int column = 0; column < columns; column++){
			final int columnF = column;
			DataType type = table.getFormat().types[column];
			float height = getRelativeHeight(table.getFormat().types[column]);
			boolean above = false;
			GUI box = new GUIText(getWindow(), new Boundary(new Rectf(0, 0, 1, height), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), theme.textColor, values[row][column]);
			if(table.getTableFromForeignKey(column) != null){
				Table foreign = table.getTableFromForeignKey(column);
				String[][] foreignData = foreign.getRepresentation();
				boolean leftMost = false;
				float n = totalWidth;
				float m = 0;
				if(column < columns - 1) n = fixedColumns[column];
				if(column > 0) m = fixedColumns[column - 1];
				if((n + m)/2f > totalWidth) leftMost = true;
				float w = n/(n - m);
				if(leftMost) w = totalWidth - w;
				GUI[] buttons = new GUI[foreignData.length];
				GUIText text = new GUIText(getWindow(), new Boundary(new Rectf(0, 0, 1, 0.5f), Scalar.STRETCHED, Alignment.MIDDLE_CENTER), theme.textColor, values[row][column]);
				GUI textcontainer = new GUI(getWindow(), new Boundary(new Rectf(0, 0, 1, 1), Scalar.STRETCHED, Alignment.MIDDLE_CENTER));
				GUIDropDown dd = new GUIDropDown(getWindow(), new Boundary(new Rectf(0, 0, 1, height), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), Mode.TOGGLE_ON_CLICK, foreignData.length){
					@Override
					protected void onOpenStateChanged(boolean open) {
						for(GUI b : buttons){
							b.setOpacity(open ? 1 : 0);
						}
						if(open) textcontainer.setBackground(new Color("#0002"));
						else textcontainer.setBackground(Color.INVISIBLE);
					}
				};
				for(int i = 0; i < foreignData.length; i++){
					int iF = i;
					GUIButton button = (GUIButton) new GUIButton(getWindow(), new Boundary(new Rectf(0, 0, w/columns, 1), Scalar.STRETCHED, leftMost ? Alignment.TOP_LEFT : Alignment.TOP_RIGHT)){
						@Override
						public void onClick() {
							if(text.getOnScreenBounds().contains(getWindow().getInput().getMousePos()) || getOpacity() < 0.1f) return;
							if(!text.equals(foreignData[iF][foreign.getFormat().primaryKey])) updateUpdate();
							text.setText(foreignData[iF][foreign.getFormat().primaryKey]);
							data[row][columnF] = foreignData[iF][foreign.getFormat().primaryKey];
							for(GUITextboxDesktop tb : textboxes) tb.setFocus(false);
						}
					}.setOpacity(0);
					dd.addGUI(button, i + 1);
					buttons[i] = button;

					int subColumns = foreign.getFormat().types.length;
					GUIContainerGrid subContainer = new GUIContainerGrid(getWindow(), new Boundary(new Rectf(0, 0, 1, 1), Scalar.STRETCHED, Alignment.TOP_LEFT), new Vector2i(subColumns, 1));
					subContainer.setBackground(theme.secondaryElementColor);
					subContainer.setBorder(1, Color.BLACK);
					subContainer.setInsideBorder(1, Color.BLACK);
					float[] subFixedColumns = new float[subColumns - 1];
					float subTotalWidth = 0;
					for(int j = 0; j < subFixedColumns.length; j++){
						subTotalWidth += 1f/getRelativeHeight(foreign.getFormat().types[j]);
						subFixedColumns[j] = subTotalWidth;
					}
					subTotalWidth += 1f/getRelativeHeight(foreign.getFormat().types[subFixedColumns.length]);
					for(int subColumn = 0; subColumn < subColumns; subColumn++){
						DataType subType = foreign.getFormat().types[subColumn];
						float h = getRelativeHeight(subType);
						//System.out.println(h + ", " + subType);
						GUI r = new GUIText(getWindow(), new Boundary(new Rectf(0, 0, 1, h), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), theme.textColor, foreignData[i][subColumn]);
						subContainer.addGUI(r, new Vector2i(subColumn, 0));
					}
					for(int j = 0; j < subFixedColumns.length; j++){
						subContainer.setColumn(j, subFixedColumns[j]/subTotalWidth);
					}
					subContainer.setResizable(false);
					button.addGUI(subContainer);
					button.addGUI(new GUIHover(getWindow(), new Boundary()) {
						@Override
						protected void onStateChange(float phase) {
							setBackground(new Color((int) (0x22000000*phase)));
						}
					});
					
				}
				dd.addGUI(new GUIHover(getWindow(), new Boundary()) {
					@Override
					protected void onStateChange(float phase) {
						setBackground(new Color((int) (0x22000000*phase)));
					}
				});
				textcontainer.addGUI(text);
				dd.addGUI(textcontainer);
				box = dd;
				above = true;
			}else if(table.getFormat().primaryKey != column) if(type == DataType.STRING || type == DataType.INT || type == DataType.UNSIGNED_INT){ 
				Theme theme2 = new Theme(Color.INVISIBLE, theme.secondaryElementColor, theme.background, theme.borderColor, theme.textColor, theme.subTextColor);
				GUITextboxDesktop tb = new GUITextboxDesktop(getWindow(), new Boundary(new Rectf(0, 0, 1, height), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), 1, theme2){
					@Override
					protected boolean validate(String keytext) {
						if(type == DataType.STRING) return true;
						for(int i = 0; i < 10; i++) if(keytext.equals(i + "")) return true;
						if(type == DataType.INT) if(keytext.equals("-")) return true;
						return false;
					}
					
					@Override
					protected void onTextChanged(){
						updateUpdate();
						data[row][columnF] = getInputText();
					}
				};
				tb.setInputText(values[row][column]);
				tb.setPlaceholderText("Enter value");
				textboxes.add(tb);
				box = tb;
			}else if(type == DataType.DATE){
				final GUIText dateBox = new GUIText(getWindow(), new Boundary(new Rectf(0, 0, 1, height), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), theme.textColor, values[row][columnF]);
				dateBox.addGUI(new GUIHover(getWindow(), new Boundary(new Rectf(0, 0, 1, 2))) {
					@Override
					protected void onStateChange(float phase) {
						setBackground(new Color((int) (0x22000000*phase)));
					}
				});
				dateBox.addGUI(new GUIButton(getWindow(), new Boundary(new Rectf(0, 0, 1, 2))){
					@Override
					public void onClick() {
						GUIDateDialog d = new GUIDateDialog(getWindow(), new Boundary(new Rectf(0, 0, 1.5f*200f/this.getOnScreenBounds().width, 1.5f*250f/this.getOnScreenBounds().height), Scalar.STRETCHED, columnF == 0 ? Alignment.TOP_LEFT : (columnF == columns - 1 ? Alignment.TOP_RIGHT : Alignment.TOP_CENTER)), "date_dialog", new Date(1, 1, 2000)){
							@Override
							protected void onDateChanged(Date d) {
								String date = d.year + "-" + (d.month < 10 ? "0" : "") + d.month + "-" + (d.day < 10 ? "0" : "") + d.day;
								updateUpdate();
								data[row][columnF] = date;
								dateBox.setText(date);
							}
						};
						this.addGUI(d);
					};
				});
				box = dateBox;
			}
			if(above) container.addGUIAboveBorder(box, new Vector2i(column, row));
			else container.addGUI(box, new Vector2i(column, row));
		}
	}

	protected void addRow() {
		table.addRow();
		container.addRow();
		container.setBounds(new Boundary(new Rectf(0, 0, 1, 1f/getTotalWidth()*(data.length + 1)*2), Scalar.HORIZONTAL, Alignment.TOP_LEFT));
		resizeContainer();
		
		String[][] newData = new String[data.length + 1][];
		for(int x = 0; x < data.length; x++){
			String[] r = new String[data[x].length];
			for(int y = 0; y < data[x].length; y++){
				r[y] = data[x][y];
			}
			newData[x] = r;
		}
		String[] r = new String[table.getRepresentation()[table.getRepresentation().length - 1].length];
		for(int x = 0; x < r.length; x++){
			r[x] = table.getRepresentation()[table.getRepresentation().length - 1][x];
		}
		newData[table.getRepresentation().length - 1] = r;
		data = newData;
		initRow(table.getRepresentation().length - 1);
		updateUpdate();
		resize();
	}

	protected void removeRow(int i) {
		table.removeRow(i);
		container.removeRow(i);
		container.setBounds(new Boundary(new Rectf(0, 0, 1, 1f/getTotalWidth()*(data.length - 1)*2), Scalar.HORIZONTAL, Alignment.TOP_LEFT));
		resizeContainer();
		String[][] newData = new String[data.length - 1][];
		for(int x = 0; x < i; x++){
			String[] r = new String[data[x].length];
			for(int y = 0; y < data[x].length; y++){
				float height = getRelativeHeight(table.getFormat().types[y]);
				if(this.table.getFormat().primaryKey == y){
					r[y] = (x + 1) + "";
					GUI box = new GUIText(getWindow(), new Boundary(new Rectf(0, 0, 1, height), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), theme.textColor, r[y]);
					container.clear(new Vector2i(y, x));
					container.addGUI(box, new Vector2i(y, x));
				}else{
					r[y] = data[x][y];
				}
			}
			newData[x] = r;
		}
		for(int x = i; x < data.length - 1; x++){
			String[] r = new String[data[x].length];
			for(int y = 0; y < data[x].length; y++){
				float height = getRelativeHeight(table.getFormat().types[y]);
				if(this.table.getFormat().primaryKey == y){
					r[y] = (x + 1) + "";
					GUI box = new GUIText(getWindow(), new Boundary(new Rectf(0, 0, 1, height), Scalar.HORIZONTAL, Alignment.MIDDLE_CENTER), theme.textColor, r[y]);
					container.clear(new Vector2i(y, x));
					container.addGUI(box, new Vector2i(y, x));
				}else{
					r[y] = data[x + 1][y];
				}
			}
			newData[x] = r;
		}
		data = newData;
		updateUpdate();
		resize();
	}

	private void resizeContainer() {
		int columns = table.getFormat().columns.length;
		float[] fixedColumns = new float[columns - 1];
		float totalWidth = 0;
		for(int i = 0; i < fixedColumns.length; i++){
			totalWidth += 1f/getRelativeHeight(table.getFormat().types[i]);
			fixedColumns[i] = totalWidth;
		}
		totalWidth += 1f/getRelativeHeight(table.getFormat().types[fixedColumns.length]);
		for(int i = 0; i < fixedColumns.length; i++){
			container.setColumn(i, fixedColumns[i]/totalWidth);
		}
	}

	protected void onUpdate(){}

	protected void updateUpdate() {
		updateRect.setBackground(Color.LIGHT_GARY);
		updateText.setTextColor(Color.DARK_GARY);
		canUpdate = true;
	}

	private float getTotalWidth() {
		float totalWidth = 0;
		for(int i = 0; i < table.getFormat().columns.length; i++){
			totalWidth += 1f/getRelativeHeight(table.getFormat().types[i]);
		}
		return totalWidth;
	}

	private float getRelativeHeight(DataType type) {
		float height = 0.02f;
		if(type == DataType.INT) height = 0.25f;
		if(type == DataType.UNSIGNED_INT) height = 0.25f;
		if(type == DataType.DATE) height = 0.15f;
		return height;
	}
	
	@Override
	protected void onResize() {
		boolean needsScroll = container.getOnScreenBounds().height > oven.getOnScreenBounds().height;
		int rows = table.getRepresentation().length;
		if(needsScroll){
			float sWidth = 19f/getOnScreenBounds().width;
			//System.out.println(a);
			sb.setOpacity(1f);
			container.setBounds(new Boundary(new Rectf(0, container.getBoundary().coordinates.y, 1, 1f/getTotalWidth()*rows*2*(1 + sWidth)), Scalar.HORIZONTAL, Alignment.TOP_LEFT));
			oven.setBounds(new Boundary(new Rectf(-sWidth/2, -0.05f, 1 - sWidth, 0.9f)));
			
		}else{
			sb.setOpacity(0f);
			container.setBounds(new Boundary(new Rectf(0, 0, 1, 1f/getTotalWidth()*rows*2), Scalar.HORIZONTAL, Alignment.TOP_LEFT));
			oven.setBounds(new Boundary(new Rectf(0, -0.05f, 1, 0.9f)));
		}
	}

	public void forceUpdate() {
		table.update(data);
		updateRect.setBackground(new Color(212, 212, 212));
		updateText.setTextColor(Color.LIGHT_GARY);
	}
	
	@Override
	protected void updateInput() {
		if(removing && getWindow().getInput().isKeyPressed(Keyboard.ESCAPE)){
			removing = false;
			updateRemoving();
		}
	}

}*/
