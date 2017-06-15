package com.asc.manager.entrance.direct;

import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.engine.direct.DirectList;
import com.asc.manager.entrance.handler.AppEntHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class AppEntDirect {

	@DirectMethod
	public JsonObject loadAppNavs() {
		DirectList directList = new DirectList();
		try {
			JsonArray objects = AppEntHandler.instance().loadAppEnts();
			directList.setDatas(objects);
			directList.setSuccess(true);
		} catch (ApplicationEntranceException e) {
			e.printStackTrace();
			directList.setSuccess(false);
			directList.setMessage(e.getMessage());
		}
		return directList.getOutput();
	}

	@DirectMethod
	public JsonArray loadAppEntranceTree(JsonObject params) {
		JsonArray datas = new JsonArray();
		try {
			JsonArray objects = AppEntHandler.instance().loadAppEnts();
			// 所有的几点都设置为叶子节点
			for (JsonElement el : objects) {
				JsonObject jo = el.getAsJsonObject();
				jo.addProperty("leaf", true);
			}

			// 把所有的菜单都加入到它的父菜单中
			for (JsonElement el : objects) {
				JsonObject jo = el.getAsJsonObject();
				if (jo.get("f_level").getAsInt() != 1 && jo.has("f_parent_id")) {
					JsonObject pjo = null;
					// 找到父节点
					for (JsonElement pel : objects) {
						if (pel.getAsJsonObject().get("id").getAsLong() == jo
								.get("f_parent_id").getAsLong()) {
							pjo = pel.getAsJsonObject();
							break;
						}
					}
					if (pjo != null) {
						// 找到父节点的children成员
						JsonArray children = null;
						if (pjo.has("children")) {
							children = pjo.get("children").getAsJsonArray();
						} else {
							children = new JsonArray();
							pjo.addProperty("expanded", true);
							pjo.remove("leaf");
							pjo.add("children", children);
						}

						// 把该节点加入到父节点的children当中
						children.add(jo);
					}

				}
			}

			// 将第一层的菜单加入到结果集当中
			for (JsonElement el : objects) {
				JsonObject jo = el.getAsJsonObject();
				if (jo.get("f_level").getAsInt() == 1) {
					datas.add(jo);
				}
			}

		} catch (ApplicationEntranceException e) {
			e.printStackTrace();
		}

		return datas;
	}

	@DirectMethod
	public JsonObject loadApplicationNodes(JsonObject node) {
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = AppEntHandler.instance().loadApplicationNodes();
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	@DirectMethod
	public JsonObject SaveAppEnts(JsonObject appEnts) {
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = appEnts.get("updates").getAsJsonArray();
		for (int i = 0; i < updates.size(); i++) {
			JsonObject data = updates.get(i).getAsJsonObject();
			try {
				JsonObject object = AppEntHandler.instance().saveAppEnt(data);
				sObjects.add(object);
			} catch (ApplicationEntranceException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		JsonArray deletes = appEnts.get("deletes").getAsJsonArray();
		for (int i = 0; i < deletes.size(); i++) {
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				AppEntHandler.instance().deleteAppEnt(id);
				sObjects.add(data);
			} catch (ApplicationEntranceException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}

	@DirectMethod
	public JsonObject saveAppEntAuths(JsonObject appEnts) {
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = appEnts.get("updates").getAsJsonArray();
		for (int i = 0; i < updates.size(); i++) {
			JsonObject data = updates.get(i).getAsJsonObject();
			try {
				JsonObject object = AppEntHandler.instance().saveAppEnt(data);
				sObjects.add(object);
			} catch (ApplicationEntranceException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}

		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}

}
