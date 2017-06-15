package com.asc.manager.mrmapping.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.bs.mrmapping.service.ModuleRoleMappingManager;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.engine.authority.OrganizationRelationService;
import com.asc.commons.engine.authority.exception.RelationException;
import com.asc.commons.engine.authority.relation.Expression;
import com.asc.commons.engine.authority.relation.Expression.ExpressionTypes;
import com.asc.commons.modulerolemapping.entity.ModuleRoleMapping;
import com.asc.commons.modulerolemapping.exception.ModuleRoleMappingException;
import com.asc.commons.modulerolemapping.service.ModuleRoleMappingService;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.commons.organization.service.OrganizationService;
import com.asc.framework.designobject.service.DesignObjectService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class ModuleRoleMappingHandler {
	private static ModuleRoleMappingHandler singleton;
	
	private static Log log = LogFactory.getLog(ModuleRoleMappingHandler.class);

	public static final String MODULE_TYPE_NAME = "module";

	public static final String MODULE_ROLE_TYPE_NAME = "modulerole";

	private ModuleRoleMappingHandler() {
	}

	public static ModuleRoleMappingHandler instance() {
		if (singleton == null) {
			singleton = new ModuleRoleMappingHandler();
		}
		return singleton;
	}
	

	/**
	 * 根据应用ID和组织ID获取模块角色列表
	 * 
	 * @param appId
	 * @param orgId
	 * @return
	 * @throws ModuleRoleMappingException
	 */
	public JsonArray loadByAppAndOrg(long appId, long orgId) throws ModuleRoleMappingException {
		JsonArray moduleRoles = new JsonArray();
		// 获取应经被映射过的角色
		List<ModuleRoleMapping> mrms = null;
		mrms = ModuleRoleMappingService.instance().findByOrgAndApp(appId, orgId);
		if (mrms == null) {
			mrms = new ArrayList<ModuleRoleMapping>(0);
		}
		Application app = null;
		try {
			app = ApplicationService.instance().getApplicationById(appId);
		} catch (ApplicationException e) {
			throw new ModuleRoleMappingException("根据ID获取应用时发生错误", e);
		}
		if (app == null) {
			throw new ModuleRoleMappingException("未能根据ID获取应用");
		}
		// 获取应用模块列表
		JsonArray modules = null;
		try {
			modules = DesignObjectService.instance().findObjects(
					this.getServiceUrl(appId), null, MODULE_TYPE_NAME);
		} catch (DesignObjectException e) {
			throw new ModuleRoleMappingException("获取模块角色列表失败", e);
		}
		// 遍历模块列表获取模块角色
		for (int i = 0; i < modules.size(); i++) {
			JsonObject module = modules.get(i).getAsJsonObject();
			// 如果该模块底下没有模块角色，跳过
			if (!module.has("f_roles") || !module.get("f_roles").isJsonArray()
					|| module.get("f_roles").getAsJsonArray().size() == 0) {
				continue;
			}
			JsonArray mrs = module.get("f_roles").getAsJsonArray();
			// 遍历该模块下的模块角色
			for (int j = 0; j < mrs.size(); j++) {
				JsonObject role = mrs.get(j).getAsJsonObject();
				JsonObject roleNode = new JsonObject();
				roleNode.addProperty("f_application_id", appId);
				roleNode.addProperty("f_org_id", orgId);
				roleNode.addProperty("f_module_key", module.get("id")
						.getAsString());
				roleNode.addProperty("f_module_caption", module
						.get("f_caption").getAsString());
				// key对应模块角色设计对象的id
				roleNode.addProperty("f_mrole_key", role.get("id")
						.getAsString());
				roleNode.addProperty("f_mrole_caption", role.get("f_caption")
						.getAsString());
				// 遍历已经存在的映射过的集合，设置ID，权限表达式，权限显示值
				for (int k = 0; k < mrms.size(); k++) {
					if (mrms.get(k).getF_mrole_key().equals(role.get("id").getAsString())) {
						roleNode.addProperty("id", mrms.get(k).getId());
						roleNode.addProperty("f_auth_expression", mrms.get(k).getF_auth_expression());
						roleNode.addProperty("f_auth_caption", mrms.get(k).getF_auth_caption());
						mrms.remove(k);
						break;
					}
				}
				roleNode.addProperty("isDeleted", false);
				moduleRoles.add(roleNode);
			}
		}
		// 处理已经被删除的模块角色
		for (int i = 0; i < mrms.size(); i++) {
			JsonObject mrNode = JsonObjectTool.object2JsonObject(mrms.get(i));
			mrNode.addProperty("isDeleted", true);
			moduleRoles.add(mrNode);
		}
		return moduleRoles;
	}
	
	/**
	 * 根据CDRU的key和应用的ID获取模块角色列表
	 * @param key
	 * @param appId
	 * @param isAll
	 * @return
	 * @throws ModuleRoleMappingException
	 */
	public JsonArray loadByCDRUAndAppId(String key, long appId, boolean isAll) throws ModuleRoleMappingException {
		JsonArray rs = new JsonArray();
		List<Expression> exps = null;
		List<String> expCaptions = new ArrayList<String>();
		Map<Long, Org> orgsCache = new HashMap<Long, Org>();
		try {
			// 获取权限表达式
			exps = getExps(key, isAll);
		} catch (Exception e) {
			throw new ModuleRoleMappingException("获取权限表达式失败", e);
		}
		try {
			for (Expression exp : exps) {
				switch (exp.getType()) {
				case User:
					expCaptions.add(((User) exp.getObject()).getF_caption());
					break;
				case Role:
					expCaptions.add(((Role) exp.getObject()).getF_caption());
					break;
				case Department:
					expCaptions.add(((Org) exp.getObject()).getF_caption());
					break;
				case Company:
					expCaptions.add(((Org) exp.getObject()).getF_caption());
					break;
				default:
					expCaptions.add("");
					break;
				}
			}
		} catch (RelationException e) {
			log.error(e.getMessage());
			throw new ModuleRoleMappingException("获取权限的表达式失败", e);
		}
		
		String expsStr = expsToString(exps);
		// 查询出了所有设置过模块角色权限映射
		List<ModuleRoleMapping> mrms = ModuleRoleMappingService.instance().findByExps(appId, expsStr);
		Application app = null;
		try {
			// 获取应用
			app = ApplicationService.instance().getApplicationById(
					appId);
			if (app == null) {
				throw new ModuleRoleMappingException("未能根据ID获取应用");
			}
		} catch (ApplicationException e) {
			log.error(e.getMessage());
			throw new ModuleRoleMappingException("根据ID获取应用时发生错误", e);
		}
		JsonArray modules = null;
		try {
			// 获取应用的模块角色列表
			modules = DesignObjectService.instance().findObjects(
					this.getServiceUrl(appId), null, MODULE_TYPE_NAME);
		} catch (DesignObjectException e) {
			log.error(e.getMessage());
			throw new ModuleRoleMappingException("获取设计对象失败", e);
		}
		// 没有被删除的模块权限集合
		Set<ModuleRoleMapping> notDeleteMrms = new HashSet<ModuleRoleMapping>();
		// 遍历所有的模块
		for(int i = 0; i < modules.size(); i++) {
			JsonObject module = modules.get(i).getAsJsonObject();
			// 如果该模块底下没有模块角色，跳过
			if (!module.has("f_roles") || !module.get("f_roles").isJsonArray()
					|| module.get("f_roles").getAsJsonArray().size() == 0) {
				continue;
			}
			JsonArray mrs = module.get("f_roles").getAsJsonArray();
			// 遍历该模块下所有模块角色
			for (int j = 0; j < mrs.size(); j++) {
				JsonObject mRole = mrs.get(j).getAsJsonObject();
				JsonObject mr = new JsonObject();
				mr.addProperty("f_application_id", appId);
				mr.addProperty("f_application_key", app.getF_key());
				mr.addProperty("f_module_key", module.get("id").getAsString());
				mr.addProperty("f_module_caption", module.get("f_caption").getAsString());
				mr.addProperty("f_mrole_key", mRole.get("id").getAsString());
				mr.addProperty("f_mrole_caption", mRole.get("f_caption").getAsString());
				// 维护单位的ID
				String orgIds = "";
				// 维护单位的名称
				String orgCaptions = "";
				String ownerCaptions = "";
				// 是否选中改权限
				boolean isSelect = false;
				for(int k = 0; k < mrms.size(); k++) {
					ModuleRoleMapping mrm = mrms.get(k);
					if (mrm.getF_mrole_key().equals(mRole.get("id").getAsString())) {
						notDeleteMrms.add(mrm);
						orgIds += mrm.getF_org_id() + ";";
						if(!orgsCache.containsKey(mrm.getF_org_id())) {
							try {
								orgsCache.put(mrm.getF_org_id(), OrganizationService.instance().getOrgById(mrm.getF_org_id()));
							} catch (OrganizationException e) {
								log.error(e.getMessage());
								throw new ModuleRoleMappingException("根据ID获取机构时发生错误", e);
							}
						}
						if(mrm.getF_org_id() != 0L) {
							orgCaptions += orgsCache.get(mrm.getF_org_id()).getF_caption() + ";";
						} else {
							orgCaptions += "无维护单位;";
						}
						isSelect = true;
						String authExpStr = mrm.getF_auth_expression();
						for(int m = 0; m < exps.size(); m++) {
							Expression exp = exps.get(m);
							String expStr = exp.getExpStr();
							if(authExpStr.startsWith(expStr + ";") || authExpStr.indexOf(";" + expStr + ";") > -1) {
								String expCaption = expCaptions.get(m);
								if(!ownerCaptions.startsWith(expCaption + ";") && ownerCaptions.indexOf(";" + expCaption + ";") == -1) {
									ownerCaptions += expCaptions.get(m) + ";";
								}
							}
						}
					}
				}
				mr.addProperty("f_org_ids", orgIds);
				mr.addProperty("f_org_captions", orgCaptions);
				mr.addProperty("f_is_select", isSelect);
				mr.addProperty("f_is_deleted", false);
				mr.addProperty("f_owner_captions", ownerCaptions);
				rs.add(mr);
			}
		}
		// 处理被删除的模块角色
		mrms.removeAll(notDeleteMrms);
		Map<String, JsonObject> deletedMrs = new HashMap<String, JsonObject>();
		for(int i = 0; i < mrms.size(); i++) {
			ModuleRoleMapping mrm = mrms.get(i);
			JsonObject mr = null;
			// 维护单位的ID
			String orgIds = "";
			// 维护单位的名称
			String orgCaptions = "";
			if(deletedMrs.containsKey(mrm.getF_mrole_key())) {
				mr = deletedMrs.get(mrm.getF_mrole_key());
				orgIds = mr.get("f_org_ids").getAsString();
				orgCaptions = mr.get("f_org_captions").getAsString();
			} else {
				mr = new JsonObject();
				mr.addProperty("f_is_deleted", true);
				mr.addProperty("f_application_id", mrm.getF_application_id());
				mr.addProperty("f_application_key", app.getF_key());
				mr.addProperty("f_module_key", mrm.getF_module_key());
				mr.addProperty("f_module_caption", mrm.getF_module_caption());
				mr.addProperty("f_mrole_key", mrm.getF_mrole_key());
				mr.addProperty("f_mrole_caption", mrm.getF_mrole_caption());
				mr.addProperty("f_is_select", true);
			}
			orgIds += mrm.getF_org_id() + ";";
			if(!orgsCache.containsKey(mrm.getF_org_id())) {
				try {
					orgsCache.put(mrm.getF_org_id(), OrganizationService.instance().getOrgById(mrm.getF_org_id()));
				} catch (OrganizationException e) {
					log.error(e.getMessage());
					throw new ModuleRoleMappingException("根据ID获取机构时发生错误", e);
				}
			}
			orgCaptions += orgsCache.get(mrm.getF_org_id()).getF_caption() + ";";
			mr.addProperty("f_org_ids", orgIds);
			mr.addProperty("f_org_captions", orgCaptions);
			rs.add(mr);
		}
		return rs;
	}
	
	private String expsToString(List<Expression> exps) {
		String rs = "";
		for(Expression exp : exps) {
			rs += exp.getExpStr() + ";";
		}
		return rs;
	}
	
	/**
	 * 根据key获取所有相关的权限表达式
	 * @param key
	 * @return
	 * @throws RelationException 
	 * @throws NumberFormatException 
	 * @throws OrganizationException 
	 */
	private List<Expression> getExps(String key, boolean isAll) throws NumberFormatException, RelationException, OrganizationException {
		List<Expression> rs = null;
		key = key.toUpperCase();
		Expression directExp = new Expression(key);
		if(!isAll) {
			rs = new ArrayList<Expression>();
			rs.add(directExp);
			return rs;
		}
		if(directExp.getType().equals(ExpressionTypes.User)) {
			rs = OrganizationRelationService.instance().getUserExpressions(Long.parseLong(directExp.getId()));
		} else if(directExp.getType().equals(ExpressionTypes.Role)) {
			rs = new ArrayList<Expression>();
			rs.add(directExp);
			Role directRole = directExp.getObject();
			// 查询角色所在的机构
			if(directRole.getF_org_id() >= 0L) {
				Org roleOrg = OrganizationService.instance().getOrgById(directRole.getF_org_id());
				rs.add(new Expression(roleOrg));
				Org currentOrg = roleOrg;
				while(currentOrg.getF_parent_id() > 0L) {
					currentOrg = OrganizationService.instance().getOrgById(currentOrg.getF_parent_id());
					rs.add(new Expression(currentOrg));
				}
			}
		} else if(directExp.getType().equals(ExpressionTypes.Company) || directExp.getType().equals(ExpressionTypes.Department)) {
			rs = new ArrayList<Expression>();
			rs.add(directExp);
			Org directtOrg = directExp.getObject();
			Org currentOrg = directtOrg;
			while(currentOrg.getF_parent_id() > 0L) {
				currentOrg = OrganizationService.instance().getOrgById(currentOrg.getF_parent_id());
				rs.add(new Expression(currentOrg));
			}
		}
		// 去除重复的表达式
		for (int i = rs.size() - 1; i > 0; i--) {
			for (int j = 0; j < i; j++) {
				if (((Expression) rs.get(i)).getExpStr().equals(
						((Expression) rs.get(j)).getExpStr())) {
					rs.remove(i);
					break;
				}
			}
		}
		return rs;
	}

	/**
	 * 保存模块角色权限映射 如果权限为空则删除 如果ID为空则增加 否则更新
	 * @param mrms
	 * @throws ModuleRoleMappingException
	 */
	public void save(JsonArray mrms) throws ModuleRoleMappingException {
		ModuleRoleMappingManager.instance().save(mrms);
	}
	
	/**
	 * 模块角色权限反向配置的保存
	 * @param datas
	 * @throws ModuleRoleMappingException 
	 */
	public void save2(JsonArray datas) throws ModuleRoleMappingException {
		ModuleRoleMappingManager.instance().save2(datas);
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

}
