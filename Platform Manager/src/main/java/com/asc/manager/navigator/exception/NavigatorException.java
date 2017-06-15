package com.asc.manager.navigator.exception;

public class NavigatorException extends Exception {
	private static final long serialVersionUID = -5330950055097870662L;
	private Exception originalE;

	public NavigatorException(String message) {
		super(message);
	}
	
	public NavigatorException(String message, Exception e) {
		super(message, e);
		originalE = e;
	}
	
	public void printStack() {
		if (this.originalE != null) {
			this.originalE.printStackTrace();
		}
	}
	
	/**
	 * 节点装载器参数错误
	 * @param data
	 * @return
	 */
	public static NavigatorException forNodeParameterError(String data) {
		
		return new NavigatorException("开发工具导航节点装载器参数错误【" + data + "】");
	}
	
	/**
	 * 未找到节点装载器
	 * 
	 * @param message
	 * @return
	 */
	public static NavigatorException forNodeLoaderNotFound(String type) {
		
		return new NavigatorException("未定义开发工具导航节点装载器【" + type + "】");
	}
}
