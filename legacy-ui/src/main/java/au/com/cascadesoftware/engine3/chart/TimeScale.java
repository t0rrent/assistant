package au.com.cascadesoftware.engine3.chart;

public class TimeScale {
	
	private Date min, max;
	private Time range;
	private Interval interval;

	public TimeScale(Date min, Date max, Interval interval){
		setRange(min, max);
		this.interval = interval;
	}
	
	public void setRange(Date min, Date max){
		this.min = min;
		this.max = max;
		if(max.isBefore(min)){
			this.max = min;
			this.min = max;
		}
		range = new Time(max, min);
	}
	
	public Date getMax(){
		return max;
	}
	
	public Date getMin(){
		return min;
	}
	
	public Time getRange(){
		return range;
	}
	
	public Interval getInterval() {
		return interval;
	}
	
	/*public int getNumberOfLeapYears() {
		int n = 0;
		for(int y = min.year; y <= max.year; y++){
			if(Time.isLeapYear(y)) n++;
		}
		return n;
	}*/
	
	public double getPosition(Date date) {
		Time position = new Time(min, date);
		double positionFloat = position.days + (position.hours + (position.minutes + position.seconds/60.0)/60.0)/24.0;
		//System.out.println(positionFloat);
		double rangeFloat = range.days + (range.hours + (range.minutes + range.seconds/60.0)/60.0)/24.0;
		return positionFloat/rangeFloat;
	}
	
	
	
	public enum Interval {
		
		SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR;
		
	}
	
}
