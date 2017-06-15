package com.asc.manager.dicdatarelation.exception;

public class DicdataRelationHandlerException extends Exception {
	private static final long serialVersionUID = -9152259083137083821L;
	
	private Exception originalE;

	public DicdataRelationHandlerException(String message) {
		super(message);
	}
	
	public DicdataRelationHandlerException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
	public static DicdataRelationHandlerException forDataServiceError(String message, Exception e) {
		return new DicdataRelationHandlerException(message, e);
	}
	
}
