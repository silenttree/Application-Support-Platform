package com.asc.manager.direct;

import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.extproperty.exception.ExtpropertyException;
import com.asc.manager.dictionary.handler.DictionaryHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class DictionaryDirect {
	@DirectMethod
	public JsonObject getDictionaryDatasByParentCode(String pCode){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DictionaryHandler.instance().getDicDatasByPCode(pCode);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (DictionaryException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		} catch (ExtpropertyException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}

	/**
	 * 保存字典
	 * 
	 * @param dicId
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveDictionary(JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = DictionaryHandler.instance().saveDictionary(0, data);
				sObjects.add(object);
			} catch (DictionaryException e) {
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
				DictionaryHandler.instance().deleteDictionary(id);
			} catch (DictionaryException e) {
				json.addProperty("success", false);
				json.addProperty("message", e.getMessage());
				return json;
			} catch (DicdataRelationException e) {
				json.addProperty("success", false);
				json.addProperty("message", e.getMessage());
				return json;
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
	 * 获取字典列表
	 * 
	 * @param dicId
	 * @return
	 */
	@DirectMethod
	public JsonObject getDictionaryList(){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DictionaryHandler.instance().getDictionaryList(0);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (DictionaryException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 装载字典树
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDictionaryNodes(){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = DictionaryHandler.instance().loadDictionaryNodes(0);
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
	 * 获取字典数据列表
	 * 
	 * @param dicId
	 * @return
	 */
	@DirectMethod
	public JsonObject getDictionaryDataList(long dicId, long dataId){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DictionaryHandler.instance().getDictionaryDataList(dicId, dataId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (DictionaryException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		} catch (ExtpropertyException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 字典数据保存
	 * 
	 * @param dicId
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveDictionaryData(long dicId, long dataId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = DictionaryHandler.instance().saveDictionaryData(dicId, dataId, data);
				sObjects.add(object);
			} catch (DictionaryException e) {
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
				DictionaryHandler.instance().deleteDictionaryData(id);
			} catch (DictionaryException e) {
				json.addProperty("success", false);
				json.addProperty("message", e.getMessage());
				return json;
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
	 * 装载字典数据树
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDictionaryDataNodes(JsonObject note){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = DictionaryHandler.instance().loadDictionaryDataNode(note.get("dicId").getAsLong(), note.get("node").getAsLong());
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
	public JsonArray loadDictionaryDataNode(JsonObject note) {
		JsonArray datas = new JsonArray();
		try {
			JsonArray objects = DictionaryHandler.instance().loadDictionaryDataNode(note.get("dicId").getAsLong(), note.get("node").getAsLong());
			// 所有的几点都设置为叶子节点
			for (JsonElement el : objects) {
				JsonObject jo = el.getAsJsonObject();
				jo.addProperty("leaf", true);
			}

			// 把所有的菜单都加入到它的父菜单中
			for (JsonElement el : objects) {
				JsonObject jo = el.getAsJsonObject();
				if (jo.get("f_level").getAsInt() != 1 && jo.has("f_parent_dictionary_data_id")) {
					JsonObject pjo = null;
					// 找到父节点
					for (JsonElement pel : objects) {
						if (pel.getAsJsonObject().get("id").getAsLong() == jo
								.get("f_parent_dictionary_data_id").getAsLong()) {
							pjo = pel.getAsJsonObject();
							break;
						}
					}
					if (pjo != null) {
						// 找到父节点的children成员
						JsonArray children = null;
						if (pjo.has("children")) {
							children = pjo.get("children").getAsJsonArray();
						} else {
							children = new JsonArray();
							pjo.addProperty("iconCls", "icon-manager-dictionarydatamanager");
							pjo.addProperty("expanded", false);
							pjo.remove("leaf");
							pjo.add("children", children);
						}

						// 把该节点加入到父节点的children当中
						children.add(jo);
					}

				}
			}

			// 将第一层的菜单加入到结果集当中
			for (JsonElement el : objects) {
				JsonObject jo = el.getAsJsonObject();
				if (jo.get("f_level").getAsInt() == 1) {
					jo.addProperty("iconCls", "icon-manager-dictionarydatamanager");
					datas.add(jo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return datas;
	}
}
