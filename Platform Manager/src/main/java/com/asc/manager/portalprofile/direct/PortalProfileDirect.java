package com.asc.manager.portalprofile.direct;

import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.manager.portalprofile.handler.PortalProfileHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class PortalProfileDirect {

	/**
	 * 保存门户模板
	 * 
	 * @param pps
	 * @return
	 */
	@DirectMethod
	public JsonObject save(JsonObject pps) {
		JsonObject rs = new JsonObject();
		try {
			PortalProfileHandler.instance().save(pps);
			rs.addProperty("success", true);
		} catch (PortalProfileException e) {
			rs.addProperty("success", false);
			rs.addProperty("message", e.getMessage());
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "保存门户模板失败");
		}
		return rs;
	}

	/**
	 * 获取门户模板类表
	 * 
	 * @return
	 */
	@DirectMethod
	public JsonObject findAll() {
		JsonObject rs = new JsonObject();
		JsonArray datas = null;
		try {
			datas = PortalProfileHandler.instance().findAll();
			rs.add("datas", datas);
			rs.addProperty("success", true);
		} catch (PortalProfileException e) {
			rs.addProperty("success", false);
			rs.addProperty("message", e.getMessage());
		} catch (Exception e) {
			rs.addProperty("success", false);
			rs.addProperty("message", "获取门户模板类表失败");
		}

		return rs;
	}
}
