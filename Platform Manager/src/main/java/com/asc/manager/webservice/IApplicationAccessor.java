package com.asc.manager.webservice;

import com.google.gson.JsonObject;


public interface IApplicationAccessor {
	/**
	 * 查询应用状态列表
	 * @param appId
	 * @param appPath
	 * @throws Exception
	 */
	public JsonObject loadApplications(long appId, String appPath) throws Exception;
	
	/**
	 * 更新应用的状态
	 * @param appId
	 * @param appPath
	 * @param state
	 * @throws Exception
	 */
	public void updateAppState(long appId, String appPath, int state) throws Exception;
}
