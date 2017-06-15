package com.asc.manager.extproperty.handler;

import java.util.List;

import com.asc.bs.extproperty.service.ExtpropertyManager;
import com.asc.commons.extproperty.entity.Extproperty;
import com.asc.commons.extproperty.entity.ExtpropertyData;
import com.asc.commons.extproperty.exception.ExtpropertyException;
import com.asc.commons.extproperty.service.ExtpropertyService;
import com.asc.manager.extproperty.exception.ExtpropertyHandlerException;
import com.asc.manager.org.exception.OrganizationHandlerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class ExtpropertyHandler {
	private static ExtpropertyHandler singleton;

	public static ExtpropertyHandler instance() {
		if (singleton == null) {
			singleton = new ExtpropertyHandler();
		}
		return singleton;
	}

	/**
	 * 查找数据扩展属性数据
	 * 
	 * @param tableName
	 * @param dataId
	 * @return
	 */
	public JsonArray findExtpropertyData(String tableName, int dataId) throws ExtpropertyHandlerException{
		JsonArray datas = new JsonArray();
		//获取扩展属性
		List<Extproperty> extpropertyList;
		try {
			extpropertyList = ExtpropertyService.instance().findExtpropertyListByTablename(tableName, dataId);
		} catch (ExtpropertyException e) {
			throw new ExtpropertyHandlerException("扩展属性列获取失败！");
		}
		//判断数据是否有扩展属性
		if(extpropertyList != null){
			for(int j = 0; j < extpropertyList.size(); j++){
				Extproperty extproperty = extpropertyList.get(j);
				//查找扩展属性数据列表
				List<ExtpropertyData> extpropertyDataList;
				try {
					extpropertyDataList = ExtpropertyService.instance().findExtpropertyData(0, extproperty.getId());
				} catch (ExtpropertyException e) {
					throw new ExtpropertyHandlerException("扩展属性数据列获取失败！");
				}
				if(extpropertyDataList != null){
					for(int i = 0; i < extpropertyDataList.size(); i++){
						JsonObject data = new JsonObject();
						ExtpropertyData extpropertyData = extpropertyDataList.get(i);
						data.addProperty("id", extpropertyData.getF_dataid());
						data.addProperty("f_field_name", extproperty.getF_field_name());
						data.addProperty("f_value", extpropertyData.getF_value());
						datas.add(data);
					}
				}
			}
		}
		return datas;
	}

	/**
	 * 查找扩展属性
	 * 
	 * @param tablename
	 * @param dataId
	 * @return
	 */
	public JsonArray loadExtproperty(String tablename, long dataId) throws ExtpropertyHandlerException{
		List<Extproperty> extpropertyList;
		try {
			extpropertyList = ExtpropertyService.instance().findExtpropertyListByTablename(tablename, dataId);
		} catch (ExtpropertyException e) {
			throw new ExtpropertyHandlerException("扩展属性列获取失败！");
		}
		JsonArray datas = JsonObjectTool.objectList2JsonArray(extpropertyList);
		//转换配置
		for(int i = 0; i < datas.size(); i++){
			JsonObject data = datas.get(i).getAsJsonObject();
			//取得扩展属性配置转为jsonobject
			if(data.has("f_config")){
				String confStr = data.get("f_config").getAsString();
				if(confStr != null && !"".equals(confStr)){
					JsonObject conf = JsonObjectTool.string2JsonObject(confStr);
					String stType = data.get("f_editor_type").getAsString();//Extproperty.Types.valueOf(data.get("f_editor_type").getAsInt()).toString();
					data.remove("f_editor_type");
					data.addProperty("f_editor_type", stType);
					//移除配置
					data.remove("f_config");
					//添加新格式配置
					data.add("f_config", conf);
				}
			}
		}
		return datas;
	}

	/**
	 * 保存扩展属性
	 * 
	 * @param dataId
	 * @param data
	 * @return
	 * @throws OrganizationHandlerException
	 */
	public JsonObject saveExtproperty(long dataId, JsonObject data) throws ExtpropertyHandlerException {
		Extproperty extproperty;
		try {
			extproperty = ExtpropertyManager.instance().saveExtproperty(dataId, data);
			return JsonObjectTool.object2JsonObject(extproperty);
		} catch (ExtpropertyException e) {
			throw new ExtpropertyHandlerException("扩展属性保存失败！");
		}
	}

	/**
	 * 删除扩展属性
	 * 
	 * @param id
	 */
	public void deleteExtproperty(long id) throws ExtpropertyHandlerException {
		try {
			ExtpropertyManager.instance().deleteExtproperty(id);
		} catch (ExtpropertyException e) {
			throw new ExtpropertyHandlerException("扩展属性删除失败！");
		}
	}

	/**
	 * 保存扩展属性数据
	 * 
	 * @param dataId
	 * @param tablename
	 * @param data
	 */
	public void saveExtpropertyData(long id, long dataId, String tablename, JsonObject data) throws ExtpropertyHandlerException{
		try {
			ExtpropertyManager.instance().saveExtpropertyData(id, dataId, tablename, data);
		} catch (ExtpropertyException e) {
			throw new ExtpropertyHandlerException("扩展属性数据保存失败！");
		}
	}
}
