
//=================================================================
//	�ļ�����AppEntAuth.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_app_ent_auth',
	// 名称
	caption : '应用导航',
	// 属性定义
	properties : [{
		xtype : 'treecolumn',
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		defaultValue : 'appent',
		description : '应用导航标识，不可重复。'
	}, {
		name : 'f_order',
		text : '序号',
		editor : 'none',
		defaultValue : 0,
		description : '应用导航序号。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'none',
		defaultValue : 0,
		description : '应用导航级别。'
	}, {
		name : 'f_caption',
		text : '名称',
		editor : 'string',
		defaultValue : '导航名称',
		description : '应用导航名称。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		defaultValue : 'app1',
		description : '相关应用标识'
	}, {
		name : 'f_module_key',
		text : '模块标识',
		editor : 'string',
		defaultValue : 'app1',
		description : '相关模块标识'
	}, {
		name : 'f_disabled',
		text : '是否可用',
		editor : 'combo',
		store : [[0, '否'], [1, '是']],
		defaultValue : 1,
		description : '应用导航是否可用。'
	}, {
		name : 'f_icon',
		text : '图标地址',
		editor : 'string',
		defaultValue : '',
		description : '图标地址。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'none',
		defaultValue : '',
		description : '说明信息。'
	}, {
		name : 'f_auth_expression',
		text : '权限表达式',
		editor : 'none',
		defaultValue : '',
		description : '权限表达式。'
	}, {
		name : 'f_auth_display',
		text : '权限',
		editor : 'none',
		defaultValue : '',
		description : '权限。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:200},
		'f_caption':{width:120},
		'f_auth_display' : {flex:1}
	}
});
//=================================================================
//	�ļ�����AppEntAuthManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationEntranceAuthority', 
	// 名称
	caption : '导航菜单',
	// 编辑器定义
	editors : [{
		title : '导航菜单',
		jspUrl : 'manager/appentrance/appent.auth.manager',
		iconCls : 'icon-manager-applicationentrancemanager'
	}]
});
//=================================================================
//	�ļ�����Application.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_application',
	// 名称
	caption : '应用系统',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		defaultValue : 'app',
		description : '应用系统标识，不可重复。'
	}, {
		name : 'f_caption',
		text : '应用名称',
		editor : 'string',
		defaultValue : '应用名称',
		description : '指定按钮对象xtype，用于生成按钮界面对象的类引用名。'
	}, {
		name : 'f_location',
		text : '位置',
		editor : 'combosimple',
		editorCfg : {
			allowEmpty : true
		},
		store : ['内网', '外网'],
		description : '应用部署所在位置。'
	}, {
		name : 'f_deploy_domain',
		text : '部署地址',
		editor : 'string',
		description : '部署地址。'
	}, {
		name : 'f_domain',
		text : '域名',
		editor : 'string',
		description : '应用访问域名。'
	}, {
		name : 'f_host',
		text : '主机地址',
		editor : 'string',
		description : '应用主机IP地址。'
	}, {
		name : 'f_port',
		text : '端口号',
		editor : 'number',
		description : '应用端口号。'
	}, {
		name : 'f_app_path',
		text : '应用路径',
		editor : 'string',
		description : '应用发布路径。'
	},{
		name : 'f_state',
		text : '运行状态',
		editor : 'combo',
		editorCfg : {
			disabled : true
		},
		store : [[0, '正在初始化'], [1, '已停止'], [2, '正在运行'],[3, '已挂起'],[4, '未知状态'],[5, '正在运行']],
		description : '应用的状态（有停止、启动和暂停响应三种状态）。'
	}, {
		name : 'f_cluster',
		text : '集群',
		editor : 'combo',
		store : [[0, '否'], [1, '是']],
		description : '应用是否集群配置。'
	}, {
		name : 'f_group',
		text : '分组',
		editor : 'string',
		description : '应用分组，多个分组之间使用";"分隔'
	}, {
		name : 'f_available',
		text : '是否可用',
		editor : 'combo',
		store : [[1, '是'], [0, '否']],
		description : '应用是否可用。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_key':{width:120},
		'f_caption':{width:180},
		'f_location':{width:50},
		'f_domain':{width:200,flex:1},
		'f_host':{width:150},
		'f_port':{width:60},
		'f_app_path':{width:100},
		'f_state':{width:120, renderer : function(v, metaData, record){
			var state = record.get('f_state');
			switch(state){
			case 0:
				metaData.style = 'color:#0000EE;';
				v = '正在初始化';
				break;
			case 1:
				metaData.style = 'color:red;';
				v = '停止';
				break;
			case 2:
				metaData.style = 'color:#00CD00;';
				v = '正在运行';
				break;
			case 3:
				metaData.style = 'color:#CD8500;';
				v = '已挂起';
				break;
			case 4:
				metaData.style = 'color:#9C9C9C;';
				v = '未知状态';
				break;
			case 5:
				metaData.style = 'color:#00CD00;';
				v = '正在运行';
				break;
			default:
				return '';
				break;
			}
			return v;
		},hidden:true},
		'f_cluster':{width:50},
		'f_group':{width:100},
		'f_available':{width:50},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����ApplicationCasLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationCasLog',
	// 名称
	caption : '认证服务日志',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/log/loglist']
});
//=================================================================
//	�ļ�����ApplicationEntrance.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_application_entrance',
	// 名称
	caption : '应用导航',
	// 属性定义
	properties : [{
		xtype : 'treecolumn',
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		defaultValue : 'appent',
		description : '应用导航标识，不可重复。'
	}, {
		name : 'f_order',
		text : '序号',
		editor : 'none',
		defaultValue : 0,
		description : '应用导航序号。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'none',
		defaultValue : 0,
		description : '应用导航级别。'
	}, {
		name : 'f_caption',
		text : '名称',
		editor : 'string',
		defaultValue : '导航名称',
		description : '应用导航名称。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		defaultValue : 'app1',
		description : '相关应用标识'
	}, {
		name : 'f_module_key',
		text : '模块标识',
		editor : 'string',
		defaultValue : 'app1',
		description : '相关模块标识'
	}, {
		name : 'f_disabled',
		text : '是否可用',
		editor : 'combo',
		store : [[0, '否'], [1, '是']],
		defaultValue : 1,
		description : '应用导航是否可用。'
	}, {
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		defaultValue : '',
		description : '图标。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'string',
		defaultValue : '',
		description : '说明信息。'
	}, {
		name : 'f_script',
		text : '脚本',
		editor : 'text',
		defaultValue : '',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:200},
		'f_caption':{width:120},
		'f_icon' : {width:120},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����ApplicationEntranceManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationEntranceManager',
	// 名称
	caption : '导航菜单',
	// 编辑器定义
	editors : [{
		title : '导航菜单',
		jspUrl : 'manager/appentrance/application.entrance.manager',
		iconCls : 'icon-manager-applicationentrancemanager'
	}]
});
//=================================================================
//	�ļ�����ApplicationInstance.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_application_instance',
	// 名称
	caption : '应用实例',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		defaultValue : 'appnode',
		description : '应用系统标识，不可重复。'
	}, {
		name : 'f_serialnumber',
		text : '编号',
		editor : 'none',
		defaultValue : 0,
		description : '应用实例节点编号。'
	}, {
		name : 'f_ip',
		text : '主机地址',
		editor : 'string',
		description : '应用主机IP地址。'
	}, {
		name : 'f_port',
		text : '端口号',
		editor : 'number',
		defaultValue : 80,
		description : '应用端口号。'
	}, {
		name : 'f_app_path',
		text : '应用路径',
		editor : 'string',
		description : '应用发布路径。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:100},
		'f_ip':{width:120},
		'f_port':{width:60},
		'f_app_path':{width:120},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����ApplicationManagerLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationManagerLog',
	// 名称
	caption : '应用管理平台日志',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/log/loglist']
});
//=================================================================
//	�ļ�����ApplicationModuleDfSettings.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationModuleDfSettings', 
	// 名称
	caption : '数据权限设置',
	// 编辑器定义
	editors : [{
		title : '数据权限设置',
		jspUrl : 'manager/datafilter/policy.manager',
		iconCls : 'icon-manager-ApplicationModuleRoleAuthorith'
	}]
});
//=================================================================
//	�ļ�����ApplicationModuleRoleAuthorith.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationModuleRoleAuthorith', 
	// 名称
	caption : '模块角色权限',
	// 编辑器定义
	editors : [{
		title : '模块角色权限',
		jspUrl : 'manager/modulerole/rolemapping.manager',
		iconCls : 'icon-manager-ApplicationModuleRoleAuthorith'
	}, {
		title : 'CDRU配模块角色',
		jspUrl : 'manager/modulerole/rolemapping.manager2',
		iconCls : 'icon-manager-ApplicationModuleRoleAuthorith'
	}]
});
//=================================================================
//	�ļ�����ApplicationRegisterFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationsFolder',
	// 名称
	caption : '应用注册',
	// 编辑器定义
	editors : [{
		title : '应用配置',
		jspUrl : 'manager/appregister/application.manager',
		iconCls : 'icon-manager-application'
	},{
		title : '集群节点',
		jspUrl : 'manager/appregister/applicationinstance.manager',
		iconCls : 'icon-manager-applicationinstance'
	}]
});
//=================================================================
//	�ļ�����ApplicationStateManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationStateManager',
	// 名称
	caption : '应用运行状态',
	// 属性定义
	// 编辑器定义
	editors : [{
		title : '运行状态管理',
		jspUrl : 'manager/appstate/application.state'
	}]
});
//=================================================================
//	�ļ�����ApplicationSysLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationSysLog',
	// 名称
	caption : '系统日志',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/log/log.tree']
});
//=================================================================
//	�ļ�����ApplicationUserLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationUserLog',
	// 名称
	caption : '用户系统日志',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/log/log.tree']
});
//=================================================================
//	�ļ�����ApplicationWorkflowRole.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationWorkflowRole', 
	// 名称
	caption : '流程角色权限',
	// 编辑器定义
	editors : [{
		title : '流程角色映射',
		jspUrl : 'manager/workflowrole/rolemapping.manager',
		iconCls : 'icon-manager-applicationmodulerolemapping'
	}]
});
//=================================================================
//	�ļ�����AppPortalProfile.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_app_portalprofile',
	// 名称
	caption : '应用导航',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		defaultValue : 'appent',
		description : '门户模板标识，不可重复。'
	}, {
		name : 'f_order',
		text : '序号',
		editor : 'none',
		defaultValue : 0,
		description : '门户模板序号。'
	}, {
		name : 'f_caption',
		text : '名称',
		editor : 'string',
		defaultValue : '门户模板',
		description : '门户模板名称。'
	}, {
		name : 'f_scope_expression',
		text : '权限表达式',
		editor : 'hidden',
		defaultValue : '',
		description : '权限表达式。'
	}, {
		name : 'f_scope_display',
		text : '权限',
		editor : 'none',
		defaultValue : '',
		description : '权限。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_caption':{width:120},
		'f_scope_display' : {width:200,flex:1}
	}
});
//=================================================================
//	�ļ�����AppPortalProfileManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ApplicationPortalProfile', 
	// 名称
	caption : '导航菜单',
	// 编辑器定义
	editors : [{
		title : '导航菜单',
		jspUrl : 'manager/appportalprofile/portalprofile.manager',
		iconCls : 'icon-manager-applicationportalprofile.png'
	}]
});
//=================================================================
//	�ļ�����CasLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'CasLog',
	// 名称
	caption : '认证服务日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_node_id',
		text : '节点',
		editor : 'string',
		description : '节点。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_id',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_resource_id',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_action_name',
		text : '操作名称',
		editor : 'string',
		description : '操作名称。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'combo',
		store : [[0,'致命错误'],[3,'错误'],[4,'警告'],[6,'普通信息'],[7,'调试输出']],
		description : '级别。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_detail',
		text : '详细信息',
		editor : 'string',
		description : '详细日志。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}, {
		name : 'f_successed',
		text : '认证结果',
		editor : 'combo',
		store : [[0,'未知'],[1,'成功'],[2,'失败']],
		description : '用户登录认证的结果。'
	}, {
		name : 'f_user_id',
		text : '操作用户id',
		editor : 'number',
		description : '操作用户的id标识。'
	}, {
		name : 'f_user_name',
		text : '用户名',
		editor : 'string',
		description : '操作用户名。'
	}, {
		name : 'f_user_host',
		text : '用户主机地址',
		editor : 'string',
		description : '操作用户主机地址。'
	}, {
		name : 'f_user_agent',
		text : '用户代理',
		editor : 'string',
		description : '用户代理。'
	},{
		name : 'f_terminal_type',
		text : '终端类型',
		editor : 'string',
		description : '终端类型。'
	},{
		name : 'f_ticket_id',
		text : '用户令牌',
		editor : 'string',
		description : '用户令牌登陆时产生的令牌。'
	},{
		name : 'f_request_url',
		text : '请求地址',
		editor : 'string',
		description : '用户请求的地址。'
	},{
		name : 'f_create_time',
		text : '操作时间',
		editor : 'string',
		description : '操作时间。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_node_id':{hidden:true},
		'f_type':{width:80,hidden:true},
		'f_level':{width:80},
		'f_application_id':{hidden:true},
		'f_create_time':{width:150},
		'f_resource_id':{hidden:true},
		'f_action_name':{width:60},
		'f_user_id':{hidden:true},
		'f_user_name':{width:70},
		'f_request_url':{width:150,flex:1},
		'f_user_host':{width:130},
		'f_user_agent':{width:200,flex:1},
		'f_successed':{width:80,renderer : function(v, metaData, record, rowIndex){
			var result = record.get('f_successed');
			switch(result){
			case 0:
				metaData.style = 'color:#A3A3A3;';
				v = '未知';
				break;
			case 1:
				v = '成功';
				break;
			case 2:
				metaData.style = 'color:#F08080;';
				v = '失败';
				break;
			default:
				return '';
				break;
			}
			return v;
		}},
		'f_terminal_type':{width:100},
		'f_ticket_id':{hidden:true},
		'f_threadid':{width:170},
		'f_log':{width:80},
		'f_detail':{hidden:true}
	}
});
//=================================================================
//	�ļ�����Database.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_database',
	// 名称
	caption : '字典列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '数据库标识',
		editor : 'string',
		description : '数据库标识。'
	}, {
		name : 'f_caption',
		text : '数据库名称',
		editor : 'string',
		description : '数据库名称。'
	}, {
		name : 'f_type',
		text : '数据库类型',
		editor : 'string',
		description : '数据库类型。'
	},{
		name : 'f_ip',
		text : 'IP',
		editor : 'string',
		description : '数据库IP地址。'
	},{
		name : 'f_port',
		text : '端口',
		editor : 'number',
		description : '数据库端口。'
	},{
		name : 'f_url',
		text : 'URL',
		editor : 'string',
		description : '数据库访问地址。'
	},{
		name : 'f_location',
		text : '位置',
		editor : 'combo',
		defaultValue: '内网',
		store : ['内网', '外网'],
		description : '数据库位置。'
	},{
		name : 'f_user',
		text : '用户名',
		editor : 'string',
		description : '数据库用户名。'
	},{
		name : 'f_password',
		text : '密码',
		editor : 'string',
		description : '数据库密码。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:120},
		'f_caption':{width:100},
		'f_type':{width:140},
		'f_ip':{width:80},
		'f_location':{width:100},
		'f_port':{width:40},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����DatabasesConfigurationFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DatabasesConfigurationFolder',
	// 名称
	caption : '数据库实例配置',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/appregister/databasesconf']
});
//=================================================================
//	�ļ�����DatabasesExchange.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_database_sync',
	// 名称
	caption : '内外网交换配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_inner_database',
		text : '内网',
		editor : 'none',
		description : '内网名称。'
	}, {
		name : 'f_outer_database',
		text : '外网',
		editor : 'none',
		description : '外网名称。'
	}, {
		name : 'f_inner_ip',
		text : '内网地址',
		editor : 'string',
		description : '内网地址。'
	}, {
		name : 'f_outer_ip',
		text : '外网地址',
		editor : 'string',
		description : '外网地址。'
	}, {
		name : 'f_synchronou_strategy',
		text : '同步策略',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '全部同步'], [1, '仅同步已选表'], [2, '仅排除已选表']],
		description : '内外网同步策略。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_inner_database':{width:120},
		'f_outer_database':{width:100},
		'f_inner_ip':{width:100},
		'f_outer_ip':{width:100},
		'f_synchronou_strategy':{
			flex:1,
			editor: new Ext.form.field.ComboBox({
                typeAhead: false,
        		editable : false,
        		defaultValue: 0,
                triggerAction: 'all',
                store: [
                    [0,'全部同步'],
                    [1,'仅同步已选表'],
                    [2,'仅排除已选表']
                ]
			})
		}
	}
});
//=================================================================
//	�ļ�����DatabasesExchangeFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DatabasesExchangeFolder',
	// 名称
	caption : '内外网交换',
	// 是否设计对象
	isDesignObject : false
	// 编辑器定义
	//editors : ['manager/appregister/databasesexchange']
	//editors : ['manager/edi/data.exchange']
});
//=================================================================
//	�ļ�����DatabasesExchangeTable.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_database_sync_table',
	// 名称
	caption : '内外网交换配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_database_sync_id',
		text : '内外网交换ID',
		editor : 'none',
		description : '内外网交换ID。'
	}, {
		name : 'f_tablename',
		text : '表名',
		editor : 'string',
		description : '需要交换的表名。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_database_sync_id':{width:120,hidden:true},
		'f_tablename':{width:300}
	}
});
//=================================================================
//	�ļ�����DatabasesFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DatabasesFolder',
	// 名称
	caption : '数据库实例',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/appregister/databasesgrid']
});
//=================================================================
//	�ļ�����Datasource.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_datasource',
	// 名称
	caption : '数据库实例配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_caption',
		text : '数据源名称',
		editor : 'string',
		description : '数据源名称。'
	}, {
		name : 'f_databases_caption',
		text : '数据库名称',
		editor : 'string',
		description : '数据库名称。'
	}, {
		name : 'f_type',
		text : '数据库类型',
		editor : 'string',
		description : '数据库类型。'
	}, {
		name : 'f_ip',
		text : 'IP',
		editor : 'string',
		description : '数据库IP地址。'
	}, {
		name : 'f_port',
		text : '端口',
		editor : 'number',
		description : '数据库端口。'
	}, {
		name : 'f_url',
		text : 'URL',
		editor : 'string',
		description : '数据库访问地址。'
	}, {
		name : 'f_location',
		text : '地址',
		editor : 'string',
		description : '数据库地址。'
	}, {
		name : 'f_user',
		text : '用户名',
		editor : 'string',
		description : '数据库用户名。'
	}, {
		name : 'f_password',
		text : '密码',
		editor : 'string',
		description : '数据库密码。'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_caption':{width:100},
		'f_databases_caption':{width:100},
		'f_type':{width:100},
		'f_ip':{width:100},
		'f_port':{width:100},
		'f_url':{width:100},
		'f_location':{width:100},
		'f_user':{
			width:100,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_password':{
			width:100,
			editor: new Ext.form.field.Text({
                typeAhead: false
            })
		},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����DicdataRelation.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dicdata_relation',
	// 名称
	caption : '字典数据关系列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '关系标识',
		editor : 'string',
		description : '关系标识（英文符）。'
	}, {
		name : 'f_caption',
		text : '关系描述',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '关系描述。'
	}, {
		name : 'f_source_dic_id',
		text : '源字典标识',
		editor : 'combo',
		defaultValue: 0,
		store : [],
		description : '源字典标识。'
	},{
		name : 'f_target_dic_id',
		text : '目的字典标识',
		editor : 'combo',
		defaultValue: 0,
		store : [],
		description : '目的字典标识。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:120},
		'f_caption':{width:120},
		'f_source_dic_id':{width:120},
		'f_target_dic_id':{width:120},
		'f_note':{width:120}
	}
});
//=================================================================
//	�ļ�����DicdataRelationData.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dicdata_relation_data',
	// 名称
	caption : '关系数据列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_relation_id',
		text : '字典数据关系',
		editor : 'combo',
		editorCfg : {
			disabled : true
		},
		defaultValue: 0,
		store : [],
		description : '字典数据关系名称。'
	},{
		name : 'f_source_data_id',
		text : '源字典数据',
		editor : 'gridcombox',
		defaultValue: 0,
		editorCfg :{
			relationId : 0,
			dicType : 0
		},
		store : [],
		description : '源字典数据。'
	},{
		name : 'f_target_data_id',
		text : '目标字典数据',
		editor : 'gridcombox',
		defaultValue: 0,
		editorCfg :{
			relationId : 0,
			dicType : 1
		},
		store : [],
		description : '目标字典数据。'
	},{
		name : 'f_order',
		text : '排序',
		editor : 'string',
		defaultValue: 0,
		description : '按源字典数据排序。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_relation_id':{width:120},
		'f_source_data_id':{width:180},
		'f_target_data_id':{width:180},
		'f_order':{width:50},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����DicdataRelationDataManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DicdataRelationDataManager',
	// 名称
	caption : '关系数据管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/dicdatarelation/dicdatarelationdata']
});
//=================================================================
//	�ļ�����DicdataRelationManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DicdataRelationManager',
	// 名称
	caption : '字典数据关系管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/dicdatarelation/dicdatarelation']
});
//=================================================================
//	�ļ�����DicdataTree.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_application_entrance_test',
	// 名称
	caption : '应用导航',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '字典标识',
		editor : 'string',
		description : '字典数据标识。'
	}, {
		name : 'f_value',
		text : '字典数据',
		editor : 'string',
		description : '字典数据名称。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:240},
		'f_value':{flex:1}
	}
});
//=================================================================
//	�ļ�����Dictionary.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dictionary',
	// 名称
	caption : '字典列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '字典标识',
		editor : 'string',
		description : '字典标识（英文符）。'
	}, {
		name : 'f_caption',
		text : '字典名称',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '字典名称。'
	}, {
		name : 'f_allow_extend',
		text : '多级字典',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '否'], [1, '是']],
		description : '是否多级字典。'
	},{
		name : 'f_allow_redefine',
		text : '重载',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '否'], [1, '是']],
		description : '是否是重载字典。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '备注。'
	},{
		name : 'f_propertys',
		text : '扩展属性',
		editor : 'none',
		description : '扩展属性。',
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_key':{width:120},
		'f_caption':{width:100},
		'f_allow_extend':{width:60},
		'f_allow_redefine':{width:40},
		'f_propertys':{flex:1}
	}
});
//=================================================================
//	�ļ�����DictionaryData.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dictionary_data',
	// 名称
	caption : '数据管理',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '数据标识',
		editor : 'string',
		description : '数据标识。'
	}, {
		name : 'f_value',
		text : '名称',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '数据值。'
	}, {
		name : 'f_shortcode',
		text : '短代码',
		editor : 'string',
		description : '数据短代码。'
	}, {
		name : 'f_state',
		text : '状态',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '启用'], [1, '禁用']],
		description : '数据状态。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'none',
		defaultValue: 0,
		description : '数据级别。'
	}, {
		name : 'f_order',
		text : '排序',
		editor : 'none',
		defaultValue: 0,
		description : '数据排序。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_key':{
			width:120,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_value':{
			width:100,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_shortcode':{
			width:80,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_state':{
			width:40,
			editor: new Ext.form.field.ComboBox({
                typeAhead: false,
        		editable : false,
        		defaultValue: 0,
                triggerAction: 'all',
                store: [
                    [0,'启用'],
                    [1,'禁用']
                ]
            })
		},
		'f_level':{width:40},
	}
});
//=================================================================
//	�ļ�����DictionaryDataManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DictionaryDataManager',
	// 名称
	caption : '数据管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/dictionary/dictionarydata']
});
//=================================================================
//	�ļ�����DictionaryManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DictionaryManager',
	// 名称
	caption : '字典管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/dictionary/dictionary']
});
//=================================================================
//	�ļ�����EsbFlowInstance.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'EsbFlowInstance',
	// 名称
	caption : '实例管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/esb/esb.flow.tree']
});
//=================================================================
//	�ļ�����ExchangeFile.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ExchangeFile',
	// 名称
	caption : '文件交换',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/edi/data.exchange.file']
});
//=================================================================
//	�ļ�����ExchangeTable.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ExchangeTable',
	// 名称
	caption : '数据表交换',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	//editors : ['manager/appregister/databasesexchange']
	editors : ['manager/edi/data.exchange.table']
});
//=================================================================
//	�ļ�����Extproperty.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_ext_property',
	// 名称
	caption : '属性扩展',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_table_name',
		text : '表名',
		editor : 'none',
		defaultValue : 't_asc_dictionary_data',
		description : '扩展表名称。'
	}, {
		name : 'f_field_name',
		text : '属性标识',
		editor : 'string',
		description : '扩展属性标识。'
	}, {
		name : 'f_field_caption',
		text : '属性名称',
		editor : 'string',
		description : '扩展属性名称。'
	}, {
		name : 'f_field_type',
		text : '类型',
		editor : 'combo',
		defaultValue: 0,
		store: [
		        [0,'String'],
				[1,'Long'],
				[2,'Float']
		],
		description : '扩展属性类型。'
	}, {
		name : 'f_editor_type',
		text : '编辑类型',
		editor : 'combo',
		defaultValue: 0,
		store: [
				['readOnly','readOnly'],
				['none','none'],
				['date','date'],
				['event','event'],
				['text','text'],
				['textarea','textarea'],
				['number','number'],
				['boolean','boolean'],
				['combo','combo'],
				['combosimple','combosimple'],
				['treecombox','treecombox'],
				['gridcombox','gridcombox'],
				['json','json'],
				['designobject','designobject'],
				['events','events'],
				['cdru','cdru']
		],
		description : '扩展属性类型。'
	}, {
		name : 'f_length',
		text : '长度',
		editor : 'number',
		description : '扩展属性长度。'
	}, {
		name : 'f_nullable',
		text : '能否为空',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '否'], [1, '是']],
		description : '属性能否为空'
	}, {
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '排序。'
	}, {
		name : 'f_config',
		text : '配置',
		editor : 'json',
		description : '配置。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:30,hidden:true},
		'f_field_name':{
			width:100,
			editor: new Ext.form.field.Text({
				typeAhead: true
			})
		},
		'f_field_caption':{
			width:100,
			editor: new Ext.form.field.Text({
				typeAhead: true
			})
		},
		'f_field_type':{
			width:60,
			editor: new Ext.form.field.ComboBox({
				typeAhead: false,
				editable : false,
				defaultValue: 0,
				triggerAction: 'all',
				store: [
				        [0,'String'],
				        [1,'Long'],
				        [2,'Float']
				]
			})
		},
		'f_length':{
			width:60,
			editor: new Ext.form.field.Number({
				typeAhead: true
			})
		},
		'f_nullable':{
			flex:1,
			editor: new Ext.form.field.ComboBox({
				typeAhead: false,
				editable : false,
				defaultValue: 0,
				triggerAction: 'all',
				store: [
				        [0,'否'],
				        [1,'是']
				]
			})
		},
		'f_order':{flex:1,hidden:true}
	}
});
//=================================================================
//	�ļ�����FileRule.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'FileRule',
	// 名称
	caption : '文件交换配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_app_id',
		text : '应用id',
		editor : 'string',
		description : '要交换数据的应用的标识。'
	}, {
		name : 'f_app_caption',
		text : '应用标题',
		editor : 'string',
		description : '应用标题名称。'
	}, {
		name : 'f_policy',
		text : '数据交换规则',
		editor : 'combo',
		defaultValue: 1,
		store : [[1, '同步'], [0, '不同步']],
		description : '内外网数据交换规则。(同步/不同步)'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_app_id':{width:120},
		'f_app_caption':{width:200},
		'f_policy':{width:150}
	}
});
//=================================================================
//	�ļ�����Log.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log',
	// 名称
	caption : '日志配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_action_type',
		text : '资源类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '资源类型。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_resource_key',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_result',
		text : '结果',
		editor : 'string',
		description : '结果。'
	}, {
		name : 'f_nodenumber',
		text : '节点编号',
		editor : 'string',
		description : '节点编号。'
	}, {
		name : 'f_thread_id',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}, {
		name : 'f_user_id',
		text : '用户ID',
		editor : 'string',
		description : '用户ID。'
	}, {
		name : 'f_user_name',
		text : '用户登录名',
		editor : 'string',
		description : '用户登录名。'
	}, {
		name : 'f_user_caption',
		text : '用户姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_user_ip',
		text : '用户IP',
		editor : 'string',
		description : '用户IP。'
	}, {
		name : 'f_useragent',
		text : 'f_useragent',
		editor : 'string',
		description : 'f_useragent。'
	}, {
		name : 'f_detail_before',
		text : '操作前',
		editor : 'string',
		description : '操作前。'
	}, {
		name : 'f_detail_after',
		text : '操作后',
		editor : 'string',
		description : '操作后。'
	}, {
		name : 'f_create_time',
		text : '操作时间',
		editor : 'string',
		description : '操作时间。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_type':{width:120},
		'f_action_type':{width:120},
		'f_application_key':{width:100},
		'f_resource_key':{width:100},
		'f_result':{width:100},
		'f_nodenumber':{width:100},
		'f_thread_id':{width:100},
		'f_user_id':{width:100},
		'f_user_name':{width:100},
		'f_user_caption':{width:100},
		'f_user_ip':{width:100},
		'f_useragent':{width:100},
		'f_detail_before':{width:100},
		'f_detail_after':{width:100},
		'f_user_ip':{width:100}
	}
});
//=================================================================
//	�ļ�����LogConf.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'LogConf',
	// 名称
	caption : '日志配置',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/log/apptree']
});
//=================================================================
//	�ļ�����LogConfig.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_config',
	// 名称
	caption : '日志配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_application_id',
		text : '应用标识',
		editor : 'none',
		description : '应用标识。'
	}, {
		name : 'f_resource_id',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_action',
		text : '动作名称',
		editor : 'textarea',
		description : '动作名称（如direct类方法名，文档动作save、delete等）。'
	}, {
		name : 'f_targets',
		text : '目标级别',
		editor : 'json',
		description : '格式如{debug:xxx,info:yyy,...}的json字符串,一共包括五个级别（debug、info、warn、error、fatal）。'
	}, {
		name : 'f_has_userinfo',
		text : '用户信息',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '否'], [1, '是']],
		description : '用户信息。'
	}, {
		name : 'f_has_requestinfo',
		text : '请求信息',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '否'], [1, '是']],
		description : '请求信息。'
	}, {
		name : 'f_has_detail',
		text : '详细信息',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '否'], [1, '是']],
		description : '详细信息。'
	}, {
		name : 'f_state',
		text : '状态',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '启用'], [1, '禁用']],
		description : '定义的日志规则是否启用。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_application_id':{width:120},
		'f_resource_id':{width:120},
		'f_type':{width:80},
		'f_action':{width:180},
		'f_targets':{width:180},
		'f_has_userinfo':{width:60},
		'f_has_requestinfo':{width:60},
		'f_has_detail':{width:60},
		'f_state':{width:60}
	}
});
//=================================================================
//	�ļ�����LogManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_manager',
	// 名称
	caption : '运维日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_user_id',
		text : '用户ID',
		editor : 'string',
		description : '用户ID。'
	}, {
		name : 'f_user_name',
		text : '用户姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_user_host',
		text : '用户IP',
		editor : 'string',
		description : '用户IP。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_user_agent',
		text : '用户代理',
		editor : 'string',
		description : '用户代理。'
	}, {
		name : 'f_action',
		text : '资源',
		editor : 'string',
		description : '资源。'
	}, {
		name : 'f_action_param',
		text : '参数',
		editor : 'string',
		description : '参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_type':{width:120},
		'f_application_key':{width:100},
		'f_user_id':{width:100},
		'f_user_name':{width:100},
		'f_user_host':{width:100},
		'f_log':{width:100},
		'f_user_agent':{width:100},
		'f_action':{width:100}
	}
});
//=================================================================
//	�ļ�����LogSystem.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_system',
	// 名称
	caption : '系统日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_node',
		text : '节点',
		editor : 'string',
		description : '节点。'
	}, {
		name : 'f_source_key',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_source_caption',
		text : '资源名称',
		editor : 'string',
		description : '资源名称。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'string',
		description : '级别。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_detail',
		text : '详细日志',
		editor : 'string',
		description : '详细日志。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_type':{width:120},
		'f_application_key':{width:100},
		'f_source_key':{width:100},
		'f_source_caption':{width:100},
		'f_level':{width:100},
		'f_log':{width:100},
		'f_detail':{width:100},
		'f_threadid':{width:100}
	}
});
//=================================================================
//	�ļ�����LogUser.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_log_user',
	// 名称
	caption : '用户日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_key',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_user_id',
		text : '用户ID',
		editor : 'string',
		description : '用户ID。'
	}, {
		name : 'f_user_name',
		text : '用户姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_user_host',
		text : '用户IP',
		editor : 'string',
		description : '用户IP。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_user_agent',
		text : '用户代理',
		editor : 'string',
		description : '用户代理。'
	}, {
		name : 'f_action',
		text : '资源',
		editor : 'string',
		description : '资源。'
	}, {
		name : 'f_action_param',
		text : '参数',
		editor : 'string',
		description : '参数。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_type':{width:120},
		'f_application_key':{width:100},
		'f_user_id':{hidden:true},
		'f_user_name':{width:100},
		'f_user_host':{width:100},
		'f_log':{width:100},
		'f_user_agent':{width:230,flex:1},
		'f_action':{width:100},
		'f_action_param':{width:100},
		'f_threadid':{hidden:true}
	}
});
//=================================================================
//	�ļ�����MgrActivityLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'MgrActivityLog',
	// 名称
	caption : '应用管理平台日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_node_id',
		text : '节点',
		editor : 'string',
		description : '节点。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_id',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_resource_id',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_action_name',
		text : '操作名称',
		editor : 'string',
		description : '操作名称。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'combo',
		store : [[0,'致命错误'],[3,'错误'],[4,'警告'],[6,'普通信息'],[7,'调试输出']],
		description : '级别。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_detail',
		text : '详细信息',
		editor : 'string',
		description : '详细日志。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}, {
		name : 'f_successed',
		text : '执行结果',
		editor : 'combo',
		store : [[0,'未知'],[1,'成功'],[2,'失败']],
		description : '执行结果。'
	}, {
		name : 'f_user_id',
		text : '操作用户id',
		editor : 'number',
		description : '操作用户的id标识。'
	}, {
		name : 'f_user_name',
		text : '用户名字',
		editor : 'string',
		description : '操作用户的名字。'
	}, {
		name : 'f_user_host',
		text : '用户主机地址',
		editor : 'string',
		description : '操作用户主机地址。'
	}, {
		name : 'f_user_agent',
		text : '用户代理',
		editor : 'string',
		description : '用户代理。'
	}, {
		name : 'f_action',
		text : '动作',
		editor : 'string',
		description : '用户操作的动作。'
	}, {
		name : 'f_action_param',
		text : '动作参数',
		editor : 'string',
		description : '操作动作的参数。'
	},{
		name : 'f_create_time',
		text : '操作时间',
		editor : 'string',
		description : '操作时间。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_node_id':{hidden:true},
		'f_type':{width:100,hidden:true},
		'f_level':{width:100},
		'f_application_id':{width:110},
		'f_create_time':{width:120},
		'f_resource_id':{width:125},
		'f_action_name':{width:110},
		'f_user_id':{hidden:true},
		'f_user_name':{width:80},
		'f_user_host':{width:200,flex:1},
		'f_user_agent':{width:100},
		'f_action':{width:80},
		'f_action_param':{width:80},
		'f_successed':{width:80,renderer : function(v, metaData, record, rowIndex){
			var result = record.get('f_successed');
			switch(result){
			case 0:
				metaData.style = 'color:#A3A3A3;';
				v = '未知';
				break;
			case 1:
				v = '成功';
				break;
			case 2:
				metaData.style = 'color:#F08080;';
				v = '失败';
				break;
			default:
				return '';
				break;
			}
			return v;
		}},
		'f_threadid':{width:170},
		'f_log':{width:80},
		'f_detail':{hidden:true}
	}
});
//=================================================================
//	�ļ�����Organization.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_org',
	// 名称
	caption : '组织机构',
	// 属性定义
	properties : [
	/*{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_no',
		text : '组织编码',
		editor : 'string',
		description : '组织唯一编码，不可重复。'
	}, {
		name : 'f_caption',
		text : '组织名称',
		editor : 'string',
		description : '指定按钮对象xtype，用于生成按钮界面对象的类引用名。'
	}, {
		name : 'f_type',
		text : '类型',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '单位'], [1, '部门']],
		description : '组织的类型。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	},{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		hidden: true,
		description : '排序。'
	},*/
	// 为伤损项目增加如下属性
	{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_no',
		text : '组织编码',
		editor : 'string',
		description : '组织唯一编码，不可重复。'
	}, {
		name : 'f_code',
		text : '机构编码',
		editor : 'string',
		allowBlank: false,
		description : '机构编码'
	}, {
		name : 'f_caption',
		text : '机构全称',
		editor : 'string',
		allowBlank: false,
		description : '机构全称'
	}/*, {
		name : 'f_orgtype',
		text : '机构类型',
		editor : 'combo',
		defaultValue: '',
		description : '机构类型（总公司、路局、工务段）',
		store : []
	}*//*, {
		name : 'f_level',
		text : '机构层级',
		editor : 'string',
		editable : false,
		description : '机构层级'
	}*/, {
		name : 'f_shortname',
		text : '机构简称',
		editor : 'string',
		allowBlank: false,
		description : '机构简称'
	}, {
		name : 'f_inputcode',
		text : '机构输入码',
		editor : 'string',
		description : '机构输入码'
	}, {
		name : 'f_found_time',
		text : '成立时间',
		editor : 'date',
		renderer: function(value) {
			if(typeof value == 'string' && value.length == 19){
  	  			return Ext.util.Format.dateRenderer('Y-m-d')(Ext.Date.parse(value, 'Y-m-d H:i:s'));
  	  		}
			return value;
		},
		description : '成立时间'
	}, /*{
		name : 'f_amount_gx',
		text : '干线数',
		editor : 'number',
		description : '干线数'
	}, {
		name : 'f_amount_zx',
		text : '正线数',
		editor : 'number',
		description : '正线数'
	},*/ {
		name : 'f_address',
		text : '机构地址',
		editor : 'string',
		description : '机构地址'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}, {
		name : 'f_order',
		text : '排序',
		editor : 'none',
		hidden: true,
		description : '排序。'
	}
	// 增加结束
	],
	// 列表编辑字段
	propertyColumns : {
/*		'id':{width:40},
		'f_no':{width:100},
		'f_caption':{width:120},
		'f_type':{width:50},
		'f_note':{flex:1}*/
		'id':{width:40},
		'f_no':{width:60},
		'f_code':{width:60},
		'f_caption':{width:100},
		//'f_orgtype':{width:60},
		//'f_level':{width:60},
		'f_shortname':{width:100},
		'f_inputcode':{width:100},
		'f_found_time':{width:100},
		//'f_amount_gx':{width:50},
		//'f_amount_zx':{width:50},
		'f_address':{width:100},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����OrganizationManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'OrganizationManager',
	// 名称
	caption : '机构管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/organization/organization']
});
//=================================================================
//	�ļ�����OrganizationUser.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_org_user',
	// 名称
	caption : '用户信息',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	},{
		name : 'f_user_id',
		text : '用户ID',
		editor : 'none',
		description : '数据表记录用户ID。'
	},{
		name : 'f_org_id',
		text : '机构ID',
		editor : 'none',
		description : '数据表记录机构ID。'
	},{
		name : 'f_isdefault',
		text : '默认机构',
		editor : 'combo',
		store : [[true, '是'], [false, '否']],
		description : '是否是用户的默认机构。'
	}, {
		name : 'f_name',
		text : '登录名',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '用户登录名。'
	}, {
		name : 'f_caption',
		text : '姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_email',
		text : 'E-mail',
		editor : 'string',
		description : '用户邮箱。'
	}, {
		name : 'f_company_caption',
		text : '单位',
		editor : 'string',
		description : '用户所在单位名称。'
	}, {
		name : 'f_cellphone',
		text : '联系方式',
		editor : 'string',
		description : '用户联系方式。'
	}, {
		name : 'f_gender',
		text : '性别',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '女'], [1, '男']],
		description : '用户的性别。'
	},{
		name : 'f_type',
		text : '类型',
		editor : 'combo',
		defaultValue: 0,
		editable : false,
		store : [[0, '普通成员'], [1, '组织领导'], [2, '组织副职领导'], [3, '组织分管领导']],
		description : '说明信息。'
	},{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		hidden: true,
		description : '排序。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_user_id':{width:60,hidden:true},
		'f_org_id':{width:60,hidden:true},
		'f_name':{width:80},
		'f_caption':{width:70},
		'f_gender':{width:40},
		'f_email' : {width:120},
		'f_company_caption' : {width:120},
		'f_cellphone' : {width:100},
		'f_type':{
			width:40,
			editor: new Ext.form.field.ComboBox({
                typeAhead: false,
        		editable : false,
        		defaultValue: 0,
                triggerAction: 'all',
                store: [
                    [0,'普通成员'],
                    [1,'组织领导'],
                    [2,'组织副职领导'],
                    [3,'组织分管领导']
                ]
            }),
			flex:1
		},
		'f_isdefault':{width:60}
	}
});
//=================================================================
//	�ļ�����OrganizationUserManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'OrganizationUserManager',
	// 名称
	caption : '机构用户',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/organization/organizationuser']
});
//=================================================================
//	�ļ�����Role.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_role',
	// 名称
	caption : '角色列表',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_type',
		text : '角色类型',
		editor : 'combo',
		store : [[0, '普通角色'], [1, '自定义']],
		description : '角色类型'
	}, {
		name : 'f_caption',
		text : '角色名称',
		editor : 'string',
		description : '角色名称。'
	}, {
		name : 'f_key',
		text : '角色标识',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '角色标识。'
	}, {
		name : 'f_config',
		text : '角色配置',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '用于自定义角色配置可解析参数'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_type':{width:80},
		'f_key':{width:120},
		'f_caption':{width:100},
		'f_config':{width:200},
		'f_note':{flex:1}
	}
});
//=================================================================
//	�ļ�����RoleManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'RoleManager',
	// 名称
	caption : '角色管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/organization/role']
});
//=================================================================
//	�ļ�����RoleUserManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'RoleUserManager',
	// 名称
	caption : '角色管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/organization/roleuser']
});
//=================================================================
//	�ļ�����SystemLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'SystemLog',
	// 名称
	caption : '系统日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_node_id',
		text : '节点',
		editor : 'string',
		description : '节点。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_id',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_resource_id',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_action_name',
		text : '操作名称',
		editor : 'string',
		description : '操作名称。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'combo',
		store : [[0,'致命错误'],[3,'错误'],[4,'警告'],[6,'普通信息'],[7,'调试输出']],
		description : '级别。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_detail',
		text : '详细信息',
		editor : 'string',
		description : '详细日志。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}, {
		name : 'f_successed',
		text : '执行结果',
		editor : 'combo',
		store : [[0,'未知'],[1,'成功'],[2,'失败']],
		description : '执行结果。'
	},{
		name : 'f_create_time',
		text : '操作时间',
		editor : 'string',
		description : '操作时间。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_node_id':{hidden:true},
		'f_type':{width:120,hidden:true},
		'f_level':{width:80},
		'f_application_id':{width:80},
		'f_create_time':{width:150},
		'f_resource_id':{width:200},
		'f_action_name':{width:150},
		'f_successed':{width:60,renderer : function(v, metaData, record, rowIndex){
			var result = record.get('f_successed');
			switch(result){
			case 0:
				metaData.style = 'color:#A3A3A3;';
				v = '未知';
				break;
			case 1:
				v = '成功';
				break;
			case 2:
				metaData.style = 'color:#F08080;';
				v = '失败';
				break;
			default:
				return '';
				break;
			}
			return v;
		}},
		'f_threadid':{width:170},
		'f_log':{flex:1},
		'f_detail':{hidden:true}
	}
});
//=================================================================
//	�ļ�����TableRule.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'TableRule',
	// 名称
	caption : '数据表交换配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_app_id',
		text : '应用id',
		editor : 'string',
		description : '要交换数据的应用的标识。'
	}, {
		name : 'f_app_caption',
		text : '应用标题',
		editor : 'string',
		description : '应用标题名称。'
	}, {
		name : 'f_dsn',
		text : '数据源',
		editor : 'string',
		description : '要交换的数据源名称。'
	}, {
		name : 'f_tablename',
		text : '数据表名',
		editor : 'string',
		description : '要交换的数据库表的名称。'
	}, {
		name : 'f_policy',
		text : '数据交换规则',
		editor : 'combo',
		defaultValue: 1,
		store : [[1, '同步'], [0, '不同步']],
		description : '内外网数据交换规则。(同步/不同步)'
	}, {
		name : 'f_scope',
		text : '规则生效范围',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '所有'], [1, '内网'], [2, '外网']],
		description : '该应用规则的生效范围。(所有/内网/外网)'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_app_id':{width:100},
		'f_app_caption':{width:100},
		'f_dsn':{width:120},
		'f_tablename':{width:100},
		'f_policy':{width:100},
		'f_scope':{width:100}
	}
});
//=================================================================
//	�ļ�����User.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_user',
	// 名称
	caption : '用户信息',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_name',
		text : '登录名',
		editor : 'string',
		allowBlank: false,
		editorCfg : {
			allowEmpty : true
		},
		description : '用户登录名。'
	}, {
		name : 'f_caption',
		text : '姓名',
		editor : 'string',
		allowBlank: false,
		description : '用户姓名。'
	}, {
		name : 'f_gender',
		text : '性别',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '女'], [1, '男']],
		description : '用户的性别。'
	}, {
		name : 'f_type',
		text : '类型',
		editor : 'combo',
		allowBlank: false,
		defaultValue: 0,
		store : [[0, '普通用户'], [1, '运维管理人员'], [2, '开发人员']],
		description : '用户类型。'
	}, {
		name : 'f_state',
		text : '状态',
		editor : 'combo',
		allowBlank: false,
		defaultValue: 0,
		store : [[0, '正常'], [1, '被锁定'], [2, '退休，离职'], [3, '标记删除']],
		description : '用户状态。'
	}, {
		name : 'f_email',
		text : 'E-mail',
		editor : 'string',
		description : '用户邮箱。'
	}, {
		name : 'f_birthdate',
		text : '出生日期',
		editor : 'date',
		renderer: function(value) {
			if(typeof value == 'string' && value.length == 19){
  	  			return Ext.util.Format.dateRenderer('Y-m-d')(Ext.Date.parse(value, 'Y-m-d H:i:s'));
  	  		}
			return value;
		},
		description : '出生日期。'
	}, {
		name : 'f_birthplace',
		text : '出生地',
		editor : 'string',
		description : '出生地。'
	}, {
		name : 'f_post',
		text : '职务',
		editor : 'string',
		description : '职务。'
	}, {
		name : 'f_hire_date',
		text : '入职年月',
		editor : 'date',
		renderer: function(value) {
			if(typeof value == 'string' && value.length == 19){
  	  			return Ext.util.Format.dateRenderer('Y-m-d')(Ext.Date.parse(value, 'Y-m-d H:i:s'));
  	  		}
			return value;
		},
		description : '入职年月。'
	}, {
		name : 'f_cellphone',
		text : '联系方式',
		editor : 'string',
		description : '用户联系方式。'
	}, {
		name : 'f_work_phone',
		text : '单位固话',
		editor : 'string',
		description : '单位固话。'
	}, {
		name : 'f_speciality',
		text : '技能特长',
		editor : 'text',
		description : '技能特长。'
	}, {
		name : 'f_company_caption',
		text : '默认单位',
		editor : 'none',
		description : '用户默认单位。',
		store : [[0, '无单位']]
	}, {
		name : 'f_dept_id',
		text : '默认部门',
		editor : 'combo',
		description : '用户默认部门 。'
		//store : [[0, '无部门']]
	}, {
		name : 'orgs',
		text : '所在机构',
		editor : 'none',
		defaultValue: 0,
		store : [],
		description : '用户所在机构列表。'
	}, {
		name : 'roles',
		text : '所有角色',
		editor : 'none',
		defaultValue: 0,
		store : [],
		description : '用户所有角色列表。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_name':{width:80},
		'f_caption':{width:70},
		'f_gender':{width:40},
		'f_type':{width:100},
		'f_state':{width:80},
		'f_email':{width:120},
		'f_company_caption':{width:120},
		'f_dept_id':{width:120},
		'f_cellphone':{width:100}
	}
});
//=================================================================
//	�ļ�����UserActivityLog.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'UserActivityLog',
	// 名称
	caption : '用户日志',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_node_id',
		text : '节点',
		editor : 'string',
		description : '节点。'
	}, {
		name : 'f_type',
		text : '日志类型',
		editor : 'combo',
		//defaultValue: 0,
		store : [],
		description : '日志类型。'
	}, {
		name : 'f_application_id',
		text : '应用标识',
		editor : 'string',
		description : '应用标识。'
	}, {
		name : 'f_resource_id',
		text : '资源标识',
		editor : 'string',
		description : '资源标识。'
	}, {
		name : 'f_action_name',
		text : '操作名称',
		editor : 'string',
		description : '操作名称。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'combo',
		store : [[0,'致命错误'],[3,'错误'],[4,'警告'],[6,'普通信息'],[7,'调试输出']],
		description : '级别。'
	}, {
		name : 'f_log',
		text : '日志',
		editor : 'string',
		description : '日志。'
	}, {
		name : 'f_detail',
		text : '详细信息',
		editor : 'string',
		description : '详细日志。'
	}, {
		name : 'f_threadid',
		text : '线程ID',
		editor : 'string',
		description : '线程ID。'
	}, {
		name : 'f_successed',
		text : '执行结果',
		editor : 'combo',
		store : [[0,'未知'],[1,'成功'],[2,'失败']],
		description : '执行结果。'
	}, {
		name : 'f_user_id',
		text : '操作用户id',
		editor : 'number',
		description : '操作用户的id标识。'
	}, {
		name : 'f_user_name',
		text : '用户名字',
		editor : 'string',
		description : '操作用户的名字。'
	}, {
		name : 'f_user_host',
		text : '用户主机地址',
		editor : 'string',
		description : '操作用户主机地址。'
	}, {
		name : 'f_user_agent',
		text : '用户代理',
		editor : 'string',
		description : '用户代理。'
	}, {
		name : 'f_action',
		text : '动作',
		editor : 'string',
		description : '用户操作的动作。'
	}, {
		name : 'f_action_param',
		text : '动作参数',
		editor : 'string',
		description : '操作动作的参数。'
	},{
		name : 'f_create_time',
		text : '操作时间',
		editor : 'string',
		description : '操作时间。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{hidden:true},
		'f_node_id':{hidden:true},
		'f_type':{width:100,hidden:true},
		'f_level':{width:100},
		'f_application_id':{width:125},
		'f_create_time':{width:150},
		'f_resource_id':{width:120,flex:1},
		'f_action_name':{width:110},
		'f_user_id':{hidden:true},
		'f_user_name':{width:80},
		'f_user_host':{width:200,flex:1},
		'f_user_agent':{width:100},
		'f_action':{hidden:true},
		'f_successed':{width:80,renderer : function(v, metaData, record, rowIndex){
			var result = record.get('f_successed');
			switch(result){
			case 0:
				metaData.style = 'color:#A3A3A3;';
				v = '未知';
				break;
			case 1:
				v = '成功';
				break;
			case 2:
				metaData.style = 'color:#F08080;';
				v = '失败';
				break;
			default:
				return '';
				break;
			}
			return v;
		}},
		'f_threadid':{width:150},
		'f_action_param':{hidden:true},
		'f_log':{flex:1},
		'f_detail':{hidden:true},
	}
});
//=================================================================
//	�ļ�����UserManager.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'UserManager',
	// 名称
	caption : '用户管理',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['manager/organization/user']
});