package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.applicationentrance.access.IApplicationEntranceReader;
import com.asc.commons.applicationentrance.entity.ApplicationEntrance;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * @author zhsq
 * <pre>
 * 包装DbacApplicationEntranceReader,为其他程序提供web service
 * 其中的方法和被包装的类一一对应
 * 所有方法的参数处理成字符串
 * 所有重载的方法重新起名字
 * 返回值为一个标准的json字符串
 * 	其中success标识成功与否
 * 	data为成功时的返回数据
 * 	message为失败时的错误信息
 * </pre>
 */
@WebService
public class AppEntWebService {
	private IApplicationEntranceReader reader;
	private Gson gson = new Gson();

	@WebMethod(exclude = true)
	public void setReader(IApplicationEntranceReader reader) {
		this.reader = reader;
	}
	
	public String getApplicationEntranceByKey(String key) {
		JsonObject result = new JsonObject();
		try {
			ApplicationEntrance appEnt = reader.getApplicationEntrance(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(appEnt));
		} catch (ApplicationEntranceException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据key获取应用导航失败");
		}
		return gson.toJson(result);
	}
	
	public String getApplicationEntranceById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			ApplicationEntrance appEnt = reader.getApplicationEntrance(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(appEnt));
		} catch (ApplicationEntranceException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据id获取应用导航失败");
		}
		return gson.toJson(result);
	}
	
	public String findApplicationEntranceByAppKey(String appKey) {
		JsonObject result = new JsonObject();
		try {
			List<ApplicationEntrance> appEnts = reader
					.findApplicationEntranceByApp(appKey);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(appEnts));
		} catch (ApplicationEntranceException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用key获取应用导航失败");
		}
		return gson.toJson(result);
	}
	
	public String findApplicationEntranceByAppId(String appIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			List<ApplicationEntrance> appEnts = reader
					.findApplicationEntranceByApp(appId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(appEnts));
		} catch (ApplicationEntranceException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用id获取应用导航失败");
		}
		return gson.toJson(result);
	}
	
	public String findApplicationEntrance(String userExps) {
		JsonObject result = new JsonObject();
		try {
			List<ApplicationEntrance> appEnts = reader
					.findApplicationEntrance(userExps);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(appEnts));
		} catch (ApplicationEntranceException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用导航列表失败");
		}
		return gson.toJson(result);
	}
	
}
