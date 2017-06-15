package com.asc.developer.exception;

public class DesignerException extends Exception {
	private static final long serialVersionUID = -5330950055097870662L;
	private Exception originalE;

	public DesignerException(String message) {
		super(message);
	}
	
	public DesignerException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}


	
	/**
	 * 非法键值
	 * 
	 * @param message
	 * @return
	 */
	public static DesignerException forIllegalObjectKey(String message) {
		return new DesignerException(message);
	}
}
