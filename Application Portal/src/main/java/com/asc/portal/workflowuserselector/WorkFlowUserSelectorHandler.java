package com.asc.portal.workflowuserselector;

import java.util.List;

import com.asc.commons.cdruselector.CDRUNode;
import com.asc.commons.organization.entity.Org;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.asc.commons.organization.service.OrganizationService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class WorkFlowUserSelectorHandler {
	private static WorkFlowUserSelectorHandler singleton;

	private WorkFlowUserSelectorHandler() {

	}

	public static WorkFlowUserSelectorHandler instance() {
		if (singleton == null) {
			singleton = new WorkFlowUserSelectorHandler();
		}
		return singleton;
	}

	/**
	 * 根据ID字符串获取对应的用户
	 * @param ids
	 * @return
	 * @throws NumberFormatException
	 * @throws OrganizationException
	 */
	public JsonArray getUsersByIds(String ids) throws NumberFormatException,
			OrganizationException {
		JsonArray jsonArray = new JsonArray();
		if (ids == null || "".equals(ids)) {
			return jsonArray;
		}
		String[] idStrArray = ids.split(";");
		for (String idStr : idStrArray) {
			if (idStr != null && idStr.startsWith("U_")) {
				User u = OrganizationService.instance().getUserById(
						Long.parseLong(idStr.substring(2)));
				if (u != null) {
					JsonObject ju = userToNode(u).toJson();
					// 智能流转和自由流转使用不同的id
					ju.addProperty("id", "I_" + u.getId());
					jsonArray.add(ju);
				}
			}
		}
		return jsonArray;
	}
	
	/**
	 * 展开节点
	 * @param params
	 * @return
	 * @throws WorkFlowUserSelectorException
	 * @throws OrganizationException
	 */
	public JsonArray loadToselectTreeNodes(JsonObject params) throws WorkFlowUserSelectorException, OrganizationException {
		JsonArray jsonArray = new JsonArray();
		if(!params.has("id") || params.get("id").isJsonNull()){
			throw new WorkFlowUserSelectorException("展开节点时 id 参数为空");
		}
		String idStr = params.get("id").getAsString();
		Long orgId = Long.parseLong(idStr);
		List<Org> orgs = OrganizationService.instance().getOrgList(orgId);
		for(Org org : orgs) {
			jsonArray.add(orgToNode(org).toJson());
		}
		List<User> users = OrganizationService.instance().getUserListByOrgId(orgId);
		for(User user : users) {
			jsonArray.add(userToNode(user).toJson());
		}
		return jsonArray;
	}
	
	/**
	 * 根据用户ID获取用户所在的单位
	 * @param id
	 * @return
	 * @throws OrganizationException
	 */
	public JsonObject getCompanyByUserId(long id) throws OrganizationException {
		User user = OrganizationService.instance().getUserById(id);
		JsonObject rs = orgToNode(OrganizationService.instance().getOrgById(user.getF_company_id())).toJson();
		return rs;
	}

	private CDRUNode userToNode(User u) {
		return new CDRUNode("U_" + u.getId(), "用户", u.getF_caption() + "("
				+ u.getF_name() + ")", true, "icon-sys-user", u.getF_email());
	}

	private CDRUNode orgToNode(Org o) {
		String prefix = "C_";
		String type = "单位";
		if (o.getF_type() == Org.Types.Department.intValue()) {
			prefix = "D_";
			type = "部门";
		}
		return new CDRUNode(prefix + o.getId(), type, o.getF_caption(), false,
				"icon-manager-organizationmanager", o.getF_caption());
	}
}
