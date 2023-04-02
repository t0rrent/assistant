package au.com.cascadesoftware.engine3.chart;

public class NumberScale {
	
	private float min, max;
	private Type type;

	public NumberScale(float min, float max, Type type){
		this.min = min;
		this.max = max;
		this.type = type;
	}
	
	public void setRange(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	private float round(float n){
		String numAsString = "" + Math.round(n/(float)Math.pow(10, type.decimalPosition));
		if(type.decimalPosition >= 0){
			for(int i = 0; i < type.decimalPosition; i++) numAsString += "0";
		}else{
			if(numAsString.length() <= -type.decimalPosition){
				String pre = "0.";
				for(int i = 0; i < -(type.decimalPosition + numAsString.length()); i++) pre += "0";
				numAsString = pre + numAsString;
			}else{
				numAsString = numAsString.substring(0, numAsString.length() + type.decimalPosition) + "." + numAsString.substring(numAsString.length() + type.decimalPosition, numAsString.length());
			}
		}
		return Float.parseFloat(numAsString);
	}
	
	public float getMax(){
		return round(max);
	}
	
	public float getMin(){
		return round(min);
	}
	
	public float getRange(){
		return round(max - min);
	}
	
	public float getIncrement() {
		return (float) Math.pow(10, type.decimalPosition);
	}
	
	public Type getType() {
		return type;
	}
	
	public float[] getSetByAmount(int n){
		return getSetByAmount(n, 0.5f, 1);
	}
	
	public float[] getSetByAmount(int n, double midpoint, double range){
		double p1 = midpoint - range/2f;
		double p2 = midpoint + range/2f;
		int start = (int) (p1*n) - 2;
		int end = (int) (p2*n) + 2;
		if(start < 0) start = 0;
		if(end > n) end = n;
		float[] out = new float[n];
		if(end < 0 || start >= n) return out;
		for(int i = start; i < end; i++){
			out[i] = round(getMin() + (getRange() / (n - 1))*i);
		}
		return out;
	}
	
	public float[] getSetByIncrement(float increment){
		int n = (int) ((getMax() - getMin())/increment + 1);
		return getSetByAmount(n);
	}
	
	
	
	public enum Type {
		
		CURRENCY(-2), INTEGER(0);
		
		public final int decimalPosition;
		
		private Type(int inc){
			this.decimalPosition = inc;
		}
		
	}
	
}
