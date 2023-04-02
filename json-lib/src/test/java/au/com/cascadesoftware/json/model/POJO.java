package au.com.cascadesoftware.json.model;

import java.util.Objects;

import au.com.cascadesoftware.util.annotation.NonNull;

public class POJO {

	@NonNull
	public String field1;
	public POJE field2;
	
	public String toJson() {
		return "{"
				+ "\"field1\": " + quotesIfNotNull(field1) + ','
				+ "\"field2\": " + quotesIfNotNull(field2)
				+ "}";
	}
	
	private String quotesIfNotNull(final Object object) {
		if (object == null) {
			return "null";
		} else {
			return '"' + object.toString() + '"';
		}
	}

	@Override
	public boolean equals(final Object that) {
		if (that == this) {
			return true;
		}
		if (that == null || !(that instanceof POJO)) {
			return false;
		}
		final POJO thatPOJO = (POJO) that;
		return Objects.equals(this.field1, thatPOJO.field1)
				&& Objects.equals(this.field2, thatPOJO.field2);
	}
	
}
