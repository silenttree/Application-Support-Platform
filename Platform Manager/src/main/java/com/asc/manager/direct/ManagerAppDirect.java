package com.asc.manager.direct;

import com.asc.commons.dbac.CommonDatabaseAccess;
import com.asc.commons.exception.DbAccessException;
import com.asc.manager.common.RuntimeFileBuilder;
import com.asc.manager.navigator.NavigatorLoaderManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

/**
 * Application Manager/ManagerAppDirect.java<br>
 * 管理工具系统级Direct注册入口
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class ManagerAppDirect {

	/**
	 * 编译运行时文件
	 * @param type	生成文件类型
	 * @return
	 */
	@DirectMethod
	public JsonObject buildFiles(String type){
		JsonObject json = new JsonObject();
		try {
			json.addProperty("success", true);
			if("class".equals(type)){
				RuntimeFileBuilder.instance().buildClassFile();
			}else if("action".equals(type)){
				RuntimeFileBuilder.instance().buildActionFile();
			}else{
				json.addProperty("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载导航菜单
	 * @param node	节点数据
	 * @return
	 */
	@DirectMethod
	public JsonObject loadNavigators(JsonObject node){
		JsonObject json = new JsonObject();
		try {
			JsonArray datas;
			datas = NavigatorLoaderManager.instance().getNavigatorTreeNodes(node);
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
	 * 获得SEQ
	 * @param tableName
	 * @return
	 */
	@DirectMethod
	public JsonObject getNewSeqId(String tableName){
		JsonObject json = new JsonObject();
		try {
			long id = CommonDatabaseAccess.instance().newSeqId(tableName);
			json.addProperty("success", true);
			json.addProperty("id", id);
		} catch (DbAccessException e) {
			e.printStackTrace();
			json.addProperty("success", false);
		}
		return json;
	}
	
	@DirectMethod
	public JsonObject loadOrganization(){
		JsonObject json = new JsonObject();
		for(int i = 0; i < 10; i++){
			json.addProperty("text", "test" + i);
		}
		return json;
	}
}
