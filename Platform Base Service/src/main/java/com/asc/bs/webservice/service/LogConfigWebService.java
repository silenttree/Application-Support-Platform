package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.log.access.ILogConfigReader;
import com.asc.commons.log.entity.LogConfigEntity;
import com.asc.commons.log.exception.LogException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

@WebService
public class LogConfigWebService {
	private ILogConfigReader reader;
	private Gson gson = new Gson();

	@WebMethod(exclude = true)
	public void setReader(ILogConfigReader reader) {
		this.reader = reader;
	}

	/**
	 * 按应用标识读取应用日志配置 标识为空则读取门户日志配置
	 * 
	 * @param appId
	 * @return
	 * @throws LogException
	 */
	public String findLogConfigs(String appId) {
		JsonObject result = new JsonObject();
		try {
			List<LogConfigEntity> lcs = reader.findLogConfigs(appId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(lcs));
		} catch (LogException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取应用日志配置失败");
		}
		return gson.toJson(result);
	}

	/**
	 * 获取日志配置
	 * 
	 * @param id
	 * @return
	 * @throws LogException
	 */
	public String getLogConf(String idStr){
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			LogConfigEntity lc = reader.getLogConf(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(lc));
		} catch (LogException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取应用日志配置失败");
		}
		return gson.toJson(result);
	}

	/**
	 * 获取日志列表
	 * 
	 * @param type
	 * @param appId
	 * @param logType
	 * @param pagenum
	 * @param pagesize
	 * @return
	 * @throws LogException
	 */
	public String findLogs(String type, String appId, String logTypeStr, String pagenumStr, String pagesizeStr){
		JsonObject result = new JsonObject();
		try {
			@SuppressWarnings("rawtypes")
			Class logType = Class.forName(logTypeStr);
			int pagenum = Integer.parseInt(pagenumStr);
			int pagesize = Integer.parseInt(pagesizeStr);
			@SuppressWarnings({ "unchecked", "rawtypes" })
			List logs = reader.findLogs(type, appId, logType, pagenum, pagesize);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(logs));
		} catch (LogException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据名称获取用户失败");
		}
		return gson.toJson(result);
	}

}
