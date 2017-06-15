package com.asc.bs.application.service;

import java.util.Date;
import java.util.List;

import com.asc.bs.application.access.imp.DbacApplicationWriterImp;
import com.asc.commons.application.access.IApplicationWriter;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.entity.ApplicationInstance;
import com.asc.commons.application.entity.Database;
import com.asc.commons.application.entity.Databasessync;
import com.asc.commons.application.entity.DatabasessyncTable;
import com.asc.commons.application.entity.Datasource;
import com.asc.commons.application.entity.Datasourcemapping;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.dbac.IDbacTransaction;
import com.asc.commons.exception.DbAccessException;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;
/**
 * 应用管理类
 * @author zhangchang
 *
 */
public class ApplicationManager {
	private static ApplicationManager singleton;
	private IApplicationWriter writer;

	public ApplicationManager() {
		// TODO 将实现类写入配置文件中，从配置文件中读取
		writer = new DbacApplicationWriterImp();
	}

	public static ApplicationManager instance() {
		if (singleton == null) {
			singleton = new ApplicationManager();
		}
		return singleton;
	}

	/**
	 * 保存应用对象
	 * 
	 * @param data
	 * @return
	 * @throws ApplicationException
	 */
	public Application saveApplication(JsonObject data) throws ApplicationException {
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		Application app = null;
		// 创建标记
		boolean isCreate = false;
		if (id > 0) {
			app = ApplicationService.instance().getApplicationById(id);
			if (app == null) {
				isCreate = true;
				app = new Application();
			}
		} else {
			isCreate = true;
			app = new Application();
		}
		// 写入对象数据
		JsonObjectTool.jsonObject2Object(data, app);
		if (isCreate) {
			app.setF_create_time(new Date());
		}
		app.setF_update_time(new Date());
		// 数据校验
//		if (data.has("f_key")) {
//			String key = data.get("f_key").getAsString();
//			// 校验键值冲突
//			Application appFind = ApplicationService.instance().getApplication(data.get("f_key")
//					.getAsString());
//			if (appFind != null && appFind.getId() != id) {
//				throw ApplicationException.forKeyIllegal(appFind.getId(), key);
//			}
//		}
		saveApplication(app);
		return app;
	}
	
	/**
	 * 保存应用对象
	 * @param app
	 * @return
	 * @throws ApplicationException
	 */
	public void saveApplication(Application app) throws ApplicationException{
		writer.saveApplication(app);
	}

	/**
	 * 保存应用实例对象
	 * 
	 * @param applicationId
	 * @param data
	 * @return
	 */
	public ApplicationInstance saveApplicationInstance(long applicationId, JsonObject data) throws ApplicationException {
		// 判断父对象是否存在
		Application app = ApplicationService.instance().getApplicationById(applicationId);
		if (app == null) {
			ApplicationException.forObectNotFound(Application.class.getName(),
					applicationId);
		}
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		ApplicationInstance appins = null;
		// 创建标记
		boolean isCreate = false;
		if (id > 0) {
			appins = ApplicationService.instance().getApplicationInstance(id);
			if (appins == null) {
				isCreate = true;
				appins = new ApplicationInstance();
			}
		} else {
			isCreate = true;
			appins = new ApplicationInstance();
			appins.setF_application_id(app.getId());
			// 初始化节点序号
			long instanceCount = ApplicationService.instance().getApplicationInstanceCount(applicationId);
			appins.setF_serialnumber(instanceCount + 1);
		}
		// 写入对象数据
		JsonObjectTool.jsonObject2Object(data, appins);
		if (isCreate) {
			appins.setF_create_time(new Date());
		}
		appins.setF_update_time(new Date());
		// 数据校验
		if (data.has("f_key")) {
			String key = data.get("f_key").getAsString();
			// 校验键值冲突
			ApplicationInstance appFind = ApplicationService.instance().getApplicationInstance(data
					.get("f_key").getAsString());
			if (appFind != null && appFind.getId() != id) {
				throw ApplicationException.forKeyIllegal(appFind.getId(), key);
			}
		}
		saveApplicationInstance(appins);
		return appins;
	}
	
	/**
	 * 保存应用实例对象
	 * @param appins
	 * @return
	 * @throws ApplicationException
	 */
	public void saveApplicationInstance(ApplicationInstance appins) throws ApplicationException{
		writer.saveApplicationInstance(appins);
	}

	/**
	 * 删除应用对象
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteApplicate(long id) throws ApplicationException {
		Application app = ApplicationService.instance().getApplicationById(id);
		if (app == null) {
			throw ApplicationException.forObectNotFound(
					Application.class.getName(), id);
		} else {
			// 删除应用对象
			writer.deleteApplication(app);
		}
	}

	/**
	 * 删除应用实例
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteApplicationInstance(long id) throws ApplicationException {
		ApplicationInstance appins = ApplicationService.instance().getApplicationInstance(id);
		if (appins == null) {
			throw ApplicationException.forObectNotFound(
					ApplicationInstance.class.getName(), id);
		} else {
			// 删除应用对象
			writer.deleteApplicationInstance(appins);
		}
	}

	/**
	 * 更新应用节点排序号
	 * 
	 * @param applicationId
	 * @throws ApplicationException
	 */
	public void updateApplicationInstanceSerialNumber(long applicationId)
			throws ApplicationException {
		List<ApplicationInstance> appinses = ApplicationService.instance()
				.findApplicationInstance(applicationId);
		if(appinses != null){
			updateApplicationInstanceSerialNumber(appinses);
		}
	}

	/**
	 * 更新应用节点排序号
	 * 
	 * @param appinses
	 * @throws ApplicationException
	 */
	private void updateApplicationInstanceSerialNumber(
			List<ApplicationInstance> appinses) throws ApplicationException {
		if (appinses != null) {
			for (int i = 0; i < appinses.size(); i++) {
				ApplicationInstance appins = appinses.get(i);
				appins.setF_serialnumber(i + 1);
				writer.saveApplicationInstance(appins);
			}
		}
	}

	/**
	 * 保存数据库实例
	 * @param applicationId
	 * @param data
	 * @return
	 * @throws ApplicationException
	 */
	public Database saveDatabase(long applicationId, JsonObject data) throws ApplicationException {
		Database database = null;
		long id = 0;
		if(data.has("id")){
			id = data.get("id").getAsLong();
		}
		boolean isCreate = false;
		if(id > 0){
			database = ApplicationService.instance().getDatabaseById(id);
			if(database == null){
				isCreate = true;
				database = new Database();
			}
		}else{
			isCreate = true;
			database = new Database();
		}

		JsonObjectTool.jsonObject2Object(data, database);
		if(isCreate){
			database.setF_create_time(new Date());
		}
		database.setF_update_time(new Date());
		writer.saveDatabase(database);
		return database;
	}

	/**
	 * 删除数据库实例
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteDatabase(long id) throws ApplicationException {
		writer.deleteDatabase(id);
	}

	/**
	 * 添加数据库实例配置
	 * 
	 * @param datasource
	 * @param database
	 * @throws DbAccessException 
	 */
	public void addDatabasesconf(JsonObject datasource, JsonObject database) throws ApplicationException {
		Datasource ds = null;
		if(datasource.has("id")){
			//判断是否已存在数据源
			ds = ApplicationService.instance().getDatasourceByKey(datasource.get("key").getAsString());
			if(ds == null){
				//添加数据源到对象
				ds = new Datasource();
				if(datasource.has("key")){
					ds.setF_key(datasource.get("key").getAsString());
				}

				if(datasource.has("text")){
					ds.setF_caption(datasource.get("text").getAsString());
				}

				if(datasource.has("appid")){
					ds.setF_application_id(datasource.get("appid").getAsLong());
				}
				IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
				try {
					ds.setId(0);
					ds.save();
					tx.commit();
				} catch (DbAccessException e) {
					tx.rollback();
					throw new ApplicationException("数据源配置保存失败。");
				} finally {
					CommonDatabaseAccess.instance().endTransaction();
				}
			}
		}

		//添加数据源和数据库实例映射
		long dbId = 0;
		if(datasource.has("id") && database.has("id")){
			dbId = database.get("id").getAsLong();
		}

		Datasourcemapping dsm = ApplicationService.instance().getDatasourcemapping(ds.getId(), dbId);
		if(dsm != null){
			throw new ApplicationException("数据源已配置该数据库实例。");
		}else{
			dsm = new Datasourcemapping();
		}
		//添加数据源ID
		if(datasource.has("id")){
			dsm.setF_datasource_id(ds.getId());
		}
		//添加数据库实例ID
		if(database.has("id")){
			dsm.setF_database_id(dbId);
		}

		if(database.has("f_user")){
			dsm.setF_user(database.get("f_user").getAsString());
		}

		if(database.has("f_password")){
			dsm.setF_password(database.get("f_password").getAsString());
		}

		IDbacTransaction tx = CommonDatabaseAccess.instance().beginTransaction();
		try {
			dsm.save();
			tx.commit();
		} catch (DbAccessException e) {
			tx.rollback();
			throw new ApplicationException("数据库实例配置映射保存失败。");
		} finally {
			CommonDatabaseAccess.instance().endTransaction();
		}
	}

	/**
	 * 删除数据库实例配置
	 * 
	 * @param id
	 * @throws ApplicationException 
	 */
	public void deleteDatasourcemapping(long id) throws ApplicationException {
		writer.deleteDatasourcemapping(id);
	}

	/**
	 * 添加内外网交换配置
	 * 
	 * @param selectOne
	 * @param selectTwo
	 * @throws ApplicationException
	 */
	public void addDatabasesExchange(JsonObject selectOne, JsonObject selectTwo) throws ApplicationException {
		long innerId = 0;
		long outId = 0;
		if(selectOne.has("f_location") && selectTwo.has("f_location")){
			//取得选中数据的地址
			String locationOne = selectOne.get("f_location").getAsString();
			String locationTwo = selectTwo.get("f_location").getAsString();
			if("".equals(locationOne) || "".equals(locationTwo)){
				throw new ApplicationException("选中数据的地址为空。");
			}
			//判断地址是否相同
			if(locationOne.equals(locationTwo)){
				throw new ApplicationException("应选择匹配的内外网。");
			}
			if("内网".equals(locationOne)){
				innerId = selectOne.get("id").getAsLong();
				outId = selectTwo.get("id").getAsLong();
			}else{
				outId = selectOne.get("id").getAsLong();
				innerId = selectTwo.get("id").getAsLong();
			}
			try {
				getDbAccess().beginTransaction();
				Databasessync dbs = getDatabasessync(innerId, outId);
				if(dbs != null){
					throw new ApplicationException("已存在该内外网配置！");
				}
			} catch (DbAccessException e) {
				throw new ApplicationException("外网配置获取错误！");
			} finally {
				getDbAccess().endTransaction();
			}
			writer.addDatabasesExchange(innerId, outId);
		}
	}

	private Databasessync getDatabasessync(long innerId, long outId) throws DbAccessException {
		String sql = "select * from t_asc_database_sync where f_inner_database_id = ? and f_outer_database_id = ?";
		return getDbAccess().getObject(sql, new Object[]{innerId, outId}, Databasessync.class);
	}

	private CommonDatabaseAccess getDbAccess() {
		return CommonDatabaseAccess.instance();
	}

	/**
	 * 保存内外网交换配置
	 * 
	 * @param dbsId
	 * @param data
	 * @return
	 * @throws ApplicationException
	 */
	public Databasessync saveDatabasessync(long dbsId, JsonObject data) throws ApplicationException {
		Databasessync dbs = ApplicationService.instance().getDatabasessyncById(dbsId);
		JsonObjectTool.jsonObject2Object(data, dbs);
		dbs.setF_update_time(new Date());
		try {
			dbs.save();
		} catch (DbAccessException e) {
			throw new ApplicationException("保存内外网交换配置失败！");
		}
		return dbs;
	}

	/**
	 * 删除内外网交换
	 * 
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteDatabasessync(long id) throws ApplicationException {
		writer.deleteDatabasessync(id);
	}

	/**
	 * 保存内外网交换数据表
	 * 
	 * @param tablename
	 * @return
	 * @throws ApplicationException
	 */
	public DatabasessyncTable saveSyncTable(long syncId, String tablename) throws ApplicationException {
		return writer.saveSyncTable(syncId, tablename);
	}

	/**
	 * 删除内外网交换数据表
	 * 
	 * @param syncId
	 * @throws ApplicationException 
	 */
	public void deleteSyncTable(long syncId) throws ApplicationException {
		writer.deleteSyncTable(syncId);
	}
	
	/**
	 * 更新应用的状态
	 * @param appId
	 * @param state
	 * @throws ApplicationException
	 */
	public void updateAppState(long appId, int state) throws ApplicationException{
		writer.updateAppState(appId, state);
	}
}
