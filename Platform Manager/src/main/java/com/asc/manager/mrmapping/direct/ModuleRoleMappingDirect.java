package com.asc.manager.mrmapping.direct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.commons.modulerolemapping.exception.ModuleRoleMappingException;
import com.asc.manager.mrmapping.handler.ModuleRoleMappingHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class ModuleRoleMappingDirect {
	
	private Log log = LogFactory.getLog(ModuleRoleMappingDirect.class);
	
	@DirectMethod
	public JsonObject load(long appId, long orgId){
		JsonObject rs = new JsonObject();
		try {
			JsonArray datas = ModuleRoleMappingHandler.instance().loadByAppAndOrg(appId, orgId);
			rs.addProperty("success", true);
			rs.add("datas", datas);
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "获取模块权限列表失败");
			log.error("获取模块权限列表失败", e);
		}
		return rs;
	}
	
	@DirectMethod
	public JsonObject save(int bugFix, JsonArray mrms) {
		JsonObject rs = new JsonObject();
		try {
			ModuleRoleMappingHandler.instance().save(mrms);
			rs.addProperty("success", true);
			rs.addProperty("message", "保存模块权限成功");
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "保存模块权限失败");
			log.error("保存模块权限失败", e);
		}
		return rs;
	}
	
	@DirectMethod
	public JsonObject loadByCDRUAndAppId(String key, long appId, boolean isAll) {
		JsonObject rs = new JsonObject();
		try {
			JsonArray datas = ModuleRoleMappingHandler.instance().loadByCDRUAndAppId(key, appId, isAll);
			rs.add("datas", datas);
			rs.addProperty("success", false);
		} catch (ModuleRoleMappingException e) {
			rs.addProperty("success", false);
			rs.addProperty("message", e.getMessage());
			log.trace("获取模块权限列表失败", e);
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "获取模块权限列表失败");
			log.trace("获取模块权限列表失败", e);
		}
		return rs;
	}
	
	@DirectMethod
	public JsonObject save2(int bugFix, JsonArray datas) {
		JsonObject rs = new JsonObject();
		try {
			System.out.println(datas);
			ModuleRoleMappingHandler.instance().save2(datas);
			rs.addProperty("success", true);
			rs.addProperty("message", "保存模块权限成功");
		} catch (ModuleRoleMappingException e) {
			rs.addProperty("success", false);
			rs.addProperty("message", e.getMessage());
			log.error("保存模块权限失败", e);
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "保存模块权限失败");
			log.error("保存模块权限失败", e);
		}
		return rs;
	}
}
