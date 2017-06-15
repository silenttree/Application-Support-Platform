package com.asc.manager.direct;

import com.asc.manager.extproperty.exception.ExtpropertyHandlerException;
import com.asc.manager.extproperty.handler.ExtpropertyHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class ExtpropertyDirect {

	@DirectMethod
	public JsonObject findExtpropertyData(String tableName, int dataId){
		JsonObject json = new JsonObject();
		JsonArray datas;
		try {
			datas = ExtpropertyHandler.instance().findExtpropertyData(tableName, dataId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (ExtpropertyHandlerException e) {
			json.addProperty("success", true);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 获取表扩展字段
	 * @return
	 */
	@DirectMethod
	public JsonObject loadExtproperty(long dataId, String tablename){
		JsonObject json = new JsonObject();
		JsonArray datas = new JsonArray();
		try {
			datas = ExtpropertyHandler.instance().loadExtproperty(tablename, dataId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (ExtpropertyHandlerException e) {
			json.addProperty("success", true);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 保存扩展属性
	 * @return
	 */
	@DirectMethod
	public JsonObject saveProperty(long dataId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = ExtpropertyHandler.instance().saveExtproperty(dataId, data);
				sObjects.add(object);
			} catch (ExtpropertyHandlerException e) {
				message = e.getLocalizedMessage();
				json.addProperty("success", false);
				json.addProperty("message", e.getMessage());
				return json;
			}
		}

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				ExtpropertyHandler.instance().deleteExtproperty(id);
			} catch (ExtpropertyHandlerException e) {
				json.addProperty("message", e.getMessage());
			}
			sObjects.add(data);
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
}
