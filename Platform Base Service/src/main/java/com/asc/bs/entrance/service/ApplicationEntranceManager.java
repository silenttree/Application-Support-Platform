package com.asc.bs.entrance.service;

import java.util.Date;

import com.asc.bs.entrance.access.imp.DbacApplicationEntranceReader;
import com.asc.bs.entrance.access.imp.DbacApplicationEntranceWriter;
import com.asc.commons.applicationentrance.access.IApplicationEntranceReader;
import com.asc.commons.applicationentrance.access.IApplicationEntranceWriter;
import com.asc.commons.applicationentrance.entity.ApplicationEntrance;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class ApplicationEntranceManager {

	private static ApplicationEntranceManager singleton;
	private static IApplicationEntranceReader reader;
	private static IApplicationEntranceWriter writer;

	private ApplicationEntranceManager() {
		reader = new DbacApplicationEntranceReader();
		writer = new DbacApplicationEntranceWriter();
	}

	public static ApplicationEntranceManager instance() {
		if (singleton == null) {
			singleton = new ApplicationEntranceManager();
		}
		return singleton;
	}

	public ApplicationEntrance saveAppEnt(JsonObject data) throws ApplicationEntranceException {
		long id = 0;
		if (data.has("id")) {
			id = data.get("id").getAsLong();
		}
		ApplicationEntrance objectToSave = null;
		// 创建标记
		boolean isCreate = false;
		if (id > 0) {
			objectToSave = reader.getApplicationEntrance(id);
			if (objectToSave == null) {
				isCreate = true;
				objectToSave = new ApplicationEntrance();
			}
		} else {
			isCreate = true;
			objectToSave = new ApplicationEntrance();
		}
		// 写入对象数据
		JsonObjectTool.jsonObject2Object(data, objectToSave);
		if (isCreate) {
			objectToSave.setF_create_time(new Date());
		}
		objectToSave.setF_update_time(new Date());
		// 数据校验
		if (data.has("f_key")) {
			String key = data.get("f_key").getAsString();
			// 校验键值冲突
			ApplicationEntrance appFind = null;
			appFind = reader.getApplicationEntrance(data.get("f_key").getAsString());
			if (appFind != null && appFind.getId() != id) {
				throw ApplicationEntranceException.forKeyIllegal(appFind.getId(), key);
			}
		}
		saveApplicationNavigator(objectToSave);
		return objectToSave;
	}

	public void saveApplicationNavigator(ApplicationEntrance app) throws ApplicationEntranceException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			writer.saveApplicationEntrance(app);
			tx.commit();
		} catch (Exception e) {
			throw new ApplicationEntranceException("保存用户导航失败", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 删除应用导航对象
	 * 
	 * @param id
	 * @throws ApplicationEntranceException
	 */
	public void deleteApplicationNavigator(long id) throws ApplicationEntranceException {
		ApplicationEntrance objectToDelete = reader.getApplicationEntrance(id);
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			if (objectToDelete == null) {
				throw ApplicationEntranceException.forObectNotFound(ApplicationEntrance.class.getName(), id);
			} else {
				// 删除应用导航对象
				writer.deleteApplicationEntrance(objectToDelete);
			}
			tx.commit();
		} catch (ApplicationEntranceException e1) {
			tx.rollback();
			throw e1;
		} catch (Exception e) {
			tx.rollback();
			throw new ApplicationEntranceException("删除应用导航失败", e);
		}
		CommonDatabaseAccess.instance().endTransaction();
	}

}
