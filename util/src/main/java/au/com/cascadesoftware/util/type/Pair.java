package au.com.cascadesoftware.util.type;

import java.util.Objects;

public class Pair<P, S> {
	
	private final P primary;
	
	private final S secondary;
	
	public Pair(final P primary, final S secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	public P getPrimary() {
		return primary;
	}

	public S getSecondary() {
		return secondary;
	}	
	
	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		} else if (that == null) {
			return false;
		} else if (getClass() != that.getClass()) {
			return false;
		} else {
			Pair<?, ?> other = (Pair<?, ?>) that;
			return Objects.equals(primary, other.primary) && Objects.equals(secondary, other.secondary);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(primary, secondary);
	}

	@Override
	public String toString() {
		return "Pair [primary=" + primary + ", secondary=" + secondary + "]";
	}

}
