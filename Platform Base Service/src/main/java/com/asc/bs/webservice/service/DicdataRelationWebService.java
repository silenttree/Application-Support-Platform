package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.dicdatarelation.access.IDicdataRelationReader;
import com.asc.commons.dicdatarelation.entity.DicdataRelation;
import com.asc.commons.dicdatarelation.entity.DicdataRelationData;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

@WebService
public class DicdataRelationWebService {
	private IDicdataRelationReader reader;
	private Gson gson = new Gson();

	@WebMethod(exclude = true)
	public void setReader(IDicdataRelationReader reader) {
		this.reader = reader;
	}
	
	public String findRelationById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			DicdataRelation relation= reader.findRelationById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relation));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据关系获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findRelationByKey(String keyStr){
		JsonObject result = new JsonObject();
		try {
			DicdataRelation relation= reader.findRelationByKey(keyStr);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relation));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据关系获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findRelationDataById(String idStr){
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			DicdataRelationData relationData = reader.findRelationDataById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relationData));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "关系数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findRelationDataListByRelationAndSourceDataId(String relationIdStr, String sourceDataIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			long sourceDataId = Long.parseLong(sourceDataIdStr);
			List<DicdataRelationData> relationDataList = reader.findRelationDataListByRelationAndSourceDataId(relationId,sourceDataId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relationDataList));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "关系数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getRelationDataList(String relationIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			List<DicdataRelationData> relationDataList = reader.getRelationDataList(relationId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relationDataList));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "关系数据列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getRelationDataListIntoPages(String relationIdStr,String pagenumStr, String limitStr,String query){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			int pagenum = Integer.parseInt(pagenumStr);
			int limit = Integer.parseInt(limitStr);
			List<DicdataRelationData> relationDataList = reader.getRelationDataListIntoPages(relationId,pagenum,limit,query);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relationDataList));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据关系获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getRelationDataOrder(String relationIdStr,String sourceIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			long sourceId = Integer.parseInt(sourceIdStr);
			long order = reader.getRelationDataOrder(relationId,sourceId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(order));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "关系数据排序号获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getRelationDataTotalById(String relationIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			long total = reader.getRelationDataTotalById(relationId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(total));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "关系数据数量获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getRelationList(){
		JsonObject result = new JsonObject();
		try {
			List<DicdataRelation> relations = reader.getRelationList();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(relations));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "字典数据关系列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getTargetDataByRelationAndSourceDataId(String relationIdStr,String sourceDataIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			long sourceDataId = Integer.parseInt(sourceDataIdStr);
			List<DictionaryData> data = reader.getTargetDataByRelationAndSourceDataId(relationId,sourceDataId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "目标字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getTargetDataByRelationAndSourceDataKey(String relation,String sourceData){
		JsonObject result = new JsonObject();
		try {
			List<DictionaryData> data = reader.getTargetDataByRelationAndSourceDataKey(relation,sourceData);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "目标字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getSourceDataByRelationAndTargetDataId(String relationIdStr,String targetDataIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			long targetDataId = Integer.parseInt(targetDataIdStr);
			List<DictionaryData> data = reader.getSourceDataByRelationAndTargetDataId(relationId,targetDataId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getSourceDataByRelationAndTargetDataKey(String relation,String targetData){
		JsonObject result = new JsonObject();
		try {
			List<DictionaryData> data = reader.getSourceDataByRelationAndTargetDataKey(relation,targetData);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getSourceDataByRelationKey(String relation){
		JsonObject result = new JsonObject();
		try {
			List<DictionaryData> data = reader.getSourceDataByRelationKey(relation);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getTargetDataByRelationKey(String relation){
		JsonObject result = new JsonObject();
		try {
			List<DictionaryData> data = reader.getTargetDataByRelationKey(relation);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "目标字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getSourceDataByRelationId(String relationIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			List<DictionaryData> data = reader.getSourceDataByRelationId(relationId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "目标字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getTargetDataByRelationId(String relationIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			List<DictionaryData> data = reader.getTargetDataByRelationId(relationId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findRelationBySourceDicId(String idStr){
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			List<DicdataRelation> data = reader.findRelationBySourceDicId(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
	}

	public String findRelationByTargetDicId(String idStr){
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			List<DicdataRelation> data = reader.findRelationByTargetDicId(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
	}
	
	public  String findRelationDataBySourceTargetId(
			String relationIdStr, String sourceDataIdStr, String targetDataIdStr){
		JsonObject result = new JsonObject();
		try {
			long relationId = Long.parseLong(relationIdStr);
			long sourceDataId = Long.parseLong(sourceDataIdStr);
			long targetDataId = Long.parseLong(targetDataIdStr);
			DicdataRelationData data = reader.findRelationDataBySourceTargetId(relationId,sourceDataId,targetDataId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(data));
		} catch (DicdataRelationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "源字典数据获取失败");
		}
		return gson.toJson(result);
		
	}
}
