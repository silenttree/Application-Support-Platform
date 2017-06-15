package com.asc.bs.dicdatarelation.service;

import java.util.Date;

import com.asc.bs.dicdatarelation.access.imp.DicdataRelationWriterImp;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.dicdatarelation.access.IDicdataRelationWriter;
import com.asc.commons.dicdatarelation.entity.DicdataRelation;
import com.asc.commons.dicdatarelation.entity.DicdataRelationData;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dicdatarelation.service.DicdataRelationService;
import com.asc.commons.exception.DbAccessException;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class DicdataRelationManager {
	private static DicdataRelationManager singleton;
	private IDicdataRelationWriter relationWriter;

	public static DicdataRelationManager instance() {
		if (singleton == null) {
			singleton = new DicdataRelationManager();
		}
		return singleton;
	}

	public DicdataRelationManager() {
		// TODO 将实现类写入配置文件中，从配置文件中读取
		relationWriter = new DicdataRelationWriterImp();
	}

	/**
	 * 保存字典数据关系
	 * @param data
	 * @return
	 * @throws DicdataRelationException
	 */
	public DicdataRelation saveDicdataRelation(JsonObject data) throws DicdataRelationException {
		DicdataRelation relation = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}

		boolean isCreate = false;
		if(id > 0){
			relation = DicdataRelationService.instance().findRelationById(id);
			if(relation == null){
				isCreate = true;
				relation = new DicdataRelation();
			}
		}else{
			isCreate = true;
			relation = new DicdataRelation();
		}

		JsonObjectTool.jsonObject2Object(data, relation);
		
		//查找key是否重复
		DicdataRelation dr = DicdataRelationService.instance().findRelationByKey(relation.getF_key());
		if(dr != null && dr.getId() != relation.getId()){
			throw new DicdataRelationException("字典数据关系key已存在。");
		}
		
		if(isCreate){
			relation.setF_create_time(new Date());
		}
		relation.setF_update_time(new Date());
		
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			relationWriter.saveDicdataRelation(relation);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DicdataRelationException("字典数据关系保存失败！");
		} finally{
			CommonDatabaseAccess.instance().endTransaction();
		}
		return relation;
	}

	/**
	 * 保存关系数据
	 * @param data
	 * @return
	 */
	public DicdataRelationData saveRelationData(long relationId,JsonObject data) throws DicdataRelationException{
		DicdataRelationData relationdata = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}

		boolean isCreate = false;
		if(id > 0){
			relationdata = DicdataRelationService.instance().findRelationDataById(id);
			if(relationdata == null){
				isCreate = true;
				relationdata = new DicdataRelationData();
			}
		}else{
			isCreate = true;
			relationdata = new DicdataRelationData();
		}
				
		JsonObjectTool.jsonObject2Object(data, relationdata);
		
		//查找数据是否重复
		DicdataRelationData rd = DicdataRelationService.instance().findRelationDataBySourceTargetId(relationId,relationdata.getF_source_data_id(),relationdata.getF_target_data_id());
		if(rd != null && rd.getId() != relationdata.getId()){
			throw new DicdataRelationException("关系数据已存在。");
		}
		
		if(isCreate){
			relationdata.setF_create_time(new Date());
		}
		relationdata.setF_relation_id(relationId);
		relationdata.setF_update_time(new Date());
		long sourceId = relationdata.getF_source_data_id();
		long order = 0;
		if(data.has("f_order")){
			if("".equals(data.get("f_order").getAsString())){
				order = DicdataRelationService.instance().getRelationDataOrder(relationId,sourceId);
			}else{
				order = data.get("f_order").getAsLong();
			}
		}else {
			order = DicdataRelationService.instance().getRelationDataOrder(relationId,sourceId);
		}
		relationdata.setF_order(order);
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			relationWriter.saveRelationData(relationdata);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DicdataRelationException("字典数据关系保存失败！");
		} finally{
			CommonDatabaseAccess.instance().endTransaction();
		}
		return relationdata;
	}
	
	/**
	 * 根据ID删除字典数据关系
	 * @param id
	 * @throws DicdataRelationException
	 */
	public void deleteDicdataRelation(long id) throws DicdataRelationException{
		relationWriter.deleteDicdataRelation(id);
	}

	/**
	 * 根据ID删除关系数据
	 * @param id
	 * @throws DicdataRelationException
	 */
	public void deleteRelationData(long id) throws DicdataRelationException{
		relationWriter.deleteRelationData(id);
	}
	
}
