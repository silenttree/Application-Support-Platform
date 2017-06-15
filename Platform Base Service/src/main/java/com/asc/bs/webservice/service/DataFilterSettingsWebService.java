package com.asc.bs.webservice.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.asc.commons.datafilter.access.IDataFilterSettingsReader;
import com.asc.commons.datafilter.entity.Policy;
import com.asc.commons.datafilter.exception.DfSettingsAccessException;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

/**
 * <pre>
 * Platform Base Service
 * 数据过滤设置数据读取Web服务代理类
 * 
 * Mixky Co., Ltd. 2016
 * @author Bill
 * </pre>
 */
@WebService
public class DataFilterSettingsWebService {
	private IDataFilterSettingsReader readerImpl;
	
	public DataFilterSettingsWebService() {
	}
	
	@WebMethod(exclude = true)
	public void setReaderImpl(IDataFilterSettingsReader readerImpl){
		this.readerImpl = readerImpl;
	}

	/**
	 * 查找指定id的资源
	 * 
	 * @param resId
	 * @return
	 * @throws DfSettingsAccessException
	 */
//	public String getResource(String resId) {
//		JsonObject result = new JsonObject();
//		try {
//			Resource resource = readerImpl.getResource(Long.valueOf(resId));
//			if (resource != null) {
//				result.add("data", JsonObjectTool.object2JsonObject(resource));
//			}
//			result.addProperty("success", true);
//		} catch (Exception e) {
//			result.addProperty("success", false);
//			result.addProperty("message", e.getMessage());
//		}
//		return result.toString();
//	}
	
	/**
	 * 查询应用已注册资源列表
	 * 
	 * @param appId
	 * @return
	 * @throws DfSettingsAccessException
	 */
//	public String listResources(String appId) {
//		JsonObject result = new JsonObject();
//		try {
//			List<Resource> resources = readerImpl.listResources(appId);
//			if (resources != null && resources.size() > 0) {
//				result.add("data", JsonObjectTool.objectList2JsonArray(resources));
//			}
//			result.addProperty("success", true);
//		} catch (Exception e) {
//			result.addProperty("success", false);
//			result.addProperty("message", e.getMessage());
//			logger.warn("服务异常," + e.getMessage());
//			if (logger.isDebugEnabled()) {
//				e.printStackTrace();
//			}
//		}
//		return result.toString();
//	}
	
	/**
	 * 查询指定id的数据过滤策略
	 * 
	 * @param pId
	 * @return
	 * @throws DfSettingsAccessException
	 */
	public String getPolicy(String pId) {
		JsonObject result = new JsonObject();
		try {
			Policy policy = readerImpl.getPolicy(Long.valueOf(pId));
			if (policy != null) {
				result.add("data", JsonObjectTool.object2JsonObject(policy));
			}
			result.addProperty("success", true);
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * <pre>
	 * 查找指定应用系统中，特定设计对象标识符配置的数据过滤规则
	 * 无需指定表名称，按照设计对象标识符查找数据过滤规则策略
	 * </pre>
	 * 
	 * @param appId 应用标识符
	 * @param objId 对象标识符
	 * @param ue 用户表达式
	 * @return
	 * @throws DfSettingsAccessException
	 */
	public String listFilterPoliciesByObject(String appId, String objId, String ue) {
		JsonObject result = new JsonObject();
		try {
			List<Policy> polices = readerImpl.listFilterPolicies(appId, objId, ue);
			if (polices != null && polices.size() > 0) {
				result.add("data", JsonObjectTool.objectList2JsonArray(polices));
			}
			result.addProperty("success", true);
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
	
	/**
	 * <pre>
	 * 查询指定应用系统中，特定设计对象、特定数据表和
	 * 
	 * </pre>
	 * @param appId 应用标识符
	 * @param objId 对象标识符
	 * @param tableName
	 * @param ue 用户表达式
	 * @return
	 * @throws DfSettingsAccessException
	 */
	public String listFilterPolicesByTablename(String appId, String objId, String tableName, String ue) {
		JsonObject result = new JsonObject();
		try {
			List<Policy> polices = readerImpl.listFilterPolicies(appId, objId, tableName, ue);
			if (polices != null && polices.size() > 0) {
				result.add("data", JsonObjectTool.objectList2JsonArray(polices));
			}
			result.addProperty("success", true);
		} catch (Exception e) {
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result.toString();
	}
}
