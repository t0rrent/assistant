package au.com.cascadesoftware.engine3.exception;

public class MultiplierException extends RuntimeException {
	
	private static final long serialVersionUID = 2577810535836740168L;
	
	private String message;
	
	public MultiplierException(String message) {
		this.message = message;
	}

	@Override
	public void printStackTrace() {
		System.err.println(message);
		super.printStackTrace();
	}

}
