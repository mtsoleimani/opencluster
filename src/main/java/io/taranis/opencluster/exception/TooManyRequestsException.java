package io.taranis.opencluster.exception;

public class TooManyRequestsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TooManyRequestsException(String message) {
        super(message);
    }
	
	public TooManyRequestsException() {
        super();
    }
}
