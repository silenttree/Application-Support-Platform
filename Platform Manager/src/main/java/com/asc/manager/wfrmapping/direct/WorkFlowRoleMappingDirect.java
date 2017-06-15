package com.asc.manager.wfrmapping.direct;

import com.asc.manager.wfrmapping.handler.WorkFlowRoleMappingHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class WorkFlowRoleMappingDirect {
	
	@DirectMethod
	public JsonObject load(long appId, long orgId){
		JsonObject rs = new JsonObject();
		try {
			JsonArray datas = WorkFlowRoleMappingHandler.instance().loadByAppAndOrg(appId, orgId);
			rs.addProperty("success", true);
			rs.add("datas", datas);
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "获取流程权限列表失败");
			e.printStackTrace();
		}
		return rs;
	}
	
	@DirectMethod
	public JsonObject save(int bugFix, JsonArray mrms) {
		JsonObject rs = new JsonObject();
		try {
			WorkFlowRoleMappingHandler.instance().save(mrms);
			rs.addProperty("success", true);
			rs.addProperty("message", "保存流程权限成功");
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "保存流程权限失败");
			e.printStackTrace();
		}
		return rs;
	}
}
