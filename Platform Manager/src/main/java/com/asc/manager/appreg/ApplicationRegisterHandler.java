package com.asc.manager.appreg;

import java.util.List;

import com.asc.bs.application.service.ApplicationManager;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.entity.ApplicationInstance;
import com.asc.commons.application.entity.Database;
import com.asc.commons.application.entity.Databasessync;
import com.asc.commons.application.entity.DatabasessyncTable;
import com.asc.commons.application.entity.Datasource;
import com.asc.commons.application.entity.Datasourcemapping;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.applicationentrance.exception.ApplicationEntranceException;
import com.asc.commons.context.ContextHolder;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.exception.DbAccessException;
import com.asc.framework.app.ApplicationStatus;
import com.asc.framework.designobject.service.DesignObjectService;
import com.asc.manager.appreg.exception.ApplicationRegisterHandlerException;
import com.asc.manager.webservice.IApplicationAccessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class ApplicationRegisterHandler {
	private static ApplicationRegisterHandler singleton;
	private IApplicationAccessor accessor;
	public ApplicationRegisterHandler(){
		// TODO 将实现类写入配置文件中，从配置文件中读取
		accessor = ContextHolder.instance().getBean("WsApplicationAccessor");
	}
	public static ApplicationRegisterHandler instance() {
		if (singleton == null) {
			singleton = new ApplicationRegisterHandler();
		}
		return singleton;
	}
	/**
	 * 装载应用配置数据
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadApplications() throws ApplicationRegisterHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<Application> apps = ApplicationService.instance().findApplication();
			if(apps != null){
				for(int i=0;i<apps.size();i++){
					Application app = apps.get(i);
					JsonObject data = JsonObjectTool.object2JsonObject(app);
					datas.add(data);
				}
			}
			return datas;
		} catch (ApplicationException e) {
			throw ApplicationRegisterHandlerException.forDataServiceError("装载应用列表失败", e);
		}
	}
	/**
	 * 装载应用集群节点
	 * @param applicationId
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadApplicationInstances(long applicationId) throws ApplicationRegisterHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<ApplicationInstance> appinses = ApplicationService.instance().findApplicationInstance(applicationId);
			if(appinses != null){
				for(int i=0;i<appinses.size();i++){
					ApplicationInstance appins = appinses.get(i);
					JsonObject data = JsonObjectTool.object2JsonObject(appins);
					datas.add(data);
				}
			}
			return datas;
		} catch (ApplicationException e) {
			throw ApplicationRegisterHandlerException.forDataServiceError("装载应用列表失败", e);
		}
	}
	/**
	 * 获得集群应用树节点数据
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadClusterApplicationNodes()throws ApplicationRegisterHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<Application> apps = ApplicationService.instance().findApplication();
			for(int i=0;i<apps.size();i++){
				Application app = apps.get(i);
				if(app.getF_cluster() == 1){
					JsonObject data = new JsonObject();
					data.addProperty("id", app.getId());
					data.addProperty("text", app.getF_caption() + " (" + app.getF_key() + ")");
					data.addProperty("leaf", true);
					data.addProperty("iconCls", "icon-manager-application");
					datas.add(data);
				}
			}
			return datas;
		} catch (ApplicationException e) {
			throw ApplicationRegisterHandlerException.forDataServiceError("装载集群应用列表失败", e);
		}
	}
	/**
	 * 保存应用对象
	 * @param data
	 * @return
	 */
	public JsonObject saveApplication(JsonObject data) throws ApplicationRegisterHandlerException{
		try {
			Application app = ApplicationManager.instance().saveApplication(data);
			return JsonObjectTool.object2JsonObject(app);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("保存应用对象失败：" + data.toString(), e);
		}
	}
	/**
	 * 保存应用对象
	 * @param data
	 * @return
	 */
	public JsonObject saveApplicationInstance(long applicationId, JsonObject data) throws ApplicationRegisterHandlerException{
		try {
			ApplicationInstance appins = ApplicationManager.instance().saveApplicationInstance(applicationId, data);
			return JsonObjectTool.object2JsonObject(appins);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("保存应用实例对象失败：" + data.toString(), e);
		}
	}
	/**
	 * 删除应用对象
	 * @param id
	 * @throws ApplicationRegisterHandlerException
	 */
	public void deleteApplication(long id) throws ApplicationRegisterHandlerException{
		try {
			ApplicationManager.instance().deleteApplicate(id);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("删除应用对象失败：" + id, e);
		}
	}
	/**
	 * 删除应用实例
	 * @param id
	 * @throws ApplicationRegisterHandlerException
	 */
	public void deleteApplicationInstance(long id) throws ApplicationRegisterHandlerException{
		try {
			ApplicationManager.instance().deleteApplicationInstance(id);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("删除应用实例对象失败：" + id, e);
		}
	}
	/**
	 * 更新节点排序号
	 * @param applicationId
	 */
	public void updateApplicationInstanceSerailNumber(long applicationId) throws ApplicationRegisterHandlerException{
		try {
			ApplicationManager.instance().updateApplicationInstanceSerialNumber(applicationId);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("更新节点排序号：" + applicationId, e);
		}
	}

	/**
	 * 保存数据库实例
	 * 
	 * @param applicationId
	 * @param data
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonObject saveDatabase(long applicationId, JsonObject data) throws ApplicationRegisterHandlerException {
		try {
			Database database = ApplicationManager.instance().saveDatabase(applicationId, data);
			return JsonObjectTool.object2JsonObject(database);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("数据库实例保存失败。");
		}
	}

	/**
	 * 删除数据库实例
	 * 
	 * @param id
	 */
	public void deleteDatabase(long id) throws ApplicationRegisterHandlerException {
		try {
			ApplicationManager.instance().deleteDatabase(id);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("数据库实例删除失败。");
		}
	}

	/**
	 * 获取数据库实例列表
	 * @param appId
	 * @return
	 */
	public JsonArray getDatabaseList(String location) throws ApplicationRegisterHandlerException {
		try {
			List<Database> databaseList = ApplicationService.instance().getDatabaseList(location);
			return JsonObjectTool.objectList2JsonArray(databaseList);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("数据库实例列表获取失败。");
		}
	}

	public JsonArray loadApplicationNodes()throws ApplicationEntranceException {
		JsonArray datas = new JsonArray();
		try {
			List<Application> apps = ApplicationService.instance().findApplication();
			for(int i=0;i<apps.size();i++){
				Application app = apps.get(i);
				JsonObject data = new JsonObject();
				data.addProperty("id", app.getId());
				data.addProperty("text", app.getF_caption() + " (" + app.getF_key() + ")");
				data.addProperty("leaf", false);
				data.addProperty("key", app.getF_key());
				data.addProperty("iconCls", "icon-manager-application");
				datas.add(data);
			}
			return datas;
		} catch (ApplicationException e) {
			throw ApplicationEntranceException.forDataServiceError("装载应用节点失败", e);
		}
	}
	
	/**
	 * 装载应用数据源树节点
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadApplicationDatasourceNodes(long appId) throws ApplicationRegisterHandlerException {
		JsonArray datas = new JsonArray();
		//远端调用应用的数据源列表
		JsonArray ds;
		try {
			ds = DesignObjectService.instance().findObjects(this.getServiceUrl(appId), null, "datasource");
		} catch (DesignObjectException e) {
			throw new ApplicationRegisterHandlerException("远程获取应用数据源失败！");
		}
		for(int j = 0; j < ds.size(); j++){
			JsonObject child = ds.get(j).getAsJsonObject();
			JsonObject jsob = new JsonObject();
			//查找本地数据源
			Datasource datasource;
			try {
				datasource = ApplicationService.instance().getDatasourceByKey(child.get("f_key").getAsString());
			} catch (ApplicationException e) {
				throw new ApplicationRegisterHandlerException("本地数据源查找失败！");
			}
			if(datasource == null){
				jsob.addProperty("id", child.get("id").getAsString() + appId);
				jsob.addProperty("text", child.get("f_caption").getAsString());
				jsob.addProperty("leaf", true);
				jsob.addProperty("appid", appId);
				jsob.addProperty("key", child.get("f_key").getAsString());
				jsob.addProperty("iconCls", "icon-manager-datasource");
			}else{
				jsob.addProperty("id", datasource.getF_key() + appId);
				jsob.addProperty("text", datasource.getF_caption());
				jsob.addProperty("leaf", true);
				jsob.addProperty("appid", appId);
				jsob.addProperty("key", datasource.getF_key());
				jsob.addProperty("route", datasource.getF_route_rule());
				jsob.addProperty("iconCls", "icon-manager-datasource");
			}
			JsonObject properties = null;
			if(child.has("f_properties") && !child.get("f_properties").isJsonNull()){
				properties = child.get("f_properties").getAsJsonObject();
			}
			if(properties != null && properties.has("params")){
				jsob.add("params", properties.get("param"));
			}
			datas.add(jsob);
		}
		return datas;
	}

	/**
	 * 添加数据库实例配置
	 * 
	 * @param datasource
	 * @param database
	 * @throws ApplicationException 
	 */
	public void addDatabasesconf(JsonObject datasource, JsonObject database) throws ApplicationException {
		ApplicationManager.instance().addDatabasesconf(datasource, database);
	}

	/**
	 * 加载数据库实例配置列表
	 * 
	 * @return
	 * @throws ApplicationException 
	 */
	public JsonArray loadDatabasesconf(String key) throws ApplicationException {
		JsonArray datas = new JsonArray();
		List<Datasourcemapping> dsmList = ApplicationService.instance().findDsmList(key);
		if(dsmList != null){
			for(int i = 0; i < dsmList.size(); i++){
				Datasourcemapping dsm = dsmList.get(i);
				JsonObject json = new JsonObject();
				Datasource datasource = ApplicationService.instance().getDatasourceById(dsm.getF_datasource_id());
				Database database = ApplicationService.instance().getDatabaseById(dsm.getF_database_id());
				json.addProperty("id", dsm.getId());
				json.addProperty("f_caption", datasource.getF_caption());
				json.addProperty("f_databases_caption", database.getF_caption());
				json.addProperty("f_type", database.getF_type());
				json.addProperty("f_port", database.getF_port());
				json.addProperty("f_ip", database.getF_ip());
				json.addProperty("f_url", database.getF_url());
				json.addProperty("f_location", database.getF_location());
				json.addProperty("f_user", dsm.getF_user());
				json.addProperty("f_password", dsm.getF_password());
				datas.add(json);
			}
		}
		return datas;
	}

	/**
	 * 脚本保存
	 * 
	 * @param key
	 * @param script
	 * @throws ApplicationException
	 */
	public void addDatasourceScript(String key, String script) throws ApplicationException {
		Datasource datasource = ApplicationService.instance().getDatasourceByKey(key);
		if(datasource != null){
			datasource.setF_route_rule(script);
			IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
			try {
				datasource.save();
				tx.commit();
			} catch (DbAccessException e) {
				tx.rollback();
				throw new ApplicationException("脚本保存失败。");
			} finally {
				CommonDatabaseAccess.instance().endTransaction();
			}
		}
	}

	public JsonObject saveDatasourcemapping(long dsmId, JsonObject data) throws ApplicationException {
		Datasourcemapping dsm = ApplicationService.instance().getDatasourcemappingById(dsmId);
		if(data.has("f_user")){
			dsm.setF_user(data.get("f_user").getAsString());
		}

		if(data.has("f_password")){
			dsm.setF_password(data.get("f_password").getAsString());
		}
		try {
			dsm.save();
		} catch (DbAccessException e) {
			throw new ApplicationException("数据库实例配置用户密码保存失败！");
		}
		return JsonObjectTool.object2JsonObject(dsm);
	}

	/**
	 * 删除数据库实例配置
	 * 
	 * @param id
	 * @throws ApplicationException 
	 */
	public void deleteDatasourcemapping(long id) throws ApplicationRegisterHandlerException {
		try {
			ApplicationManager.instance().deleteDatasourcemapping(id);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("数据库实例配置删除失败。");
		}
	}

	/**
	 * 添加内外网交换配置
	 * 
	 * @param selectOne
	 * @param selectTwo
	 * @throws ApplicationRegisterHandlerException
	 */
	public void addDatabasesExchange(JsonObject selectOne, JsonObject selectTwo) throws ApplicationRegisterHandlerException {
		try {
			ApplicationManager.instance().addDatabasesExchange(selectOne, selectTwo);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("添加内外网交换失败:" + e.getMessage());
		}
	}

	/**
	 * 装载内外网交换配置列表
	 * 
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadDatabasessync() throws ApplicationRegisterHandlerException {
		JsonArray datas = new JsonArray();
		//查找内外网交换
		CommonDatabaseAccess.instance().beginTransaction();
		List<Databasessync> dbsList = null;
		try {
			String sql = "select * from " + CommonDatabaseAccess.getTableName(Databasessync.class);
			dbsList = getDbAccess().listObjects(sql, null, Databasessync.class, 0, 0);
		} catch (DbAccessException e) {
			throw new ApplicationRegisterHandlerException("内外网交换配置查找失败。");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}

		//添加到视图
		if(dbsList != null){
			for(int i = 0; i < dbsList.size(); i++){
				Databasessync dbs = dbsList.get(i);
				try {
					//Datasource innerds = ApplicationService.instance().getDatasourceById(dbs.getF_inner_database_id());
					//Datasource outerds = ApplicationService.instance().getDatasourceById(dbs.getF_outer_database_id());
					Database innerDB = ApplicationService.instance().getDatabaseById(dbs.getF_inner_database_id());
					Database outerDB = ApplicationService.instance().getDatabaseById(dbs.getF_outer_database_id());
					JsonObject data = new JsonObject();
					data.addProperty("id", dbs.getId());
					data.addProperty("f_inner_database", innerDB.getF_caption());
					data.addProperty("f_outer_database", outerDB.getF_caption());
					data.addProperty("f_inner_ip", dbs.getF_inner_ip());
					data.addProperty("f_outer_ip", dbs.getF_outer_ip());
					data.addProperty("f_synchronou_strategy", dbs.getF_synchronou_strategy());
					datas.add(data);
				} catch (ApplicationException e) {
					throw new ApplicationRegisterHandlerException("数据库实例读取失败。");
				}
			}
		}
		return datas;
	}

	/**
	 * 保存内外网交换
	 * 
	 * @param dbsId
	 * @param data
	 * @return
	 */
	public JsonObject saveDatabasessync(long dbsId, JsonObject data) throws ApplicationRegisterHandlerException {
		try {
			Databasessync dbs = ApplicationManager.instance().saveDatabasessync(dbsId, data);
			return JsonObjectTool.object2JsonObject(dbs);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("内外网交换保存失败！");
		}
	}

	/**
	 * 删除内外网交换
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteDatabasessync(long id) throws ApplicationRegisterHandlerException {
		try {
			ApplicationManager.instance().deleteDatabasessync(id);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("内外网交换配置删除失败！");
		}
	}

	/**
	 * 保存内外网交换数据表
	 * 
	 * @param tablename
	 * @return
	 * @throws ApplicationException
	 */
	public JsonObject saveSyncTable(long syncId, String tablename) throws ApplicationException {
		DatabasessyncTable dbst = ApplicationManager.instance().saveSyncTable(syncId, tablename);
		return JsonObjectTool.object2JsonObject(dbst);
	}

	/**
	 * 删除内外网交换数据表
	 * 
	 * @param syncId
	 * @throws ApplicationException 
	 */
	public void deleteSyncTable(long syncId) throws ApplicationException {
		ApplicationManager.instance().deleteSyncTable(syncId);
	}
	/**
	 * 加载内外网交换数据表
	 * 
	 * @param syncId
	 * @return
	 * @throws ApplicationException
	 */
	public JsonArray loadDataSyncTable(long syncId) throws ApplicationRegisterHandlerException {
		try {
			List<DatabasessyncTable> syncTable = ApplicationService.instance().loadDataSyncTable(syncId);
			return JsonObjectTool.objectList2JsonArray(syncTable);
		} catch (ApplicationException e) {
			throw new ApplicationRegisterHandlerException("内外网交换数据表加载失败！");
		}
	}

	/**
	 * 根据应用ID获取应用的url，
	 * 如果应用的ID不存在，返回null
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

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}
	
	/**
	 * 装载应用配置数据
	 * @return
	 * @throws ApplicationRegisterHandlerException
	 */
	public JsonArray loadApplicationsStates() throws ApplicationRegisterHandlerException {
		JsonArray datas = new JsonArray();
		try {
			List<Application> apps = ApplicationService.instance().findApplication();
			if(apps != null){
				for(int i=0;i<apps.size();i++){
					Application app = apps.get(i);
					if(!app.isAvailable()){
						continue;
					}
					String url = app.getF_domain() + "/services/ApplicationControlService?wsdl";
					try {
						JsonObject result = accessor.loadApplications(app.getId(), url);
						ApplicationStatus state = ApplicationStatus.valueOf(result.get("data").getAsString());
						switch (state) {
						case Unknown:
							app.setF_state(4);
							break;
						case Loading:
							app.setF_state(0);
							break;
						case Running:
							app.setF_state(2);
							break;
						case Suspended:
							app.setF_state(3);
							break;
						case Stopped:
							app.setF_state(1);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						//continue;
						//e.printStackTrace();
						app.setF_state(4);
						//throw new ApplicationRegisterHandlerException("加载应用系统【" + app.getF_caption() + "】状态时失败！");
					}
					JsonObject data = JsonObjectTool.object2JsonObject(app);
					datas.add(data);
				}
			}
			return datas;
		} catch (ApplicationException e) {
			throw ApplicationRegisterHandlerException.forDataServiceError("装载应用列表和状态失败", e);
		}
	}
	
	/**
	 * 更新应用系统的运行状态
	 * @param appId
	 * @param state
	 */
	public void updateAppState(long appId, String appPath, int state) throws Exception {
		String url = appPath + "/services/ApplicationControlService?wsdl";
		accessor.updateAppState(appId, url, state);
	}
}
