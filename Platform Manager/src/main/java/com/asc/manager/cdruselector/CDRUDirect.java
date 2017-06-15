package com.asc.manager.cdruselector;

import com.asc.commons.cdruselector.handler.CDRUHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class CDRUDirect {

	@DirectMethod
	public JsonObject loadRangeTree(JsonObject params){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = CDRUHandler.instance().getRangeTreeNodes(params);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject loadToSelectDatas(String key, String selectType, String range){
		JsonObject json = new JsonObject();
		JsonObject params = new JsonObject();
		if(key != null){
			params.addProperty("key", key);
		}
		if(selectType != null){
			params.addProperty("selectType", selectType);
		}
		if(range != null){
			params.addProperty("range", range);
		}
		try {
			JsonArray datas;
			datas = CDRUHandler.instance().loadToSelectDatas(params);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject loadRelativeDatas(String key){
		JsonObject json = new JsonObject();
		try {
			if(key == null) {
				throw new IllegalArgumentException("key不能为空");
			}
			JsonArray datas = CDRUHandler.instance().loadRelativeDatas(key);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject loadSelectedDatas(String value){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = CDRUHandler.instance().loadSelectedDatas(value);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
}
