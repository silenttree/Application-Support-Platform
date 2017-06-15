package com.asc.manager.direct;

import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.manager.dicdatarelation.exception.DicdataRelationHandlerException;
import com.asc.manager.dicdatarelation.handler.DicdataRelationHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class DicdataRelationDirect {
	/**
	 * 获取字典数据关系列表
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject getRelationList(){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DicdataRelationHandler.instance().getRelationList();
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	/**
	 * 获取关系数据列表
	 * @param relationId
	 * @return
	 * @throws DicdataRelationHandlerException 
	 */
	@DirectMethod
	public JsonObject getRelationDataList(long relationId, int page, int limit,String query) throws DicdataRelationHandlerException{
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray data = DicdataRelationHandler.instance().getRelationDataList(relationId,page,limit,query);
			json.add("datas", data);
			long total = DicdataRelationHandler.instance().getRelationDataTotalById(relationId);
			json.addProperty("total", total);
			json.addProperty("success", true);
		} catch (DicdataRelationException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;	
	}
	
	/**
	 * 获取下拉框字典列表
	 * @return
	 */
	@DirectMethod
	public JsonObject getDictionaryList(){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DicdataRelationHandler.instance().getDictionaryList();
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 获取下拉框字典数据列表
	 * @return
	 */
	@DirectMethod
	public JsonObject getDictionaryDataList(long relationId){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DicdataRelationHandler.instance().getRelationList();
			json.add("datas", datas);
			JsonArray source = DicdataRelationHandler.instance().getDictionaryDataList(relationId,0);
			json.add("source", source);
			JsonArray target = DicdataRelationHandler.instance().getDictionaryDataList(relationId,1);
			json.add("target", target);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 装载量数据字典数据列表
	 * @param params
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDictionaryDataList(JsonObject params){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DicdataRelationHandler.instance().getDictionaryDataList(params.get("relationId").getAsLong(),params.get("dicType").getAsInt());
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 装载量数据字典数据列表(分页)
	 * @param String 
	 * @param params
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDictionaryDataListIntoPages(long relationId,int dicType,int page,int limit,String query){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = DicdataRelationHandler.instance().getDictionaryDataList(relationId,dicType,page,limit,query);
			json.add("datas", datas);
			long total = DicdataRelationHandler.instance().getDictionaryDataTotalById(relationId,dicType);
			json.addProperty("total", total);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 批量保存关系数据
	 * @param datas
	 * @return
	 * @throws DicdataRelationException
	 * @throws  
	 */
	@DirectMethod
	public JsonObject saveRelationData(JsonObject datas) throws DicdataRelationException{
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		long relationId = datas.get("relationId").getAsLong();
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = DicdataRelationHandler.instance().saveRelationData(relationId,data);
				sObjects.add(object);
			} catch (DicdataRelationException e) {
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
				DicdataRelationHandler.instance().deleteRelationData(id);
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
	 * 批量保存字典数据关系
	 * @param datas
	 * @return
	 * @throws DicdataRelationException
	 */
	@DirectMethod
	public JsonObject saveDicdataRelation(JsonObject datas) throws DicdataRelationException{
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			object = DicdataRelationHandler.instance().saveDicdataRelation(data);
			sObjects.add(object);
		}

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				DicdataRelationHandler.instance().deleteDicdataRelation(id);
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
	 * 装载字典树
	 * @return
	 */
	@DirectMethod
	public JsonObject loadRelationNodes(){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = DicdataRelationHandler.instance().loadRelationNodes();
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
	 * 装载字典数据树
	 * @param note
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDictionaryDataNodes(JsonObject note){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = DicdataRelationHandler.instance().loadDictionaryDataNode(note.get("relationId").getAsLong(), note.get("node").getAsLong(),note.get("dicType").getAsInt());
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
	 * 异步装载字典数据树
	 * @param params
	 * @return
	 */
	@DirectMethod
	public JsonArray loadDictionaryDataNode(JsonObject params){
		JsonArray datas = new JsonArray();
		try {
			JsonArray objects = DicdataRelationHandler.instance().loadDictionaryDataNode(params.get("dicId").getAsLong(),params.get("node").getAsLong(),params.get("dicType").getAsInt());
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
							pjo.addProperty("expanded", true);
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
					datas.add(jo);
				}
			}

		} catch (DicdataRelationException e) {
		
		}

		return datas;	
	}
	
}
