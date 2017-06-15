package com.asc.manager.navigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asc.manager.navigator.exception.NavigatorException;
import com.asc.manager.navigator.loader.ApplicationConfigFolderLoader;
import com.asc.manager.navigator.loader.ApplicationDictionaryFolderLoader;
import com.asc.manager.navigator.loader.ApplicationLogFolderLoader;
import com.asc.manager.navigator.loader.ApplicationManagerFolderLoader;
import com.asc.manager.navigator.loader.ApplicationModuleFolder;
import com.asc.manager.navigator.loader.ApplicationNavigatorFolderLoader;
import com.asc.manager.navigator.loader.ApplicationRegisterFolderLoader;
import com.asc.manager.navigator.loader.ApplicationWorkflowFolderLoader;
import com.asc.manager.navigator.loader.BaseDataManagerFolderLoader;
import com.asc.manager.navigator.loader.DictionaryFolderLoader;
import com.asc.manager.navigator.loader.OrganizationFolderLoader;
import com.asc.manager.navigator.loader.RootLoader;
import com.asc.manager.navigator.loader.ServicePlatformConfigFolderLoader;
import com.asc.manager.navigator.loader.ServicePlatformManagerFolderLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NavigatorLoaderManager {
	private static NavigatorLoaderManager singleton;
	private Map<String, ILoader> loaders;
	
	public NavigatorLoaderManager(){
		loaders = new HashMap<String, ILoader>();
		// 根节点
		this.addLoader(new RootLoader());
		// 应用注册
		this.addLoader(new ApplicationRegisterFolderLoader());
		//内外网交换装载器
		//this.addLoader(new ElectrialDataExchangeFloderLoader());
		// 基础数据管理
		this.addLoader(new BaseDataManagerFolderLoader());
		this.addLoader(new OrganizationFolderLoader());
		this.addLoader(new DictionaryFolderLoader());
		// 应用配置
		this.addLoader(new ApplicationConfigFolderLoader());
		this.addLoader(new ApplicationNavigatorFolderLoader());
		this.addLoader(new ApplicationModuleFolder());
		this.addLoader(new ApplicationWorkflowFolderLoader());
		this.addLoader(new ApplicationDictionaryFolderLoader());
		// 服务平台配置
		this.addLoader(new ServicePlatformConfigFolderLoader());
		// 应用运行管理
		this.addLoader(new ApplicationManagerFolderLoader());
		this.addLoader(new ApplicationLogFolderLoader());
		// 服务平台运行管理
		this.addLoader(new ServicePlatformManagerFolderLoader());
	}

	public static NavigatorLoaderManager instance() {
		if (singleton == null) {
			singleton = new NavigatorLoaderManager();
		}
		return singleton;
	}

	public void addLoader(ILoader loader){
		loaders.put(loader.getType(), loader);
	}
	
	public void removeLoader(String type){
		loaders.remove(type);
	}
	
	public boolean hasLoader(String type){
		return loaders.containsKey(type);
	}
	
	public ILoader getLoader(String type) throws NavigatorException{
		if(hasLoader(type)){
			return loaders.get(type);
		}else{
			throw NavigatorException.forNodeLoaderNotFound(type);
		}
	}
	/**
	 * 获得请求节点列表
	 * @param node
	 * @return
	 */
	public JsonArray getNavigatorTreeNodes(JsonObject node) throws NavigatorException{
		JsonArray results = new JsonArray();
		if(!node.has("type")){
			// 参数不正确
			throw NavigatorException.forNodeParameterError(node.toString());
		}
		String type = node.get("type").getAsString();
		String key = node.has("key") ? node.get("key").getAsString() : null;
		// 获得节点装载器
		ILoader loader = this.getLoader(type);
		if(loader == null){
			// 未找到匹配的装载器
			 throw NavigatorException.forNodeLoaderNotFound(type);
			
		}else{
			// 装载子节点
			List<INode> nodes = loader.getChildren(key);
			for(int i=0;i<nodes.size();i++){
				results.add(nodes.get(i).toJson());
			}
		}
		return results;
	}
	
	/**
	 * 获得组织树
	 * @param node
	 * @return
	 */
	public JsonArray getOrganization() throws NavigatorException{
		JsonArray results = new JsonArray();
		// 装载子节点
		for(int i = 0; i < 10; i++){
			JsonObject json = new JsonObject();
			json.addProperty("id", i + 1);
			json.addProperty("t_text", "单位" + i);
			results.add(json);
		}
		return results;
	}
	
	/**
	 * 获得用户
	 * @param node
	 * @return
	 */
	public JsonArray getUser() throws NavigatorException{
		JsonArray results = new JsonArray();
		// 装载子节点
		for(int i = 0; i < 10; i++){
			JsonObject json = new JsonObject();
			json.addProperty("name", "测试用户" + i);
			json.addProperty("org", "组织" + i);
			json.addProperty("type", "系统用户");
			json.addProperty("phone", "12345678");
			json.addProperty("notes", "备注");
			results.add(json);
		}
		return results;
	}
}
