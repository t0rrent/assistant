package au.com.cascadesoftware.openai.model;

import java.io.Serializable;
import java.util.Objects;

public class OpenAIConfig implements Serializable {
	
	private static final long serialVersionUID = -4602216250438572202L;
	
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(final String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(apiKey);
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
			final OpenAIConfig other = (OpenAIConfig) obj;
			return Objects.equals(apiKey, other.apiKey);
		}
	}

}
