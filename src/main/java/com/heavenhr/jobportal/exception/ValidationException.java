package com.heavenhr.jobportal.exception;

public class ValidationException extends BaseException {

	private static final long serialVersionUID = -939152134977915215L;

	public ValidationException() {
		super();
	}

	public ValidationException(final String errorCode, final String message) {
		super(message, errorCode);
	}

	public ValidationException(final String errorCode, final String message, final Throwable cause) {
		super(message, errorCode, cause);
	}

}
