package com.asc.bs.log.access.imp;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.log.access.ILogConfigWrite;
import com.asc.commons.log.entity.LogConfigEntity;
import com.asc.commons.log.exception.LogException;

public class LogConfigWriteImpl implements ILogConfigWrite {
	private static CommonDatabaseAccess getDbAccess(){
		return CommonDatabaseAccess.instance();
	}
	
	@Override
	public void saveLogConfig(LogConfigEntity logConfig) throws LogException{
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			getDbAccess().saveObject(logConfig);
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new LogException("日志配置保存失败！" + e.getMessage());
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public void delLogConf(long id) throws LogException {
		IDbacTransaction tx = getDbAccess().beginTransaction();
		try {
			String sql = "delete from " + CommonDatabaseAccess.getTableName(LogConfigEntity.class) +
					" where id = ?";
			getDbAccess().execute(sql, new Object[]{id});
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new LogException("日志配置删除失败！" + e.getMessage());
		} finally {
			getDbAccess().endTransaction();
		}
	}
}
