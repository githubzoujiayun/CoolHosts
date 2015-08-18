package com.find.coolhosts;

public class UnableToMountSystemException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnableToMountSystemException(Throwable ex) {
		super(ex);
	}

	public UnableToMountSystemException(String message) {
		super(message);
	}

	public UnableToMountSystemException(String message, Throwable ex) {
		super(message, ex);
	}
}
