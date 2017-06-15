package com.asc.manager.portalprofile.handler;

import java.util.List;

import com.asc.bs.portalprofile.entity.PortalProfile;
import com.asc.bs.portalprofile.exception.PortalProfileException;
import com.asc.bs.portalprofile.service.PortalProfileManager;
import com.asc.bs.portalprofile.service.PortalProfileService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class PortalProfileHandler {
	
	private static PortalProfileHandler singleton;

	private PortalProfileHandler() {
	}

	public static PortalProfileHandler instance() {
		if (singleton == null) {
			singleton = new PortalProfileHandler();
		}
		return singleton;
	}

	/**
	 * 获取所有的门户模板，返回JsonArray
	 * 
	 * @return
	 * @throws PortalProfileException
	 */
	public JsonArray findAll() throws PortalProfileException {
		JsonArray rs = null;
		try{
			List<PortalProfile> pps = PortalProfileService.instance().findAll();
			rs = JsonObjectTool.objectList2JsonArray(pps);
		}catch(PortalProfileException e){
			throw new PortalProfileException("获取门户模板列表失败", e);
		}
		return rs;
	}

	/**
	 * 保存门户模板类表
	 * @param pps
	 * @throws Exception
	 */
	public void save(JsonObject pps) throws Exception{
		JsonArray updates = pps.get("updates").getAsJsonArray();
		for (int i = 0; i < updates.size(); i++) {
			JsonObject data = updates.get(i).getAsJsonObject();
			PortalProfileManager.instance().save(data);
		}
		JsonArray deletes = pps.get("deletes").getAsJsonArray();
		for (int i = 0; i < deletes.size(); i++) {
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			PortalProfileManager.instance().deleteById(id);
		}
	}

}
