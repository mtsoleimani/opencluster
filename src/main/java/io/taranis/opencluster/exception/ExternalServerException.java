package io.taranis.opencluster.exception;

public class ExternalServerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExternalServerException(String message) {
        super(message);
    }
	
	public ExternalServerException() {
        super();
    }
}
