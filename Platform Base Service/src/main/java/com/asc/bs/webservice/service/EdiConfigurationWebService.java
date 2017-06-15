package com.asc.bs.webservice.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.commons.edi.access.IEdiConfiguationReader;
import com.asc.commons.edi.entity.FileRule;
import com.asc.commons.edi.entity.TableRule;
import com.asc.commons.edi.exception.EdiConfiguationException;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class EdiConfigurationWebService {
	private Log logger;
	private IEdiConfiguationReader reader;
	
	public EdiConfigurationWebService() {
		logger = LogFactory.getLog(this.getClass());
	}

	public void setReader(IEdiConfiguationReader reader) {
		this.reader = reader;
	}
	
	/**
	 * 根据规则id查询数据表同步规则
	 * 
	 * @param ruleId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	/*public String getTableSyncRule(long ruleId) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			TableRule tableRule = reader.getTableSyncRule(ruleId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(tableRule));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}*/
	
	/**
	 * 查询特定应用指定数据表的同步规则
	 * 
	 * @param appId
	 * @param tableName
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String getTableSyncRule(String appId, String dsn, String tableName) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			TableRule tableRule = reader.getTableSyncRule(appId, dsn, tableName);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(tableRule));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 查询特定应用系统的所有数据表同步规则
	 * 
	 * @param appId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String findTableSyncRules(String appId) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			List<TableRule> list =  reader.findTableSyncRules(appId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch(Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 获取所有的数据交换规则
	 * @return
	 */
	public String findAllTableRules() throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			List<TableRule> list =  reader.findAllTableRules();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 分页查询所有的数据交换规则
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws EdiConfiguationException
	 */
	public String findTableRules(int pageNum, int pageSize) throws EdiConfiguationException {
		
		JsonObject result = new JsonObject();
		try {
			List<TableRule> list =  reader.findTableRules(pageNum, pageSize);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	/**
	 * 根据规则id查询文件同步规则
	 * 
	 * @param ruleId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String getFileSyncRule(long fileRuleId) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			FileRule fileRule = reader.getFileSyncRule(fileRuleId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.object2JsonObject(fileRule));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 查询特定应用系统的所有文件同步规则
	 * 
	 * @param appId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String findFileRules(String appId) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			List<FileRule> list =  reader.findFileRules(appId);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 分页查询特定应用系统的所有文件的同步规则
	 * 
	 * @param appId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String findFileRules(String appId, int pageNum, int pageSize) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			List<FileRule> list =  reader.findFileRules(appId, pageNum, pageSize);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 查询所有应用系统的所有文件的同步规则
	 * 
	 * @param appId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String findAllFileRules() throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			List<FileRule> list =  reader.findAllFileRules();
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * 分页查询所有应用系统的所有文件的同步规则
	 * 
	 * @param appId
	 * @return
	 * @throws EdiConfiguationException 
	 */
	public String findAllFileRules(int pageNum, int pageSize) throws EdiConfiguationException {
		JsonObject result = new JsonObject();
		try {
			List<FileRule> list =  reader.findAllFileRules(pageNum, pageSize);
			result.addProperty("success", true);
			result.add("data", JsonObjectTool.objectList2JsonArray(list));
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				e.printStackTrace();
			}
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
}
