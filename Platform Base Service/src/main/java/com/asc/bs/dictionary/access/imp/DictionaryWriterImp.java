package com.asc.bs.dictionary.access.imp;

import java.util.Date;
import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.dicdatarelation.entity.DicdataRelationData;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dictionary.access.IDictionaryWriter;
import com.asc.commons.dictionary.entity.Dictionary;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.dictionary.entity.DictionaryDataLog;
import com.asc.commons.dictionary.entity.DictionaryLog;
import com.asc.commons.dictionary.exception.DictionaryException;
import com.asc.commons.dictionary.service.DictionaryService;
import com.asc.commons.exception.DbAccessException;

public class DictionaryWriterImp implements IDictionaryWriter {

	@Override
	public void saveDictionary(Dictionary dictionary) throws DictionaryException {
		//判断是否有key
		if(dictionary.getF_key() == "" || "".equals(dictionary.getF_key())){
			throw new DictionaryException("未指定字典标识。");
		}
		//判断是否有caption
		if(dictionary.getF_caption() == "" || "".equals(dictionary.getF_caption())){
			throw new DictionaryException("未指定字典名称。");
		}
		
		try {
			getDbAccess().saveObject(dictionary);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典保存失败。", e);
		}
	}

	@Override
	public void deleteDictionar(long id) throws DictionaryException, DicdataRelationException {
		Dictionary dictionary = DictionaryService.instance().findDictionaryById(id);
		//判断字典是否为多级字典  如果存在下级字典不许删除
		if(dictionary.getF_allow_extend() == 1){
			List<Dictionary> dicList = DictionaryService.instance().getDictionaryList(id);
			if(dicList != null && dicList.size() > 0){
				throw new DictionaryException("存在下级字典，删除失败。");
			}
		}
		//判断字典是否有数据
		List<DictionaryData> dicDataList = DictionaryService.instance().getDictionaryDataListByDicId(dictionary.getId());
		if(dicDataList.size() != 0){
			throw new DictionaryException("字典存在数据，删除失败。");
		}
		
		//判断字典是否有数据关系
		//lhy 2016-03-04 将此部分代码注销 原因： 现ASC平台数据库没有t_asc_dicdata_relation 表 ，查询报错
	/*	List<DicdataRelation> srelationList = DicdataRelationService.instance().findRelationBySourceDicId(dictionary.getId());
		List<DicdataRelation> trelationList = DicdataRelationService.instance().findRelationByTargetDicId(dictionary.getId());
		if(srelationList.size() != 0 || trelationList.size() != 0){
			throw new DictionaryException("字典存在数据关系，删除失败。");
			}*/
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(Dictionary.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			saveDictionaryLog(dictionary, "delete");//lhy add 记录字典删除操作日志
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DictionaryException("字典删除失败。");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	@Override
	public void saveDictionaryData(DictionaryData dictionaryData, long dicId) throws DictionaryException {
		//判断数据字典是否为多级字典
		Dictionary dictionary = DictionaryService.instance().findDictionaryById(dictionaryData.getF_dictionary_id());
		if(dictionary.getF_allow_extend() == 0 && dictionaryData.getF_parent_dictionary_data_id() != 0){
			throw new DictionaryException("字典不是多级字典,不能创建子字典数据。");
		}
		//判断是否有key
		if(dictionaryData.getF_key() == "" || "".equals(dictionaryData.getF_key())){
			throw new DictionaryException("未指定数据标识。");
		}
		//判断是否有value
		if(dictionaryData.getF_value() == "" || "".equals(dictionaryData.getF_value())){
			throw new DictionaryException("未指定数据值。");
		}
		//查找key是否重复
		DictionaryData d = DictionaryService.instance().findDictionaryDataByKey(dictionaryData.getF_key(),dicId);
		if(d != null && d.getId() != dictionaryData.getId()){
			throw new DictionaryException("字典数据key已存在。");
		}
		//判断当前操作的类型：当操作类型为修改时，使用DictionaryData装载DictionaryDataLog lhy start
		boolean isCreate = false;
		DictionaryData oldDicData = DictionaryService.instance().findDictionaryDataById(dictionaryData.getId());
		if(oldDicData == null || oldDicData.getId()<=0){
			isCreate = true;
		}
		// end
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			getDbAccess().saveObject(dictionaryData);
			if(!isCreate){//当操作类型为更新时，记录日志 lhy
				saveDictionaryDataLog(oldDicData,"update");
			}
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DictionaryException("字典数据保存失败。");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	@Override
	public void deleteDictionaryData(long id) throws DictionaryException {
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(DictionaryData.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			//删除对应扩展属性数据
			String s = "delete from t_asc_ext_property_data " + 
					" where f_dataid = ?";
			getDbAccess().execute(s, new Object[]{id});
			//删除对应的关系数据
			String str = "delete from "  + CommonDatabaseAccess.getTableName(DicdataRelationData.class) +
					" where f_source_data_id = ? or f_target_data_id = ?";
			getDbAccess().execute(str, new Object[]{id,id});
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据删除失败。");
		}
	}

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public void saveDictionaryLog(Dictionary dic,String operateType) throws DictionaryException {
		try {
			DictionaryLog dl = loadDicLog(dic, operateType);
			getDbAccess().saveObject(dl);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典操作日志记录失败。");
		}
	}
	@Override
	public void saveDictionaryDataLog(DictionaryData dicData,String operateType)
			throws DictionaryException {
		try {
			DictionaryDataLog ddl = loadDicDataLog(dicData, operateType);
			getDbAccess().saveObject(ddl);
		} catch (DbAccessException e) {
			throw new DictionaryException("字典数据操作日志记录失败。");
		}
	}
	
	/**
	 * 使用字典装载字典操作日志
	 * @author lhy
	 * @date 2016年3月1日 下午3:20:18
	 * @param dic
	 * @param operateType
	 * @return
	 */
	private DictionaryLog loadDicLog(Dictionary dic,String operateType){
		DictionaryLog dl = new DictionaryLog();
		dl.setF_parent_id(dic.getF_parent_id());
		dl.setF_key(dic.getF_key());
		dl.setF_caption(dic.getF_caption());
		dl.setF_allow_extend(dic.getF_allow_extend());
		dl.setF_allow_redefine(dic.getF_allow_redefine());
		dl.setF_multi_level(dic.getF_multi_level());
		dl.setF_note(dic.getF_note());
		dl.setF_create_time(dic.getF_create_time());
		dl.setF_update_time(dic.getF_update_time());
		dl.setF_dic_id(dic.getId());
		dl.setF_operate_type(operateType);
		//dl.setF_operater(f_operater);
		dl.setF_operate_time(new Date());
		return dl;
	}
	/**
	 * 使用字典数据装载字典数据操作日志
	 * @author lhy
	 * @date 2016年3月1日 下午2:47:19
	 * @param dictionaryData
	 * @param operateType
	 * @return
	 */
	private DictionaryDataLog loadDicDataLog(DictionaryData dicData,String operateType){
		DictionaryDataLog ddl = new DictionaryDataLog();
		ddl.setF_dictionary_id(dicData.getF_dictionary_id());
		ddl.setF_parent_dictionary_data_id(dicData.getF_parent_dictionary_data_id());
		ddl.setF_level(dicData.getF_level());
		ddl.setF_key(dicData.getF_key());
		ddl.setF_value(dicData.getF_value());
		ddl.setF_state(dicData.getF_state());
		ddl.setF_shortcode(dicData.getF_shortcode());
		ddl.setF_order(dicData.getF_order());
		ddl.setF_create_time(dicData.getF_create_time());
		ddl.setF_dic_data_id(dicData.getId());
		ddl.setF_operate_type(operateType);
		//ddl.setF_operater(dicData);
		ddl.setF_operate_time(new Date());
		return ddl;
	}
}
