package com.asc.manager.webservice.impl;

import com.asc.manager.webservice.IApplicationAccessor;
import com.asc.util.ws.WebServiceInvoke;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class WsApplicationAccessor implements IApplicationAccessor{
	private String namespace;
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public void updateAppState(long appId, String wsdlurl, int state) throws Exception {
		// 调用远程方法，处理返回数据，生成对象实例
		try {
			WebServiceInvoke wsi = new WebServiceInvoke(wsdlurl, namespace);
			String method = "";
			switch (state) {
			case 1:
				method = "stop";//停止
				break;
			case 2:
				method = "start";//启动
				break;
			case 3:
				method = "suspend";//挂起
				break;
			case 5:
				method = "resume";//继续
				break;
			default:
				throw new Exception("未知的应用系统运行状态【" + state + "】");
			}
			Object result = wsi.invoke(method, new Object[]{}, new Class[]{String.class});
			if (result != null && result instanceof String) {
				JsonObject jsonData = JsonObjectTool.string2JsonObject(result.toString());
				if (jsonData.has("success") && jsonData.get("success").getAsBoolean()) {
					//执行成功，更新应用系统列表的状态
					//ApplicationManager.instance().updateAppState(appId, state);
				} else {
					// 请求失败或返回结果标记失败
					throw new Exception("改变应用系统状态失败," + jsonData.get("message").getAsString());
				}
			} else {
				throw new Exception("返回数据格式异常,期望的返回数据类型是String");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("改变应用系统状态失败，" + e.getMessage());
		}
	}

	@Override
	public JsonObject loadApplications(long appId, String appPath) throws Exception {
		// 调用远程方法，处理返回数据，生成对象实例
		try {
			WebServiceInvoke wsi = new WebServiceInvoke(appPath, namespace);
			Object result = wsi.invoke("getAppStatus", new Object[] {}, new Class[] { String.class });
			if (result != null && result instanceof String) {
				JsonObject jsonData = JsonObjectTool.string2JsonObject(result.toString());
				if (jsonData.has("success") && jsonData.get("success").getAsBoolean()) {
					// 执行成功，更新应用系统列表的状态
					return jsonData;
				} else {
					// 请求失败或返回结果标记失败
					throw new Exception("查询应用系统状态失败," + jsonData.get("message").getAsString());
				}
			} else {
				throw new Exception("返回数据格式异常,期望的返回数据类型是String");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("查询应用系统状态失败，" + e.getMessage());
		}
	}

}
