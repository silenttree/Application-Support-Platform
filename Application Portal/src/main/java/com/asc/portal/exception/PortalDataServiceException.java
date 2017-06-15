package com.asc.portal.exception;

public class PortalDataServiceException extends Exception {
	private static final long serialVersionUID = 1L;

	public PortalDataServiceException(String message) {
		super(message);
	}
	
	public PortalDataServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public static PortalDataServiceException forLoadApplicationFaild(String message) {
		return new PortalDataServiceException("装载应用列表数据失败：" + message);
	}
}
