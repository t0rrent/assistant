package au.com.cascadesoftware.util.model;

import java.time.Duration;

public class CachingStrategy {
	
	private final Duration expiry;
	
	private CachingStrategy(final Duration expiry) {
		this.expiry = expiry;
	}

	public Duration getExpiry() {
		return expiry;
	}

	public static CachingStrategy none() {
		return builder().build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private Duration expiry;

		public Builder setExpiry(final Duration expiry) {
			this.expiry = expiry;
			return this;
		}
		
		public CachingStrategy build() {
			return new CachingStrategy(expiry);
		}
		
	}

}
