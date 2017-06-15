package com.asc.manager.org;

import java.util.Date;
import java.util.List;

import com.asc.bs.organization.service.OrganizationManager;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.engine.authority.exception.RelationException;
import com.asc.commons.exception.DbAccessException;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.OrgUser;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.RoleUser;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.commons.organization.service.OrganizationService;
import com.asc.manager.org.exception.OrganizationHandlerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class OrganizationHandler {

	private static OrganizationHandler singleton;

	public static OrganizationHandler instance() {
		if (singleton == null) {
			singleton = new OrganizationHandler();
		}
		return singleton;
	}

	/**
	 * 加载组织列表
	 * 
	 * @return
	 * @throws OrganizationHandlerException
	 */
	public JsonArray loadOrgList(long orgId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		List<Org> orgList;
		try {
			orgList = OrganizationService.instance().getOrgList(orgId);
			if(orgList != null){
				for(int i = 0; i < orgList.size(); i++){
					Org org = orgList.get(i);
					JsonObject data = JsonObjectTool.object2JsonObject(org);
					datas.add(data);
				}
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("获取组织列表失败。", e);
		}
	}

	/**
	 * 保存组织
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 * @throws OrganizationException
	 */
	public JsonObject saveOrgInstance(long orgId, JsonObject data) throws OrganizationHandlerException {
		try {
			Org org = OrganizationManager.instance().saveOrg(orgId, data);
			return JsonObjectTool.object2JsonObject(org);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException(e.getMessage(), e);
		} catch (Exception e) {
			throw new OrganizationHandlerException("机构保存失败，" + e.getMessage() , e);
		}
	}

	/**
	 * 删除组织
	 * 
	 * @param id
	 */
	public void deleteOrg(long id) throws OrganizationHandlerException {
		try {
			OrganizationManager.instance().deleteOrg(id);
		} catch (OrganizationException e) { 
			throw new OrganizationHandlerException("组织删除失败，" + e.getMessage(), e);
		}
	}

	/**
	 * 获取组织树
	 * 
	 * @return
	 */
	public JsonArray loadOrganizationNodes(long orgId, boolean containDept) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<Org> orgList = null;
			if(containDept) {				
				orgList = OrganizationService.instance().getOrgList(orgId);
			}else {
				orgList = OrganizationService.instance().getCompaniesList(orgId);
			}
			if(orgList != null){
				for(int i = 0; i< orgList.size(); i++){
					Org org = orgList.get(i);
					JsonObject data = new JsonObject();
					data.addProperty("id", org.getId());
					data.addProperty("text", org.getF_caption());
					data.addProperty("leaf", false);
					data.addProperty("iconCls", "icon-manager-organizationmanager");
					data.addProperty("type", org.getF_type());
					datas.add(data);
				}
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("组织树获取失败。", e);
		}
	}

	/**
	 * 获取单位树
	 * 
	 * @return
	 */
	public JsonArray loadCompanyNodes(long orgId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<Org> orgList = OrganizationService.instance().getCompaniesList(orgId);
			if(orgList != null){
				for(int i = 0; i< orgList.size(); i++){
					Org org = orgList.get(i);
					JsonObject data = new JsonObject();
					data.addProperty("id", org.getId());
					data.addProperty("text", org.getF_caption());
					data.addProperty("leaf", false);
					data.addProperty("iconCls", "icon-manager-organizationmanager");
					datas.add(data);
				}
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("组织树获取失败。", e);
		}
	}

	/**
	 * 通过组织ID获取组织用户
	 * 
	 * @param orgId
	 * @return
	 */
	public JsonArray getUserListByOrgId(long orgId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<User> userList = OrganizationService.instance().getUserListByOrgId(orgId);
			datas = JsonObjectTool.objectList2JsonArray(userList);
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("机构用户获取失败", e);
		}
	}

	/**
	 * 获取机构用户映射
	 * 
	 * @param orgId
	 * @return
	 * @throws OrganizationException 
	 */
	public JsonArray getOrgUser(long orgId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		List<OrgUser> orgUserList = null;
		try {
			orgUserList = OrganizationService.instance().getOrgUsers(orgId);
		} catch (OrganizationException e1) {
			throw new OrganizationHandlerException("机构用户获取失败。");
		}
		if(orgUserList != null){
			JsonArray orgUserDatas = JsonObjectTool.objectList2JsonArray(orgUserList);
			for(int i = 0; i < orgUserDatas.size(); i++){
				JsonObject jsob = (JsonObject) orgUserDatas.get(i);
				try {
					User user = OrganizationService.instance().getUserById(jsob.get("f_user_id").getAsLong());
					if(user != null){
						jsob.addProperty("id", jsob.get("id").getAsLong());
						jsob.addProperty("f_user_id", jsob.get("f_user_id").getAsLong());
						jsob.addProperty("f_org_id", jsob.get("f_org_id").getAsLong());
						jsob.addProperty("f_name", user.getF_name());
						jsob.addProperty("f_caption", user.getF_caption());
						jsob.addProperty("f_gender", user.getF_gender());
						jsob.addProperty("f_email", user.getF_email());
						jsob.addProperty("f_company_caption", user.getF_company_caption());
						jsob.addProperty("f_cellphone", user.getF_cellphone());
						//是否是用户默认组织
						boolean isdefault = false;
						if(jsob.get("f_org_id").getAsLong() == user.getF_company_id() || jsob.get("f_org_id").getAsLong() == user.getF_dept_id()){
							isdefault = true;
						}
						jsob.addProperty("f_isdefault", isdefault);
					}
					
					datas.add(jsob);
				} catch (OrganizationException e) {
					throw new OrganizationHandlerException("机构用户映射获取失败。");
				}
			}
		}
		return datas;
	}
	
	/**
	 * 保存用户
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 * @throws RelationException 
	 * @throws OrganizationException
	 */
	public JsonObject saveUser(long orgId, JsonObject data) throws OrganizationHandlerException {
		try {
			User user = OrganizationManager.instance().saveUser(orgId, data);
			return JsonObjectTool.object2JsonObject(user);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户保存失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 根据ID删除用户
	 * 
	 * @param id
	 */
	public void deleteUser (long id) throws OrganizationHandlerException {
		try {
			OrganizationManager.instance().delUserById(id);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户删除失败:" + e.getMessage(), e);
		}
	}

	/**
	 * 获得角色列表
	 * 
	 * @param orgId
	 * @return
	 */
	public JsonArray getRoleListByOrgId(long orgId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<Role> roleList = OrganizationService.instance().getRoleListByOrgId(orgId);
			datas = JsonObjectTool.objectList2JsonArray(roleList);
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("角色列表获取失败。", e);
		}
	}

	/**
	 * 保存角色
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 */
	public JsonObject saveRole(long orgId, JsonObject data) throws OrganizationHandlerException {
		Role role;
		try {
			role = OrganizationManager.instance().saveRole(orgId, data);
			return JsonObjectTool.object2JsonObject(role);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("角色保存失败," + e.getMessage(), e);
		}
	}

	/**
	 * 删除角色
	 * 
	 * @param id
	 */
	public void deleteRole(long id) throws OrganizationHandlerException {
		try {
			OrganizationManager.instance().delRole(id);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("角色删除失败，" + e.getMessage(), e);
		}
	}

	/**
	 * 搜索机构
	 * 
	 * @param query
	 * @return
	 * @throws OrganizationException
	 */
	public JsonArray loadOrganizationNode(String query) throws OrganizationHandlerException, OrganizationException {
		JsonArray datas = new JsonArray();
		List<Org> orgList = OrganizationService.instance().getOrgList(query);
		if(orgList != null){
			for(int i = 0; i< orgList.size(); i++){
				Org org = orgList.get(i);
				JsonObject data = new JsonObject();
				data.addProperty("id", org.getId());
				data.addProperty("text", org.getF_caption());
				data.addProperty("leaf", true);
				data.addProperty("iconCls", "icon-manager-organizationmanager");

				String path = "";
				path = buidPath(org, path);
				data.addProperty("path", path);
				datas.add(data);
			}
		}
		return datas;
	}

	public String buidPath(Org org, String path) throws OrganizationException{
		try {
			if(org != null){
				path = "/" + org.getF_parent_id() + path;
				Org upOrg = OrganizationService.instance().getOrgById(org.getF_parent_id());
				path = buidPath(upOrg, path);
			}
			return path;
		} catch (OrganizationException e) {
			throw new OrganizationException("机构树地址获取失败。");
		}
	}

	/**
	 * 生成组织编码
	 * @return
	 * @throws DbAccessException 
	 */
	public void createOrgCode() throws OrganizationHandlerException {
		try {
			List<Org> orgList = OrganizationService.instance().getOrgList(0);
			for(int i = 0; i < orgList.size(); i++){
				Org org = orgList.get(i);
				String code = "";
				if(i >= 9){
					code = "0" + (i+1);
				}else{
					code = "00" + (i+1);
				}
				org.setF_no(code);
				
				IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
				try {
					org.save();
					tx.commit();
				} catch (DbAccessException e) {
					tx.rollback();
					throw new OrganizationHandlerException("生成组织编码保存失败。", e);
				} finally {
					CommonDatabaseAccess.instance().endTransaction();
				}
				recreateOrgLv(code, org);
			}
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("", e);
		}
	}

	/**
	 * 递归生成机构下级机构编码
	 * @param code
	 * @param org
	 * @throws DbAccessException
	 */
	public void recreateOrgLv(String code, Org org) throws OrganizationHandlerException{
		try {
			List<Org> orgList = OrganizationService.instance().getOrgList(org.getId());
			if(orgList != null){
				for(int i = 0; i < orgList.size(); i++){
					Org orglv = orgList.get(i);
					if(i >= 9){
						code = org.getF_no() + ".0" + (i+1);
					}else{
						code = org.getF_no() + ".00" + (i+1);
					}
					orglv.setF_no(code);
					IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
					try {
						orglv.save();
						tx.commit();
					} catch (DbAccessException e) {
						tx.rollback();
						throw new OrganizationHandlerException("生成组织编码保存失败。", e);
					} finally {
						CommonDatabaseAccess.instance().endTransaction();
					}
					recreateOrgLv(code, orglv);
				}
			}
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("机构编码生产失败。", e);
		}
	}

	/**
	 * 递归获取机构用户包含下级机构用户
	 * 
	 * @param orgId
	 * @param userList
	 * @throws OrganizationException
	 */
	public void orgLvList(long orgId, List<User> userList) throws OrganizationHandlerException{
		try {
			List<Org> orgList = OrganizationService.instance().getOrgList(orgId);
			if(orgList != null){

				for(int i = 0; i < orgList.size(); i++){
					Org org = orgList.get(i);
					List<User> userLvList = OrganizationService.instance().getUserListByOrgId(org.getId());
					userList.addAll(userLvList);
					orgLvList(org.getId(), userList);
				}
			}
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("下级机构用户获取失败。", e);
		}
	}

	/**
	 * 获取角色用户
	 * @return
	 */
	public JsonArray findRoleUsers(long companyId, long roleId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<User> roleUserList = OrganizationService.instance().findRoleUsers(companyId, roleId);
			datas = JsonObjectTool.objectList2JsonArray(roleUserList);
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("角色用户获取失败。");
		}

	}

	/**
	 * 角色树
	 * 
	 * @param orgId
	 * @return
	 * @throws OrganizationException
	 */
	public JsonArray loadRoleNodes(long orgId) throws OrganizationHandlerException {
		JsonArray datas = new JsonArray();
		try {
			//获得机构角色
			List<Role> roleList = null;
			if(orgId > 0){
				roleList = OrganizationService.instance().getRoleListByOrgId(orgId);
			}
			//获得全局角色
			List<Role> qjList = OrganizationService.instance().getRoleListByOrgId(0);
			//获取机构用户
			List<User> userList=  null;
			if(orgId > 0){
				userList = OrganizationService.instance().getUserListByOrgId(orgId);
			}
			//生成全局角色节点
			JsonObject qjjs = new JsonObject();
			if(qjList != null){
				qjjs.addProperty("id", 0);
				qjjs.addProperty("text", "全局角色");
				qjjs.addProperty("leaf", false);
				qjjs.addProperty("iconCls", "icon-manager-group");
				JsonArray qjjsdata = new JsonArray();
				for(int i = 0; i< qjList.size(); i++){
					Role role = qjList.get(i);
					JsonObject qj = new JsonObject();
					qj.addProperty("id", role.getId());
					qj.addProperty("text", role.getF_caption());
					qj.addProperty("leaf", true);
					qj.addProperty("iconCls", "icon-manager-applicationmoduleroleauthorith");
					qjjsdata.add(qj);
				}
				qjjs.add("children", qjjsdata);
				datas.add(qjjs);
			}
			//生成机构角色节点
			JsonObject jgjs = new JsonObject();
			if(roleList != null){
				jgjs.addProperty("id", "jgjs");
				jgjs.addProperty("text", "机构角色");
				jgjs.addProperty("leaf", false);
				jgjs.addProperty("iconCls", "icon-manager-nocompanyuser");
				JsonArray jgjsdata = new JsonArray();
				for(int i = 0; i< roleList.size(); i++){
					JsonObject jg = new JsonObject();
					Role role = roleList.get(i);
					jg.addProperty("id", role.getId());
					jg.addProperty("text", role.getF_caption());
					jg.addProperty("leaf", true);
					jg.addProperty("iconCls", "icon-manager-contact_blue");
					jgjsdata.add(jg);
				}
				jgjs.add("children", jgjsdata);
				datas.add(jgjs);
			}
			//生成机构用户
			JsonObject jgyh = new JsonObject();
			if(userList != null){
				jgyh.addProperty("id", "jgyh");
				jgyh.addProperty("text", "机构用户");
				jgyh.addProperty("leaf", false);
				jgyh.addProperty("iconCls", "icon-manager-group");
				JsonArray jgyhdata = new JsonArray();
				for(int i = 0; i< userList.size(); i++){
					User user = userList.get(i);
					JsonObject jgy = new JsonObject();
					jgy.addProperty("id", user.getId());
					jgy.addProperty("text", user.getF_caption());
					jgy.addProperty("leaf", true);
					jgy.addProperty("iconCls", "icon-manager-applicationmoduleroleauthorith");
					jgyhdata.add(jgy);
				}
				jgyh.add("children", jgyhdata);
				datas.add(jgyh);
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("角色获取失败。", e);
		}
	}

	/**
	 * 获取用户所有角色
	 * @return
	 */
	public JsonArray findUserRoles(long userId) throws OrganizationHandlerException {
		try {
			JsonArray datas = new JsonArray();
			List<Role> roleList = OrganizationService.instance().findUserRoles(userId);
			datas = JsonObjectTool.objectList2JsonArray(roleList);
			for(int i=0;i<roleList.size();i++){
				long orgId = datas.get(i).getAsJsonObject().get("f_org_id").getAsLong();
				Org org = OrganizationService.instance().getOrgById(orgId);
				if (org == null) {
					datas.get(i).getAsJsonObject().addProperty("f_org_caption", "");
				} else {
					datas.get(i).getAsJsonObject().addProperty("f_org_caption", org.getF_caption());
				}
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户所有角色获取失败。");
		}
	}

	/**
	 * 获取用户所有角色名
	 * @return
	 */
	public JsonArray findUserRolesName(long userId) throws OrganizationHandlerException {
		try {
			JsonArray datas = new JsonArray();
			List<Role> roleList = OrganizationService.instance().findUserRoles(userId);
			if(roleList != null){
				for(int i = 0; i < roleList.size(); i++){
					Role role = roleList.get(i);
					JsonObject json = new JsonObject();
					json.addProperty("text", role.getF_caption());
					datas.add(json);
				}
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户所有角色获取失败。");
		}
	}

	/**
	 * 保存机构用户
	 * 
	 * @param orgId
	 * @param data
	 * @return
	 * @throws OrganizationException
	 */
	public JsonObject saveOrgUsers(long orgUserId, JsonObject data) throws OrganizationHandlerException {
		try {
			OrgUser orguser = OrganizationManager.instance().saveOrgUsers(orgUserId, data);
			return JsonObjectTool.object2JsonObject(orguser);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("保存机构用户失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 密码重置
	 * 
	 * @param userId
	 * @throws OrganizationException
	 */
	public void retPassword(long userId) throws OrganizationHandlerException {
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			User user = OrganizationService.instance().getUserById(userId);
			if (user == null) {
				throw OrganizationException.forUserNotExist("ID="+userId);
			}
			user.setF_password("1234");
			user.save();
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationHandlerException("密码重置失败。", e);
		} catch (OrganizationException e) {
			tx.rollback();
			throw new OrganizationHandlerException("密码重置失败。", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 变更用户机构
	 * 
	 * @param userId
	 * @param orgId
	 * @throws OrganizationException
	 */
	public void updateUserOrg(long userId, long orgId) throws OrganizationException {
		Org org = OrganizationService.instance().getOrgById(orgId);
		User user = OrganizationService.instance().getUserById(userId);
		OrgUser orgUser = OrganizationService.instance().findUserDefaultOrg(user);
		//判断机构类型
		if(org == null) { // 用户的默认机构设置为空
			user.setF_company_id(0L);
			user.setF_company_caption(null);
			user.setF_dept_id(0L);
			user.setF_dept_caption(null);
		}else if(org.getF_type() == Org.Types.Company.intValue()){
			if(org.getId() == user.getF_company_id()){
				throw new OrganizationException("变更机构不能为用户所在机构。");
			}
			user.setF_company_id(org.getId());
			user.setF_company_caption(org.getF_caption());
			user.setF_dept_id(org.getId());
			user.setF_dept_caption(org.getF_caption());
		}else{
			if(org.getId() == user.getF_dept_id()){
				throw new OrganizationException("变更机构不能为用户所在机构。");
			}
			Org cmpn = OrganizationService.instance().getCompany(org.getId());
			user.setF_company_id(cmpn.getF_company_id());
			user.setF_company_caption(cmpn.getF_company_caption());
			user.setF_dept_id(org.getId());
			user.setF_dept_caption(org.getF_caption());
		}
		if(org != null && orgUser != null) {
			orgUser.setF_org_id(org.getId());
		} else if(org != null && orgUser == null) {
			List<OrgUser> orgUsers = OrganizationService.instance().getOrgUserByOrgAndUser(userId, orgId);
			for(OrgUser ou : orgUsers) {
				OrganizationManager.instance().deleteOrgUser(ou.getId());
			}
			orgUser = new OrgUser();
			orgUser.setId(0);
			orgUser.setF_order(1);
			orgUser.setF_org_id(org.getId());
			orgUser.setF_type(0);
			orgUser.setF_user_id(userId);
			orgUser.setF_create_time(new Date());
			orgUser.setF_update_time(new Date());
		}
		//保存机构用户
		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			user.save();
			if(orgUser != null) {				
				orgUser.save();
			}
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new OrganizationException("变更用户组织保存失败。", e);
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 获取用户所有机构名
	 * @return
	 */
	public JsonArray findUserOrgs(long userId) throws OrganizationHandlerException {
		try {
			JsonArray datas = new JsonArray();
			List<Org> orgList = OrganizationService.instance().findUserOrgs(userId);
			for(int i = 0; i < orgList.size(); i++){
				Org org = orgList.get(i);
				JsonObject json = new JsonObject();
				json.addProperty("num", i);
				json.addProperty("text", org.getF_caption());
				datas.add(json);
			}
			return datas;
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户所在机构获取失败。");
		}
	}

	public JsonArray getUserDeptList(long userId) throws OrganizationHandlerException {
		try{
			JsonArray datas = new JsonArray();
			List<Org> orgList = OrganizationService.instance().findUserOrgs(userId);
			for(int i = 0; i < orgList.size(); i++){
				String caption = "";
				Org org = orgList.get(orgList.size()-1-i);
				Org parOrg = org;
				while(parOrg.getF_type() != Org.Types.Company.intValue()){
					caption += parOrg.getF_caption()+"-";
					parOrg = OrganizationService.instance().getOrgById(parOrg.getF_parent_id());
				}
				caption += parOrg.getF_caption();	
				JsonObject json = new JsonObject();
				json.addProperty("id", org.getId());
				json.addProperty("caption", caption);
				datas.add(json);
			}
			return datas;
		}catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户所在机构获取失败。");
		}
	}
	
	public String getDeptCaption(long orgId) throws OrganizationException {
		Org org = OrganizationService.instance().getOrgById(orgId);
		Org parOrg = org;
		String caption = "";
		while(parOrg.getF_type() != Org.Types.Company.intValue()){
			caption += parOrg.getF_caption()+"-";
			parOrg = OrganizationService.instance().getOrgById(parOrg.getF_parent_id());
			if(parOrg == null) break;
		}
		caption += parOrg.getF_caption();	
		return caption;
	}
	/**
	 * 搜索用户
	 * 
	 * @param query
	 * @return
	 * @throws OrganizationException
	 */
	public JsonArray findUser(String query) throws OrganizationException {
		JsonArray datas = new JsonArray();
		//搜索出来的用户列表
		List<User> userList = OrganizationService.instance().findUsers(query);
		if(userList != null){
			for(int i = 0; i< userList.size(); i++){
				User user = userList.get(i);
				Org org = null;
				JsonObject data = new JsonObject();
				data = JsonObjectTool.object2JsonObject(user);
				if(user.getF_dept_id() == 0){
					org = OrganizationService.instance().getOrgById(user.getF_company_id());
					data.addProperty("f_dept_id", user.getF_company_id());
				}else{
					org = OrganizationService.instance().getOrgById(user.getF_dept_id());
					data.addProperty("f_dept_id", user.getF_dept_id());
				}
				//data.addProperty("id", user.getId());
				data.addProperty("text", user.getF_caption());
				String path = "";
				path = buidPath(org, path);
				if(user.getF_state() == User.States.Deleted.intValue()){
					data.addProperty("path", "/-1");
				}else{
					data.addProperty("path", path);
				}
				datas.add(data);
			}
		}
		return datas;
	}

	/**
	 * 删除角色用户
	 * 
	 * @param roleId
	 * @param userId
	 * @throws OrganizationException
	 */
	public void deleteRoleUser(long roleId, long userId) throws OrganizationHandlerException {
		try {
			OrganizationManager.instance().delRoleUser(roleId , userId);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("角色用户删除失败。" + e.getMessage());
		}
	}

	/**
	 * 选中用户保存
	 * 
	 * @param userId
	 * @param orgId
	 * @param roleId
	 * @throws OrganizationException
	 */
	public void saveRoleUsers(String userId, long orgId, long roleId) throws OrganizationHandlerException {
		String[] userIdList = userId.split(";");
		first:
		for(int i = 0; i < userIdList.length; i++){
			boolean permit = false;
			String idStr = userIdList[i];
			long id = Long.parseLong(idStr.substring(2));
			try {
				RoleUser roleUser = OrganizationService.instance().findRoleUser(orgId, roleId, id);
				if(roleUser == null){
					roleUser = new RoleUser();
					roleUser.setId(0);
					roleUser.setF_company_id(orgId);
					roleUser.setF_role_id(roleId);
					roleUser.setF_user_id(id);
					roleUser.setF_create_time(new Date());
					
					//角色为机构角色时,用户必须为该机构的用户
					Role role = OrganizationService.instance().getRoleById(roleId);
					// TODO
					if(role.getF_org_id() != 0){
						List<Org> orgList = OrganizationService.instance().findUserOrgs(id);
						for(int j=0;j<orgList.size();j++){
							long deptId = orgList.get(j).getId();
							if(orgList.get(j).getF_type() == Org.Types.Company.intValue()){
								if(isLowerCmp(deptId, orgId)){
									permit = true;break;
								}
							}else{
								Org cmp = OrganizationService.instance().getCompany(deptId);
								if(isLowerCmp(cmp.getId(), orgId)){
									permit = true;break;
								}
							}
						}
					}else{
						permit = true;
					}
					if(permit){
						OrganizationManager.instance().saveRoleuser(roleUser);
					}else{
						throw new OrganizationHandlerException("用户不属于角色所在机构。");
					}
				}else{
					continue first;
//					throw new OrganizationHandlerException("角色用户已存在。");
				}
			} catch (OrganizationException e) {
				throw new OrganizationHandlerException("角色用户获取失败");
			}
		}
	}
	
	/**
	 * 判断第一个单位ID是否是第二个单位ID的下级单位，如果两个单位一样返回true
	 * @param lowerId
	 * @param higherId
	 * @return
	 * @throws OrganizationException 
	 */
	private boolean isLowerCmp(long lowerId, long higherId) throws OrganizationException {
		if(lowerId == higherId) {
			return true;
		}
		Org lowerOrg = OrganizationService.instance().getOrgById(lowerId);
		while(lowerOrg.getF_parent_id() != 0L) {
			lowerOrg = OrganizationService.instance().getOrgById(lowerOrg.getF_parent_id());
			if(lowerOrg.getId() == higherId) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 删除机构用户
	 * 
	 * @param id
	 * @throws OrganizationException
	 */
	public void deleteOrgUser(long id) throws OrganizationException {
		OrganizationManager.instance().deleteOrgUser(id);
	}

	/**
	 * 通过人员选择添加机构用户
	 * 
	 * @param orgUserId
	 * @param orgId
	 * @throws OrganizationHandlerException
	 */
	public void addOrgUser(String orgUserId, long orgId) throws OrganizationHandlerException {
		String[] userIdList = orgUserId.split(";");
		for(int i = 0; i < userIdList.length; i++){
			String idStr = userIdList[i];
			long id = Long.parseLong(idStr.substring(2));
			OrgUser orgUser;
			try {
				orgUser = OrganizationService.instance().findOrgUser(id, orgId);
				if(orgUser == null){
					orgUser = new OrgUser();
					orgUser.setId(0);
					orgUser.setF_user_id(id);
					orgUser.setF_org_id(orgId);
					orgUser.setF_create_time(new Date());
					IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
					try {
						orgUser.save();
						tx.commit();
					} catch (DbAccessException e) {
						tx.rollback();
						throw new OrganizationHandlerException("保存错误。");
					} finally {
						CommonDatabaseAccess.instance().endTransaction();
					}
				}
			} catch (OrganizationHandlerException e) {
				throw new OrganizationHandlerException("机构用户添加失败：" + e.getMessage());
			} catch (OrganizationException e1) {
				throw new OrganizationHandlerException("机构用户已存在。");
			}
		}
	}

	/**
	 * 机构转移
	 * 
	 * @param orgId
	 * @param targetId
	 * @throws OrganizationException
	 */
	public void moveOrg(long orgId, long targetId) throws OrganizationException {
		OrganizationManager.instance().moveOrg(orgId, targetId);
		Org targetOrg = OrganizationService.instance().getOrgById(targetId);
		try {
			recreateOrgLv(targetOrg.getF_no(),targetOrg);
		} catch (OrganizationHandlerException e) {
			
		}
	}

	public JsonArray getDeleteUserList() throws OrganizationHandlerException{
		
		try {
			List<User> delUser = OrganizationService.instance().findDelUser();
			return JsonObjectTool.objectList2JsonArray(delUser);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("已删除用户列表获取失败！");
		}
	}

	/**
	 * 根据用户ID物理删除用户
	 * @param userId
	 * @return
	 */
	public void deleteUserInFact(long id) throws OrganizationHandlerException{
		try {
			OrganizationManager.instance().delUserByIdInFact(id);
		} catch (OrganizationException e) {
			throw new OrganizationHandlerException("用户删除失败:" + e.getMessage(), e);
		}
	}

	

	

	
}
