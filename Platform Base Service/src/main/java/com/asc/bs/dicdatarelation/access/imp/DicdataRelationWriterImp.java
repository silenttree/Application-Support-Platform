package com.asc.bs.dicdatarelation.access.imp;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.dicdatarelation.access.IDicdataRelationWriter;
import com.asc.commons.dicdatarelation.entity.DicdataRelation;
import com.asc.commons.dicdatarelation.entity.DicdataRelationData;
import com.asc.commons.dicdatarelation.exception.DicdataRelationException;
import com.asc.commons.dicdatarelation.service.DicdataRelationService;
import com.asc.commons.exception.DbAccessException;

public class DicdataRelationWriterImp implements IDicdataRelationWriter{
	
	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}
	
	/**
	 * 保存字典数据关系
	 */
	@Override
	public void saveDicdataRelation(DicdataRelation relation) throws DicdataRelationException {
		//判断是否有key
		if(relation.getF_key() == "" || "".equals(relation.getF_key())){
			throw new DicdataRelationException("未指定字典数据关系标识。");
		}
		//判断是否有caption
		if(relation.getF_caption() == "" || "".equals(relation.getF_caption())){
			throw new DicdataRelationException("未指定字典数据关系名称。");
		}

		try {
			getDbAccess().saveObject(relation);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("字典数据关系保存失败。", e);
		}
		
	}

	/**
	 * 保存关系数据
	 */
	@Override
	public void saveRelationData(DicdataRelationData relationdata) throws DicdataRelationException {
		//判断是否有源字典数据
		if(relationdata.getF_source_data_id() == 0){
			throw new DicdataRelationException("未指定源字典数据。");
		}
		//判断是否有目标字典数据
		if(relationdata.getF_target_data_id() == 0){
			throw new DicdataRelationException("未指定目标字典数据。");
		}
		//判断源与目标是否相等
		if(relationdata.getF_target_data_id() == relationdata.getF_source_data_id()){
			throw new DicdataRelationException("源字典数据与目标字典数据相同。");
		}
		
		try {
			getDbAccess().saveObject(relationdata);
		} catch (DbAccessException e) {
			throw new DicdataRelationException("关系数据保存失败。", e);
		}
	}
	
	/**
	 * 删除字典数据关系
	 */
	@Override
	public void deleteDicdataRelation(long id) throws DicdataRelationException {
		DicdataRelation relation =DicdataRelationService.instance().findRelationById(id);
		//判断字典数据关系是否有数据
		List<DicdataRelationData> relationDataList = DicdataRelationService.instance().getRelationDataListByRelationId(relation.getId());
		if(relationDataList.size()!= 0){
			throw new DicdataRelationException("字典数据关系存在数据，删除失败。");
		}
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(DicdataRelation.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DicdataRelationException("字典数据关系删除失败。");
		}finally {
			getDbAccess().endTransaction();
		}
		
	}

	/**
	 * 删除关系数据
	 */
	@Override
	public void deleteRelationData(long id) throws DicdataRelationException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(DicdataRelationData.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new DicdataRelationException("关系数据删除失败。");
		}finally {
			getDbAccess().endTransaction();
		}
	}

}
