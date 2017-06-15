package com.asc.bs.wfrmapping.service;

import java.util.Date;

import com.asc.bs.wfrmapping.access.dbacimp.DbacWorkFlowRoleMappingAccessor;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.wfrolemapping.access.IWorkFlowRoleMappingReader;
import com.asc.commons.wfrolemapping.access.IWorkFlowRoleMappingWriter;
import com.asc.commons.wfrolemapping.entity.WorkFlowRoleMapping;
import com.asc.commons.wfrolemapping.exception.WorkFlowRoleMappingException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class WorkFlowRoleMappingManager {

	private static WorkFlowRoleMappingManager singleton;
	private IWorkFlowRoleMappingWriter moduleRoleMappingWriter;
	private IWorkFlowRoleMappingReader moduleRoleMappingReader;

	/**
	 * 保存流程角色权限映射 如果权限为空则删除 如果ID为空则增加 否则更新
	 * 
	 * @param mrms
	 * @throws WorkFlowRoleMappingException
	 */
	public void save(JsonArray mrms) throws WorkFlowRoleMappingException {
		if (mrms == null || mrms.size() == 0) {
			return;
		}
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			for (int i = 0; i < mrms.size(); i++) {
				JsonObject mrmJson = mrms.get(i).getAsJsonObject();
				// 没有权限有ID，删除
				if (mrmJson.get("f_auth_expression").isJsonNull() 
						|| "".equals(mrmJson.get("f_auth_expression").getAsString())) {
					if( mrmJson.has("id")) {
						moduleRoleMappingWriter.delete(mrmJson.get("id").getAsLong());
					}
				} else {
					long id = 0;
					if(mrmJson.has("id")){
						id = mrmJson.get("id").getAsLong();
					}
					WorkFlowRoleMapping objectToSave = new WorkFlowRoleMapping();
					JsonObjectTool.jsonObject2Object(mrmJson, objectToSave);
					moduleRoleMappingWriter.save(objectToSave);
					
					/*
					WorkFlowRoleMapping objectToSave = new WorkFlowRoleMapping();
					Date createTime = null;
					// 创建标记
					if (id == 0) {
						createTime = new Date();
					} else {
						WorkFlowRoleMapping oldObject = moduleRoleMappingReader.get(id);
						createTime = oldObject.getF_create_time();
					}
					// 写入对象数据
					JsonObjectTool.jsonObject2Object(mrmJson, objectToSave);
					objectToSave.setF_create_time(createTime);
					objectToSave.setF_update_time(new Date());
					moduleRoleMappingWriter.save(objectToSave);*/
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new WorkFlowRoleMappingException("保存流程角色失败", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}

	private WorkFlowRoleMappingManager() {
		// TODO
		DbacWorkFlowRoleMappingAccessor accessor = new DbacWorkFlowRoleMappingAccessor();
		moduleRoleMappingWriter = accessor;
		moduleRoleMappingReader = accessor;
	}

	public IWorkFlowRoleMappingWriter getWorkFlowRoleMappingWriter() {
		return moduleRoleMappingWriter;
	}

	public void setWorkFlowRoleMappingWriter(
			IWorkFlowRoleMappingWriter moduleRoleMappingWriter) {
		this.moduleRoleMappingWriter = moduleRoleMappingWriter;
	}

	public static WorkFlowRoleMappingManager instance() {
		if (singleton == null) {
			singleton = new WorkFlowRoleMappingManager();
		}
		return singleton;
	}

}
