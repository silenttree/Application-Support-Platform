package com.asc.demo.event;

import static com.asc.commons.dbac.CommonDatabaseAccess.db;

import java.util.Date;

import com.asc.commons.exception.DbAccessException;
import com.asc.demo.entity.AppDemo;

/**
 * <pre>
 * 事件调用的数据操作工具类
 *
 * @author Bill
 * Mixky Technology Co., Ltd. 2016
 * </pre>
 */
public class EventDataUtil {
	
	public static void exeSQL(String sql, Object[] params) throws DbAccessException {
		db().execute(sql, params);
	}
	
	public static void saveObject() throws DbAccessException {
		String sql = "select * from t_app_demo where id=?";
		AppDemo ad = db().getObject(sql, new Object[]{11}, AppDemo.class);
		ad.setF_title(String.valueOf(System.currentTimeMillis()));
		ad.setF_update_time(new Date());
		ad.save();
	}
}
