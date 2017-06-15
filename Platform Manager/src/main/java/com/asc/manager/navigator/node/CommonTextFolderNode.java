package com.asc.manager.navigator.node;

public class CommonTextFolderNode extends BasicNode {
	
	public static final String TYPE_APPLICATION_REGISTER = "ApplicationRegisterFolder";
	public static final String TYPE_APPLICATION_LIST = "ApplicationsFolder";
	public static final String TYPE_DATABASE_LIST = "DatabasesFolder";
	public static final String TYPE_DATABASE_CONFIGURATION_LIST = "DatabasesConfigurationFolder";
	public static final String TYPE_DATABASE_EXCHANGE_LIST = "DatabasesExchangeFolder";
	
	public static final String TYPE_BASE_DATA_MANAGER = "BaseDataManagerFolder";
	public static final String TYPE_ORGANIZATION_FOLDER = "OrganizationFolder";
	public static final String TYPE_ORGANIZATION_MANAGER = "OrganizationManager";
	public static final String TYPE_ROLE_MANAGER = "RoleManager";
	public static final String TYPE_USER_MANAGER = "UserManager";
	public static final String TYPE_ORGINAZATION_USER_MANAGER = "OrganizationUserManager";
	public static final String TYPE_ROLE_USER_MANAGER = "RoleUserManager";
	public static final String TYPE_DICTIONARY_FOLDER = "DictionaryFolder";
	public static final String TYPE_DICTIONARY_MANAGER = "DictionaryManager";
	public static final String TYPE_DICTIONARY_DATA_MANAGER = "DictionaryDataManager";
	public static final String TYPE_DICDATA_RELATION_MANAGER = "DicdataRelationManager";
	public static final String TYPE_DICDATA_RELATION_DATA_MANAGER = "DicdataRelationDataManager";
	
	public static final String TYPE_APPLICATION_CONFIG = "ApplicationConfigFolder";
	public static final String TYPE_APPLICATION_NAVIGATOR = "ApplicationEntranceFolder";
	public static final String TYPE_APPLICATION_NAVIGATOR_MANAGER = "ApplicationEntranceManager";
	public static final String TYPE_APPLICATION_NAVIGATOR_AUTHORITY = "ApplicationEntranceAuthority";
	public static final String TYPE_APPLICATION_PORTAL_PROFILE = "ApplicationPortalProfile";
	public static final String TYPE_APPLICATION_MODULE_FOLDER = "ApplicationModuleFolder";
	public static final String TYPE_APPLICATION_MODULE_MENU = "ApplicationModuleMenu";
	public static final String TYPE_APPLICATION_MODULE_ROLE_MAPPING = "ApplicationModuleRoleMapping";
	public static final String TYPE_APPLICATION_MODULE_ROLE_AUTHORITY = "ApplicationModuleRoleAuthorith";
	public static final String TYPE_APPLICATION_MODULE_DF_SETTINGS = "ApplicationModuleDfSettings";
	public static final String TYPE_APPLICATION_WORKFLOW_FOLDER = "ApplicationWorkflowFolder";
	public static final String TYPE_APPLICATION_WORKFLOW_CONFIG = "ApplicationWorkflowConfig";
	public static final String TYPE_APPLICATION_WORKFLOW_ROLE = "ApplicationWorkflowRole";
	public static final String TYPE_APPLICATION_SCHEDULE_TASK = "ApplicationScheduleTask";
	public static final String TYPE_APPLICATION_DICTIONARY_FOLDER = "ApplicationDictionaryFolder";
	public static final String TYPE_APPLICATION_DICTIONARY_OVERRIDE = "ApplicationDictionaryOverride";
	public static final String TYPE_APPLICATION_DICTIONARY_DATA_OVERRIDE = "ApplicationDictionaryDataOverride";
	
	public static final String TYPE_SERVICE_PLATFORM_CONFIG = "ServicePlatformConfigFolder";
	public static final String TYPE_APPLICATION_ESB_INTERFACE = "ApplicationEsbInterface";
	public static final String TYPE_ESB_MESSAGE = "EsbMessage";
	public static final String TYPE_ESB_FLOW = "EsbFlow";
	public static final String TYPE_SP_SCHEDULE_TASK = "SpScheduleTask";
	
	public static final String TYPE_APPLICATION_MANAGER = "ApplicationManagerFolder";
	public static final String TYPE_APPLICATION_STATE_MANAGER = "ApplicationStateManager";
	public static final String TYPE_APPLICATION_CACHE_MANAGER = "ApplicationCacheManager";
	public static final String TYPE_APPLICATION_FLOW_INSTANCE = "ApplicationFlowInstance";
	public static final String TYPE_APPLICATION_LOG_FOLDER = "ApplicationLogFolder";
	public static final String TYPE_APPLICATION_SYS_LOG = "ApplicationSysLog";
	public static final String TYPE_APPLICATION_USER_LOG = "ApplicationUserLog";
	public static final String TYPE_APPLICATION_MANAGER_LOG = "ApplicationManagerLog";
	public static final String TYPE_APPLICATION_CAS_LOG = "ApplicationCasLog";
	
	
	public static final String TYPE_SERVICE_PLATFORM_MANAGER = "ServicePlatformManagerFolder";
	public static final String TYPE_ESB_FLOW_INSTANCE = "EsbFlowInstance";
	public static final String TYPE_SP_CACHE_MANAGER = "SpCacheManager";
	public static final String TYPE_SP_LOG_MANAGER = "SpLogManager";
	public static final String TYPE_APPLICATION_LOGCONF_FOLDER = "LogConf";
	
	public static final String TYPE_DATA_EXCHANGE_TABLE_FOLDER = "ExchangeTable";//内外网数据交换的数据表
	public static final String TYPE_DATA_EXCHANGE_FILE_FOLDER = "ExchangeFile";//内外网数据交换的文件
	
	// 节点对象标识
	private String key;
	// 节点类型
	private String type;
	// 节点名称
	private String text;
	// 是否叶子节点
	private boolean leaf;
	/**
	 * 构造函数
	 * @param appConnId
	 * @param type
	 * @param text
	 */
	public CommonTextFolderNode(String type, String key, String text, boolean leaf){
		this.type = type;
		this.text = text;
		this.key = key;
		this.leaf = leaf;
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
