package au.com.cascadesoftware.util.exception;

public class WrappedException extends RuntimeException {

	private static final long serialVersionUID = -6396501644289335345L;

	public WrappedException(final Throwable cause) {
		super(cause);
	}

	public WrappedException(final String message, final Throwable cause) {
		super(message, cause);
	}	

}
