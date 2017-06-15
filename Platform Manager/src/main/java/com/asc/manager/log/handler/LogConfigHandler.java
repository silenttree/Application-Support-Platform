package com.asc.manager.log.handler;

import java.util.List;

import com.asc.bs.log.service.LogConfigManager;
import com.asc.commons.application.entity.Application;
import com.asc.commons.application.exception.ApplicationException;
import com.asc.commons.application.service.ApplicationService;
import com.asc.commons.context.ContextHolder;
import com.asc.commons.log.entity.LogConfigEntity;
import com.asc.commons.log.exception.LogException;
import com.asc.commons.log.service.LogConfigService;
import com.asc.manager.log.exception.LogConfigHandlerException;
import com.asc.util.ws.WebServiceInvoke;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mixky.toolkit.JsonObjectTool;

public class LogConfigHandler {
	private static LogConfigHandler singleton;
	public final static String ESB_APPLICATION = "ESB应用系统";
	public final static String BS_APPLICATION = "BS应用系统";
	public final static String PORTAL_APPLICATION = "portal门户系统";
	public final static String MANAGER_APPLICATION = "应用管理平台";
	public final static String CAS_APPLICATION = "认证服务日志";
	public final static String CAS_TYPE = "ApplicationCasLog";
	public final static String MANAGER_TYPE = "ApplicationManagerLog";
	
	private String namespace;
	private String wsName;
	//应用系统的地址
	private String casUrl;
	private String bsUrl;
	private String esbUrl;
	private String portalUrl;
	private String managerUrl;
	
	public void setCasUrl(String casUrl) {
		this.casUrl = casUrl;
	}

	public void setBsUrl(String bsUrl) {
		this.bsUrl = bsUrl;
	}

	public void setEsbUrl(String esbUrl) {
		this.esbUrl = esbUrl;
	}

	public void setPortalUrl(String portalUrl) {
		this.portalUrl = portalUrl;
	}

	public void setManagerUrl(String managerUrl) {
		this.managerUrl = managerUrl;
	}
	
	public String getCasUrl() {
		return casUrl;
	}

	public String getBsUrl() {
		return bsUrl;
	}

	public String getEsbUrl() {
		return esbUrl;
	}

	public String getPortalUrl() {
		return portalUrl;
	}

	public String getManagerUrl() {
		return managerUrl;
	}
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getWsName() {
		return wsName;
	}

	public void setWsName(String wsName) {
		this.wsName = wsName;
	}

	public static LogConfigHandler instance() {
		if (singleton == null) {
			singleton = ContextHolder.instance().getBean("LogConfigHandler");
		}
		return singleton;
	}

	/**
	 * 保存日志配置
	 * 
	 * @param data
	 * @return
	 * @throws LogConfigHandlerException
	 */
	public JsonObject saveLogConf(String appkey, JsonObject data) throws LogConfigHandlerException {
		LogConfigEntity logConf;
		try {
			logConf = LogConfigManager.instance().saveLogConf(appkey, data);
			return JsonObjectTool.object2JsonObject(logConf);
		} catch (LogException e) {
			throw new LogConfigHandlerException("日志配置保存失败！");
		}
	}

	/**
	 * 删除日志配置
	 * 
	 * @param id
	 * @throws LogConfigHandlerException
	 */
	public void delLogConf(long id) throws LogConfigHandlerException {
		try {
			LogConfigManager.instance().delLogConf(id);
		} catch (LogException e) {
			throw new LogConfigHandlerException("日志配置删除失败！");
		}
	}

	/**
	 * 获取日志配置列表
	 * 
	 * @param appkey
	 * @return
	 * @throws LogConfigHandlerException
	 */
	public JsonArray loadLogConfig(String appkey) throws LogConfigHandlerException {
		List<LogConfigEntity> logConfList;
		try {
			logConfList = LogConfigService.instance().findLogConfigs(appkey);
			JsonArray datas = new JsonArray();
			//循环logConfList，把每个元素都放到jsonarray中
			for(int i = 0; i < logConfList.size();i++){
				LogConfigEntity logConfig = logConfList.get(i);
				JsonObject json = new JsonObject();
				json.addProperty("id", logConfig.getId());
				json.addProperty("f_application_id", logConfig.getF_application_id());
				json.addProperty("f_resource_id", logConfig.getF_resource_id());
				json.addProperty("f_action", logConfig.getF_action());
				json.add("f_targets", JsonObjectTool.string2JsonObject(logConfig.getF_targets()));
				json.addProperty("f_has_detail", logConfig.getF_has_detail());
				json.addProperty("f_has_requestinfo", logConfig.getF_has_requestinfo());
				json.addProperty("f_has_userinfo", logConfig.getF_has_userinfo());
				json.addProperty("f_state", logConfig.getF_state());
				json.addProperty("f_type", logConfig.getF_type());
				datas.add(json);
			}
			return datas;
		} catch (LogException e) {
			throw new LogConfigHandlerException("日志配置列表获取失败！");
		}
	}
	
	public JsonArray loadAppTree(String type) throws LogConfigHandlerException{
		JsonArray children = new JsonArray();//子节点
		if(LogConfigHandler.CAS_TYPE.equals(type)){
			//认证服务日志
			children.add(createNode(LogConfigHandler.CAS_APPLICATION, casUrl));
		}else if(LogConfigHandler.MANAGER_TYPE.equals(type)){
			//应用管理平台日志
			children.add(createNode(LogConfigHandler.MANAGER_APPLICATION, managerUrl));
		}else{
			//固定的几个应用系统
			children.add(createNode(LogConfigHandler.BS_APPLICATION, bsUrl));
			children.add(createNode(LogConfigHandler.PORTAL_APPLICATION, portalUrl));
			children.add(createNode(LogConfigHandler.ESB_APPLICATION, esbUrl));
			//每个注册的应用系统
			JsonObject appNode = new JsonObject();
			appNode.addProperty("text", "已注册的应用");
			appNode.addProperty("leaf", false);
			appNode.addProperty("url", "");
			appNode.addProperty("id", "");
			appNode.addProperty("expanded", false);
			//已注册的应用
			JsonArray appNodeChildren = new JsonArray();
			try {
				List<Application> apps = ApplicationService.instance().findApplication();
				if(apps != null){
					for(int i=0;i<apps.size();i++){
						Application app = apps.get(i);
						if(!app.isAvailable()) {
							continue;
						}
						JsonObject node = new JsonObject();
						node.addProperty("id", app.getId());
						node.addProperty("text", app.getF_caption());
						node.addProperty("url", app.getF_domain());
						node.addProperty("leaf", true);
						appNodeChildren.add(node);
					}
				}
				appNode.add("children", appNodeChildren);
			} catch (ApplicationException e) {
				e.printStackTrace();
				throw new LogConfigHandlerException("装载应用列表失败,", e);
			}
			children.add(appNode);
		}
		return children;
	}
	
	/**
	 * 分页查询日志
	 * @param text
	 * @param url
	 * @return
	 */
	public JsonObject findLogsByType(String type, String url, int pagenum, int pagesize) throws Exception{
		try {
			Object result = getWsdl(url).invoke("findLogs", new Object[]{type, pagenum, pagesize},  new Class[]{String.class});
			if (result != null && result instanceof String) {
				JsonObject jsonData = JsonObjectTool.string2JsonObject(result.toString());
				if (jsonData.has("success") && jsonData.get("success").getAsBoolean()) {
					return jsonData;
				} else {
					// 请求失败或返回结果标记失败
					throw new Exception("加载日志[" + type + "]类型数据失败," + jsonData.get("message").getAsString());
				}
			} else {
				throw new Exception("返回数据格式异常,期望的返回数据类型是String");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("请求加载日志[" + type + "]类型数据失败," + e.getMessage());
		}
	}
	
	private WebServiceInvoke getWsdl(String url) {
		StringBuffer sb = new StringBuffer();
		sb.append(url).append("/services/").append(wsName).append("?wsdl");
		WebServiceInvoke wsdl = new WebServiceInvoke(sb.toString(), namespace);
		return wsdl;
	}
	
	private JsonObject createNode(String text, String url){
		JsonObject node = new JsonObject();
		node.addProperty("text", text);
		node.addProperty("url", url);
		node.addProperty("id", "");
		node.addProperty("leaf", true);
		return node;
	}
}
