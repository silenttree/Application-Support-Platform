package com.asc.manager.dictionary.handler;

import java.util.List;

import com.asc.bs.dictionary.service.DictionaryManager;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dictionary.entity.Dictionary;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.dictionary.service.DictionaryService;
import com.asc.commons.extproperty.entity.Extproperty;
import com.asc.commons.extproperty.entity.ExtpropertyData;
import com.asc.commons.extproperty.exception.ExtpropertyException;
import com.asc.commons.extproperty.service.ExtpropertyService;
import com.asc.manager.extproperty.exception.ExtpropertyHandlerException;
import com.asc.manager.extproperty.handler.ExtpropertyHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class DictionaryHandler {

	private static DictionaryHandler singleton;

	public static DictionaryHandler instance() {
		if (singleton == null) {
			singleton = new DictionaryHandler();
		}
		return singleton;
	}
	/**
	 * 通过上级词典编码获取字典项
	 * 
	 * @param dicId
	 * @param data
	 * @return
	 * @throws DictionaryException
	 * @throws ExtpropertyException 
	 */
	public JsonArray getDicDatasByPCode(String pCode) throws DictionaryException, ExtpropertyException {
		JsonArray datas =  new JsonArray();
		Dictionary ParentDic = DictionaryService.instance().findDictionaryByKey(pCode);
		if(ParentDic.getId()>0){
			datas = getDictionaryDataList(ParentDic.getId(), 0);
		}
		return datas;
	}

	/**
	 * 保存字典
	 * 
	 * @param dicId
	 * @param data
	 * @return
	 * @throws DictionaryException
	 */
	public JsonObject saveDictionary(long dicId, JsonObject data) throws DictionaryException{
		Dictionary dictionary = DictionaryManager.instance().saveDictionary(0, data);
		return JsonObjectTool.object2JsonObject(dictionary);
	}

	/**
	 * 查找字典列表
	 * 
	 * @param dicId
	 * @return
	 * @throws DictionaryException
	 */
	public JsonArray getDictionaryList(long dicId) throws DictionaryException {
		JsonArray datas = new JsonArray();
		List<Dictionary> dicList = DictionaryService.instance().getDictionaryList(dicId);
		datas = JsonObjectTool.objectList2JsonArray(dicList);
		//查找字典扩展属性
		for(int i = 0; i < datas.size(); i++){
			JsonObject data = datas.get(i).getAsJsonObject();
			List<Extproperty> extpropertyList;
			try {
				extpropertyList = ExtpropertyService.instance().findExtpropertyListByTablename("t_asc_dictionary_data", data.get("id").getAsLong());
			} catch (ExtpropertyException e) {
				throw new DictionaryException("字典扩展属性查找失败！");
			}
			String extpropertyname = "";
			for(int j = 0; j < extpropertyList.size(); j++){
				Extproperty extproperty = extpropertyList.get(j);
				if(j == 0){
					extpropertyname = extproperty.getF_field_caption();
				}else{
					extpropertyname = extpropertyname + ";" + extproperty.getF_field_caption();
				}
			}
			data.addProperty("f_propertys", extpropertyname);
		}
		return datas;
	}

	/**
	 * 获取字典树
	 * 
	 * @return
	 */
	public JsonArray loadDictionaryNodes(long dicId) throws DictionaryException {
		JsonArray datas = new JsonArray();
		try {
			List<Dictionary> dicList = DictionaryService.instance().getDictionaryList(0);
			if(dicList != null){
				for(int i = 0; i< dicList.size(); i++){
					Dictionary dictionary = dicList.get(i);
					JsonObject data = new JsonObject();
					data.addProperty("id", dictionary.getId());
					data.addProperty("text", dictionary.getF_key()+":"+dictionary.getF_caption());//lhy update 将字典树显示形式由 "名称" 改为 "标识:名称"
					data.addProperty("leaf", true);
					data.addProperty("iconCls", "icon-manager-dictionarymanager");
					datas.add(data);
				}
			}
			return datas;
		} catch (DictionaryException e) {
			throw new DictionaryException("字典树获取失败。", e);
		}
	}

	/**
	 * 删除字典
	 * 
	 * @param id
	 * @throws DictionaryException
	 * @throws DicdataRelationException 
	 */
	public void deleteDictionary(long id) throws DictionaryException, DicdataRelationException {
		DictionaryManager.instance().deleteDictionary(id);
	}

	/**
	 * 查找字典数据列表
	 * 
	 * @param dicId
	 * @return
	 * @throws DictionaryException 
	 * @throws ExtpropertyException 
	 */
	public JsonArray getDictionaryDataList(long dicId, long dataId) throws DictionaryException, ExtpropertyException {
		JsonArray datas = new JsonArray();
		List<DictionaryData> dicDataList = DictionaryService.instance().getDictionaryDataListByParId(dicId, dataId);
		if(dicDataList != null){
			datas = JsonObjectTool.objectList2JsonArray(dicDataList);
			//查找字典扩展属性数据
			for(int i = 0; i < datas.size(); i++){
				JsonObject data = datas.get(i).getAsJsonObject();
				List<Extproperty> extpropertyList = ExtpropertyService.instance().findExtpropertyListByTablename("t_asc_dictionary_data", data.get("f_dictionary_id").getAsLong());
				if(extpropertyList != null){
					for(int j = 0; j < extpropertyList.size(); j++){
						Extproperty extproperty = extpropertyList.get(j);
						ExtpropertyData extpropertyData = ExtpropertyService.instance().getExtprodata(data.get("id").getAsLong(), extproperty.getId());
						data.addProperty(extproperty.getF_field_name(), extpropertyData.getF_value());
					}
				}
			}
		}
		return datas;
	}

	/**
	 * 保存字典数据
	 * 
	 * @param dicId
	 * @param data
	 * @return
	 * @throws DictionaryException 
	 */
	public JsonObject saveDictionaryData(long dicId, long dataId, JsonObject data) throws DictionaryException {
		DictionaryData dictionaryData = DictionaryManager.instance().saveDictionaryData(dicId, dataId, data);
		if(dictionaryData != null){
			try {
				ExtpropertyHandler.instance().saveExtpropertyData(dictionaryData.getId(), dictionaryData.getF_dictionary_id(), "t_asc_dictionary_data", data);
			} catch (ExtpropertyHandlerException e) {
				throw new DictionaryException("字典扩展属性数据保存失败！");
			}
		}
		return JsonObjectTool.object2JsonObject(dictionaryData);
	}

	/**
	 * 删除字典数据
	 * 
	 * @param id
	 * @throws DictionaryException 
	 */
	public void deleteDictionaryData(long id) throws DictionaryException {
		DictionaryManager.instance().deleteDictionaryData(id);
	}

	/**
	 * 字典数据树
	 * 
	 * @param dataId
	 * @return
	 * @throws DictionaryException
	 */
	public JsonArray loadDictionaryDataNodes(long dicId, long dataId) throws DictionaryException {
		JsonArray datas = new JsonArray();
		try {
			List<DictionaryData> DataList = DictionaryService.instance().getDictionaryDataListByParId(dicId, dataId);
			if(DataList != null){
				for(int i = 0; i< DataList.size(); i++){
					DictionaryData data = DataList.get(i);
					JsonObject json = new JsonObject();
					json.addProperty("id", data.getId());
					json.addProperty("text", data.getF_key());
					json.addProperty("leaf", false);
					json.addProperty("iconCls", "icon-manager-dictionarydatamanager");
					datas.add(json);
				}
			}
			return datas;
		} catch (DictionaryException e) {
			throw new DictionaryException("字典数据树获取失败。", e);
		}
	}

	/**
	 * 字典数据树
	 * 
	 * @param dataId
	 * @return
	 * @throws DictionaryException
	 */
	public JsonArray loadDictionaryDataNode(long dicId, long dataId) throws DictionaryException {
		JsonArray datas = new JsonArray();
		try {
			List<DictionaryData> dataList = DictionaryService.instance().getDictionaryDataListByParId(dicId, dataId);
			if(dataList != null){
				datas = JsonObjectTool.objectList2JsonArray(dataList);
			}
			return datas;
		} catch (DictionaryException e) {
			throw new DictionaryException("字典数据树获取失败。", e);
		}
	}
}
