package com.asc.bs.entrance.access.imp;

import java.util.List;

import com.asc.commons.application.entity.Application;
import com.asc.commons.applicationentrance.access.IApplicationEntranceReader;
import com.asc.commons.applicationentrance.entity.ApplicationEntrance;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;

public class DbacApplicationEntranceReader implements IApplicationEntranceReader {
	
	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	@Override
	public ApplicationEntrance getApplicationEntrance(String key) throws ApplicationEntranceException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * " +
					"FROM " + CommonDatabaseAccess.getTableName(ApplicationEntrance.class) + " " + 
					"WHERE F_KEY=?";
			return getDbAccess().getObject(sql, new Object[]{key}, ApplicationEntrance.class);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forGetObjectFaild("F_KEY = " + key, e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public ApplicationEntrance getApplicationEntrance(long id) throws ApplicationEntranceException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * " +
					"FROM " + CommonDatabaseAccess.getTableName(ApplicationEntrance.class) + " " + 
					"WHERE ID=?";
			return getDbAccess().getObject(sql, new Object[]{id}, ApplicationEntrance.class);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forGetObjectFaild("id = " + id, e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<ApplicationEntrance> findApplicationEntranceByApp(String appKey) throws ApplicationEntranceException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT APPNAV.*" +
					"FROM " + CommonDatabaseAccess.getTableName(Application.class) + " APP " + 
					"LEFT JOIN " + CommonDatabaseAccess.getTableName(ApplicationEntrance.class)  + " APPNAV " +
					"ON APPNAV.F_APPLICATION_ID = APP.ID " +
					"WHERE APP.F_KEY = ? ORDER BY APPNAV.F_KEY";
			return getDbAccess().listObjects(sql, new Object[]{appKey}, ApplicationEntrance.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forFindObjectFaild("appKey = " + appKey, e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<ApplicationEntrance> findApplicationEntranceByApp(long appId) throws ApplicationEntranceException {
		getDbAccess().beginTransaction();
		try {
			String sql = "SELECT * " +
					"FROM " + CommonDatabaseAccess.getTableName(ApplicationEntrance.class) + " " + 
					"WHERE F_APPLICATION_ID=?";
			return getDbAccess().listObjects(sql, new Object[]{appId}, ApplicationEntrance.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forFindObjectFaild("appId = " + appId, e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public List<ApplicationEntrance> findApplicationEntrance(String userExps) throws ApplicationEntranceException {
		getDbAccess().beginTransaction();
		try {
			String whereSql = "";
			if(userExps != null && !"".equals(userExps)) {
				whereSql = " WHERE ASC_AUTH(F_AUTH_EXPRESSION, '" + userExps +"' ) >= 0";
			}
			String sql = "SELECT * " 
					+ " FROM " + CommonDatabaseAccess.getTableName(ApplicationEntrance.class)
					+ whereSql + " ORDER BY F_LEVEL, F_ORDER";
			return getDbAccess().listObjects(sql, null, ApplicationEntrance.class, 0, 0);
		} catch (DbAccessException e) {
			throw ApplicationEntranceException.forFindObjectFaild("无参数", e);
		} finally {
			getDbAccess().endTransaction();
		}
	}

}
