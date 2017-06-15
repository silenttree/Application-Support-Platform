package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.wfrolemapping.access.IWorkFlowRoleMappingReader;
import com.asc.commons.wfrolemapping.entity.WorkFlowRoleMapping;
import com.asc.commons.wfrolemapping.exception.WorkFlowRoleMappingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

@WebService
public class WorkFlowRoleMappingWebService {
	private IWorkFlowRoleMappingReader reader;
	private Gson gson = new Gson();
	
	public String getById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			WorkFlowRoleMapping wrm = reader.get(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(wrm));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据id获取流程角色失败");
		}
		return gson.toJson(result);
	}
	
	public String findAll() {
		JsonObject result = new JsonObject();
		try {
			List<WorkFlowRoleMapping> wrms = reader.findAll();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(wrms));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取流程角色列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findByAppAndKey(String appIdStr, String roleKey) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			List<WorkFlowRoleMapping> wrms = reader.findByAppAndKey(appId, roleKey);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(wrms));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用ID和流程角色KEY查询流程角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
	public String getByOrgAndAppAndKey(String appIdStr, String orgIdStr, String roleKey) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long orgId = Long.parseLong(orgIdStr);
			WorkFlowRoleMapping wrm = reader.getByOrgAndAppAndKey(appId,
					orgId, roleKey);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(wrm));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用ID,单位ID和流程角色KEY查询流程角色映射失败");
		}
		return gson.toJson(result);
	}
	
	public String findByExps(String appIdStr, String exps) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			List<WorkFlowRoleMapping> wrms = reader.findByExps(appId, exps);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(wrms));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用ID和流程角色KEY查询流程角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findByCompany(String appIdStr, String companyIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long companyId = Long.parseLong(companyIdStr);
			List<WorkFlowRoleMapping> wrms = reader.findByCompany(appId, companyId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(wrms));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用ID和单位ID查询流程角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
	public String findByExpsAndCompany(String appIdStr, String exps, String companyIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long companyId = Long.parseLong(companyIdStr);
			List<WorkFlowRoleMapping> wrms = reader.findByExpsAndCompany(appId, exps, companyId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(wrms));
		} catch (WorkFlowRoleMappingException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用ID,权限表达式和单位ID查询流程角色映射列表失败");
		}
		return gson.toJson(result);
	}
	
	@WebMethod(exclude = true)
	public IWorkFlowRoleMappingReader getReader() {
		return reader;
	}
	
	@WebMethod(exclude = true)
	public void setReader(IWorkFlowRoleMappingReader reader) {
		this.reader = reader;
	}

}
