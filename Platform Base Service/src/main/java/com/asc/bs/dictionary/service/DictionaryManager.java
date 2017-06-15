package com.asc.bs.dictionary.service;

import java.util.Date;

import com.asc.bs.dictionary.access.imp.DictionaryWriterImp;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dictionary.access.IDictionaryWriter;
import com.asc.commons.dictionary.entity.Dictionary;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.dictionary.service.DictionaryService;
import com.asc.commons.exception.DbAccessException;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class DictionaryManager {
	private static DictionaryManager singleton;
	private IDictionaryWriter dicWriter;

	public static DictionaryManager instance() {
		if (singleton == null) {
			singleton = new DictionaryManager();
		}
		return singleton;
	}

	public DictionaryManager() {
		// TODO 将实现类写入配置文件中，从配置文件中读取
		dicWriter = new DictionaryWriterImp();
	}

	/**
	 * 保存字典
	 * 
	 * @param dicId
	 * @param data
	 * @return
	 * @throws DictionaryException
	 */
	public Dictionary saveDictionary(long dicId, JsonObject data) throws DictionaryException{
		Dictionary dictionary = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}

		boolean isCreate = false;
		if(id > 0){
			dictionary = DictionaryService.instance().findDictionaryById(id);
			if(dictionary == null){
				isCreate = true;
				dictionary = new Dictionary();
				dictionary.setF_parent_id(dicId);
			}
		}else{
			isCreate = true;
			dictionary = new Dictionary();
			dictionary.setF_parent_id(dicId);
		}

		JsonObjectTool.jsonObject2Object(data, dictionary);
		//查找父节点是否为多级字典
		if(dictionary.getF_parent_id() > 0){
			Dictionary upDic = DictionaryService.instance().findDictionaryById(dictionary.getF_parent_id());
			if(upDic.getF_allow_extend() == 0){
				throw new DictionaryException("字典不是多级字典，无法创建子字典。");
			}
		}
		
		//查找key是否重复
		Dictionary d = DictionaryService.instance().findDictionaryByKey(dictionary.getF_key());
		if(d != null && d.getId() != dictionary.getId()){
			throw new DictionaryException("字典key已存在。");
		}
		
		if(isCreate){
			dictionary.setF_create_time(new Date());
		}
		dictionary.setF_update_time(new Date());
		Dictionary oldDic = DictionaryService.instance().findDictionaryById(id);//获取原字典记录
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			dicWriter.saveDictionary(dictionary);
			if(!isCreate){//当类型为更新时记录日志 lhy
				dicWriter.saveDictionaryLog(oldDic, "update");
			}
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DictionaryException("字典保存失败！");
		} finally{
			CommonDatabaseAccess.instance().endTransaction();
		}
		return dictionary;
	}

	/**
	 * 删除字典
	 * 
	 * @param id
	 * @throws DictionaryException 
	 * @throws DicdataRelationException 
	 */
	public void deleteDictionary(long id) throws DictionaryException, DicdataRelationException {
		dicWriter.deleteDictionar(id);
	}

	/**
	 * 保存字典数据
	 * 
	 * @param dicId
	 * @param data
	 * @return
	 * @throws DictionaryException 
	 */
	public DictionaryData saveDictionaryData(long dicId, long dataId, JsonObject data) throws DictionaryException {
		DictionaryData dictionaryData = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}

		boolean isCreate = false;
		if(id > 0){
			dictionaryData = DictionaryService.instance().findDictionaryDataById(id);
			if(dictionaryData == null){
				isCreate = true;
				dictionaryData = new DictionaryData();
				dictionaryData.setF_dictionary_id(dicId);
				dictionaryData.setF_parent_dictionary_data_id(data.get("parentId").getAsLong());
			}
		}else{
			isCreate = true;
			dictionaryData = new DictionaryData();
			dictionaryData.setF_dictionary_id(dicId);
			dictionaryData.setF_parent_dictionary_data_id(data.get("parentId").getAsLong());
		}

		if(data.has("f_order")){
			dictionaryData.setF_order(data.get("f_order").getAsInt());
		}

		JsonObjectTool.jsonObject2Object(data, dictionaryData);
		if(isCreate){
			dictionaryData.setF_create_time(new Date());
		}
		dicWriter.saveDictionaryData(dictionaryData, dicId);
		//查找是否有扩展属性
		return dictionaryData;
	}

	public void deleteDictionaryData(long id) throws DictionaryException {
		DictionaryData dicData = DictionaryService.instance().findDictionaryDataById(id);//lhy add 获取当前操作的字典数据
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			dicWriter.deleteDictionaryData(id);
			dicWriter.saveDictionaryDataLog(dicData, "delete");//lhy add 记录日志
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DictionaryException("字典数据删除失败！");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

}
