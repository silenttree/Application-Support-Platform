package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.organization.access.IOrganizationReader;
import com.asc.commons.organization.access.IOrganizationWriter;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.OrgLog;
import com.asc.commons.organization.entity.OrgUser;
import com.asc.commons.organization.entity.Role;
import com.asc.commons.organization.entity.RoleUser;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * @author zhsq
 * <pre>
 * 包装OrganizationReaderImp,为其他程序提供web service
 * 其中的方法和被包装的类一一对应
 * 所有方法的参数处理成字符串
 * 所有重载的方法重新起名字
 * 返回值为一个标准的json字符串
 * 	其中success标识成功与否
 * 	data为成功时的返回数据
 * 	message为失败时的错误信息
 * </pre>
 */
@WebService
public class OrganizationWebService {
	private IOrganizationReader reader;
	private IOrganizationWriter writer;
	private Gson gson = new Gson();

	@WebMethod(exclude = true)
	public void setReader(IOrganizationReader reader) {
		this.reader = reader;
	}
	
	@WebMethod(exclude = true)
	public void setWriter(IOrganizationWriter writer) {
		this.writer = writer;
	}
	
	public String getUserByName(String name) {
		JsonObject result = new JsonObject();
		try {
			User user = reader.getUserByName(name);
			/*if (user == null) {
				throw OrganizationException.forUserNotExist(name);
			}*/
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(user));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据名称获取用户失败");
		}
		return gson.toJson(result);
	}
	
	public String getUserById(String userIdStr) {
		JsonObject result = new JsonObject();
		try {
			if (userIdStr == null || "".equals(userIdStr)) {
				throw new IllegalArgumentException("未设置用户Id");
			}
			long userId = Long.parseLong(userIdStr);
			if (userId == 0) {
				throw new IllegalArgumentException("用户Id为0");
			}
			User user = reader.getUserById(userId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(user));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据ID获取用户失败");
		}
		return gson.toJson(result);
	}
	
	public String getOrgById(String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long orgId = Long.parseLong(orgIdStr);
			Org org = reader.getOrgById(orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(org));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构失败");
		}
		return gson.toJson(result);
	}
	
	public String getRoleByKey(String key){
		JsonObject result = new JsonObject();
		try {
			Role role = reader.getRoleByKey(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(role));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取角色失败");
		}
		return gson.toJson(result);
	}
	
	public String updateUserPswd(String strUserId, String password){
		JsonObject result = new JsonObject();
		try {
			long lngUserId = Long.parseLong(strUserId);
			IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
			try {
				writer.updateUserPswd(lngUserId, password);
				tx.commit();
				result.addProperty("success", true);
				result.addProperty("data", "");
			} catch (Exception e) {
				tx.rollback();
				throw new OrganizationException("用户" + strUserId + "修改密码失败", e);
			} finally {
				CommonDatabaseAccess.instance().endTransaction();
			}
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "更新用户密码失败");
		}
		return gson.toJson(result);
	}
	
	public String findOrgsByParent(String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long orgId = Long.parseLong(orgIdStr);
			List<Org> orgs = reader.findOrgsByParent(orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构列表失败");
		}
		return gson.toJson(result);
	}

	public String findOrgUsers(String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long orgId = Long.parseLong(orgIdStr);
			List<User> users = reader.findOrgUsers(orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(users));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构用户列表失败");
		}
		return gson.toJson(result);
	}
	
	public String getRoleById(String roleIdStr) {
		JsonObject result = new JsonObject();
		try {
			long roleId = Long.parseLong(roleIdStr);
			Role role = reader.getRoleById(roleId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(role));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取角色失败");
		}
		return gson.toJson(result);
	}
	
	public String findCompanies(String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long orgId = Long.parseLong(orgIdStr);
			List<Org> cs = reader.findCompanies(orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(cs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findUserCompanies(String userIdStr) {
		JsonObject result = new JsonObject();
		try {
			long userId = Long.parseLong(userIdStr);
			List<Org> cs = reader.findUserCompanies(userId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(cs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取用户所在单位列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findUserOrgs(String userIdStr) {
		JsonObject result = new JsonObject();
		try {
			long userId = Long.parseLong(userIdStr);
			List<Org> os = reader.findUserOrgs(userId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(os));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取用户所在机构列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findCompanyRoles(String companyIdStr) {
		JsonObject result = new JsonObject();
		try {
			long companyId = Long.parseLong(companyIdStr);
			List<Role> roles = reader.findCompanyRoles(companyId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(roles));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构角色列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findUserRoles(String userIdStr) {
		JsonObject result = new JsonObject();
		try {
			long userId = Long.parseLong(userIdStr);
			List<Role> roles = reader.findUserRoles(userId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(roles));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取用户角色列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findRoleUsers(String companyIdStr, String roleIdStr) {
		JsonObject result = new JsonObject();
		try {
			long companyId = Long.parseLong(companyIdStr);
			long roleId = Long.parseLong(roleIdStr);
			List<User> users = reader.findRoleUsers(companyId, roleId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(users));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取角色用户列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findOrgUserById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			OrgUser user = reader.findOrgUserById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(user));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "机构用户获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getOrgUsers(String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long orgId = Long.parseLong(orgIdStr);
			List<OrgUser> users = reader.getOrgUsers(orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(users));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "机构用户获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getOrgList(String query) {
		JsonObject result = new JsonObject();
		try {
			List<Org> orgs = reader.getOrgList(query);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "机构搜索失败");
		}
		return gson.toJson(result);
	}
	
	public String findUserDefaultOrg(String userIdStr) {
		JsonObject result = new JsonObject();
		try {
			long userId = Long.parseLong(userIdStr);
			User user = reader.getUserById(userId);
			OrgUser orgUser = reader.findUserDefaultOrg(user);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(orgUser));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "用户默认机构获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findUsers(String query) {
		JsonObject result = new JsonObject();
		try {
			List<User> users = reader.findUsers(query);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(users));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "用户查询错误");
		}
		return gson.toJson(result);
	}
	
	public String findRoleUser(String orgIdStr, String roleIdStr, String idStr) {
		JsonObject result = new JsonObject();
		try {
			long orgId = Long.parseLong(orgIdStr);
			long roleId = Long.parseLong(roleIdStr);
			long id = Long.parseLong(idStr);
			RoleUser roleUser = reader.findRoleUser(orgId, roleId, id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(roleUser));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "角色用户查找失败");
		}
		return gson.toJson(result);
	}
	
	public String findOrgUser(String idStr, String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			long orgId = Long.parseLong(orgIdStr);
			OrgUser orgUser = reader.findOrgUser(id, orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(orgUser));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "机构用户查找失败");
		}
		return gson.toJson(result);
	}
	
	public String findDelUser() {
		JsonObject result = new JsonObject();
		try {
			List<User> users = reader.findDelUser();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(users));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "已删除用户列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findLowerOrgs(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			List<Org> orgs = reader.findLowerOrgs(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据组织结构ID获取下级机构失败");
		}
		return gson.toJson(result);
	}
	
	public String findUpperOrgs(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			List<Org> orgs = reader.findUpperOrgs(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据组织结构ID获取上级机构失败");
		}
		return gson.toJson(result);
	}
	
	public String findRolesByUserAndCom(String userIdStr, String companyIdStr) {
		JsonObject result = new JsonObject();
		try {
			long userId = Long.parseLong(userIdStr);
			long companyId = Long.parseLong(companyIdStr);
			List<Role> roles = reader.findRolesByUserAndCom(userId, companyId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(roles));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据用户和单位获取角色失败");
		}
		return gson.toJson(result);
	}
	
	public String findOrgByOrgTypeAndParId(String orgType, String parentId) {
		JsonObject result = new JsonObject();
		try {
			long pId = Long.parseLong(parentId);
			List<Org> orgs = reader.findOrgByOrgTypeAndParId(orgType, pId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "组织机构获取失败");
		}
		return gson.toJson(result);
	}
	
	public String findOrgByOrgType(String orgType) {
		JsonObject result = new JsonObject();
		try {
			List<Org> orgs = reader.findOrgByOrgType(orgType);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "组织机构获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getOrgListByPidAndQuery(String parentIdStr,String query) {
		JsonObject result = new JsonObject();
		try {
			long parentId = Long.parseLong(parentIdStr);
			List<Org> orgs = reader.getOrgListByPidAndQuery(parentId, query);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "机构搜索失败");
		}
		return gson.toJson(result);
	}
	
	public String getOrgByOrgCode(String orgCode) {
		JsonObject result = new JsonObject();
		try {
			Org org = reader.getOrgByOrgCode(orgCode);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(org));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "机构搜索失败");
		}
		return gson.toJson(result);
	} 
	
	public String findOrgLog(String operateType,String beginTime, String endTime) {
		JsonObject result = new JsonObject();
		try {
			List<OrgLog> orgLogs = reader.findOrgLog(operateType, beginTime, endTime);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgLogs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "组织机构日志获取失败");
		}
		return gson.toJson(result);
	} 
	
	public String findUpdateOrgsByTimeFrame(String beginTime, String endTime) {
		JsonObject result = new JsonObject();
		try {
			List<Org> orgs = reader.findUpdateOrgsByTimeFrame(beginTime, endTime);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "组织机构获取失败");
		}
		return gson.toJson(result);
	} 
	
	public String findInsertOrgsByTimeFrame(String beginTime, String endTime) {
		JsonObject result = new JsonObject();
		try {
			List<Org> orgs = reader.findInsertOrgsByTimeFrame(beginTime, endTime);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "组织机构获取失败");
		}
		return gson.toJson(result);
	}

	public String findOrgList(String currentCode,String parentCode, String orgType, String appendSQL) {
		JsonObject result = new JsonObject();
		try {
			List<Org> orgs = reader.findOrgList(currentCode,parentCode, orgType, appendSQL);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(orgs));
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "组织机构获取失败");
		}
		return result.toString();
	}
	
}