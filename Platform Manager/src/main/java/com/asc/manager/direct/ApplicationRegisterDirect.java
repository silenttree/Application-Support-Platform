package com.asc.manager.direct;

import javax.servlet.http.HttpServletRequest;

import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.engine.direct.DirectList;
import com.asc.manager.appreg.ApplicationRegisterHandler;
import com.asc.manager.appreg.exception.ApplicationRegisterHandlerException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class ApplicationRegisterDirect {
	/**
	 * 装载应用列表
	 * @return
	 */
	@DirectMethod
	public JsonObject loadApplications(HttpServletRequest request){
		DirectList directList = new DirectList();
		try {
			JsonArray objects = ApplicationRegisterHandler.instance().loadApplications();
			directList.setDatas(objects);
			directList.setSuccess(true);
		} catch (ApplicationRegisterHandlerException e) {
			e.printStackTrace();
			directList.setSuccess(false);
			directList.setMessage(e.getMessage());
		}
		return directList.getOutput();
	}
	/**
	 * 批量保存应用对象
	 * @param datas
	 * @return
	 */
	// TODO 直接传JSONARRAY参数接收错误
	@DirectMethod
	public JsonObject saveApplications(JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			try {
				JsonObject object = ApplicationRegisterHandler.instance().saveApplication(data);
				sObjects.add(object);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				ApplicationRegisterHandler.instance().deleteApplication(id);
				sObjects.add(data);
			} catch (ApplicationRegisterHandlerException e) {
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
	/**
	 * 装载集群应用树节点
	 * @param node
	 * @return
	 */
	@DirectMethod
	public JsonObject loadClusterApplicationNodes(JsonObject node){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = ApplicationRegisterHandler.instance().loadClusterApplicationNodes();
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 装载应用集群节点对象列表
	 * @param applicationId
	 * @return
	 */
	@DirectMethod
	public JsonObject loadApplicationInstances(long applicationId){
		DirectList directList = new DirectList();
		try {
			JsonArray objects = ApplicationRegisterHandler.instance().loadApplicationInstances(applicationId);
			directList.setDatas(objects);
			directList.setSuccess(true);
		} catch (ApplicationRegisterHandlerException e) {
			e.printStackTrace();
			directList.setSuccess(false);
			directList.setMessage(e.getMessage());
		}
		return directList.getOutput();
	}
	/**
	 * 批量保存应用对象
	 * @param datas
	 * @return
	 */
	// TODO 直接传JSONARRAY参数接收错误
	@DirectMethod
	public JsonObject saveApplicationInstances(long applicationId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			try {
				JsonObject object = ApplicationRegisterHandler.instance().saveApplicationInstance(applicationId, data);
				sObjects.add(object);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				ApplicationRegisterHandler.instance().deleteApplication(id);
				sObjects.add(data);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		if(deletes.size() > 0){
			try {
				ApplicationRegisterHandler.instance().updateApplicationInstanceSerailNumber(applicationId);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
			}
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
	
	/**
	 * 批量保存数据库实例对象
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveDatabase(long applicationId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			try {
				JsonObject object = ApplicationRegisterHandler.instance().saveDatabase(applicationId, data);
				sObjects.add(object);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				ApplicationRegisterHandler.instance().deleteDatabase(id);
				sObjects.add(data);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(data);
				message = message + "<br>" + e.getMessage();
			}
		}
		if(deletes.size() > 0){
			try {
				ApplicationRegisterHandler.instance().updateApplicationInstanceSerailNumber(applicationId);
			} catch (ApplicationRegisterHandlerException e) {
				e.printStackTrace();
			}
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
	
	/**
	 * 获取数据库实例列表
	 * 
	 * @param appId
	 * @return
	 */
	@DirectMethod
	public JsonObject getDatabaseList(String localhost){
		JsonObject json = new JsonObject();
		String message = "";
		try {
			JsonArray datas = ApplicationRegisterHandler.instance().getDatabaseList(localhost);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (ApplicationRegisterHandlerException e) {
			json.addProperty("success", false);
			json.addProperty("message", message);
		}
		return json;
	}
	
	/**
	 * 装载应用数据源树节点
	 * @param node
	 * @return
	 */
	@DirectMethod
	public JsonArray loadApplicationDatasourceNodes(JsonObject node){
		JsonObject json = new JsonObject();
		JsonArray datas = new JsonArray();
		try {
			if(node.get("node").getAsInt() == 0){
				datas = ApplicationRegisterHandler.instance().loadApplicationNodes();
			}else{
				datas = ApplicationRegisterHandler.instance().loadApplicationDatasourceNodes(node.get("node").getAsLong());
			}
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return datas;
	}
	
	/**
	 * 装载数据库实例配置
	 * @param node
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDatabasesconf(String key){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = ApplicationRegisterHandler.instance().loadDatabasesconf(key);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 添加数据库实例配置
	 * 
	 * @param datasource
	 * @param database
	 * @return
	 */
	@DirectMethod
	public JsonObject addDatabasesconf(JsonObject datasource, JsonObject database){
		JsonObject json = new JsonObject();
		try {
			ApplicationRegisterHandler.instance().addDatabasesconf(datasource, database);
			json.addProperty("success", true);
		} catch (ApplicationException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 脚本保存
	 * 
	 * @param key
	 * @param script
	 * @return
	 */
	@DirectMethod
	public JsonObject addDatasourceScript(String key, String script){
		JsonObject json = new JsonObject();
		try {
			ApplicationRegisterHandler.instance().addDatasourceScript(key, script);
			json.addProperty("success", true);
		} catch (ApplicationException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}

	/**
	 * 保存数据库实例配置映射
	 * 
	 * @param dsmId
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveDatasourcemapping(long dsmId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = ApplicationRegisterHandler.instance().saveDatasourcemapping(dsmId, data);
				sObjects.add(object);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				ApplicationRegisterHandler.instance().deleteDatasourcemapping(id);
				sObjects.add(data);
			} catch (ApplicationRegisterHandlerException e) {
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
	
	/**
	 * 添加数据库实例配置
	 * 
	 * @param datasource
	 * @param database
	 * @return
	 */
	@DirectMethod
	public JsonObject addDatabasesExchange(JsonObject selectOne, JsonObject selectTwo){
		JsonObject json = new JsonObject();
		try {
			ApplicationRegisterHandler.instance().addDatabasesExchange(selectOne, selectTwo);
			json.addProperty("success", true);
		} catch (ApplicationRegisterHandlerException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 装载内外网交换配置
	 * @param node
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDatabasessync(){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = ApplicationRegisterHandler.instance().loadDatabasessync();
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 内外网交换配置
	 * 
	 * @param dsmId
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveDatabasessync(long dbsId, JsonObject datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonArray fObjects = new JsonArray();
		String message = "";
		JsonArray updates = datas.get("updates").getAsJsonArray();
		for(int i=0;i<updates.size();i++){
			JsonObject data = updates.get(i).getAsJsonObject();
			JsonObject object;
			try {
				object = ApplicationRegisterHandler.instance().saveDatabasessync(dbsId, data);
				sObjects.add(object);
			} catch (ApplicationRegisterHandlerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JsonArray deletes = datas.get("deletes").getAsJsonArray();
		for(int i=0;i<deletes.size();i++){
			JsonObject data = deletes.get(i).getAsJsonObject();
			long id = data.get("id").getAsLong();
			try {
				ApplicationRegisterHandler.instance().deleteDatabasessync(id);
				sObjects.add(data);
			} catch (ApplicationRegisterHandlerException e) {
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
	
	/**
	 * 保存内外网交换配置表
	 * 
	 * @param syncId
	 * @param tablename
	 * @return
	 */
	@DirectMethod
	public JsonObject saveSyncTable(long syncId, String tablename){
		JsonObject json = new JsonObject();
		try {
			ApplicationRegisterHandler.instance().saveSyncTable(syncId, tablename);
			json.addProperty("success", true);
		} catch (ApplicationException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 删除内外网交换数据表
	 * 
	 * @param syncId
	 * @return
	 */
	@DirectMethod
	public JsonObject deleteSyncTable(long syncId){
		JsonObject json = new JsonObject();
		try {
			ApplicationRegisterHandler.instance().deleteSyncTable(syncId);
			json.addProperty("success", true);
		} catch (ApplicationException e) {
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	
	/**
	 * 装载内外网交换表
	 * @param node
	 * @return
	 */
	@DirectMethod
	public JsonObject loadDataSyncTable(long syncId){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas = ApplicationRegisterHandler.instance().loadDataSyncTable(syncId);
			json.add("datas", datas);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载应用列表
	 * 状态从远程webservice取
	 * @return
	 */
	@DirectMethod
	public JsonObject loadApplicationsState(HttpServletRequest request){
		DirectList directList = new DirectList();
		try {
			JsonArray objects = ApplicationRegisterHandler.instance().loadApplicationsStates();
			directList.setDatas(objects);
			directList.setSuccess(true);
		} catch (ApplicationRegisterHandlerException e) {
			e.printStackTrace();
			directList.setSuccess(false);
			directList.setMessage(e.getMessage());
		}
		return directList.getOutput();
	}
	
	/**
	 * 更新应用系统的状态
	 * @param appId 注册的应用系统的id
	 * @param state 要更新的状态
	 * @return
	 */
	@DirectMethod
	public JsonObject updateAppState(long appId, String appPath, int state){
		JsonObject result = new JsonObject();
		try {
			ApplicationRegisterHandler.instance().updateAppState(appId, appPath, state);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
}
