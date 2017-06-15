package com.asc.portal.builder;

public class FileBuilderException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception originalE;

	
	public FileBuilderException(String message) {
		super(message);
	}
	
	public FileBuilderException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
	
}
