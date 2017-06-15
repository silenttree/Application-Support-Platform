package com.asc.developer.direct;

import com.asc.commons.design.exception.DesignObjectException;
import com.asc.developer.service.DesignObjectServiceProxy;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

public class FlowAppDirect {
	/**
	 * @param key
	 * @param versionId
	 * @param refresh
	 * @return
	 */
	@DirectMethod
	public JsonObject getFlowCells(String appId, String key, int versionId, boolean refresh) {
		JsonObject result = new JsonObject();
		try {
			JsonObject json = DesignObjectServiceProxy.instance().getFlowCells(appId, key, versionId, refresh);
			result.addProperty("success", true);
			result.addProperty("checkLog", json.get("checkLog").getAsBoolean());
			result.add("nodes", json.get("nodes").getAsJsonArray());
			result.add("routes", json.get("routes").getAsJsonArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 锁定流程
	 * 
	 * @param key
	 * @param versionId
	 * @param locked
	 * @return
	 */
	@DirectMethod
	public JsonObject setFlowLocked(String appId, String key, int versionId, boolean locked) {
		JsonObject result = new JsonObject();
		boolean success = false;
		try {
			DesignObjectServiceProxy.instance().setFlowLocked(appId, key, versionId, locked);
			success = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.addProperty("message", e.getMessage());
		}
		result.addProperty("success", success);
		return result;
	}
	
	/**
	 * 保存流程节点位置
	 * 
	 * @param key
	 * @param versionId
	 * @param posList
	 * @return
	 * @throws DesignObjectException 
	 */
	@DirectMethod
	public JsonObject saveFlowPosition(String appId, String key, int versionId, JsonObject posList, JsonObject routeList){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().saveFlowPosition(appId, key, versionId, posList, routeList);
			result.addProperty("success", true);
		}  catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 插入新节点
	 * 
	 * @param key
	 * @param sourceKey
	 * @param x
	 * @param y
	 * @return
	 */
	@DirectMethod
	public JsonObject insertNode(String appId, String key, int versionId, String sourceKey, int x, int y) {
		JsonObject result = new JsonObject();
		try {
			JsonObject json = DesignObjectServiceProxy.instance().insertNode(appId, key, versionId, sourceKey, x, y);
			result.addProperty("nodekey", json.get("nodekey").getAsString());
			result.addProperty("routekey", json.get("routekey").getAsString());
			result.addProperty("success", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 创建连接节点的路由
	 * 
	 * @param key
	 * @param versionId
	 * @param sourceNodeKey
	 * @param targetNodeKey
	 * @return
	 */
	@DirectMethod
	public JsonObject insertRoute(String appId, String key, int versionId, String sourceNodeKey, String targetNodeKey) {
		JsonObject result = new JsonObject();
		try {
			String routeKey = DesignObjectServiceProxy.instance().insertRoute(appId, key, versionId, sourceNodeKey, targetNodeKey);
			if(null != routeKey && !"".equals(routeKey)){
				result.addProperty("routekey", routeKey);
			}
			result.addProperty("success", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	@DirectMethod
	public JsonObject removeCell(String appId, String key, int versionId, String objKey) {
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().removeCell(appId, key, versionId, objKey);
			result.addProperty("success", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 更新流程标题
	 * 
	 * @param key
	 * @param caption
	 * @return
	 */
	@DirectMethod
	public JsonObject updateCaption(String appId, String key, int versionId, String caption) {
		JsonObject result = new JsonObject();
		try {
			JsonObject json = DesignObjectServiceProxy.instance().updateCaption(appId, key, versionId, caption);
			if(json.isJsonNull() && json.has("caption") && json.get("caption").isJsonNull() 
					&& !"".equals(json.get("caption").getAsString())){
				result.addProperty("caption", json.get("caption").getAsString());
			}
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	
	/**
	 * 更新路由指向
	 * 
	 * @param key
	 * @param versionId
	 * @param routeKey
	 * @param targetNodeKey
	 * @return
	 */
	@DirectMethod
	public JsonObject updateRouteTarget(String appId, String key, int versionId, String routeKey, String targetNodeKey){
		JsonObject result = new JsonObject();
		try {
			String newRouteKey = DesignObjectServiceProxy.instance().updateRouteTarget(appId, key, versionId, routeKey, targetNodeKey);
			if(null != newRouteKey && !"".equals(newRouteKey)){
				result.addProperty("key", newRouteKey);
			}
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
}
