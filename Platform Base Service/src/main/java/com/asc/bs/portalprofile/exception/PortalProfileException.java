package com.asc.bs.portalprofile.exception;

public class PortalProfileException extends Exception {

	private static final long serialVersionUID = -5492903475657858328L;

	public PortalProfileException() {
		super();
	}

	public PortalProfileException(String message) {
		super(message);
	}

	public PortalProfileException(Throwable cause) {
		super(cause);
	}

	public PortalProfileException(String message, Throwable cause) {
		super(message, cause);
	}

	public static PortalProfileException forDataServiceError(String message,
			Exception e) {
		return new PortalProfileException(message, e);
	}

	public static PortalProfileException forGetObjectFaild(String paramStr,
			Exception e) {
		return new PortalProfileException("获取对象访问失败，参数描述:" + paramStr, e);
	}

	public static PortalProfileException forFindObjectFaild(String paramStr,
			Exception e) {
		return new PortalProfileException("查找对象访问失败，参数描述:" + paramStr, e);
	}

	public static PortalProfileException forSaveObjectFaild(long id, Exception e) {
		return new PortalProfileException("保存对象失败，ID:" + id, e);
	}

	public static PortalProfileException forDeleteObjectFaild(long id,
			Exception e) {
		return new PortalProfileException("删除对象失败，ID:" + id, e);
	}

	public static PortalProfileException forKeyIllegal(long sameId, String key) {
		return new PortalProfileException("保存对象失败，key值已存在:" + key + ", 与ID为【"
				+ sameId + "】的对象冲突");
	}

	public static PortalProfileException forObectNotFound(String objectType,
			long id) {
		return new PortalProfileException("对象未找到，参数描述:" + objectType + "[" + id
				+ "]");
	}

	public static PortalProfileException forNewSeqFaild(String className,
			Exception e) {
		return new PortalProfileException("获得对象SEQ失败：" + className, e);
	}
}
