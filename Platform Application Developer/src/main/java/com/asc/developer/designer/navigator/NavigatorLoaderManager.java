package com.asc.developer.designer.navigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asc.commons.design.DesignObjectFactory;
import com.asc.commons.engine.design.entity.db.DataSource;
import com.asc.commons.engine.design.entity.db.Query;
import com.asc.commons.engine.design.entity.db.StaticDictionary;
import com.asc.commons.engine.design.entity.db.Table;
import com.asc.commons.engine.design.entity.module.Document;
import com.asc.commons.engine.design.entity.module.LayoutPage;
import com.asc.commons.engine.design.entity.module.Module;
import com.asc.commons.engine.design.entity.module.ModuleMenu;
import com.asc.commons.engine.design.entity.module.ModuleRole;
import com.asc.commons.engine.design.entity.module.QueryFormPage;
import com.asc.commons.engine.design.entity.module.TableForm;
import com.asc.commons.engine.design.entity.module.TreePage;
import com.asc.commons.engine.design.entity.module.UrlPage;
import com.asc.commons.engine.design.entity.module.View;
import com.asc.developer.config.AppConnectionFactory;
import com.asc.developer.config.ApplicationConnection;
import com.asc.developer.designer.exception.NavigatorException;
import com.asc.developer.designer.navigator.loader.AppConnectionLoader;
import com.asc.developer.designer.navigator.loader.DataAccessLoader;
import com.asc.developer.designer.navigator.loader.DesignObjectNodeLoader;
import com.asc.developer.designer.navigator.loader.EsbAppLoader;
import com.asc.developer.designer.navigator.loader.ModuleNodeLoader;
import com.asc.developer.designer.navigator.loader.ModulePageLoader;
import com.asc.developer.designer.navigator.loader.PortalLoader;
import com.asc.developer.designer.navigator.loader.RootLoader;
import com.asc.developer.designer.navigator.loader.TableFormLoader;
import com.asc.developer.designer.navigator.loader.WorkFlowNodeLoader;
import com.asc.developer.designer.navigator.node.AppConnectionNode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;
import com.asc.framework.importfile.design.entity.FileImportSchema;
import com.asc.workflow.core.template.Flow;
import com.asc.workflow.core.template.FlowRole;
import com.asc.workflow.core.template.Node;
import com.asc.workflow.core.template.Route;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class NavigatorLoaderManager {
	private static NavigatorLoaderManager singleton;
	private Map<String, ILoader> loaders;
	
	public NavigatorLoaderManager(){
		loaders = new HashMap<String, ILoader>();
		/**
		List<Class<?>> classes = ClassUtil.scanPackage(this.getClass().getPackage().getName() + ".loader");
		for(int i=0;i<classes.size();i++){
			try {
				this.addLoader((ILoader) classes.get(i).newInstance());
				logger.info("开发工具导航节点装载器【" + classes.get(i).getName() + "】初始化完毕。");
			} catch (Exception e) {
				logger.info("开发工具导航节点装载器【" + classes.get(i).getName() + "】初始化失败。");
			}
		}
		**/
		// 根节点
		this.addLoader(new RootLoader());
		// 应用连接
		this.addLoader(new AppConnectionLoader());
		//esb流程列表
		//this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_ESB_WORKFLOW, DesignObjectFactory.instance().getTypeName(ServiceFlow.class)));
		//this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(ServiceFlow.class), DesignObjectFactory.instance().getTypeName(SfNode.class)));
		//this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(SfNode.class), DesignObjectFactory.instance().getTypeName(SfRoute.class)));
		// 数据访问
		this.addLoader(new DataAccessLoader());
		// 数据源
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_DATASOURCES, DesignObjectFactory.instance().getTypeName(DataSource.class)));
		// 数据表
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_TABLES, DesignObjectFactory.instance().getTypeName(Table.class)));
		// 静态数据字典列表
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_DICTIONARY, DesignObjectFactory.instance().getTypeName(StaticDictionary.class)));
		// 静态数据字典
		//this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(StaticDictionary.class), DesignObjectFactory.instance().getTypeName(StaticDictionaryData.class)));
		// 静态数据字典
		//this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(StaticDictionaryData.class), DesignObjectFactory.instance().getTypeName(StaticDictionaryData.class)));
		// 数据检索
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_QUERYS, DesignObjectFactory.instance().getTypeName(Query.class)));
		// 模块列表
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_MODULES, DesignObjectFactory.instance().getTypeName(Module.class)));
		
		// 模块
		this.addLoader(new ModuleNodeLoader());
		// 模块菜单列表
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_MODULE_MENUS, DesignObjectFactory.instance().getTypeName(ModuleMenu.class)));
		// 模块菜单
		this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(ModuleMenu.class), DesignObjectFactory.instance().getTypeName(ModuleMenu.class)));
		// 页面视图
		this.addLoader(new ModulePageLoader());
		// 视图页面
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PAGE_VIEWS, DesignObjectFactory.instance().getTypeName(View.class)));
		// 表单页面
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PAGE_FORMS, DesignObjectFactory.instance().getTypeName(TableForm.class)));
		// 树页面
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PAGE_TREES, DesignObjectFactory.instance().getTypeName(TreePage.class)));
		// 表单页面布局
		this.addLoader(new TableFormLoader());
		// 查询表单
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PAGE_QUERYFORMS, DesignObjectFactory.instance().getTypeName(QueryFormPage.class)));
		// 布局页面
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PAGE_LAYOUTS, DesignObjectFactory.instance().getTypeName(LayoutPage.class)));
		// HTTP页面
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PAGE_URLS, DesignObjectFactory.instance().getTypeName(UrlPage.class)));
		
		// 文档列表
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_MODULE_DOCUMENTS, DesignObjectFactory.instance().getTypeName(Document.class)));
		// 模块角色
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_MODULE_ROLES, DesignObjectFactory.instance().getTypeName(ModuleRole.class)));
		// 流程
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_WORKFLOWS, DesignObjectFactory.instance().getTypeName(Flow.class)));
		// 节点和流程角色列表
		//this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(Flow.class), DesignObjectFactory.instance().getTypeName(Node.class)));
		this.addLoader(new WorkFlowNodeLoader());
		//流程角色
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_FLOW_ROLE, DesignObjectFactory.instance().getTypeName(FlowRole.class)));
		// 路由
		this.addLoader(new DesignObjectNodeLoader(DesignObjectFactory.instance().getTypeName(Node.class), DesignObjectFactory.instance().getTypeName(Route.class)));
		// 门户
		this.addLoader(new PortalLoader());
		// TODO 桌面栏目
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PORTAL_PORTLETS, "portlet"));
		//DesignObjectFactory.instance().getTypeName(Portlet.class)));
		// TODO 文档类型
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PORTAL_DOCUMENTTYPES, "documenttype"));
		// DesignObjectFactory.instance().getTypeName(DocumentType.class)));
		// TODO 快捷方式
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_PORTAL_SHORTCUTS, "shortcut"));
		//DesignObjectFactory.instance().getTypeName(Shortcut.class)));
		//ESB业务流 的应用事件和应用服务
		this.addLoader(new EsbAppLoader());
		this.addLoader(new DesignObjectNodeLoader(CommonTextFolderNode.TYPE_FILE_IMPORT, DesignObjectFactory.instance().getTypeName(FileImportSchema.class)));
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
		String appId = node.has("appId") ? node.get("appId").getAsString() : null;
		String key = node.has("key") ? node.get("key").getAsString() : null;
		// 获得节点装载器
		ILoader loader = this.getLoader(type);
		if(loader == null){
			// 未找到匹配的装载器
			 throw NavigatorException.forNodeLoaderNotFound(type);
			
		}else{
			// 装载子节点
			if(AppConnectionNode.TYPE.equals(type)){
				ApplicationConnection conn = AppConnectionFactory.instance().getConnection(appId);
				if(null == conn){
					throw new NavigatorException("链接【" + appId + "】不存在！");
				}
				String appType = conn.getAppType();
				if(null == appType || "".equals(appType)){
					appType = CommonTextFolderNode.TYPE_APP;
				}
				List<INode> nodes = ((AppConnectionLoader)loader).getChildren(appId, key, appType);
				for(int i=0;i<nodes.size();i++){
					results.add(nodes.get(i).toJson());
				}
			}else{
				List<INode> nodes = loader.getChildren(appId, key);
				for(int i=0;i<nodes.size();i++){
					results.add(nodes.get(i).toJson());
				}
			}
		}
		return results;
	}
}
