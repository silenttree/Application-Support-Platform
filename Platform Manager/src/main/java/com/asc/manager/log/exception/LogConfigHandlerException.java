package com.asc.manager.log.exception;

import com.asc.commons.log.exception.LogException;

public class LogConfigHandlerException extends Exception {
	private static final long serialVersionUID = -8512677203278265759L;
	
	private Exception originalE;

	public LogConfigHandlerException(String message) {
		super(message);
	}
	
	public LogConfigHandlerException(String message, Exception e) {
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
	public static LogException forNotConfiguredProperly(String message) {
		return new LogException("配置错误：" + message);
	}
}
