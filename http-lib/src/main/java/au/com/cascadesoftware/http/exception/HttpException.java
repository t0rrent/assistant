package au.com.cascadesoftware.http.exception;

public class HttpException extends Exception {

	private static final long serialVersionUID = 4181956792084932374L;

	public static final int TIMEOUT_CODE = 408;
	
	private final int responseCode;

	@Deprecated
	public HttpException(final int responseCode) {
		super("Response code: " + responseCode);
		this.responseCode = responseCode;
	}

	public HttpException(final int responseCode, final String message) {
		super("Response code: " + responseCode + "\nError message: " + message);
		this.responseCode = responseCode;
	}

	public int getResponseCode() {
		return responseCode;
	}

}
