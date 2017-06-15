package com.asc.developer.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asc.commons.context.ContextVariablesParser;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;

/**
 * Application Developer/AppConnectionFactory.java<br>
 * 应用服务连接管理，提供连接信息注册、编辑、删除和查找
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class AppConnectionFactory {
	private Log logger;
	private static AppConnectionFactory singleton;
	private Map<String, ApplicationConnection> appConnections;
	private String configFilePath;
	public static final String LOCAL_ID = "local";
	
	public static AppConnectionFactory instance() {
		if (singleton == null) {
			singleton = new AppConnectionFactory();
		}
		return singleton;
	}
	
	private AppConnectionFactory() {
		logger = LogFactory.getLog(this.getClass());
		appConnections = new HashMap<String, ApplicationConnection>();
		configFilePath = "|webroot|/WEB-INF/connections";
		initialize();
	}

	/**
	 * 应用连接信息初始化<br>
	 * 从文件装载应用连接文件，注册连接信息到工厂当中
	 */
	public void initialize() {
		logger.info("开始初始化设计工具注册连接信息");
		// 读取文件列表
		List<String> filenames = SerializeUtil.listSerializedFiles(this.getConnectionFileRoot());
		// 逐个装载对象并注册到连接清单当中
		for (int i = 0; i < filenames.size(); i++) {
			ApplicationConnection conn = SerializeUtil.loadObject(filenames.get(i));
			if (conn != null) {
				registerConnection(conn);
			}
		}
		logger.info("连接信息装载结束，已装载连接数量[" + appConnections.size() + "]");
	}
	
	/**
	 * 将连接信息持久化到指定的文件目录
	 */
	public void serialize() {
		logger.info("开始执行连接信息持久化");
		// 遍历连接清单，逐个写入到文件
		Iterator<String> keys = appConnections.keySet().iterator();
		while (keys.hasNext()) {
			String appId = keys.next();
			SerializeUtil.saveObject(SerializeUtil.getObjectFilename(appId), appConnections.get(appId).toJson(), this.getConnectionFileRoot());
		}
		logger.info("连接信息持久化结束");
	}
	
	/**
	 * 注册连接到工厂
	 * @param connection 连接实例
	 */
	public void registerConnection(ApplicationConnection connection) {
		if(LOCAL_ID.equals(connection.getAppId())){
			throw new IllegalArgumentException("不允许修改本地默认链接。");
		}
		appConnections.put(connection.getAppId(), connection);
	}
	
	/**
	 * 获取应用标识对应的连接信息
	 * @param appId 应用标识
	 * @return
	 */
	public ApplicationConnection getConnection(String appId) {
		if (appId == null) {
			throw new IllegalArgumentException("未提供应用服务标识");
		}
		if(LOCAL_ID.equals(appId)){
			return getLocalApplicationConnection();
		}
		if (appConnections.containsKey(appId)) {
			return appConnections.get(appId);
		}
		return null;
	}
	/**
	 * 判断是否系统默认创建的本地连接
	 * @param appId
	 * @return
	 */
	public boolean isLocal(String appId){
		return LOCAL_ID.equals(appId);
	}
	
	/**
	 * 返回应用连接列表
	 * @return
	 */
	public List<ApplicationConnection> listAppConnections() {
		List<ApplicationConnection> conns = new ArrayList<ApplicationConnection>();
		// 添加默认创建本地连接
		conns.add(getLocalApplicationConnection());
		// 添加注册链接
		Iterator<String> keys = appConnections.keySet().iterator();
		while (keys.hasNext()) {
			String appId = keys.next();
			ApplicationConnection conn = appConnections.get(appId);
			conns.add(conn);
		}
		return conns;
	}
	
	/**
	 * 删除应用连接
	 * @param appId
	 */
	public void unregisterConnection(String appId) {
		if(LOCAL_ID.equals(appId)){
			throw new IllegalArgumentException("不允许修改本地默认链接。");
		}
		if (appConnections.containsKey(appId)) {
			SerializeUtil.deleteObject(SerializeUtil.getObjectFilename(appId), this.getConnectionFileRoot());
			appConnections.remove(appId);
		}
	}
	/**
	 * 创建默认本地连接
	 * @return
	 */
	private ApplicationConnection getLocalApplicationConnection(){
		ApplicationConnection conn = new ApplicationConnection();
		conn.setAppId(LOCAL_ID);
		conn.setAppCaption("本地连接");
		conn.setAppHost("");
		conn.setServiceUrl("");
		conn.setAppType(CommonTextFolderNode.TYPE_APP);
		conn.setNote("系统默认创建访问设计工具本地设计对象连接，一般用于测试，连接属性不允许修改");
		return conn;
	}
	
	/**
	 * 计算连接配置文件实际根路径
	 * @return
	 */
	private String getConnectionFileRoot() {
		return ContextVariablesParser.instance().parse(getConfigFilePath(), null);
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
}
