package au.com.cascadesoftware.engine4.exception;

public class FailedToStartException extends RuntimeException {
	
	private static final long serialVersionUID = 4253783617843759786L;

	public FailedToStartException(final String message, final Throwable reason) {
		super(message, reason);
	}

}
