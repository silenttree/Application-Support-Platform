package com.asc.manager.common.exception;

public class FileBuilderException extends Exception {
	private static final long serialVersionUID = 3340033141458272000L;
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
