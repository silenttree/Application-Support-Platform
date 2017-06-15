package com.asc.bs.dicdatarelation.access.imp;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.DataValueUtil;
import com.asc.commons.dicdatarelation.access.IDicdataRelationReader;
import com.asc.commons.dicdatarelation.entity.DicdataRelation;
import com.asc.commons.dicdatarelation.entity.DicdataRelationData;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dictionary.entity.DictionaryData;
import com.asc.commons.exception.DbAccessException;

public class DicdataRelationReaderImp implements IDicdataRelationReader{

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}
	/**
	 * 获取全部字典数据关系列表
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DicdataRelation> getRelationList() throws DicdataRelationException{
		CommonDatabaseAccess.instance().beginTransaction();
		try {
			List<DicdataRelation> rList = null;
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelation.class);

				rList = getDbAccess().listObjects(sql, new Object[]{}, DicdataRelation.class, 0, 0);
			
			return rList;
		} catch (DbAccessException e) {
			throw new DicdataRelationException("字典数据关系列表获取失败。", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}
	/**
	 * 根据ID获取字典数据关系
	 * @param id
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public DicdataRelation findRelationById(long id) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelation.class) +
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{id}, DicdataRelation.class);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("字典数据关系获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据标识获取字典数据关系
	 * @param key
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public DicdataRelation findRelationByKey(String key) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelation.class) +
					" where f_key = ?";
			return getDbAccess().getObject(sql, new Object[]{key}, DicdataRelation.class);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("字典数据关系获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据关系ID获取关系数据列表
	 * @param relationId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DicdataRelationData> getRelationDataList(long relationId) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) +
					" where f_relation_id = ?";
			return getDbAccess().listObjects(sql, new Object[]{relationId}, DicdataRelationData.class, 0, 0);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据列表获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据关系ID获取关系数据列表（分页）
	 * @param relationId
	 * @param pagenum
	 * @param limit
	 * @param query
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DicdataRelationData> getRelationDataListIntoPages(long relationId, int pagenum, int limit,String query) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) + " where f_relation_id = ?";
			if(query != null && !"".equals(query)){
				sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) + " where f_relation_id = ?"+" and f_source_data_id in (select id from "+ CommonDatabaseAccess.getTableName(DictionaryData.class)+" where f_value like '%"+query+"%')";
			}
			return getDbAccess().listObjects(sql, new Object[]{relationId}, DicdataRelationData.class, pagenum, limit);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据列表获取失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据关系ID获取关系数据总数
	 * @param relationId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public long getRelationDataTotalById(long relationId) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select count(*) from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) + 
					" where f_relation_id = ?";
			return DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{relationId}), 0L);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据列表获取失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据关系ID和源字典数据ID获取当前排序值
	 * @param relationId
	 * @param sourceId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public long getRelationDataOrder(long relationId,long sourceId) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select max(f_order) from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) +
					" where f_relation_id = ? and f_source_data_id = ?";
			long order = DataValueUtil.getLong(getDbAccess().getFirstCell(sql, new Object[]{relationId, sourceId}), 0L);
			order = order + 1;	// ?
			return order;
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据索引获取失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据ID获取关系数据
	 * @param id
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public DicdataRelationData findRelationDataById(long id) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) +
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{id}, DicdataRelationData.class);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据关系ID、源字典数据ID获取关系数据列表
	 * @param relationId
	 * @param sourceDataId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DicdataRelationData> findRelationDataListByRelationAndSourceDataId(
			long relationId, long sourceDataId) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) + " where f_relation_id = ? and f_source_data_id = ?";
			return getDbAccess().listObjects(sql, new Object[]{relationId,sourceDataId}, DicdataRelationData.class,0,0);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据列表获取失败。", e);
		}finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据关系ID、源和目标字典数据ID获取关系数据
	 * @param relationId
	 * @param sourceDataId
	 * @param targetDataId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public DicdataRelationData findRelationDataBySourceTargetId(long relationId, long sourceDataId, long targetDataId) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) +
					" where f_relation_id = ? and f_source_data_id = ? and f_target_data_id = ?";
			return getDbAccess().getObject(sql, new Object[]{relationId,sourceDataId,targetDataId}, DicdataRelationData.class);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据获取失败。", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据源字典ID获取字典关系列表
	 * @param id
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DicdataRelation> findRelationBySourceDicId(long id) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelation.class) + " where f_source_dic_id = ?";
			return getDbAccess().listObjects(sql, new Object[]{id}, DicdataRelation.class,0,0);
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	/**
	 * 根据目标字典ID获取字典关系列表
	 * @param id
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DicdataRelation> findRelationByTargetDicId(long id) throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(DicdataRelation.class) + " where f_target_dic_id = ?";
			return getDbAccess().listObjects(sql, new Object[]{id}, DicdataRelation.class,0,0);
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系ID和源字典数据ID获取目标字典数据列表
	 * @param relationId
	 * @param sourceDataId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getTargetDataByRelationAndSourceDataId(long relationId,long sourceDataId)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select f_target_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = ? and f_source_data_id = ?)";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relationId,sourceDataId}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系标识和源字典数据标识获取目标字典数据列表
	 * @param relation
	 * @param sourceData
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getTargetDataByRelationAndSourceDataKey(String relation,String sourceData)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select f_target_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = (select id from "
					+CommonDatabaseAccess.getTableName(DicdataRelation.class)+" where f_key = ?) and f_source_data_id =(select id from "
					+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where f_key = ?))";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relation,sourceData}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系ID和目标字典数据ID获取源字典数据列表
	 * @param relationId
	 * @param targetDataId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getSourceDataByRelationAndTargetDataId(long relationId,long targetDataId)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select f_source_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = ? and f_target_data_id = ?)";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relationId,targetDataId}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系标识和目标字典数据标识获取源字典数据列表
	 * @param relation
	 * @param targetData
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getSourceDataByRelationAndTargetDataKey(String relation,String targetData)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select f_source_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = (select id from "
					+CommonDatabaseAccess.getTableName(DicdataRelation.class)+" where f_key = ?) and f_target_data_id =(select id from "
					+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where f_key = ?))";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relation,targetData}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系ID获取源字典数据列表
	 * @param relationId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getSourceDataByRelationId(long relationId)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select DISTINCT * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select DISTINCT f_source_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = ?)";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relationId}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系标识获取源字典数据列表
	 * @param relation
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getSourceDataByRelationKey(String relation)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select f_source_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = (select id from "
					+CommonDatabaseAccess.getTableName(DicdataRelation.class)+" where f_key = ?))";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relation}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系ID获取目标字典数据列表
	 * @param relationId
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getTargetDataByRelationId(long relationId)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select DISTINCT * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select DISTINCT f_target_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = ?)";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relationId}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}
	
	/**
	 * 根据关系标识获取目标字典数据列表
	 * @param relation
	 * @return
	 * @throws DicdataRelationException
	 */
	@Override
	public List<DictionaryData> getTargetDataByRelationKey(String relation)throws DicdataRelationException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from "+ CommonDatabaseAccess.getTableName(DictionaryData.class) + " where id in (select f_target_data_id from "
					+CommonDatabaseAccess.getTableName(DicdataRelationData.class)+" where f_relation_id = (select id from "
					+CommonDatabaseAccess.getTableName(DicdataRelation.class)+" where f_key = ?))";
			List<DictionaryData> list =  getDbAccess().listObjects(sql, new Object[]{relation}, DictionaryData.class,0,0);
			return list;
		} catch (DbAccessException e) {
			return null;
		} finally{
			getDbAccess().endTransaction();
		}
	}

}
