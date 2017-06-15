package com.asc.developer.designer.navigator.node;

public class CommonTextFolderNode extends BasicNode {
	public static final String TYPE_DATA_ACCESS = "DataAccessFolder";
	public static final String TYPE_MODULES = "ModuleFolder";
	public static final String TYPE_WORKFLOWS = "WorkFlowFolder";
	public static final String TYPE_PORTALS = "PortalFolder";
	
	public static final String TYPE_DATASOURCES = "DataSourceFolder";
	public static final String TYPE_TABLES = "TableFolder";
	public static final String TYPE_DICTIONARY = "DictionaryFolder";
	public static final String TYPE_QUERYS = "QueryFolder";

	public static final String TYPE_MODULE_REFREENCE = "Reference";
	public static final String TYPE_MODULE_MENUS = "ModuleMenuFolder";
	public static final String TYPE_MODULE_PAGES = "PageFolder";
	public static final String TYPE_MODULE_DOCUMENTS = "DocumentFolder";
	public static final String TYPE_MODULE_ROLES = "ModuleRoleFolder";
	public static final String TYPE_MODULE_AUTHORITIES = "ModuleAuthorities";

	public static final String TYPE_PAGE_VIEWS = "ViewFolder";
	public static final String TYPE_PAGE_FORMS = "FormFolder";
	public static final String TYPE_PAGE_TREES = "TreeFolder";
	public static final String TYPE_PAGE_QUERYFORMS = "QueryFormFolder";
	public static final String TYPE_PAGE_LAYOUTS = "LayoutFolder";
	public static final String TYPE_PAGE_URLS = "UrlPageFolder";
	
	public static final String TYPE_PORTAL_PORTLETS = "PortletFolder";
	public static final String TYPE_PORTAL_DOCUMENTTYPES = "DocumentTypeFolder";
	public static final String TYPE_PORTAL_SHORTCUTS = "ShortcutFolder";
	
	public static final String TYPE_FLOW_ROLE = "FlowRoleFolder";
	
	public static final String TYPE_ESB_SERVICE_EVENT = "EsbServiceEventFolder";
	public static final String TYPE_ESB_APPEVENT = "EsbAppEvent";
	public static final String TYPE_ESB_APPSERVICE = "EsbAppService";
	public static final String TYPE_ESB_WORKFLOW = "EsbWorkflowFolder";
	
	public static final String TYPE_ESB = "esb";
	public static final String TYPE_APP = "app";
	
	public static final String TYPE_FILE_IMPORT = "FileImportSchemaForder";
	
	// 节点所属应用标识
	private String appConnId;
	// 节点对象标识
	private String key;
	// 节点类型
	private String type;
	// 节点类型
	private String text;
	// 节点类型
	private boolean leaf;
	/**
	 * 构造函数
	 * @param appConnId
	 * @param type
	 * @param text
	 */
	public CommonTextFolderNode(String type, String appConnId, String key, String text, boolean leaf){
		this.appConnId = appConnId;
		this.type = type;
		this.text = text;
		this.key = key;
		this.leaf = leaf;
	}

	public String getAppId() {
		return appConnId;
	}

	public String getType() {
		return type;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	public String getText() {
		return text;
	}

	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return leaf;
	}

}
