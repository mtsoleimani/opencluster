package io.taranis.opencluster.exception;

public class DuplicatedRecordException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicatedRecordException(String message) {
        super(message);
    }
	
	public DuplicatedRecordException() {
        super();
    }
}
