package com.asc.manager.dicdatarelation.handler;

import java.util.List;

import com.asc.bs.dicdatarelation.service.DicdataRelationManager;
import com.asc.commons.dicdatarelation.entity.DicdataRelation;
import com.asc.commons.dicdatarelation.entity.DicdataRelationData;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dicdatarelation.service.DicdataRelationService;
import com.asc.commons.dictionary.entity.Dictionary;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.dictionary.service.DictionaryService;
import com.asc.manager.dicdatarelation.exception.DicdataRelationHandlerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class DicdataRelationHandler {
	private static DicdataRelationHandler singleton;

	public static DicdataRelationHandler instance() {
		if (singleton == null) {
			singleton = new DicdataRelationHandler();
		}
		return singleton;
	}

	/**
	 * 获取字典数据关系列表
	 * @return
	 * @throws DicdataRelationHandlerException
	 */
	public JsonArray getRelationList() throws DicdataRelationHandlerException {
		try {
			List<DicdataRelation> relationList = DicdataRelationService.instance().getRelationList();
			return JsonObjectTool.objectList2JsonArray(relationList);
		} catch (DicdataRelationException e) {
			throw new DicdataRelationHandlerException("字典数据关系列表获取失败。");
		}
	}

	/**
	 * 获取字典选择列表
	 * @return
	 * @throws DicdataRelationHandlerException
	 */
	public JsonArray getDictionaryList() throws DicdataRelationHandlerException{
		try {
			List<Dictionary> dicList = DictionaryService.instance().getDictionaryList(0);
			return JsonObjectTool.objectList2JsonArray(dicList);
		} catch (DictionaryException e) {
			throw new DicdataRelationHandlerException("字典列表获取失败。");
		}
	}

	/**
	 * 保存字典数据关系
	 * @param data
	 * @return
	 * @throws DicdataRelationException
	 */
	public JsonObject saveDicdataRelation(JsonObject data) throws DicdataRelationException {
		DicdataRelation relation = DicdataRelationManager.instance().saveDicdataRelation(data);
		return JsonObjectTool.object2JsonObject(relation);
	}

	/**
	 * 保存关系数据
	 * @param data
	 * @return
	 */
	public JsonObject saveRelationData(long relationId,JsonObject data) throws DicdataRelationException{
		DicdataRelationData relationdata = DicdataRelationManager.instance().saveRelationData(relationId,data);
		return JsonObjectTool.object2JsonObject(relationdata);
	}

	/**
	 * 装载字典数据关系树
	 * @return
	 * @throws DicdataRelationException
	 */
	public JsonArray loadRelationNodes() throws DicdataRelationException{
		JsonArray datas = new JsonArray();
		try {
			List<DicdataRelation> relationList = DicdataRelationService.instance().getRelationList();
			if(relationList != null){
				for(int i = 0; i< relationList.size(); i++){
					DicdataRelation relation = relationList.get(i);
					JsonObject data = new JsonObject();
					data.addProperty("id", relation.getId());
					data.addProperty("text", relation.getF_caption());
					data.addProperty("leaf", true);
					data.addProperty("iconCls", "icon-manager-dictionarymanager");
					datas.add(data);
				}
			}
			return datas;
		} catch (DicdataRelationException e) {
			throw new DicdataRelationException("字典数据关系树获取失败。", e);
		}
	}

	/**
	 * 获取关系数据列表（分页显示）
	 * @param relationId
	 * @param pagenum
	 * @param limit
	 * @return
	 * @throws DicdataRelationException
	 */
	public JsonArray getRelationDataList(long relationId, int pagenum, int limit,String query) throws DicdataRelationException {
		// TODO Auto-generated method stub
		JsonArray datas = new JsonArray();
		List<DicdataRelationData> relationDataList = DicdataRelationService.instance().getRelationDataListIntoPages(relationId,pagenum,limit,query);
		if(relationDataList != null){
			datas = JsonObjectTool.objectList2JsonArray(relationDataList);
		}
		return datas;
	}

	/**
	 * 根据字典ID获取字典数据列表
	 * @param dicId
	 * @param i 
	 * @return
	 * @throws DicdataRelationHandlerException
	 * @throws DicdataRelationException 
	 */
	public JsonArray getDictionaryDataList(long relationId, int type) throws DicdataRelationHandlerException, DicdataRelationException {
		DicdataRelation relation = DicdataRelationService.instance().findRelationById(relationId);
		long dicId;
		if(type==0){
			dicId = relation.getF_source_dic_id();
		} else{
			dicId = relation.getF_target_dic_id();
		}
		try {
			List<DictionaryData> dataList = DictionaryService.instance().getDictionaryDataListByDicId(dicId);
			return JsonObjectTool.objectList2JsonArray(dataList);
		} catch (DictionaryException e) {
			throw new DicdataRelationHandlerException("字典数据列表获取失败。");
		}
	}

	/**
	 * 根据字典ID获取字典数据列表(分页)
	 * @param relationId
	 * @param dicType
	 * @param page
	 * @param limit
	 * @param query 
	 * @return
	 * @throws DicdataRelationException 
	 * @throws DicdataRelationHandlerException 
	 */
	public JsonArray getDictionaryDataList(long relationId, int type,int page, int limit, String query) throws DicdataRelationException, DicdataRelationHandlerException{
		DicdataRelation relation = DicdataRelationService.instance().findRelationById(relationId);
		long dicId;
		if(type==0){
			dicId = relation.getF_source_dic_id();
		} else{
			dicId = relation.getF_target_dic_id();
		}
		try {
			List<DictionaryData> dataList =DictionaryService.instance().getDictionaryDataListByDicId(dicId,page,limit,query);
			return JsonObjectTool.objectList2JsonArray(dataList);
		} catch (DictionaryException e) {
			throw new DicdataRelationHandlerException("字典数据列表获取失败。");
		}
	}
	
	

	
	/**
	 * 根据ID删除字典数据关系
	 * @param id
	 * @throws DicdataRelationException
	 */
	public void deleteDicdataRelation(long id) throws DicdataRelationException{
		DicdataRelationManager.instance().deleteDicdataRelation(id);		
	}
	
	public void deleteRelationData(long id) throws DicdataRelationException {
		DicdataRelationManager.instance().deleteRelationData(id);
	}

	public JsonArray loadDictionaryDataNode(long relationId, long dataId, int type) throws DicdataRelationException{
		JsonArray datas = new JsonArray();
		DicdataRelation relation = DicdataRelationService.instance().findRelationById(relationId);
		long dicId;
		if(type==0){
			dicId = relation.getF_source_dic_id();
		} else{
			dicId = relation.getF_target_dic_id();
		}
		try {
			List<DictionaryData> objs = DictionaryService.instance().getDictionaryDataListByParId(dicId,dataId);
			if(objs != null){
				datas = JsonObjectTool.objectList2JsonArray(objs);
			}
			return datas;
		} catch (DictionaryException e) {
			throw new DicdataRelationException("装载字典数据失败。");
		}
	}

	/**
	 * 根据关系ID获取关系数据数量
	 * @param relationId
	 * @return
	 * @throws DicdataRelationHandlerException
	 */
	public long getRelationDataTotalById(long relationId) throws DicdataRelationHandlerException {
		try {
			return DicdataRelationService.instance().getRelationDataTotalById(relationId);
		} catch (DicdataRelationException e) {
			throw new DicdataRelationHandlerException("关系数据数量获取失败。");
		}
	}
	
	public long getDictionaryDataTotalById(long relationId, int type) throws DicdataRelationHandlerException, DicdataRelationException {
		DicdataRelation relation = DicdataRelationService.instance().findRelationById(relationId);
		long dicId;
		if(type==0){
			dicId = relation.getF_source_dic_id();
		} else{
			dicId = relation.getF_target_dic_id();
		}
		try {
			return DictionaryService.instance().getDictionaryDataTotalById(dicId);
		} catch (DictionaryException e) {
			throw new DicdataRelationHandlerException("字典数据数量获取失败。");
		}
	}

}
