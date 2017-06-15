package com.asc.manager.wfrmapping.handler;

import java.util.ArrayList;
import java.util.List;

import com.asc.bs.wfrmapping.service.WorkFlowRoleMappingManager;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.wfrolemapping.entity.WorkFlowRoleMapping;
import com.asc.commons.wfrolemapping.exception.WorkFlowRoleMappingException;
import com.asc.commons.wfrolemapping.service.WorkFlowRoleMappingService;
import com.asc.framework.designobject.service.DesignObjectService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class WorkFlowRoleMappingHandler {
	private static WorkFlowRoleMappingHandler singleton;

	public static final String FLOW_TYPE_NAME = "flow";
	
	private WorkFlowRoleMappingHandler() {
	}

	public static WorkFlowRoleMappingHandler instance() {
		if (singleton == null) {
			singleton = new WorkFlowRoleMappingHandler();
		}
		return singleton;
	}
	
	/**
	 * 根据应用ID和组织ID获取流程角色列表
	 * 
	 * @param appId
	 * @param orgId
	 * @return
	 * @throws WorkFlowRoleMappingException
	 */
	public JsonArray loadByAppAndOrg(long appId, long orgId) throws WorkFlowRoleMappingException {
		JsonArray workFlowRoles = new JsonArray();
		// 获取应经被映射过的角色
		List<WorkFlowRoleMapping> frms = null;
		frms = WorkFlowRoleMappingService.instance().findByCompany(appId, orgId);

		if (frms == null) {
			frms = new ArrayList<WorkFlowRoleMapping>(0);
		}
		Application app = null;
		try {
			app = ApplicationService.instance().getApplicationById(appId);
		} catch (ApplicationException e) {
			throw new WorkFlowRoleMappingException("根据ID获取应用时发生错误", e);
		}
		if (app == null) {
			throw new WorkFlowRoleMappingException("未能根据ID获取应用");
		}
		// 获取应用流程列表
		JsonArray workFlows = null;
		try {
			workFlows = DesignObjectService.instance().findObjects(this.getServiceUrl(appId), null, FLOW_TYPE_NAME);
		} catch (DesignObjectException e) {
			throw new WorkFlowRoleMappingException("获取流程角色列表失败", e);
		}
		// 遍历流程列表获取流程角色
		for (int i = 0; i < workFlows.size(); i++) {
			JsonObject workFlow = workFlows.get(i).getAsJsonObject();
			// 如果该流程底下没有流程角色，跳过
			if (!workFlow.has("f_roles")
					|| !workFlow.get("f_roles").isJsonArray()
					|| workFlow.get("f_roles").getAsJsonArray().size() == 0) {
				continue;
			}
			JsonArray mrs = workFlow.get("f_roles").getAsJsonArray();
			// 遍历该流程下的流程角色
			for (int j = 0; j < mrs.size(); j++) {
				JsonObject role = mrs.get(j).getAsJsonObject();
				JsonObject roleNode = new JsonObject();
				roleNode.addProperty("f_application_id", appId);
				roleNode.addProperty("f_org_id", orgId);
				roleNode.addProperty("f_flow_key", workFlow.get("id").getAsString());
				roleNode.addProperty("f_flow_caption", workFlow.get("f_caption").getAsString());
				// key对应流程角色设计对象的id
				roleNode.addProperty("f_wrole_key", role.get("id").getAsString());
				roleNode.addProperty("f_wrole_caption", role.get("f_caption").getAsString());
				// 遍历已经存在的映射过的集合，设置ID，权限表达式，权限显示值
				for (int k = 0; k < frms.size(); k++) {
					if (frms.get(k).getF_wrole_key().equals(role.get("id").getAsString())) {
						roleNode.addProperty("id", frms.get(k).getId());
						roleNode.addProperty("f_auth_expression", frms.get(k).getF_auth_expression());
						roleNode.addProperty("f_auth_caption", frms.get(k).getF_auth_caption());
						frms.remove(k);
						break;
					}
				}
				roleNode.addProperty("isDeleted", false);
				workFlowRoles.add(roleNode);
			}
		}
		// 处理已经被删除的流程角色
		for (int i = 0; i < frms.size(); i++) {
			JsonObject mrNode = JsonObjectTool.object2JsonObject(frms.get(i));
			mrNode.addProperty("isDeleted", true);
			workFlowRoles.add(mrNode);
		}
		return workFlowRoles;
	}
	
	/**
	 * 保存流程角色权限映射 如果权限为空则删除 如果ID为空则增加 否则更新
	 * @param mrms
	 * @throws WorkFlowRoleMappingException
	 */
	public void save(JsonArray mrms) throws WorkFlowRoleMappingException {
		WorkFlowRoleMappingManager.instance().save(mrms);
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
