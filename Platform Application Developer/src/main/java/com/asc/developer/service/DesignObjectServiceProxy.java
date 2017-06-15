package com.asc.developer.service;

import java.util.Arrays;

import com.asc.commons.design.DesignObject;
import com.asc.commons.design.DesignObjectFactory;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.engine.design.DesignObjectManager;
import com.asc.commons.engine.exception.DesignObjectHandlerException;
import com.asc.developer.config.AppConnectionFactory;
import com.asc.developer.config.ApplicationConnection;
import com.asc.framework.designobject.service.DesignObjectService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Application Framework/DesignObjectService.java<br>
 * 设计对象存取服务（为远程开发工具提供）<br>
 * 1 装载和查询指定id的设计对象 <br>
 * 2 查询特定条件的设计对象列表<br>
 * 3 保存设计对象<br>
 * 4 删除设计对象
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class DesignObjectServiceProxy {
	private static DesignObjectServiceProxy singleton;
	
	public static DesignObjectServiceProxy instance() {
		if (singleton == null) {
			singleton = new DesignObjectServiceProxy();
		}
		return singleton;
	}
	private String getServiceUrl(String appId) {
		ApplicationConnection conn = AppConnectionFactory.instance().getConnection(appId);
		String host = conn.getAppHost();
		String surl = conn.getServiceUrl();
		StringBuffer sb = new StringBuffer();
		sb.append(host);
		if (!host.endsWith("/") && !surl.startsWith("/")) {
			sb.append("/");
		}
		sb.append(surl);
		if (!surl.endsWith("?wsdl")) {
			sb.append("?wsdl");
		}
		return sb.toString();
	}
	/**
	 * 根据指定数据构造设计对象
	 * 
	 * @param objData 设计对象数据
	 * @return
	 * @throws DesignObjectException 
	 */
	public <T extends DesignObject> T buildObject(JsonObject objData) throws DesignObjectException {
		String typeName = objData.get("f_class").getAsString();
		T obj = DesignObjectFactory.instance().buildDesignObject(typeName);
		obj.update(objData);
		return obj;
	}
	/**
	 * 创建设计对象
	 * @param appId		设计对象所属app标识
	 * @param parentId	设计对象父节点标识
	 * @param type		创建类型
	 * @param key		设计对象标识
	 * @return
	 */
	public JsonObject createObject(String appId, String parentId, String type, String key, JsonObject values) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObject obj = DesignObjectManager.instance().createObject(parentId, type, key, values);
			try {
				return obj.asJsonObject(false);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("创建对象[" + appId + " " + parentId + "." + key + " " + type +"]失败");
			}
		}else{
			try {
				return DesignObjectService.instance().createObject(getServiceUrl(appId), parentId, type, key, values);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("创建对象[" + appId + " " + parentId + "." + key + " " + type +"]失败");
			}
		}
	}
	/**
	 * 删除设计对象
	 * @param appId		设计对象所属app标识
	 * @param id		设计对象标识
	 * @return
	 */
	public void deleteObject(String appId, String id) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().deleteObject(id);
		}else{
			try {
				DesignObjectService.instance().deleteObject(getServiceUrl(appId), id);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("删除对象[" + appId + " " + id +"]失败");
			}
		}
	}
	
	/**
	 * 装载设计对象数据
	 * @param appId
	 * @param id
	 * @return
	 */
	public JsonObject loadObject(String appId, String id) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadObject(id);
		}else{
			try {
				return DesignObjectService.instance().getObject(getServiceUrl(appId), id);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("获取对象[" + appId + " " + id +"]失败");
			}
		}
	}
	/**
	 * 保存设计对象
	 * @param appId
	 * @param datas
	 * @return
	 */
	public JsonObject saveObject(String appId, String id ,JsonObject data) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().saveObject(id, data);
		}else{
			try {
				return DesignObjectService.instance().saveObject(getServiceUrl(appId), id, data);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("保存对象[" + appId + " " + id +"]失败");
			}
		}
	}
	/**
	 * 装载设计对象列表数据
	 * @param appId
	 * @param parentId
	 * @param type
	 * @return
	 */
	public JsonArray loadObjects(String appId, String parentId, String type) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadObjects(parentId, type);
		}else{
			try {
				return DesignObjectService.instance().findObjects(getServiceUrl(appId), parentId, type);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("装载对象树[" + appId + " " + parentId + " " + type +"]失败");
			}
		}
	}
	/**
	 * 装载树结构设计对象
	 * @param appId
	 * @param parentId
	 * @param type
	 * @return
	 */
	public JsonArray loadTreeObjects(String appId, String parentId, String type) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadTreeObjects(parentId, type);
		}else{
			try {
				return DesignObjectService.instance().loadTreeObjects(getServiceUrl(appId), parentId, type);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("装载对象树[" + appId + " " + parentId + " " + type +"]失败");
			}
		}
	}
	/**
	 * 装在当前模块所有设计对象，包括外部引用的设计对象
	 */
	public JsonArray loadCheckTreeObjectsAndRefer(String appId, String moduleKey, String[] noContain) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadCheckTreeObjectsAndRefer(moduleKey, noContain);
		}else{
			try {
				return DesignObjectService.instance().loadCheckTreeObjectsAndRefer(getServiceUrl(appId), moduleKey, noContain);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("装载对象树[" + appId + " " + moduleKey + "]失败");
			}
		}
	}
	
	/**
	 * 装载当前模块下的所有设计对象
	 * @param appId
	 * @return
	 */
	public JsonArray loadCheckTreeObjects(String appId, String moduleKey, String[] noContain) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadCheckTreeObjects(moduleKey, noContain);
		}else{
			try {
				return DesignObjectService.instance().loadCheckTreeObjects(getServiceUrl(appId), moduleKey, noContain);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("装载对象树[" + appId + " " + moduleKey + "]失败");
			}
		}
	}
	/**
	 * 添加模块对象
	 * @param ids
	 * @return
	 */
	public JsonObject addAuthObject(String appId, String moduleKey, String[] ids) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().addAuthObject(moduleKey, ids);
		}else{
			try {
				return DesignObjectService.instance().addAuthObject(getServiceUrl(appId), moduleKey, ids);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("添加模块角色权限[" + appId + " " + moduleKey + "]失败");
			}
		}
	}
	/**
	 * 刷新模块权限列表
	 * @appId
	 * @obj
	 */
	public void updateAuthorities(String appId, String moduleKey, JsonObject object) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().updateAuthorities(moduleKey, object);
		}else{
			try {
				DesignObjectService.instance().updateAuthorities(getServiceUrl(appId), moduleKey, object);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("更新模块角色权限[" + appId + " " + moduleKey + "]失败");
			}
		}
	}
	/**
	 * 获取模块角色列表
	 * @param appId
	 * @return
	 */
	public JsonArray getModuleRole(String appId, String moduleKey) throws DesignObjectHandlerException, DesignObjectException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().getModuleRole(moduleKey);
		}else{
			try {
				return DesignObjectService.instance().getModuleRole(getServiceUrl(appId), moduleKey);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载模块角色列表数据[" + appId + " " + moduleKey + "]失败");
			}
		}
	}
	/**
	 * 加载模块权限列表
	 * @param appId
	 * @return
	 */
	public JsonArray loadGridModuleAuth(String appId, String moduleKey) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadGridObject(moduleKey);
		}else{
			try {
				return DesignObjectService.instance().loadGridModuleAuth(getServiceUrl(appId), moduleKey);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载模块权限列表数据[" + appId + " " + moduleKey + "]失败");
			}
		}
	}
	/**
	 * 加载设计对象列表
	 * @param appId
	 * @param type
	 * @param scope
	 * @return
	 */
	public JsonArray loadGridDesignObject(String appId, String key, String[] type, String scope) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadGridDesignObject(key, type, scope);
		}else{
			try {
				return DesignObjectService.instance().loadGridDesignObject(getServiceUrl(appId), key, type, scope);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("装载模块[" + appId + " " + key + "]中特定类型的【" + Arrays.toString(type) + "】设计对象失败");
			}
		}
	}
	/**
	 * 加载文档设计对象映射表的下拉列表list
	 * @param key
	 * @param mclass
	 * @return
	 */
	public JsonArray getSubObjectList(String appId, String key, String mclass) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().getSubObjectList(key, mclass);
		}else{
			try {
				return DesignObjectService.instance().getSubObjectList(getServiceUrl(appId), key, mclass);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载文档映射权限列表数据[" + appId + " " + key + "]失败");
			}
		}
	}
	/**
	 * 添加文档权限映射表结构树的节点
	 * @param documentKey
	 * @param f_states
	 * @param f_extendstates
	 * @param f_identities
	 * @throws Exception
	 */
	public void addDocumentAuthorityMap(String appId, String documentKey, String f_states, String f_extendstates, String f_identities) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().addDocumentAuthorityMap(documentKey, f_states, f_extendstates, f_identities);
		}else{
			DesignObjectService.instance().addDocumentAuthorityMap(getServiceUrl(appId), documentKey, f_states, f_extendstates, f_identities);
		}
	}
	/**
	 * 加载文档权限映射表的结构树
	 * @param documentKey
	 * @return
	 */
	public JsonArray loadDocumentAuthorityMapTree(String appId, String documentKey) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadDocumentAuthorityMapTree(documentKey);
		}else{
			try {
				return DesignObjectService.instance().loadDocumentAuthorityMapTree(getServiceUrl(appId), documentKey);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载文档[" + appId + " " + documentKey + "]映射权限列结构树数据失败");
			}
		}
	}
	/**
	 * 添加文档设计对象权限 0表示复选框未选中，1表示复选框已经选中
	 * @param documentKey
	 * @param keys
	 */
	public void addDocumentDesignObject(String appId, String documentKey, String[] keys) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().addDocumentDesignObject(documentKey, keys);
		}else{
			try {
				DesignObjectService.instance().addDocumentDesignObject(getServiceUrl(appId), documentKey, keys);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("添加文档设计对象权限[" + appId + " " + documentKey + "]数据失败");
			}
		}
	}
	/**
	 * 加载文档设计对象权限表
	 * @param authoritymapId
	 * @return
	 */
	public JsonArray loadAuthoritymapGrid(String appId, String authoritymapId) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadAuthoritymapGrid(authoritymapId);
		}else{
			try {
				return DesignObjectService.instance().loadAuthoritymapGrid(getServiceUrl(appId), authoritymapId);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载文档设计对象权限表[" + appId + " " + authoritymapId + "]数据失败");
			}
		}
	}
	/**
	 * 根据authoritymapId更新设计文档权限映射表对象
	 * @param authoritymapId
	 * @param obj
	 * @throws DesignObjectHandlerException
	 */
	public void updateAuthoritiesMap(String appId, String authoritymapId, JsonObject obj) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().updateAuthoritiesMap(authoritymapId, obj);
		}else{
			try {
				DesignObjectService.instance().updateAuthoritiesMap(getServiceUrl(appId), authoritymapId, obj);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("更新设计文档权限映射表对象[" + appId + " " + authoritymapId + "]数据失败");
			}
		}
	}
	/**
	 * 删除文档设计对象权限，对应的每个权限规则中，都要删除此文档设计对象
	 * @param documentKey
	 * @param type
	 * @param key
	 * @throws DesignObjectHandlerException
	 */
	public void deleteDocumentAuthority(String appId, String documentKey, String type, String key) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().deleteDocumentAuthority(documentKey, type, key);
		}else{
			try {
				DesignObjectService.instance().deleteDocumentAuthority(getServiceUrl(appId), documentKey, type, key);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("删除文档设计对象权限[" + appId + " " + documentKey + "]的数据失败");
			}
		}
	}
	/**
	 * 删除设计对象映射
	 * @param documentKey
	 * @param authoritymapId
	 * @throws DesignObjectHandlerException
	 */
	public void deleteAuthoritymap(String appId, String documentKey, String authoritymapId) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().deleteAuthoritymap(documentKey, authoritymapId);
		}else{
			try {
				DesignObjectService.instance().deleteAuthoritymap(getServiceUrl(appId), documentKey, authoritymapId);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("删除设计对象映射规则对象[" + appId + " " + documentKey + "]的失败！");
			}
		}
	}
	/**
	 * 加载模块的外部引用列表
	 * @param moduleKey
	 * @return
	 */
	public JsonArray loadGridReferences(String appId, String moduleKey) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadGridReferences(moduleKey);
		}else{
			try {
				return DesignObjectService.instance().loadGridReferences(getServiceUrl(appId), moduleKey);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载模块[" + appId + " " + moduleKey + "]的外部引用列表失败！");
			}
		}
	}
	/**
	 * 加载外部引用带有复选框的可选的树
	 * @param types
	 * @param moduleId
	 * @return
	 * @throws DesignObjectHandlerException
	 */
	public JsonArray loadReferenceCheckObjects(String appId, String[] types, String moduleId) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadReferenceCheckObjects(types, moduleId);
		}else{
			try {
				return DesignObjectService.instance().loadReferenceCheckObjects(getServiceUrl(appId), types, moduleId);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("加载模块[" + appId + " " + moduleId + "]的外部引用树失败！");
			}
		}
	}
	/**
	 * 添加外部引用
	 * @param appId
	 * @param moduleKey
	 * @param ids
	 * @throws DesignObjectHandlerException
	 */
	public void addReferences(String appId, String moduleKey, String[] ids) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().addReferences(moduleKey, ids);
		}else{
			try {
				DesignObjectService.instance().addReferences(getServiceUrl(appId), moduleKey, ids);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("添加模块[" + appId + " " + moduleKey + "]的外部引用失败！");
			}
		}
	}
	/**
	 * 删除外部引用
	 * @param appId
	 * @param moduleKey
	 * @param id
	 * @throws DesignObjectHandlerException
	 */
	public void deleteReference(String appId, String moduleKey, String id) throws DesignObjectHandlerException{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().deleteReference(moduleKey, id);
		}else{
			try {
				DesignObjectService.instance().deleteReference(getServiceUrl(appId), moduleKey, id);
			} catch (DesignObjectException e) {
				throw new DesignObjectHandlerException("删除模块[" + appId + " " + moduleKey + "]的外部引用失败！");
			}
		}
	}
	/**
	 * 从数据库中引入字段
	 * @param appId
	 * @param tableName
	 */
	public void buildTableFields(String appId, String tableName) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().buildTableFields(tableName);
		}else{
			DesignObjectService.instance().buildTableFields(getServiceUrl(appId), tableName);
		}
	}
	/**
	 * 加载数据表字段
	 * @param appId
	 * @param formTableLayoutId
	 * @return
	 * @throws Exception
	 */
	public JsonArray loadTableFields(String appId, String formTableLayoutId, String type) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadTableFields(formTableLayoutId, type);
		}else{
			return DesignObjectService.instance().loadTableFields(getServiceUrl(appId), formTableLayoutId, type);
		}
	}
	/**
	 * 查询表单加载输入域
	 * @param appId
	 * @param formId
	 * @return
	 * @throws Exception
	 */
	public JsonArray loadQueryFormFields(String appId, String formId) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadQueryFormFields(formId);
		}else{
			return DesignObjectService.instance().loadQueryFormFields(getServiceUrl(appId), formId);
		}
	}
	/**
	 * 引入输入域
	 * @param appId
	 * @param tableFormId
	 * @param type
	 * @param datas
	 * @throws Exception
	 */
	public void importFields(String appId, String tableFormId, String type, JsonArray datas) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().importFields(tableFormId, type, datas);
		}else{
			DesignObjectService.instance().importFields(getServiceUrl(appId), tableFormId, type, datas);
		}
	}
	
	/**
	 * 文档权限映射的权限下，所对应设计对象结构树
	 * @param appId
	 * @param documentKey
	 * @param noContainTypes
	 * @return
	 */
	public JsonArray loadDocCheckTree(String appId, String documentKey, String[] noContainTypes) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().loadDocCheckTree(documentKey, noContainTypes);
		}else{
			return DesignObjectService.instance().loadDocCheckTree(getServiceUrl(appId), documentKey, noContainTypes);
		}
	}
	/**
	 * 在数据库中创建表
	 * @param appId
	 * @param tableId
	 * @param createTableSql
	 */
	public void createTable(String appId, String tableId, String createTableSql) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().createTable(tableId, createTableSql);
		}else{
			DesignObjectService.instance().createTable(getServiceUrl(appId), tableId, createTableSql);
		}
	}
	/**
	 * 文档身份中引入流程身份
	 * @param appId
	 * @param docId
	 * @throws DesignObjectException
	 */
	public void importIdentify(String appId, String docId) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().importIdentify(docId);
		}else{
			DesignObjectService.instance().importIdentify(getServiceUrl(appId), docId);
		}
	}
	/**
	 * 文档状态中引入流程节点
	 * @param appId
	 * @param stateId
	 * @throws Exception
	 */
	public void importNodes(String appId, String docId) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().importNodes(docId);
		}else{
			DesignObjectService.instance().importNodes(getServiceUrl(appId),docId);
		}
	}
	/**
	 * 获取流程的信息
	 * @param key
	 * @param versionId
	 * @param refresh
	 * @return
	 */
	public JsonObject getFlowCells(String appId, String key, int versionId, boolean refresh) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().getFlowCells(key, versionId, refresh);
		}else{
			return DesignObjectService.instance().getFlowCells(getServiceUrl(appId), key, versionId, refresh);
		}
	}
	/**
	 * 锁定流程
	 * 
	 * @param key
	 * @param versionId
	 * @param locked
	 * @throws Exception 
	 */
	public void setFlowLocked(String appId, String key, int versionId, boolean locked) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().setFlowLocked(key, versionId, locked);
		}else{
			DesignObjectService.instance().setFlowLocked(getServiceUrl(appId), key, versionId, locked);
		}
	}
	/**
	 * 保存流程节点位置
	 * 
	 * @param key
	 * @param versionId
	 * @param posList
	 * @throws DesignObjectException 
	 */
	public void saveFlowPosition(String appId, String key, int versionId, JsonObject posList, JsonObject routeList) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().saveFlowPosition(key, versionId, posList, routeList);
		}else{
			DesignObjectService.instance().saveFlowPosition(getServiceUrl(appId), key, versionId, posList, routeList);
		}
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
	public JsonObject insertNode(String appId, String key, int versionId, String sourceKey, int x, int y) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().insertNode(key, versionId, sourceKey, x, y);
		}else{
			return DesignObjectService.instance().insertNode(getServiceUrl(appId), key, versionId, sourceKey, x, y);					
		}
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
	public String insertRoute(String appId, String key, int versionId, String sourceNodeKey, String targetNodeKey) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().insertRoute(key, versionId, sourceNodeKey, targetNodeKey);
		}else{
			return DesignObjectService.instance().insertRoute(getServiceUrl(appId), key, versionId, sourceNodeKey, targetNodeKey);
		}
	}
	/**
	 * 删除节点或路由
	 * @param appId
	 * @param key
	 * @param versionId
	 * @param objKey
	 * @throws Exception
	 */
	public void removeCell(String appId, String key, int versionId, String objKey) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			DesignObjectManager.instance().removeCell(key, versionId, objKey);
		}else{
			DesignObjectService.instance().removeCell(getServiceUrl(appId), key, versionId, objKey);
		}
	}
	/**
	 * 更新流程标题
	 * 
	 * @param key
	 * @param caption
	 * @return
	 */
	public JsonObject updateCaption(String appId, String key, int versionId, String caption) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().updateCaption(key, versionId, caption);
		}else{
			return DesignObjectService.instance().updateCaption(getServiceUrl(appId), key, versionId, caption);
		}
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
	public String updateRouteTarget(String appId, String key, int versionId, String routeKey, String targetNodeKey) throws Exception{
		if(appId == null || "".equals(appId)){
			throw DesignObjectHandlerException.forAppNotFound(appId);
		}
		if(AppConnectionFactory.instance().isLocal(appId)){
			return DesignObjectManager.instance().updateRouteTarget(key, versionId, routeKey, targetNodeKey);
		}else{
			return DesignObjectService.instance().updateRouteTarget(getServiceUrl(appId), key, versionId, routeKey, targetNodeKey);
		}
	}
}
