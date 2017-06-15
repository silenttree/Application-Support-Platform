package com.asc.portal.preferences.exception;

public class PreferencesException extends Exception {
	private static final long serialVersionUID = 1L;
	private Exception originalE;

	public PreferencesException(String message) {
		super(message);
	}

	public PreferencesException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
}
