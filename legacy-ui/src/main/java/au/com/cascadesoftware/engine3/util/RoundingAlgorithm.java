package au.com.cascadesoftware.engine3.util;

import au.com.cascadesoftware.engine3.chart.NumberScale;

public abstract class RoundingAlgorithm {

	public abstract int getN(NumberScale ns, int n);
	
	public static final RoundingAlgorithm NONE = new RoundingAlgorithm(){
		@Override
		public int getN(NumberScale ns, int n) {
			return n;
		}
	};
	
	public static final RoundingAlgorithm POWERS_OF_TWO = new RoundingAlgorithm(){
		@Override
		public int getN(NumberScale ns, int n) {
			return (int) Math.pow(2, Math.floor(Math.log(n)/Math.log(2)));
		}
	};

	public static final RoundingAlgorithm INCREMENTAL_TENTHS = new RoundingAlgorithmIncremental(new float[]{1});
	public static final RoundingAlgorithm INCREMENTAL_TENTHS_AND_FIFTHS = new RoundingAlgorithmIncremental(new float[]{5, 2, 1});
	
	public static class RoundingAlgorithmIncremental extends RoundingAlgorithm {
		
		private float[] increments;
		
		private RoundingAlgorithmIncremental(float[] increments) {
			this.increments = increments;
		}

		@Override
		public int getN(NumberScale ns, int n) {
			if(n == 0) return n;
			float range = ns.getRange();
			float a = n/range;
			int k = 0;
			while(a >= 10){
				a/=10;
				k++;
			}
			while(a < 1){
				a*=10;
				k--;
			}
			for(float i : increments){
				if(a>i){
					a = i;
					break;
				}
			}
			return (int) (Math.pow(10, k)*a*range + 1);
		}
	}

}
