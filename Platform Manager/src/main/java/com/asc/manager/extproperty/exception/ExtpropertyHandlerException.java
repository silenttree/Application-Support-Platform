package com.asc.manager.extproperty.exception;

public class ExtpropertyHandlerException extends Exception {
	private static final long serialVersionUID = -834352057105791416L;
	
	private Exception originalE;

	public ExtpropertyHandlerException(String message) {
		super(message);
	}
	
	public ExtpropertyHandlerException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
	/**
	 * 未正确配置
	 * 
	 * @param message
	 * @return
	 */
	public static ExtpropertyHandlerException forNotConfiguredProperly(String message) {
		return new ExtpropertyHandlerException("配置错误：" + message);
	}
}
