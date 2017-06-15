package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.modulerolemapping.access.IModuleRoleMappingReader;
import com.asc.commons.modulerolemapping.entity.ModuleRoleMapping;
import com.asc.commons.modulerolemapping.exception.ModuleRoleMappingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

@WebService
public class ModuleRoleMappingWebService {
	private IModuleRoleMappingReader reader;
	private Gson gson = new Gson();

	@WebMethod(exclude = true)
	public void setReader(IModuleRoleMappingReader reader) {
		this.reader = reader;
	}

	public String getById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			ModuleRoleMapping mrm = reader.getById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(mrm));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据id获取模块角色失败");
		}
		return gson.toJson(result);
	}
	
	public String findByOrgAndApp(String appIdStr, String orgIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long orgId = Long.parseLong(orgIdStr);
			List<ModuleRoleMapping> mrms = reader.findByOrgAndApp(appId, orgId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(mrms));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用和机构获取模块角色列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findByAppAndKey(String appIdStr, String roleKey) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			List<ModuleRoleMapping> mrms = reader.findByAppAndKey(appId, roleKey);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(mrms));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据KEY获取模块角色列表失败");
		}
		return gson.toJson(result);
	}
	
	public String getByOrgAndAppAndKey(String appIdStr, String orgIdStr, String roleKey) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long orgId = Long.parseLong(orgIdStr);
			ModuleRoleMapping mrm = reader.getByOrgAndAppAndKey(appId, orgId,
					roleKey);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(mrm));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据名称获取用户失败");
		}
		return gson.toJson(result);
	}
	
	public String findAll() {
		JsonObject result = new JsonObject();
		try {
			List<ModuleRoleMapping> mrms = reader.findAll();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(mrms));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取模块角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
//	public List<ModuleRoleMapping> findByExps(long appId, String exps)
	
	public String findByExps(String appIdStr, String exps) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			List<ModuleRoleMapping> mrms = reader.findByExps(appId, exps);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(mrms));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用和权限表达式获取模块角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findByCompany(String appIdStr, String companyIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long companyId = Long.parseLong(companyIdStr);
			List<ModuleRoleMapping> mrms = reader.findByCompany(appId, companyId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(mrms));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用和单位获取模块角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findByExpsAndCompany(String appIdStr, String exps, String companyIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long companyId = Long.parseLong(companyIdStr);
			List<ModuleRoleMapping> mrms = reader.findByExpsAndCompany(appId, exps, companyId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(mrms));
		} catch (ModuleRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用，权限表达式和单位获取模块角色映射列表失败");
		}
		return gson.toJson(result);
	}
}
