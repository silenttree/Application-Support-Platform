package com.asc.developer.config;

import com.google.gson.JsonObject;

/**
 * Application Developer/ApplicationConnection.java<br>
 * 应用服务连接注册信息
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class ApplicationConnection {
	private String appId;		// 主机标识符
	private String appCaption;	// 主机标题
	private String appType;		//主机链接类型
	private String appHost;		// 主机地址
	private String serviceUrl;	// 服务地址（默认，但允许自定义）
	private String note;		// 备注
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		// 加入对Id名称的规范
		if (appId == null || "".equals(appId)) {
			throw new IllegalArgumentException("未设置连接应用标识符");
		}
		if (appId.contains("/") || appId.contains("\\") || appId.contains("·")) {
			throw new IllegalArgumentException("应用标识符不允许出现'\\ / · 等特殊符号'");
		}
		this.appId = appId;
	}

	public String getAppCaption() {
		return appCaption;
	}

	public void setAppCaption(String appCaption) {
		this.appCaption = appCaption;
	}

	public String getAppHost() {
		return appHost;
	}

	public void setAppHost(String appIp) {
		this.appHost = appIp;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public JsonObject toJson(){
		JsonObject json = new JsonObject();
		json.addProperty("appId", appId);
		json.addProperty("appCaption", appCaption);
		json.addProperty("appType", appType);
		json.addProperty("appHost", appHost);
		json.addProperty("serviceUrl", serviceUrl);
		json.addProperty("note", note);
		return json;
	}
	
	public void fromJson(JsonObject json){
		if(json.has("appId")){
			this.setAppId(json.get("appId").getAsString());
		}
		if(json.has("appCaption")){
			this.setAppCaption(json.get("appCaption").getAsString());
		}
		if(json.has("appType")){
			this.setAppType(json.get("appType").getAsString());
		}
		if(json.has("appHost")){
			this.setAppHost(json.get("appHost").getAsString());
		}
		if(json.has("serviceUrl")){
			this.setServiceUrl(json.get("serviceUrl").getAsString());
		}
		if(json.has("note")){
			this.setNote(json.get("note").getAsString());
		}
	}
}