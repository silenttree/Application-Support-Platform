package com.asc.bs.log.access.imp;

import static com.asc.commons.dbac.CommonDatabaseAccess.getTableName;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.log.access.ILogConfigReader;
import com.asc.commons.log.entity.LogConfigEntity;
import com.asc.commons.log.exception.LogException;

public class LogConfigReaderImpl implements ILogConfigReader {
	private static CommonDatabaseAccess getDbAccess(){
		return CommonDatabaseAccess.instance();
	}
	
	@Override
	public List<LogConfigEntity> findLogConfigs(String appId) throws LogException {
		getDbAccess().beginTransaction();
		try {
			// 该应用系统配置或全局配置
			String sql = "select * from " + getTableName(LogConfigEntity.class) +
					" where f_application_id = ? or f_application_id is null order by id";
			return getDbAccess().listObjects(sql, new Object[]{appId}, LogConfigEntity.class, 0, 0);
		} catch (DbAccessException e) {
			throw new LogException("日志配置列表读取失败！" + e.getMessage());
		} finally {
			getDbAccess().endTransaction();
		}
	}

	@Override
	public LogConfigEntity getLogConf(long id) throws LogException {
		getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + getTableName(LogConfigEntity.class) +
					" where id = ?";
			return getDbAccess().getObject(sql, new Object[]{id}, LogConfigEntity.class);
		} catch (DbAccessException e) {
			throw new LogException("日志配置读取失败！" + e.getMessage());
		} finally {
			getDbAccess().endTransaction();
		}
	}

	// FIXME
	@Override
	public <T> List<T> findLogs(String type, String appId, Class<T> logType, int pagenum, int pagesize) throws LogException {
		return null;
		/*getDbAccess().beginTransaction();
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Log.class);
			return getDbAccess().listObjects(sql, new Object[]{}, Log.class, 0, 0);
		} catch (DbAccessException e) {
			throw new LogException("日志列表读取失败！" + e.getMessage());
		} finally {
			getDbAccess().endTransaction();
		}*/
	}

}
