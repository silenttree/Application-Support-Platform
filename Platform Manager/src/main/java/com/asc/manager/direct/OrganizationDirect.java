package com.asc.manager.direct;

import com.asc.commons.organization.exception.OrganizationException;
import com.asc.manager.org.OrganizationHandler;
import com.asc.manager.org.exception.OrganizationHandlerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class OrganizationDirect {

	/**
	 * 装载组织机构
	 * @return
	 */
	@DirectMethod
	public JsonObject loadOrganization(long orgId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = OrganizationHandler.instance().loadOrgList(orgId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载组织机构树
	 * @return
	 */
	@DirectMethod
	public JsonObject loadOrganizationNodes(JsonObject node){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			boolean containDept = true;
			if(node.has("containDept") && !node.get("containDept").isJsonNull()) {
				containDept = node.get("containDept").getAsBoolean();
			}
			
			datas = OrganizationHandler.instance().loadOrganizationNodes(node.get("node").getAsLong(), containDept);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载单位机构树
	 * @return
	 */
	@DirectMethod
	public JsonObject loadCompanyNodes(JsonObject note){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = OrganizationHandler.instance().loadCompanyNodes(note.get("node").getAsLong());
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 保存机构
	 * @return
	 */
	@DirectMethod
	public JsonObject saveOrg(long orgId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = OrganizationHandler.instance().saveOrgInstance(orgId, data);
				sObjects.add(object);
			} catch (OrganizationHandlerException e) {
				data.addProperty("msg", e.getMessage());
				fObjects.add(data);
				e.printStackTrace();
			}
		}
		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				OrganizationHandler.instance().deleteOrg(id);
				sObjects.add(data);
			} catch (OrganizationHandlerException e) {
				data.addProperty("msg", e.getMessage());
				fObjects.add(data);
				e.printStackTrace();
			}
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
	/**
	 * 装载用户
	 * @return
	 */
	@DirectMethod
	public JsonObject loadUsers(long orgId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = null;
			if(orgId >= 0){
				datas = OrganizationHandler.instance().getUserListByOrgId(orgId);
			}else{
				datas = OrganizationHandler.instance().getDeleteUserList();
			}
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载机构用户
	 * @return
	 */
	@DirectMethod
	public JsonObject loadOrgUsers(long orgId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = OrganizationHandler.instance().getUserListByOrgId(orgId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载机构用户数据
	 * @return
	 */
	@DirectMethod
	public JsonObject getOrgUser(long orgId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = OrganizationHandler.instance().getOrgUser(orgId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 保存角色
	 * @return
	 */
	@DirectMethod
	public JsonObject saveRole(long orgId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			try {
				JsonObject object = OrganizationHandler.instance().saveRole(orgId, data);
				sObjects.add(object);
			} catch (OrganizationHandlerException e) {
				message = e.getMessage();
				json.addProperty("success", false);
				json.addProperty("message", message);
				return json;
			}
		}

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				OrganizationHandler.instance().deleteRole(id);
			} catch (OrganizationHandlerException e) {
				message = e.getMessage();
				json.addProperty("success", false);
				json.addProperty("message", message);
				return json;
			}
			sObjects.add(data);
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
	/**
	 * 装载角色列表
	 * @return
	 */
	@DirectMethod
	public JsonObject loadRole(long orgId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = OrganizationHandler.instance().getRoleListByOrgId(orgId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	
	@DirectMethod 
	public JsonObject deleteUser(long userId){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().deleteUserInFact(userId);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 保存用户
	 * @return
	 */
	@DirectMethod
	public JsonObject saveUsers(long orgId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = OrganizationHandler.instance().saveUser(orgId, data);
				sObjects.add(object);
			} catch (OrganizationHandlerException e) {
				data.addProperty("msg", e.getMessage());
				fObjects.add(data);
				e.printStackTrace();
			}
		}

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				OrganizationHandler.instance().deleteUser(id);
				sObjects.add(data);
			} catch (OrganizationHandlerException e) {
				data.addProperty("msg", e.getMessage());
				fObjects.add(data);
				e.printStackTrace();
			}
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}

	/**
	 * 搜索机构
	 * @return
	 */
	@DirectMethod
	public JsonObject loadOrganizationNode(JsonObject note){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			if(note.get("query").getAsString() != "" && !"".equals(note.get("query").getAsString())){
				datas = OrganizationHandler.instance().loadOrganizationNode(note.get("query").getAsString());
			}else{
				datas = new JsonArray();
			}
			//datas = OrganizationHandler.instance().loadOrganizationNode("");
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 生成组织编码
	 * @return
	 */
	@DirectMethod
	public JsonObject createOrgCode(){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().createOrgCode();
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 角色树
	 * @return
	 */
	@DirectMethod
	public JsonArray loadRoleNodes(JsonObject node){
		JsonArray datas = new JsonArray();
		try {
			datas = OrganizationHandler.instance().loadRoleNodes(node.get("orgId").getAsLong());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * 获取角色用户
	 * @return
	 */
	@DirectMethod
	public JsonObject findRoleUsers(long orgId, long roleId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = new JsonArray();
			datas = OrganizationHandler.instance().findRoleUsers(roleId, orgId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 获取用户所有角色
	 * @return
	 */
	@DirectMethod
	public JsonObject findUserRoles(long userId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = new JsonArray();
			datas = OrganizationHandler.instance().findUserRoles(userId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 获取用户所有角色名称
	 * @return
	 */
	@DirectMethod
	public JsonObject findUserRolesName(long userId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = new JsonArray();
			datas = OrganizationHandler.instance().findUserRolesName(userId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 获取用户所有机构
	 * @return
	 */
	@DirectMethod
	public JsonObject findUserOrgs(long userId){
		JsonObject json = new JsonObject();
		try {
			JsonArray orgs = OrganizationHandler.instance().findUserOrgs(userId);
			JsonArray roles = OrganizationHandler.instance().findUserRolesName(userId);
			json.add("orgs", orgs);
			json.add("roles", roles);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	@DirectMethod
	public JsonObject getUserDeptList(long userId){
		JsonObject json = new JsonObject();
		try {
			JsonArray depts = OrganizationHandler.instance().getUserDeptList(userId);
			json.add("depts", depts);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject getDeptCaption(long orgId){
		JsonObject json = new JsonObject();
		try {
			if(orgId != -1){
				String caption = null;
				if(orgId == 0) {
					caption = "无单位";
				}else {
					caption = OrganizationHandler.instance().getDeptCaption(orgId);
				}
				json.addProperty("text", caption);
				json.addProperty("success", true);
			}else{
				JsonArray datas = new JsonArray();
				datas = OrganizationHandler.instance().getDeleteUserList();
				json.add("datas", datas);
				json.addProperty("success", true);
			}
		} catch (OrganizationException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	
	/**
	 * 添加机构用户
	 * 
	 * @param orgUserId
	 * @param datas
	 * @return
	 * @throws OrganizationHandlerException 
	 */
	@DirectMethod
	public JsonObject saveOrgUser(String orgUserId, long orgId){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().addOrgUser(orgUserId, orgId);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 保存机构用户
	 * @return
	 */
	@DirectMethod
	public JsonObject saveOrgUsers(long orgUserId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = OrganizationHandler.instance().saveOrgUsers(orgUserId, data);
				sObjects.add(object);
			} catch (OrganizationHandlerException e) {
				message = e.getMessage();
				json.addProperty("success", false);
				json.addProperty("message", message);
				return json;
			}
		}

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			boolean isdefault = data.get("isdefault").getAsBoolean();
			try {
				if(isdefault){
					throw new OrganizationException("删除机构用户错误: 机构默认用户不能删除。");
				}
				OrganizationHandler.instance().deleteOrgUser(id);
			} catch (OrganizationException e) {
				json.addProperty("success", false);
				json.addProperty("message", e.getMessage());
				return json;
			}
			sObjects.add(data);
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}

	/**
	 * 重置密码
	 * @return
	 */
	@DirectMethod
	public JsonObject retPassword(long userId){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().retPassword(userId);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 用户变更组织
	 * @return
	 */
	@DirectMethod
	public JsonObject updateUserOrg(String userId, String orgId){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().updateUserOrg(Long.parseLong(userId), Long.parseLong(orgId));
			json.addProperty("success", true);
		} catch (OrganizationException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 搜索用户
	 * @return
	 */
	@DirectMethod
	public JsonObject findUser(JsonObject note){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			if(note.get("query").getAsString() != "" && !"".equals(note.get("query").getAsString())){
				datas = OrganizationHandler.instance().findUser(note.get("query").getAsString());
			}else{
				datas = new JsonArray();
			}
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 选择用户框保存角色用户
	 * @return
	 */
	@DirectMethod
	public JsonObject saveSelectorRoleUsers(String userId, long orgId, long roleId){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().saveRoleUsers(userId, orgId, roleId);
			json.addProperty("success", true);
		} catch (OrganizationHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 保存角色用户
	 * @return
	 */
	@DirectMethod
	public JsonObject saveRoleUsers(long roleId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";

		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				OrganizationHandler.instance().deleteRoleUser(roleId, id);
			} catch (OrganizationHandlerException e) {
				json.addProperty("success", false);
				json.addProperty("message", e.getMessage());
				return json;
			}
			sObjects.add(data);
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}

	/**
	 * 机构转移
	 * @return
	 */
	@DirectMethod
	public JsonObject moveOrg(long orgId, long targetId){
		JsonObject json = new JsonObject();
		try {
			OrganizationHandler.instance().moveOrg(orgId, targetId);
			json.addProperty("success", true);
		} catch (OrganizationException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
}
