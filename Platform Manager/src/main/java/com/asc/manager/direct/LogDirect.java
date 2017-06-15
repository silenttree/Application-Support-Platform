package com.asc.manager.direct;

import com.asc.manager.log.handler.LogConfigHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class LogDirect {
	
	/**
	 * 保存日志配置
	 * 
	 * @param appkey
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveLogConf(String appkey, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = LogConfigHandler.instance().saveLogConf(appkey, data);
				sObjects.add(object);
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getLocalizedMessage();
				json.addProperty("success", false);
				json.addProperty("message", message);
				return json;
			}
		}

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				LogConfigHandler.instance().delLogConf(id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sObjects.add(data);
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
	
	/**
	 * 获取日志配置列表
	 * 
	 * @param appId
	 * @return
	 */
	@DirectMethod
	public JsonObject loadLogConfig(String appId){
		JsonObject json = new JsonObject();
		JsonArray datas;
		try {
			datas = LogConfigHandler.instance().loadLogConfig(appId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 获取应用系统的日志结构树
	 * @return
	 */
	@DirectMethod
	public JsonArray loadAppTree(String type){
		JsonArray datas = new JsonArray();
		try {
			datas = LogConfigHandler.instance().loadAppTree(type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datas;
	}
	
	/**
	 * 分页查询，获取日志列表
	 */
	@DirectMethod
	public JsonObject loadLogsByType(String type, String url, int pagenum, int pagesize){
		JsonObject result = new JsonObject();
		JsonObject datas = new JsonObject();
		try {
			datas = LogConfigHandler.instance().findLogsByType(type, url, pagenum, pagesize);
			result.add("datas", datas.get("datas").getAsJsonArray());
			result.addProperty("total", datas.get("total").getAsLong());
			result.addProperty("success", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}
