package com.asc.manager.org.exception;

public class OrganizationHandlerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8382161830304578303L;
	
	private Exception originalE;

	public OrganizationHandlerException(String message) {
		super(message);
	}
	
	public OrganizationHandlerException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
	public static OrganizationHandlerException forDataServiceError(String message, Exception e) {
		return new OrganizationHandlerException(message, e);
	}
}
