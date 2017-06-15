package com.asc.log;

import java.util.List;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.log.entity.CasLog;
import com.asc.commons.log.entity.MgrActivityLog;
import com.asc.commons.log.entity.SystemLog;
import com.asc.commons.log.entity.UserActivityLog;
import com.asc.commons.log.exception.LogException;
import com.google.gson.JsonArray;
import com.mixky.toolkit.JsonObjectTool;
/**
 * 日志查询管理
 *
 */
public class LogService {
	private static LogService singleton;

	public static LogService instance() {
		if (singleton == null) {
			singleton = new LogService();
		}
		return singleton;
	}
	
	private static CommonDatabaseAccess getDbAccess(){
		return CommonDatabaseAccess.instance();
	}
	
	/**
	 * 获取日志列表
	 * 
	 * @param type
	 *            日志类型
	 * @param pagenum
	 *            页码
	 * @param pagesize
	 *            每页记录数量
	 * @return
	 * @throws LogException
	 */
	@SuppressWarnings("unchecked")
	public <T> JsonArray findLogsByClassType(String type) throws LogException {
		getDbAccess().beginTransaction();
		try {
			Class<T> clazz = null;
			if(null == type || "".equals(type)){
				throw new LogException("日志类型为空！");
			}else{
				if("SystemLog".equals(type)){
					clazz = (Class<T>) SystemLog.class;
				}else if("MgrActivityLog".equals(type)){
					clazz = (Class<T>) MgrActivityLog.class;
				}else if("UserActivityLog".equals(type)){
					clazz = (Class<T>) UserActivityLog.class;
				}else if("CasLog".equals(type)){
					clazz = (Class<T>) CasLog.class;
				}else{
					clazz = (Class<T>) Object.class;
				}
			}
			String sql = "select * from " + CommonDatabaseAccess.getTableName(clazz) + " order by id desc,f_create_time desc";
			List<T> list = getDbAccess().listObjects(sql, new Object[]{}, clazz, 0, 0);
			return JsonObjectTool.objectList2JsonArray(list);
		} catch (DbAccessException e) {
			throw new LogException("获取【" + type + "】类型的日志列表失败！" + e.getMessage());
		}finally{
			getDbAccess().endTransaction();
		}
	}
	
}
