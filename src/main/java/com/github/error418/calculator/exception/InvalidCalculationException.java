package com.github.error418.calculator.exception;

public class InvalidCalculationException extends CalculationException {
	private static final long serialVersionUID = -5666040651164465476L;

	public InvalidCalculationException(String message) {
		super(message);
	}
	
	public InvalidCalculationException(String message, Throwable cause) {
		super(message, cause);
	}
}
