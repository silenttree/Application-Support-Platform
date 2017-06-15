package com.asc.manager.appreg.exception;

public class ApplicationRegisterHandlerException extends Exception {
	private static final long serialVersionUID = -3691738004133349412L;
	
	private Exception originalE;

	public ApplicationRegisterHandlerException(String message) {
		super(message);
	}
	
	public ApplicationRegisterHandlerException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
	public static ApplicationRegisterHandlerException forDataServiceError(String message, Exception e) {
		return new ApplicationRegisterHandlerException(message, e);
	}
}
