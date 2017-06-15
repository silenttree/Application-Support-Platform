package com.asc.bs.datafilter.access.imp;

import static com.asc.commons.dbac.CommonDatabaseAccess.db;
import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.Date;
import java.util.List;

import com.asc.commons.datafilter.access.IDataFilterSettingsWriter;
import com.asc.commons.datafilter.entity.Policy;
import com.asc.commons.datafilter.exception.DfSettingsAccessException;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;

/**
 * <pre>
 * Platform Base Service
 * 数据过滤设置数据写入实现类
 * 
 * Mixky Co., Ltd. 2016
 * @author Bill
 * </pre>
 */
public class DataFilterSettingsWriterImpl implements IDataFilterSettingsWriter {

	@Override
	public void addPolicy(Policy policy) throws DfSettingsAccessException {
		IDbacTransaction tx = db().beginTransaction();
		try {
			db().saveObject(policy);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw DfSettingsAccessException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public void deletePolicy(long pId) throws DfSettingsAccessException {
		IDbacTransaction tx = db().beginTransaction();
		try {
			String sql = "delete from " + getTableName(Policy.class) + " where id=?";
			db().execute(sql, new Object[]{pId});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw DfSettingsAccessException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
	}

	@Override
	public void addPolicyByArray(String[] arr) throws DfSettingsAccessException {
		IDbacTransaction tx = db().beginTransaction();
		String term = "(";
		for(int i = 0; i < arr.length; i++){
			if(i == arr.length - 1){
				term +="?)";
				continue;
			}
			term +="?,";
		}
		List<Policy> list = null;
		try {
			String sql = "select * from "  + getTableName(Policy.class) + " where id in ";
			String exeSql = sql + term;
			list = db().listObjects(exeSql, arr, Policy.class, 0, 0);
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					long seqid = db().newSeqId(getTableName(Policy.class));
					list.get(i).setId(seqid);
					list.get(i).setF_create_time(new Date());
					list.get(i).save();
				}
			}
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			e.printStackTrace();
		} finally {
			db().endTransaction();
		}
	}
	
}
