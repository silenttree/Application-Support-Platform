package com.asc.bs.portalprofile.service;

import java.util.Date;

import com.asc.bs.portalprofile.access.IPortalProfileReader;
import com.asc.bs.portalprofile.access.IPortalProfileWriter;
import com.asc.bs.portalprofile.access.imp.DbacPoralProfileWriter;
import com.asc.bs.portalprofile.access.imp.DbacPortalProfileReader;
import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class PortalProfileManager {
	
	private IPortalProfileReader reader;
	private IPortalProfileWriter writer;
	
	private static PortalProfileManager singleton;

	private PortalProfileManager() {
		this.reader = new DbacPortalProfileReader();
		this.writer = new DbacPoralProfileWriter();
	}

	public static PortalProfileManager instance() {
		if (singleton == null) {
			singleton = new PortalProfileManager();
		}
		return singleton;
	}
	
	/**
	 * 删除一个门户模板
	 * @param id
	 * @throws PortalProfileException
	 */
	public void deleteById(long id) throws PortalProfileException{
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {			
			writer.delete(id);
			tx.commit();
		} catch (PortalProfileException e){
			tx.rollback();
			throw e;
		} catch (Exception e) {
			tx.rollback();
			throw new PortalProfileException("删除门户模板失败", e);
		}finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}
	
	/**
	 * 根据json对象保存一个门户模板
	 * @param data
	 * @return
	 * @throws PortalProfileException
	 */
	public PortalProfile save(JsonObject data) throws PortalProfileException{
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			long id = 0;
			if(data.has("id")){
				id = data.get("id").getAsLong();
			}
			PortalProfile objectToSave = null;
			// 创建标记
			boolean isCreate = false;
			if (id > 0) {
				objectToSave = reader.getById(id);
				if (objectToSave == null) {
					isCreate = true;
					objectToSave = new PortalProfile();
				}
			} else {
				isCreate = true;
				objectToSave = new PortalProfile();
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
				PortalProfile objFind = reader.getByKey(data.get("f_key")
						.getAsString());
				if (objFind != null && objFind.getId() != id) {
					throw PortalProfileException.forKeyIllegal(objFind.getId(), key);
				}
			}
			writer.save(objectToSave);
			tx.commit();
			return objectToSave;
		} catch (PortalProfileException e) {
			tx.rollback();
			throw e;
		} catch (Exception e){
			tx.rollback();
			throw new PortalProfileException("保存失败", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}
	
	/**
	 * 保存门户模板
	 * @param portalProfile
	 * @throws PortalProfileException
	 */
	public void save(PortalProfile portalProfile) throws PortalProfileException{
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {			
			writer.save(portalProfile);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new PortalProfileException("保存门户模板失败", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

}
