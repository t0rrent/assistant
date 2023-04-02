package au.com.cascadesoftware.json.model;

import java.util.List;
import java.util.Objects;

public class WrappedListExample {
	
	public List<GenericClassExample<POJO>> complexList;
	
	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || !(that instanceof WrappedListExample)) {
			return false;
		}
		final WrappedListExample thatComplexListWrapper = (WrappedListExample) that;
		for (int i = 0; i < complexList.size(); i++) {
			final GenericClassExample<POJO> thisElement = this.complexList.get(i);
			final GenericClassExample<POJO> thatElement = thatComplexListWrapper.complexList.get(i);
			if (
					!Objects.equals(thisElement.generic.field1, thisElement.generic.field1)
					|| !Objects.equals(thisElement.generic.field2, thatElement.generic.field2)
			) {
				return false;
			}
		}
		return true;
	}
	
}
