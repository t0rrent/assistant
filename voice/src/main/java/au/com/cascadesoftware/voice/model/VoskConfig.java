package au.com.cascadesoftware.voice.model;

import java.io.Serializable;
import java.util.Objects;

public class VoskConfig implements Serializable {
	
	private static final long serialVersionUID = -4602216250438572202L;
	
	private String englishModelLocation;

	public String getEnglishModelLocation() {
		return englishModelLocation;
	}
	
	public void setEnglishModelLocation(final String englishModelLocation) {
		this.englishModelLocation = englishModelLocation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(englishModelLocation);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} if (getClass() != obj.getClass()) {
			return false;
		} else {
			final VoskConfig other = (VoskConfig) obj;
			return Objects.equals(englishModelLocation, other.englishModelLocation);
		}
	}

}
