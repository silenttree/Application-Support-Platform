package com.asc.portal.workflowuserselector;

import javax.servlet.http.HttpServletRequest;

import com.asc.cas.server.CentralAuthenticationService;
import com.asc.commons.organization.entity.User;
import com.asc.commons.organization.exception.OrganizationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class WorkFlowUserSelectorDirect {
	
	@DirectMethod
	public JsonObject getUsersByIds(String ids) {
		JsonObject result = new JsonObject();
		try {
			JsonArray datas = WorkFlowUserSelectorHandler.instance().getUsersByIds(ids);
			result.addProperty("success", true);
			result.add("data", datas);
		} catch (NumberFormatException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据ID字符串获取用户列表失败");
		}
		return result;
	}
	
	/**
	 * 展开备选树节点，传入的参数中必须包含一个id字段，表示要展开的单位的ID
	 * @param params
	 * @return
	 */
	@DirectMethod
	public JsonObject loadToselectTreeNodes(JsonObject params) {
		JsonObject result = new JsonObject();
		try {
			JsonArray datas = WorkFlowUserSelectorHandler.instance().loadToselectTreeNodes(params);
			result.addProperty("success", true);
			result.add("data", datas);
		} catch (WorkFlowUserSelectorException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}  catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构的下属机构和用户失败");
		}
		return result;
	}
	
	@DirectMethod
	public JsonObject getDefaultCompany(HttpServletRequest request){
		JsonObject result = new JsonObject();
		User user = CentralAuthenticationService.instance().getUserInfo(request);
		try {
			JsonObject data = WorkFlowUserSelectorHandler.instance().getCompanyByUserId(user.getId());
			result.addProperty("success", true);
			result.add("data", data);
		} catch (OrganizationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取机构的下属机构和用户失败");
		}
		return result;
	}
	
}
