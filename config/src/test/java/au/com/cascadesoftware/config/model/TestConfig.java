package au.com.cascadesoftware.config.model;

import au.com.cascadesoftware.util.annotation.NonNull;

public class TestConfig {

	@NonNull
	public String field1;
	
	private boolean field2;

	private double field3;

	public String field4;

	public String field5;

	public boolean getField2() {
		return field2;
	}

	public void setField2(final boolean field2) {
		this.field2 = field2;
	}

	public double getField3() {
		return field3;
	}

	public void setField3(final double field3) {
		this.field3 = field3;
	}
	
}
