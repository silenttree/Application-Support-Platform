package com.asc.developer.direct;

import com.asc.commons.engine.direct.DirectList;
import com.asc.commons.engine.exception.DesignObjectHandlerException;
import com.asc.developer.config.AppConnectionFactory;
import com.asc.developer.config.ApplicationConnection;
import com.asc.developer.designer.RuntimeFileBuilder;
import com.asc.developer.designer.exception.FileBuilderException;
import com.asc.developer.designer.exception.NavigatorException;
import com.asc.developer.designer.navigator.NavigatorLoaderManager;
import com.asc.developer.service.DesignObjectServiceProxy;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.softwarementors.extjs.djn.config.annotations.DirectMethod;

/**
 * Application Developer<br>
 * 开发工具系统级Direct入口
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class DeveloperAppDirect {
	
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
				RuntimeFileBuilder.instance().buildDesignerClasses();
			}else if("action".equals(type)){
				RuntimeFileBuilder.instance().buildDesignerActions();
			}else{
				json.addProperty("success", false);
			}
		} catch (FileBuilderException e) {
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
		} catch (NavigatorException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 创建设计对象
	 * @param appId		设计对象所属app标识
	 * @param parentId	设计对象父节点标识
	 * @param type		创建类型
	 * @param key		设计对象标识
	 * @return
	 */
	@DirectMethod
	public JsonObject createObject(String appId, String parentId, String type, String key, JsonObject values){
		JsonObject json = new JsonObject();
		try {
			JsonObject obj = DesignObjectServiceProxy.instance().createObject(appId, parentId, type, key, values);
			json.addProperty("success", true);
			json.addProperty("objectId", obj.get("id").getAsString());
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 删除设计对象
	 * @param appId		设计对象所属app标识
	 * @param id		设计对象标识
	 * @return
	 */
	@DirectMethod
	public JsonObject deleteObject(String appId, String id){
		JsonObject json = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().deleteObject(appId, id);
			json.addProperty("success", true);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 装载设计对象数据
	 * @param appId
	 * @param id
	 * @return
	 */
	@DirectMethod
	public JsonObject loadObject(String appId, String id){
		JsonObject json = new JsonObject();
		try {
			JsonObject object = DesignObjectServiceProxy.instance().loadObject(appId, id);
			json.addProperty("success", true);
			json.add("object", object);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 批量保存设计对象
	 * @param appId
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject saveObjects(String appId, JsonArray datas){
		JsonObject json = new JsonObject();
		JsonArray sObjects = new JsonArray();
		JsonObject fObjects = new JsonObject();
		String message = "";
		for(int i=0;i<datas.size();i++){
			JsonObject data = datas.get(i).getAsJsonObject();
			String id = null;
			if(data.has("id")){
				id = data.get("id").getAsString();
			}
			try {
				JsonObject object = DesignObjectServiceProxy.instance().saveObject(appId, id, data);
				sObjects.add(object);
			} catch (DesignObjectHandlerException e) {
				e.printStackTrace();
				json.addProperty("success", false);
				fObjects.add(id, data);
				message = message + "<br>" + e.getMessage();
			}
		}
		json.addProperty("success", true);
		json.addProperty("message", message);
		json.add("sObjects", sObjects);
		json.add("fObjects", fObjects);
		return json;
	}
	/**
	 * 装载设计对象列表数据
	 * @param appId
	 * @param parentId
	 * @param type
	 * @return
	 */
	@DirectMethod
	public JsonObject loadObjects(String appId, String parentId, String type){
		DirectList directList = new DirectList();
		try {
			JsonArray objects = DesignObjectServiceProxy.instance().loadObjects(appId, parentId, type);
			directList.setDatas(objects);
			directList.setSuccess(true);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			directList.setSuccess(false);
			directList.setMessage(e.getMessage());
		}
		return directList.getOutput();
	}
	/**
	 * 装载树结构设计对象
	 * @param appId
	 * @param parentId
	 * @param type
	 * @return
	 */
	@DirectMethod
	public JsonArray loadTreeObjects(String appId, String parentId, String type){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().loadTreeObjects(appId, parentId, type);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 装在当前模块所有设计对象，包括外部引用的设计对象
	 * @param appId
	 * @param moduleKey
	 * @param noContain
	 * @return
	 */
	 
	@DirectMethod
	public JsonArray loadCheckTreeObjectsAndRefer(String appId, String moduleKey, String[] noContain){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().loadCheckTreeObjectsAndRefer(appId, moduleKey, noContain);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 装载当前模块下的所有设计对象
	 * @param appId
	 * @param moduleKey
	 * @param noContain
	 * @return
	 */
	@DirectMethod
	public JsonArray loadCheckTreeObjects(String appId, String moduleKey, String[] noContain){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().loadCheckTreeObjects(appId, moduleKey, noContain);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 添加模块对象
	 * @param appId
	 * @param moduleKey
	 * @param ids
	 * @return
	 */
	@DirectMethod
	public JsonObject addAuthObject(String appId, String moduleKey, String[] ids){
		JsonObject json = new JsonObject();
		try {
			JsonObject moduleAuthKey = DesignObjectServiceProxy.instance().addAuthObject(appId, moduleKey, ids);
			json.addProperty("success", true);
			json.add("moduleAuth", moduleAuthKey);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 刷新模块权限列表
	 * @param appId
	 * @param moduleKey
	 * @param obj
	 * @return
	 */
	@DirectMethod
	public JsonObject updateAuthorities(String appId, String moduleKey,JsonObject obj){
		JsonObject json = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().updateAuthorities(appId, moduleKey, obj);
			json.addProperty("success", true);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 获取模块角色列表
	 * @param appId
	 * @param moduleKey
	 * @return
	 */
	@DirectMethod
	public JsonObject getModuleRole(String appId, String moduleKey){
		JsonObject json = new JsonObject();
		try {
			JsonArray jarray = DesignObjectServiceProxy.instance().getModuleRole(appId, moduleKey);
			json.add("fields", jarray);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 加载模块权限列表
	 * @param appId
	 * @param moduleKey
	 * @return
	 */
	@DirectMethod
	public JsonArray loadGridModuleAuth(String appId, String moduleKey){
		JsonArray jarray = new JsonArray();
		try {
			jarray = DesignObjectServiceProxy.instance().loadGridModuleAuth(appId, moduleKey);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return jarray;
	}
	/**
	 * 加载设计对象列表
	 * @param appId
	 * @param type
	 * @param scope
	 * @return
	 */
	@DirectMethod
	public JsonArray loadGridDesignObject(String appId, String key, String[] type, String scope){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().loadGridDesignObject(appId, key, type, scope);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 加载文档设计对象映射表的下拉列表list
	 * @param key
	 * @param mclass
	 * @return
	 */
	@DirectMethod
	public JsonArray getSubObjectList(String appId, String key, String mclass){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().getSubObjectList(appId, key, mclass);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 添加文档权限映射表
	 * @param documentKey
	 * @param f_states
	 * @param f_extendstates
	 * @param f_identities
	 * @return
	 */
	@DirectMethod
	public JsonObject addDocumentAuthorityMap(String appId, String documentKey, String f_states, String f_extendstates, String f_identities){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().addDocumentAuthorityMap(appId, documentKey, f_states, f_extendstates, f_identities);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 加载文档权限映射表的结构树
	 * @param documentKey
	 * @return
	 */
	@DirectMethod
	public JsonArray loadDocumentAuthorityMapTree(String appId, String documentKey){
		JsonArray result = new JsonArray();
		try{
			result = DesignObjectServiceProxy.instance().loadDocumentAuthorityMapTree(appId, documentKey);
		}catch(DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 添加文档设计对象权限 ，文档的每个对应规则对象都添加选中的设计对象
	 * @param documentKey
	 * @param keys
	 * @return
	 */
	@DirectMethod
	public JsonObject addDocumentDesignObject(String appId, String documentKey, String[] keys){
		JsonObject result = new JsonObject();
		try{
			DesignObjectServiceProxy.instance().addDocumentDesignObject(appId, documentKey, keys);
			result.addProperty("success", true);
		}catch(DesignObjectHandlerException e){
			e.printStack();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 加载文档设计对象权限表
	 * @param authoritymapId
	 * @return
	 */
	@DirectMethod
	public JsonArray loadAuthoritymapGrid(String appId, String authoritymapId){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().loadAuthoritymapGrid(appId, authoritymapId);
		} catch (DesignObjectHandlerException e) {
			e.printStack();
		}
		return datas;
	}
	/**
	 * 更新文档设计对象权限表
	 * @param authoritymapId
	 * @param obj
	 * @return
	 */
	@DirectMethod
	public JsonObject updateAuthoritiesMap(String appId, String authoritymapId,JsonObject obj){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().updateAuthoritiesMap(appId, authoritymapId, obj);
			result.addProperty("success", true);
		} catch (DesignObjectHandlerException e) {
			e.printStack();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 删除文档设计对象权限
	 * @param documentKey
	 * @param type
	 * @param key
	 * @return
	 */
	@DirectMethod
	public JsonObject deleteDocumentAuthority(String appId, String documentKey, String type, String key){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().deleteDocumentAuthority(appId, documentKey, type, key);
			result.addProperty("success", true);
		} catch (DesignObjectHandlerException e) {
			e.printStack();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 装载连接对象信息
	 * @param appId
	 * @return
	 */
	@DirectMethod
	public JsonObject loadAppConnection(String appId){
		JsonObject json = new JsonObject();
		try {
			JsonObject object = AppConnectionFactory.instance().getConnection(appId).toJson();
			json.addProperty("success", true);
			json.add("object", object);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 保存应用连接配置
	 * @param appId
	 * @param data
	 * @return
	 */
	@DirectMethod
	public JsonObject saveAppConnection(String appId, JsonObject data){
		JsonObject json = new JsonObject();
		try {
			// 获得对象
			ApplicationConnection appconn = AppConnectionFactory.instance().getConnection(appId);
			if(appconn == null){
				appconn = new ApplicationConnection();
				appconn.setAppId(appId);
				appconn.setAppCaption(appId);
				AppConnectionFactory.instance().registerConnection(appconn);
			}
			// 更新数据
			appconn.fromJson(data);
			AppConnectionFactory.instance().serialize();
			json.addProperty("success", true);
			json.add("object", appconn.toJson());
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 注销应用连接对象
	 * @param appId		应用连接标识
	 * @return
	 */
	@DirectMethod
	public JsonObject deleteAppConnection(String appId){
		JsonObject json = new JsonObject();
		try {
			AppConnectionFactory.instance().unregisterConnection(appId);
			json.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			json.addProperty("success", false);
			json.addProperty("message", e.getMessage());
		}
		return json;
	}
	/**
	 * 右键删除文档权限的一条映射记录
	 * @param appId
	 * @param documentKey
	 * @param authoritymapId
	 * @return
	 */
	@DirectMethod
	public JsonObject deleteAuthoritymap(String appId, String documentKey, String authoritymapId){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().deleteAuthoritymap(appId, documentKey, authoritymapId);
			result.addProperty("success", true);
		} catch (DesignObjectHandlerException e) {
			e.printStack();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 加载外部应用列表
	 * @param appId
	 * @param moduleKey
	 * @return
	 */
	@DirectMethod
	public JsonArray loadGridReferences(String appId, String moduleKey){
		JsonArray records = new JsonArray();
		try {
			records = DesignObjectServiceProxy.instance().loadGridReferences(appId, moduleKey);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return records;
	}
	/**
	 * 文档权限映射的设计对象
	 * @param appId
	 * @param documentKey
	 * @param noContainTypes
	 * @return
	 */
	@DirectMethod
	public JsonArray loadDocCheckTree(String appId, String documentKey, String[] noContainTypes){
		JsonArray datas = new JsonArray();
		try {
			datas = DesignObjectServiceProxy.instance().loadDocCheckTree(appId, documentKey, noContainTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 加载外部引用选择树
	 * @param appId
	 * @param types
	 * @param moduleId
	 * @return
	 */
	@DirectMethod
	public JsonArray loadReferenceCheckObjects(String appId, String[] types, String moduleId){
		JsonArray array = new JsonArray();
		try {
			array = DesignObjectServiceProxy.instance().loadReferenceCheckObjects(appId, types, moduleId);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return array;
	}
	/**
	 * 添加外部引用
	 * @param appId
	 * @param moduleKey
	 * @param ids
	 * @return
	 */
	@DirectMethod
	public JsonObject addReferences(String appId, String moduleKey, String[] ids){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().addReferences(appId, moduleKey, ids);
			result.addProperty("success", true);
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 删除外部引用
	 * @param appId
	 * @param moduleKey
	 * @param id
	 * @return
	 */
	@DirectMethod
	public JsonObject deleteReference(String appId, String moduleKey, String id){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().deleteReference(appId, moduleKey, id);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 从数据库中引入字段
	 * @param appId
	 * @param tableName 就是table设计对象的id
	 * @return
	 */
	@DirectMethod
	public JsonObject buildTableFields(String appId, String tableName){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().buildTableFields(appId, tableName);
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
	 * 加载数据表字段
	 * @param appId
	 * @param formTableLayoutId
	 * @return
	 */
	@DirectMethod
	public JsonArray loadTableFields(String appId, String formId, String type){
		JsonArray datas = new JsonArray();
		try {
			if(type.equals("formtablelayout")||type.equals("formpositionlayout")){
				datas = DesignObjectServiceProxy.instance().loadTableFields(appId, formId, type);
			}else if(type.equals("queryform")){
				datas = DesignObjectServiceProxy.instance().loadQueryFormFields(appId, formId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 加载输入域相关联的数据字段
	 * @param appId
	 * @param id
	 * @param type
	 * @return
	 */
	@DirectMethod
	public JsonArray loadDbFields(String appId, String id, String type){
		JsonArray datas = new JsonArray();
		try {
			JsonObject object = DesignObjectServiceProxy.instance().loadObject(appId, id);
			if(object.has("f_i_table") && null != object.get("f_i_table")){
				datas = DesignObjectServiceProxy.instance().loadObjects(appId, object.get("f_i_table").getAsJsonObject().get("data").getAsString(), type);
			}
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		return datas;
	}
	/**
	 * 引入输入域字段
	 * @param appId
	 * @param id
	 * @param type
	 * @param datas
	 * @return
	 */
	@DirectMethod
	public JsonObject importFields(String appId, String tableFormId, String type, JsonArray datas){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().importFields(appId, tableFormId, type, datas);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 在数据库中创建表
	 * @param appId
	 * @param tableId
	 * @param createTableSql
	 * @return
	 */
	@DirectMethod
	public JsonObject createTable(String appId, String tableId, String createTableSql){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().createTable(appId, tableId, createTableSql);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 在文档身份中引入流程身份
	 * @param appId
	 * @param docId
	 * @return
	 */
	@DirectMethod
	public JsonObject importIdentify(String appId, String docId){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().importIdentify(appId, docId);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
	/**
	 * 文档状态中引入流程节点
	 * @param appId
	 * @param stateId
	 * @return
	 */
	@DirectMethod
	public JsonObject importNodes(String appId, String docId){
		JsonObject result = new JsonObject();
		try {
			DesignObjectServiceProxy.instance().importNodes(appId, docId);
			result.addProperty("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.addProperty("success", false);
			result.addProperty("message", e.getMessage());
		}
		return result;
	}
}
