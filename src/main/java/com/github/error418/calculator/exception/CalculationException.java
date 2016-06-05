package com.github.error418.calculator.exception;

public class CalculationException extends Exception {
	private static final long serialVersionUID = -5748282724724084158L;

	public CalculationException() {
		super();
	}
	
	public CalculationException(String message) {
		super(message);
	}
	
	public CalculationException(String message, Throwable cause) {
		super(message, cause);
	}
}
