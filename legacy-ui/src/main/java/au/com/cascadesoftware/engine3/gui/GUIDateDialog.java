package au.com.cascadesoftware.engine3.gui;

import au.com.cascadesoftware.engine2.math.Rectf;
import au.com.cascadesoftware.engine2.math.Vector2i;
import au.com.cascadesoftware.engine3.chart.Date;
import au.com.cascadesoftware.engine3.chart.Day;
import au.com.cascadesoftware.engine3.display.Window;
import au.com.cascadesoftware.engine3.graphics.Color;
import au.com.cascadesoftware.legacyui.config.WindowConfig;

public class GUIDateDialog extends GUIDialogDesktop {
	
	private GUIText year, month;
	private GUI days;

	//public GUIDateDialog(Window window, Boundary bounds, String parameters, Date date){
		//super(window, bounds, parameters);
	public GUIDateDialog(final Window window, final Boundary bounds, final WindowConfig windowConfig, final Date date){
		super(window, bounds, windowConfig);
		year = (GUIText) new GUIText(dialog, new Boundary(new Rectf(0, 2/250f, 150/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_CENTER), new Color(0xFF222222), "" + date.year).setBackground(Color.LIGHT_GARY);
		GUIText lYear = (GUIText) new GUIText(dialog, new Boundary(new Rectf(0, 0, 25/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_LEFT), new Color(0xFF222222), "<").setBackground(Color.GARY);
		GUIText rYear = (GUIText) new GUIText(dialog, new Boundary(new Rectf(0, 0, 25/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_RIGHT), new Color(0xFF222222), ">").setBackground(Color.GARY);
		addGUI(new GUI(dialog, new Boundary(new Rectf(0, 0, 150/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_CENTER)).setBackground(Color.LIGHT_GARY));
		addGUI(year);
		addGUI(lYear);
		addGUI(rYear);
		lYear.addGUI(new GUIButton(dialog, new Boundary()){
			@Override
			public void onClick() {
				year.setText((Integer.parseInt(year.getText()) - 1) + "");
				initDays();
			}
		});
		rYear.addGUI(new GUIButton(dialog, new Boundary()){
			@Override
			public void onClick() {
				year.setText((Integer.parseInt(year.getText()) + 1) + "");
				initDays();
			}
		});
		addHover(lYear);
		addHover(rYear);
		
		month = (GUIText) new GUIText(dialog, new Boundary(new Rectf(0, 25/250f, 150/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_CENTER), new Color(0xFF222222), "" + getMonth(date.month)).setBackground(new Color(0xFFDDDDDD));
		GUIText lMonth = (GUIText) new GUIText(dialog, new Boundary(new Rectf(0, 25/250f, 25/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_LEFT), new Color(0xFF222222), "<").setBackground(Color.LIGHT_GARY);
		GUIText rMonth = (GUIText) new GUIText(dialog, new Boundary(new Rectf(0, 25/250f, 25/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_RIGHT), new Color(0xFF222222), ">").setBackground(Color.LIGHT_GARY);
		addGUI(new GUI(dialog, new Boundary(new Rectf(0, 25/250f, 150/200f, 25/250f), Scalar.STRETCHED, Alignment.TOP_CENTER)).setBackground(new Color(0xFFDDDDDD)));
		addGUI(month);
		addGUI(lMonth);
		addGUI(rMonth);
		lMonth.addGUI(new GUIButton(dialog, new Boundary()){
			@Override
			public void onClick() {
				int i = getMonthIndex(month.getText());
				i--;
				if(i < 1) i = 12;
				month.setText(getMonth(i));
				initDays();
			}
		});
		rMonth.addGUI(new GUIButton(dialog, new Boundary()){
			@Override
			public void onClick() {
				int i = getMonthIndex(month.getText());
				i++;
				if(i > 12) i = 1;
				month.setText(getMonth(i));
				initDays();
			}
		});
		addHover(lMonth);
		addHover(rMonth);
		
		days = new GUI(dialog, new Boundary(new Rectf(0, 0, 1, 200/250f), Scalar.STRETCHED, Alignment.BOTTOM_CENTER));
		addGUI(days);
		initDays();
	}

	private void addHover(GUI g) {
		g.addGUI(new GUIHover(dialog, new Boundary()) {
			@Override
			protected void onStateChange(float phase) {
				setBackground(new Color((int) (0x22000000*phase)));
			}
		});
	}

	private void initDays() {
		days.clear();
		int yr = Integer.parseInt(year.getText());
		int m = getMonthIndex(month.getText());
		int firstOfMonth = new Date(1, m, yr).getDayOfWeek().ordinal();
		GUIContainerGrid panel = new GUIContainerGrid(dialog, new Boundary(), new Vector2i(7, 7));
		panel.setResizable(false);
		panel.setInsideBorder(1, new Color("#222"));
		for(int x = 0; x < 7; x++){
			panel.addGUI(new GUIText(dialog, new Boundary(new Rectf(0, 0, 1, 0.5f)), new Color("#222"), "" + Day.values()[x].toString().substring(0, 2)), new Vector2i(x, 0));
		}
		for(int y = 0; y < 6; y++){
			for(int x = 0; x < 7; x++){
				int d = y*7 + x - firstOfMonth;
				int mSize = Date.getLastDayOfMonth(m, yr);
				if(d < 0){
					panel.addGUI(new GUIText(dialog, new Boundary(new Rectf(0, 0, 1, 0.5f)), new Color("#888"), "" + (d + mSize + 1)), new Vector2i(x, y + 1));
				}else if(d >= mSize){
					panel.addGUI(new GUIText(dialog, new Boundary(new Rectf(0, 0, 1, 0.5f)), new Color("#888"), "" + (d - mSize + 1)), new Vector2i(x, y + 1));
				}else{
					GUI day = new GUIButton(dialog, new Boundary()){
						@Override
						public void onClick(){
							onDateChanged(new Date(d + 1, m, yr));
							closeDialog();
						}
					};
					day.addGUI(new GUIText(dialog, new Boundary(new Rectf(0, 0, 1, 0.5f)), new Color("#222"), "" + (d + 1)));
					panel.addGUI(day, new Vector2i(x, y + 1));
					addHover(day);
				}
			}
		}
		days.addGUI(panel);
		//System.out.println(firstOfMonth);
	}

	private String getMonth(int month) {
		if(month == 1) return "January";
		else if(month == 2) return "February";
		else if(month == 3) return "March";
		else if(month == 4) return "April";
		else if(month == 5) return "May";
		else if(month == 6) return "June";
		else if(month == 7) return "July";
		else if(month == 8) return "August";
		else if(month == 9) return "September";
		else if(month == 10) return "October";
		else if(month == 11) return "November";
		else if(month == 12) return "December";
		return "error";
	}

	private int getMonthIndex(String month) {
		if(month.equals("January")) return 1;
		else if(month.equals("February")) return 2;
		else if(month.equals("March")) return 3;
		else if(month.equals("April")) return 4;
		else if(month.equals("May")) return 5;
		else if(month.equals("June")) return 6;
		else if(month.equals("July")) return 7;
		else if(month.equals("August")) return 8;
		else if(month.equals("September")) return 9;
		else if(month.equals("October")) return 10;
		else if(month.equals("November")) return 11;
		else if(month.equals("December")) return 12;
		return -1;
	}

	protected void onDateChanged(Date d){}

}
