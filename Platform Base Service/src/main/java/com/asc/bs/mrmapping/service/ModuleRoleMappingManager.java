package com.asc.bs.mrmapping.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asc.bs.mrmapping.access.dbacimp.DbacModuleRoleMappingAccessor;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.engine.authority.exception.RelationException;
import com.asc.commons.engine.authority.relation.Expression;
import com.asc.commons.modulerolemapping.access.IModuleRoleMappingReader;
import com.asc.commons.modulerolemapping.access.IModuleRoleMappingWriter;
import com.asc.commons.modulerolemapping.entity.ModuleRoleMapping;
import com.asc.commons.modulerolemapping.exception.ModuleRoleMappingException;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.User;
import com.asc.framework.designobject.service.DesignObjectService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;
import com.mixky.toolkit.StringTool;

public class ModuleRoleMappingManager {
	private static ModuleRoleMappingManager singleton;
	private IModuleRoleMappingWriter moduleRoleMappingWriter;
	private IModuleRoleMappingReader moduleRoleMappingReader;

	/**
	 * 模块角色权限反向配置的保存
	 * @param datas
	 * @throws ModuleRoleMappingException 
	 */
	public void save2(JsonArray datas) throws ModuleRoleMappingException {
		// 需要操作的数据集合
		Map<String, List<ModuleRoleMapping>> toOperate = new HashMap<String, List<ModuleRoleMapping>>();
		toOperate.put("toSave", new ArrayList<ModuleRoleMapping>());
		toOperate.put("toDelete", new ArrayList<ModuleRoleMapping>());
		for(int i = 0; i < datas.size(); i++) {
			JsonObject data = datas.get(i).getAsJsonObject();
			long appId = data.get("f_application_id").getAsLong();
			String mRoleKey = data.get("f_mrole_key").getAsString();
			boolean isSelect = data.get("f_is_select").getAsBoolean();
			String relativeKey = data.get("f_relative_key").getAsString();
			if(isSelect) {
				// 添加权限
				String moduleKey = data.get("f_module_key").getAsString();
				String orgIds = data.get("f_org_ids").getAsString(); // 维护单位
				try {
					addAuth(appId, moduleKey, mRoleKey, relativeKey, orgIds, toOperate);
				} catch (Exception e) {
					throw new ModuleRoleMappingException("保存权限失败", e);
				}
			} else {
				// 删除权限
				boolean isDeleted = data.get("f_is_deleted").getAsBoolean();
				try {
					delAuth(appId, mRoleKey, relativeKey, isDeleted, toOperate);
				} catch (Exception e) {
					throw new ModuleRoleMappingException("保存权限失败", e);
				}
			}
		}
		// 持久化到数据库
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			for(ModuleRoleMapping mrm : toOperate.get("toSave")) {
				moduleRoleMappingWriter.save(mrm);
			}
			for(ModuleRoleMapping mrm : toOperate.get("toDelete")) {
				moduleRoleMappingWriter.delete(mrm.getId());
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new ModuleRoleMappingException("保存权限失败", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
		
	}
	
	private void addAuth(long appId, String moduleKey, String mRoleKey, String relativeKey, String orgIds, 
			Map<String, List<ModuleRoleMapping>> toOperate) throws RelationException, ModuleRoleMappingException, DesignObjectException {
		String[] orgIdStrs = orgIds.split(";");
		// 获取维护机构的ID列表
		List<Long> orgIdList = new ArrayList<Long>();
		for(String idStr : orgIdStrs) {
			if(!StringTool.isEmpty(idStr)) {
				orgIdList.add(Long.parseLong(idStr));
			}
		}
		Expression relativeExp = new Expression(relativeKey);
		String relativeCaption = getExpCaption(relativeExp);
		for(long orgId: orgIdList) {
			ModuleRoleMapping mrm = moduleRoleMappingReader.getByOrgAndAppAndKey(appId, orgId, mRoleKey);
			if(mrm != null) {
				// 已经维护过模块角色权限的权限字段加上relativeKey
				if(StringTool.isEmpty(mrm.getF_auth_expression())) {
					mrm.setF_auth_expression(relativeKey + ";");
					mrm.setF_auth_caption(relativeCaption + ";");
					mrm.setF_update_time(Calendar.getInstance().getTime());
				} else if(mrm.getF_auth_expression().indexOf(relativeKey + ";") == -1) {
					mrm.setF_auth_expression(mrm.getF_auth_expression() + relativeKey + ";");
					mrm.setF_auth_caption(mrm.getF_auth_caption() + relativeCaption + ";");
					mrm.setF_update_time(Calendar.getInstance().getTime());
				}
			} else {
				// 没有维护过模块角色权限保存新的mapping
				mrm = new ModuleRoleMapping();
				mrm.setF_application_id(appId);
				mrm.setF_org_id(orgId);
				mrm.setF_module_key(moduleKey);
				mrm.setF_mrole_key(mRoleKey);
				mrm.setF_auth_expression(relativeKey + ";");
				mrm.setF_auth_caption(relativeCaption + ";");
				mrm.setF_create_time(Calendar.getInstance().getTime());
				mrm.setF_update_time(Calendar.getInstance().getTime());
				// 获取模块名和模块角色名
				String moduleCaption = DesignObjectService.instance().getObject(getServiceUrl(appId), moduleKey).get("f_caption").getAsString();
				String mRoleCaption = DesignObjectService.instance().getObject(getServiceUrl(appId), mRoleKey).get("f_caption").getAsString();
				mrm.setF_module_caption(moduleCaption);
				mrm.setF_mrole_caption(mRoleCaption);
			}
			toOperate.get("toSave").add(mrm);
		}
	}
	
	private void delAuth(long appId, String mRoleKey, String relativeKey, boolean isDeleted, 
			Map<String, List<ModuleRoleMapping>> toOperate) throws ModuleRoleMappingException, RelationException {
		List<ModuleRoleMapping> oriMappings = moduleRoleMappingReader.findByAppAndKey(appId, mRoleKey);
		for(ModuleRoleMapping mrm : oriMappings) {
			Expression relativeExp = new Expression(relativeKey);
			String relativeCaption = getExpCaption(relativeExp);
			if(!StringTool.isEmpty(mrm.getF_auth_expression())) {
				mrm.setF_auth_expression(mrm.getF_auth_expression().replaceFirst(relativeKey + ";", ""));
			}
			if(!StringTool.isEmpty(mrm.getF_auth_caption())) {
				mrm.setF_auth_caption(mrm.getF_auth_caption().replaceFirst(relativeCaption + ";", ""));
			}
			if(StringTool.isEmpty(mrm.getF_auth_expression())) {
				toOperate.get("toDelete").add(mrm);
			} else {
				toOperate.get("toSave").add(mrm);
			}
		}
	}
	
	/**
	 * 根据应用ID获取应用的url， 如果应用的ID不存在，返回null
	 * 
	 * @param appId
	 * @return
	 */
	private String getServiceUrl(long appId) {
		Application app;
		try {
			app = ApplicationService.instance().getApplicationById(appId);
		} catch (ApplicationException e) {
			return null;
		}
		if (app == null) {
			return null;
		}
		String url = app.getF_deploy_domain() + "/services/DesignObjectAccessService?wsdl";
		return url;
	}
	
	private String getExpCaption(Expression exp) throws RelationException {
		switch (exp.getType()) {
		case User:
			return ((User) exp.getObject()).getF_caption();
		case Role:
			return ((Role) exp.getObject()).getF_caption();
		case Department:
			return ((Org) exp.getObject()).getF_caption();
		case Company:
			return ((Org) exp.getObject()).getF_caption();
		default:
			// 没有处理CDRU之外的情况
			return "";
		}
	}
	
	/**
	 * 保存模块角色权限映射 如果权限为空则删除 如果ID为空则增加 否则更新
	 * 
	 * @param mrms
	 * @throws ModuleRoleMappingException
	 */
	public void save(JsonArray mrms) throws ModuleRoleMappingException {
		if (mrms == null || mrms.size() == 0) {
			return;
		}
		List<ModuleRoleMapping> objectToSaves = new ArrayList<ModuleRoleMapping>(mrms.size());
		for(int i = 0; i < mrms.size(); i++) {
			JsonObject mrmJson = mrms.get(i).getAsJsonObject();
			// 没有权限有ID，删除
			if (!(mrmJson.get("f_auth_expression").isJsonNull() 
					|| "".equals(mrmJson.get("f_auth_expression").getAsString()))) {
				long id = 0;
				if(mrmJson.has("id")){
					id = mrmJson.get("id").getAsLong();
				}
				ModuleRoleMapping objectToSave = new ModuleRoleMapping();
				Date createTime = null;
				// 创建标记
				if (id == 0) {
					createTime = new Date();
				} else {
					ModuleRoleMapping oldObject = moduleRoleMappingReader.getById(id);
					createTime = oldObject.getF_create_time();
				}
				// 写入对象数据
				JsonObjectTool.jsonObject2Object(mrmJson, objectToSave);
				objectToSave.setF_create_time(createTime);
				objectToSave.setF_update_time(new Date());
				objectToSaves.add(i, objectToSave);
			} else {
				objectToSaves.add(i, null);
			}
		}
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			for (int i = 0; i < mrms.size(); i++) {
				JsonObject mrmJson = mrms.get(i).getAsJsonObject();
				// 没有权限有ID，删除
				if (objectToSaves.get(i) != null) {
					moduleRoleMappingWriter.save(objectToSaves.get(i));
				} else if( mrmJson.has("id")) {
					moduleRoleMappingWriter.delete(mrmJson.get("id").getAsLong());
				}
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw new ModuleRoleMappingException("角色保存失败", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	private ModuleRoleMappingManager() {
		DbacModuleRoleMappingAccessor accessor = new DbacModuleRoleMappingAccessor();
		moduleRoleMappingWriter = accessor;
		moduleRoleMappingReader = accessor;
	}

	public IModuleRoleMappingWriter getModuleRoleMappingWriter() {
		return moduleRoleMappingWriter;
	}

	public void setModuleRoleMappingWriter(
			IModuleRoleMappingWriter moduleRoleMappingWriter) {
		this.moduleRoleMappingWriter = moduleRoleMappingWriter;
	}

	public static ModuleRoleMappingManager instance() {
		if (singleton == null) {
			singleton = new ModuleRoleMappingManager();
		}
		return singleton;
	}


}
