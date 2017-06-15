package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.commons.application.access.IApplicationReader;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.entity.ApplicationInstance;
import com.asc.commons.application.entity.Database;
import com.asc.commons.application.entity.Databasessync;
import com.asc.commons.application.entity.DatabasessyncTable;
import com.asc.commons.application.entity.Datasource;
import com.asc.commons.application.entity.Datasourcemapping;
import com.asc.commons.application.exception.ApplicationException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * @author zhsq
 * <pre>
 * 包装DbacApplicationReaderImp,为其他程序提供web service
 * 其中的方法和被包装的类一一对应
 * 所有方法的参数处理成字符串
 * 所有重载的方法重新起名字
 * 返回值为一个标准的json字符串
 * 	其中success标识成功与否
 * 	data为成功时的返回数据
 * 	message为失败时的错误信息
 * </pre>
 */
@WebService
public class ApplicationWebService {
	
	/**
	 * 真正实现的查找应用的类
	 */
	private IApplicationReader reader;
	private Gson gson = new Gson();
	private Log logger;
	
	public ApplicationWebService() {
		logger = LogFactory.getLog(this.getClass());
	}
	
	@WebMethod(exclude = true)
	public void setReader(IApplicationReader reader){
		this.reader = reader;
	}
	
	/**
	 * 根据标识获得应用程序对象
	 * @param key
	 * @return
	 */
	public String getApplicationByKey(String key) {
		JsonObject result = new JsonObject();
		try {
			Application app = reader.getApplicationByKey(key);
			if (app == null) {
				throw ApplicationException.forApplicationNotExist(key);
			}
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(app));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
			if (logger.isDebugEnabled()) {
				e.printStack();
			}
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据key查找应用失败");
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		return gson.toJson(result);
	}
	
	public String getApplicationById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			Application app = reader.getApplicationById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(app));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
			if (logger.isDebugEnabled()) {
				e.printStack();
			}
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据ID查找应用失败");
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
		}
		return gson.toJson(result);
	}
	
	public String findApplication() {
		JsonObject result = new JsonObject();
		try {
			List<Application> apps = reader.findApplication();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(apps)); 
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取应用列表失败");
		}
		return gson.toJson(result);
	}
	
	public String getAppInsCount(String appIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			long cnt = reader.getApplicationInstanceCount(appId);
			result.addProperty("success", true);
			result.addProperty("data", cnt);
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取应用的实例个数失败");
		}
		return gson.toJson(result);
	}
	
	public String getAppInsByKey(String key) {
		JsonObject result = new JsonObject();
		try {
			ApplicationInstance appIns = reader.getApplicationInstance(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(appIns));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据key获取应用实例失败");
		}
		return gson.toJson(result);
	}
	
	public String getAppInsById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			ApplicationInstance appIns = reader.getApplicationInstance(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(appIns));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据id获取应用实例失败");
		}
		return gson.toJson(result);
	}
	
	public String findAppInsesByAppId(String appIdStr) {
		JsonObject result = new JsonObject();
		try {
			long appId = Long.parseLong(appIdStr);
			List<ApplicationInstance> appInses = reader
					.findApplicationInstance(appId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(appInses));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用id获取应用实例失败");
		}
		return gson.toJson(result);
	}

	public String getDatabaseById(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			Database db = reader.getDatabaseById(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(db));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据id获取数据库实例失败");
		}
		return gson.toJson(result);
	}
	
	public String getDataBaseList(String location) {
		JsonObject result = new JsonObject();
		try {
			List<Database> dbs = reader.getDatabaseList(location);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dbs));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "数据库实例列表获取失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatasourceByKey(String key) {
		JsonObject result = new JsonObject();
		try {
			Datasource ds = reader.getDatasourceByKey(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(ds));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取数据源失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatasourcemapping(String dsIdStr, String dbIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dsId = Long.parseLong(dsIdStr);
			long dbId = Long.parseLong(dbIdStr);
			Datasourcemapping dsm = reader.getDatasourcemapping(dsId, dbId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dsm));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取数据库实例配置映射失败");
		}
		return gson.toJson(result);
	}
	
	public String findDsmList(String key) {
		JsonObject result = new JsonObject();
		try {
			List<Datasourcemapping> dsms = reader.findDsmList(key);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dsms));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取数据库实例配置映射失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatasourceById (String dsIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dsId = Long.parseLong(dsIdStr);
			Datasource ds = reader.getDatasourceById(dsId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(ds));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取数据源失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatabasessyncById(String dbsIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dbsId = Long.parseLong(dbsIdStr);
			Databasessync dbs = reader.getDatabasessyncById(dbsId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dbs));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取内外网交换失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatasourcemappingById(String dsmIdStr) {
		JsonObject result = new JsonObject();
		try {
			long dsmId = Long.parseLong(dsmIdStr);
			Datasourcemapping dsm = reader.getDatasourcemappingById(dsmId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dsm));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "数据库实例配置查找失败");
		}
		return gson.toJson(result);
	}
	
	public String loadDataSyncTable(String syncIdStr) {
		JsonObject result = new JsonObject();
		try {
			long syncId = Long.parseLong(syncIdStr);
			List<DatabasessyncTable> dst = reader.loadDataSyncTable(syncId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dst));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "获取内外网交换数据表失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatabasetableByName(String syncIdStr, String tablename) {
		JsonObject result = new JsonObject();
		try {
			long syncId = Long.parseLong(syncIdStr);
			DatabasessyncTable dbt = reader.getDatabasetableByName(syncId,
					tablename);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(dbt));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "查找内外网交换数据表失败");
		}
		return gson.toJson(result);
	}
	
	public String getDatasourceList(String idStr) {
		JsonObject result = new JsonObject();
		try {
			long id = Long.parseLong(idStr);
			List<Datasource> dses = reader.getDatasourceList(id);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(dses));
		} catch (ApplicationException e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", "根据应用ID获取本地数据源失败");
		}
		return gson.toJson(result);
	}
	
}
