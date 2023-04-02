package au.com.cascadesoftware.engine3.chart;

public enum Month {
	
	JAN("January"), FEB("February"), Mar("March"), APR("April"), MAY("May"), JUN("June"), JUL("July"), AUG("Augest"), SEP("September"), OCT("October"), NOV("November"), DEC("December");
	
	public final String display, shortDisplay;
	
	Month(String display){
		this.display = display;
		this.shortDisplay = display.substring(0, 3);
	}
	
}