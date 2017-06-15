package com.asc.bs.datafilter.access.imp;


import static com.asc.commons.dbac.CommonDatabaseAccess.db;
import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.List;

import com.asc.commons.datafilter.access.IDataFilterSettingsReader;
import com.asc.commons.datafilter.entity.Policy;
import com.asc.commons.datafilter.exception.DfSettingsAccessException;
import com.asc.commons.exception.DbAccessException;

/**
 * <pre>
 * Platform Base Service
 * 数据过滤设置数据读取实现类
 * 
 * Mixky Co., Ltd. 2016
 * @author Bill
 * </pre>
 */
public class DataFilterSettingsReaderImpl implements IDataFilterSettingsReader {

	@Override
	public Policy getPolicy(long pId) throws DfSettingsAccessException {
		Policy result = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Policy.class) + " where id=?";
			result = db().getObject(sql, new Object[]{pId}, Policy.class);
		} catch (DbAccessException e) {
			throw DfSettingsAccessException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
		return result;
	}

	@Override
	public List<Policy> listFilterPolicies(String appId, String objId, String ue) throws DfSettingsAccessException {
		List<Policy> result = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Policy.class) + " where f_app_id=? and f_object_id=?"
					+ " and asc_auth(f_ue_expressions,'" + ue + "')>0";
			result = db().listObjects(sql, new Object[]{appId, objId}, Policy.class, 0, 0);
		} catch (DbAccessException e) {
			throw DfSettingsAccessException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
		return result;
	}

	@Override
	public List<Policy> listFilterPolicies(String appId, String objId, String tableName, String ue) throws DfSettingsAccessException {
		List<Policy> result = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Policy.class) + " where f_app_id=? and f_object_id=? and f_tablename=?"
					+ " and asc_auth(f_ue_expressions,'" + ue + "')>0";
			result = db().listObjects(sql, new Object[]{appId, objId, tableName}, Policy.class, 0, 0);
		} catch (DbAccessException e) {
			throw DfSettingsAccessException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
		return result;
	}

	@Override
	public List<Policy> listPolicys(String appId)
			throws DfSettingsAccessException {
		List<Policy> result = null;
		db().beginTransaction();
		try {
			String sql = "select * from " + getTableName(Policy.class) + " where f_app_id=? order by f_create_time desc";
			result = db().listObjects(sql, new Object[]{appId}, Policy.class, 0, 0);
		} catch (DbAccessException e) {
			throw DfSettingsAccessException.forDbAccessException(e);
		} finally {
			db().endTransaction();
		}
		return result;
	}
	
}
