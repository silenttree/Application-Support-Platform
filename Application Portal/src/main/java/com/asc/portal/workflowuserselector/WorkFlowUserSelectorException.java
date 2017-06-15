package com.asc.portal.workflowuserselector;

public class WorkFlowUserSelectorException extends Exception {

	private static final long serialVersionUID = 1L;

	public WorkFlowUserSelectorException() {
		super();
	}

	public WorkFlowUserSelectorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		//super(message, cause, enableSuppression, writableStackTrace);
		super(message, cause);
	}

	public WorkFlowUserSelectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public WorkFlowUserSelectorException(String message) {
		super(message);
	}

	public WorkFlowUserSelectorException(Throwable cause) {
		super(cause);
	}
	

}
