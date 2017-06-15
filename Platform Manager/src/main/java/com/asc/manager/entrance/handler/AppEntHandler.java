package com.asc.manager.entrance.handler;

import java.util.List;

import com.asc.bs.entrance.service.ApplicationEntranceManager;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.applicationentrance.entity.ApplicationEntrance;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.applicationentrance.service.ApplicationEntranceService;
import com.asc.manager.appreg.exception.ApplicationRegisterHandlerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * @author lenovo
 *
 */
public class AppEntHandler {
	
	private static AppEntHandler singleton;

	private AppEntHandler() {
	}

	public static AppEntHandler instance() {
		if (singleton == null) {
			singleton = new AppEntHandler();
		}
		return singleton;
	}
	
	/**
	 * 获得集群应用树节点数据
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadApplicationNodes()throws ApplicationEntranceException {
		JsonArray datas = new JsonArray();
		try {
			List<Application> apps = ApplicationService.instance().findApplication();
			for(int i=0;i<apps.size();i++){
				Application app = apps.get(i);
				JsonObject data = new JsonObject();
				data.addProperty("id", app.getId());
				data.addProperty("text", app.getF_caption() + " (" + app.getF_key() + ")");
				data.addProperty("leaf", true);
				data.addProperty("key", app.getF_key());
				data.addProperty("iconCls", "icon-manager-application");
				datas.add(data);
			}
			return datas;
		} catch (ApplicationException e) {
			throw ApplicationEntranceException.forDataServiceError("装载应用节点失败", e);
		}
	}
	
	public JsonArray loadAppEnts() throws ApplicationEntranceException {
		JsonArray datas = new JsonArray();
		try {
			List<ApplicationEntrance> objs = ApplicationEntranceService.instance().findApplicationEntrance();
			if(objs != null){
				for(int i=0;i<objs.size();i++){
					ApplicationEntrance obj = objs.get(i);
					JsonObject data = JsonObjectTool.object2JsonObject(obj);
					datas.add(data);
				}
			}
			return datas;
		} catch (ApplicationEntranceException e) {
			throw ApplicationEntranceException.forDataServiceError("装载应用导航菜单失败", e);
		}
	}
	
	public JsonObject saveAppEnt(JsonObject data) throws ApplicationEntranceException{
		if(data.has("parentId")){
			long parentId = data.get("parentId").getAsLong();
			data.remove("f_parent_id");
			data.addProperty("f_parent_id", parentId);
		}
		ApplicationEntrance appEnt =  ApplicationEntranceManager.instance().saveAppEnt(data);
		return JsonObjectTool.object2JsonObject(appEnt);
	}
	
	public void deleteAppEnt(long id) throws ApplicationEntranceException{
		ApplicationEntranceManager.instance().deleteApplicationNavigator(id);
	}
}
