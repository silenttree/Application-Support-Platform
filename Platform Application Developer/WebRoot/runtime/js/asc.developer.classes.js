
//=================================================================
//	�ļ�����Action.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'action',
	// 名称
	caption : '动作按钮',
	// 是否设计对象
	isDesignObject : true,
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����AppConn.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'appconn',
	// 名称
	caption : '应用连接',
	// 是否设计对象
	isDesignObject : false,
	// 属性定义
	properties : [{
		name : 'appId',
		text : '标识',
		editor : 'none',
		description : '应用连接标识'
	}, {
		name : 'appCaption',
		text : '名称',
		editor : 'string',
		description : '应用连接名称，用于显示。'
	},{
		name : 'appType',
		text : '链接类型',
		editor : 'combo',
		store : [['app', '应用链接'],['esb', 'ESB业务流链接']],
		defaultValue : 'app',
		description : '应用连接类型，根据链接类型，显示不同的结构树。'
	},{
		name : 'appHost',
		text : '访问地址',
		editor : 'string',
		defaultValue : 'http://localhost:8080/demo',
		description : '应用连接访问地址。'
	}, {
		name : 'serviceUrl',
		text : '服务路径',
		editor : 'string',
		defaultValue : '/services/DesignObjectAccessService?wsdl',
		description : '数字编辑框。'
	}, {
		name : 'note',
		text : '备注',
		editor : 'text',
		description : '应用连接备注信息'
	}],
	// 编辑器定义
	editors : ['designer/appconnection.editor']
});
//=================================================================
//	�ļ�����Button.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'button',
	// 名称
	caption : '按钮',
	//前缀
	prefix : 'btn',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '视图按钮排序号。'
	}, {
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '按钮图标样式名。'
	}, {
		name : 'f_xtype',
		text : '按钮类名',
		editor : 'combo',
		editorCfg : {
			editable : true
		},
		store : ['button','text','splitbutton','uploadbutton', 'separator', 'fill'],
		defaultValue : 'button',
		description : '指定按钮对象xtype，用于生成按钮界面对象的类引用名。'
	}, {
		name : 'f_script',
		text : 'JS脚本',
		editor : 'jsarea',
		description : '该按钮点击时执行的客户端脚本。'
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue : [{name:'isDefault',type:'boolean',cfg:{},value:true}],
		description : '按钮的扩展属性，设置该按钮的一些基本默认属性。'
	}, {
		name : 'f_events',
		text : '事件脚本',
		editor : 'event',
		description : '按钮事件脚本。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:120},
		'f_xtype':{width:80},
		'f_script':{width:200},
		'f_events':{width:120},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����DataGroup.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'datagroup',
	// 名称
	caption : '数据分组',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['datagroup'],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����DataSource.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'datasource',
	// 名称
	caption : '数据源',
	//前缀
	prefix : 'ds',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_dbtype',
		text : '数据库类型',
		editor : 'combosimple',
		store : ['oracle', 'mysql', 'mssql', 'db2'],
		defaultValue : 'oracle',
		description : '选择数据源对应的数据库类型。'
	}, {
		name : 'f_driverclass',
		text : '驱动类名',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		defaultValue : 'oracle.jdbc.driver.OracleDriver',
		store : ['oracle.jdbc.driver.OracleDriver'],
		description : '选择或输入数据源对应的驱动类名称。'
	},{
		name : 'f_url',
		text : '访问URL',
		editor : 'string',
		defaultValue : 'jdbc:oracle:thin:@localhost:1521:orcl',
		description : 'JDBC数据库访问路径定义，例：jdbc:oracle:thin:@localhost:1521:orcl。'
	},{
		name : 'f_username',
		text : '用户名',
		editor : 'string',
		description : '数据库访问用户名。'
	},{
		name : 'f_password',
		text : '密码',
		editor : 'string',
		description : '数据库访问密码。'
	}, {
		name : 'f_dbcp_type',
		text : '连接池类型',
		editor : 'combosimple',
		store : ['DBCP', 'BONECP', 'DRUID'],
		defaultValue : 'DBCP',
		description : '指定数据库连接池管理类型。'
	}, {
		name : 'f_maxconns',
		text : '最大连接数',
		editor : 'number',
		defaultValue : 100,
		description : '设定数据库连接池的最大连接数。'
	}, {
		name : 'f_minconns',
		text : '最小连接数',
		editor : 'number',
		defaultValue : 10,
		description : '设定数据库连接池的最小连接数。'
	}, {
		name : 'f_initconns',
		text : '初始连接数',
		editor : 'number',
		defalutValue : 30,
		description : '设定数据库连接池初始连接数。'
	}, {
		name : 'f_stepconns',
		text : '连接池步长',
		editor : 'number',
		defaultValue : 10,
		description : '设定数据库连接池每次创建的连接数。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����DataSourceFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DataSourceFolder',
	// 名称
	caption : '数据源列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['datasource']
});
//=================================================================
//	�ļ�����DBField.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'dbfield',
	// 名称
	caption : '表字段',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '字段排序号。'
	}, {
		name : 'f_db_type',
		text : 'db数据类型',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['Char', 'Number', 'Datetime', 'Blob', 'Clob'],
		defaultValue : 'Char',
		description : '字段数据库数据类型定义。'
	}, {
		name : 'f_db_type_ext',
		text : '数字精度',
		editor : 'string',
		description : '数字类型数据库字段精度定义，例(10,2)。'
	}, {
		name : 'f_length',
		text : '字符长度',
		editor : 'string',
		defaultValue : 50,
		description : '指定字段的字符长度，当数据类型为char或clob时有效。'
	}, {
		name : 'f_java_type',
		text : 'java数据类型',
		editor : 'combosimple',
		store : ['String', 'Long', 'Float', 'Datetime', 'Blob'],
		defaultValue : 'String',
		description : '字段对应的JAVA数据类型定义。'
	}, {
		name : 'f_keyfield',
		text : '是否主键',
		editor : 'boolean',
		defaultValue : false,
		description : '标识当前字段在数据库表中是否为主键字段。'
	}, {
		name : 'f_allownull',
		text : '允许为空',
		editor : 'boolean',
		defaultValue : true,
		description : '标识当前字段在数据库表中是否允许为空。'
	}, {
		name : 'f_defaultvalue',
		text : '默认值',
		editor : 'string',
		description : '指定字段的默认值。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:150},
		'f_name':{width:150, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_db_type':{width:100},
		'f_db_type_ext':{width:70},
		'f_length':{width:80},
		'f_java_type':{width:100},
		'f_keyfield':{width:80},
		'f_allownull':{width:80},
		'f_defaultvalue':{width:100},
		'f_properties' : {width:120},
		'f_description':{flex:1}
	}
});
//=================================================================
//	�ļ�����Dictionary.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'dictionary',
	// 名称
	caption : '静态数据字典',
	//前缀
	prefix : 'dic',
	// 是否设计对象
	isDesignObject : true,
	// 编辑器定义
	editors : [
		'designer/properties.editor', 
	{
		title : '字典数据列表',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'dictionarydata'}
	}]
});
//=================================================================
//	�ļ�����DictionaryData.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'dictionarydata',
	// 名称
	caption : '字典数据',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '字典排序号。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_name':{width:150, header:'字典值'},
		'f_caption':{width:120, header:'名称'},
		'f_properties':{flex:1},
		'f_description':{width:200}
	}
});
//=================================================================
//	�ļ�����DictionaryFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DictionaryFolder',
	// 名称
	caption : '静态数据字典',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['dictionary']
});
//=================================================================
//	�ļ�����Document.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'document',
	// 名称
	caption : '文档',
	//前缀
	prefix : 'doc',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '文档图标样式名。'
	}, {
		name : 'f_panel_layout',
		text : '标签布局',
		editor : 'combosimple',
		store : ['Tabs', 'Single'],
		defaultValue : 'Single',
		description : '文档表单展示类型,多标签窗口或单页显示。'
	},{
		name : 'f_i_table',
		text : '数据表',
		editor : 'designobject',
		editorCfg : {
			type : ['table'],
			scope : 'app'
		},
		description : '设置文档关联的主数据表。'
	},{
		name : 'f_i_flow',
		text : '流程对象',
		editor : 'designobject',
		editorCfg : {
			type : ['flow'],
			scope : 'flow'
		},
		description : '设置文档应用的流程对象。'
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'containerCfg',type:'object',cfg:{},value:{},
			children:[{name:'modal',type:'boolean',cfg:{},value:true}]}],
		description : '文档的扩展属性，设置该文档的一些基本默认属性。'
	}, {
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '文档事件，定义文档事件代码。',
		editorCfg : {
			server : {
				BeforeLoadData : 'unimplemented function',
				LoadData : 'unimplemented function',
				AfterLoadData : 'unimplemented function',
				BeforeSave : 'JsonObject function(Document doc, long dataId, JsonObject olddata, Map<String, String> formdata, User user)',	// y
				Save : 'unimplemented function',
				AfterSave : 'JsonObject function(Document doc, long dataId, JsonObject olddata, Map<String, String> formdata, JsonObject newdata, User user)',	// y
				BeforeDelete : 'function(Document doc, long dataId, JsonObject olddata, User user)',
				Delete : 'unimplemented function',
				AfterDelete : 'function(Document doc, long dataId, JsonObject olddata, User user)'
			},
			client : {
				beforeOpen : 'boolean function(dData, dCmp)',
				afterOpen : 'function(dCmp)',
				active : 'function(dCmp)',
				close : 'function(dCmp)',
				beforeSubmit : 'function(dCmp)',
				doSubmit : 'function(dCmp)',
				afterSubmit : 'function(dCmp)'
			},
			extJs : ['document']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '文档按钮',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'button'}
	},{
		title : '身份定义',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'useridentity'}
	},{
		title : '文档状态',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'state'}
	},{
		title : '辅助状态',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'extstate'}
	},{
		title : '标签页面',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'panel'}
	},{
		title : '权限映射',
		jspUrl : 'designer/document.authorities.editor'
	}]
});
//=================================================================
//	�ļ�����DocumentFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DocumentFolder',
	// 名称
	caption : '文档类表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['document']
});
//=================================================================
//	�ļ�����DocumentType.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'documenttype',
	// 名称
	caption : '文档类型',
	//前缀
	prefix : 'dt',
	// 是否设计对象
	isDesignObject : true,
	//属性定义
	properties : [{
		name : 'f_type',
		text : '文档类型',
		editor : 'number',
		description : '定义文档的类型。'
	},{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '设置文档类型的图标。'
	},{
		name : 'f_action',
		text : '文档对应动作',
		editor : 'combo',
		store : [['None', '无动作'],['Script', '执行脚本'],['PageObject', '打开页面'],['Hyperlink', '超链接']],
		defaultValue : 'None',
		description : '设置文档类型的动作类型。'
	},{
		name : 'f_param',
		text : '参数',
		editor : 'json',
		description : '设置文档类型传入的参数。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����DocumentTypeFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'DocumentTypeFolder',
	// 名称
	caption : '文档类表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['documenttype']
});
//=================================================================
//	�ļ�����EsbAppEvents.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'EsbAppEvent',
	// 名称
	caption : '应用服务',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['designer/esb/esb.interface.list']
});
//=================================================================
//	�ļ�����EsbAppService.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'EsbAppService',
	// 名称
	caption : '应用服务',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['designer/esb/esb.interface.list']
});
//=================================================================
//	�ļ�����EsbWorkflowFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'EsbWorkflowFolder',
	// 名称
	caption : '业务流列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['serviceflow']
});
//=================================================================
//	�ļ�����FileImportDelParam.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'fileimportdelparam',
	// 名称
	caption : '删除参数',
	//前缀
	prefix : 'fidp',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_sql',
		text : '删除的sql语句',
		editor : 'text',
		description : '删除的sql语句（delete XXX from XXX）。'
	},{
		name : 'f_null_sql',
		text : '空值的删除sql语句',
		editor : 'text',
		description : '空值删除的sql语句（delete XXX from XXX where XXX is null）。'
	},{
		name : 'f_is_default',
		text : '默认参数',
		editor : 'boolean',
		defaultValue : false,
		description : '标识参数是否是默认参数，创建时设定。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:120},
		'f_name':{width:120, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_sql':{width:120, header:'删除的sql语句'},
		'f_null_sql':{width:120,header:'空值sql语句'},
		'f_is_default':{width:80, header:'默认参数'},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����FileImportField.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'fileimportfield',
	// 名称
	caption : '字段匹配列表',
	//前缀
	prefix : 'fif',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_column_name',
		text : '数据库表的列名',
		editor : 'string',
		description : '对应数据库的数据表的列名。'
	},{
		name : 'f_file_column_index',
		text : '对应文件的列下标。',
		editor : 'number',
		description : '导入对应文件的列下标。'
	},{
		name : 'f_java_type',
		text : 'java类型',
		editor : 'combo',
		store : ['String','Long','Float', 'Datetime', 'Integer', 'Double', 'Boolean'],
		defaultValue : 'String',
		description : '标识导入文件的字段对应的java数据类型。'
	},{
		name : 'f_is_primary_key',
		text : '是否关键字段',
		editor : 'boolean',
		defaultValue : false,
		description : '标识导入文件的字段是否是主键。'
	},{
		name : 'f_is_display',
		text : '是否显示',
		editor : 'boolean',
		defaultValue : false,
		description : '标识导入文件的字段是否是在页面上展示出来。'
	},{
		name : 'f_i_dictionary',
		text : ' 关联转换字典',
		editor : 'designobject',
		editorCfg : {
			type : 'dictionary',
			scope : 'app'
		},
		description : '标识导入文件的字段是否转换字典。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:120},
		'f_name':{width:120, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_column_name':{width:120, header:'数据库表的列名'},
		'f_file_column_index':{width:50,header:'列下标'},
		'f_java_type':{width:80, header:'java类型'},
		'f_is_primary_key':{width:120, header:'是否关键字段'},
		'f_is_display':{width:80, header:'是否显示'},
		'f_i_dictionary':{width:120,header:' 关联转换字典'},
		'f_properties':{width:90},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����FileImportSchema.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'fileimportschema',
	// 名称
	caption : '文件导入对象',
	//前缀
	prefix : 'fis',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_table_name',
		text : '表名',
		editor : 'string',
		description : '对应到数据库的表名。'
	}, {
		name : 'f_begin_row_index',
		text : '开始行数',
		editor : 'number',
		defaultValue : 1,
		description : '导入的文件，从第几行开始导入。'
	},{
		name : 'f_default_operations',
		text : '默认操作',
		editor : 'json',
		defaultValue : {"add":true,"update":true,"delete":false},
		/*initialValue :[{name:'add',type:'boolean',cfg:{},value:true},
		               {name:'update',type:'boolean',cfg:{},value:true},
		               {name:'delete',type:'boolean',cfg:{},value:false}],*/
		description : '设置默认操作，add增加，update更新，delete删除。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '删除参数',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'fileimportdelparam'}
	},{
		title : '字段匹配列表',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'fileimportfield'}
	}]
});
//=================================================================
//	�ļ�����FileImportSchemaForder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'FileImportSchemaForder',
	// 名称
	caption : '文件导入配置列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['fileimportschema']
});
//=================================================================
//	�ļ�����Flow.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'flow',
	// 名称
	caption : '流程',
	//前缀
	prefix : 'flow',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['node'],
	properties : [{
		name : 'f_type',
		text : '流程类型',
		editor : 'combo',
		store : [['MainWorkflow', '主流程'],['SubWorkflow', '子流程']],
		description : '定义流程的类型，流程是主流程或者子流程。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '流程事件，定义流程事件代码。',
		editorCfg : {
			server : {
				Started : 'function(long flowlogId, String flowId, int flowVersionId, String dataType, long dataId, User user)',
				Completed : 'function(long flowlogId, String flowId, int flowVersionId, String dataType, long dataId, User user)',
				Canceled : 'function(long flowlogId, String flowId, int flowVersionId, String dataType, long dataId, User user)',
				Resumed : 'function(long flowlogId, String flowId, int flowVersionId, String dataType, long dataId, User user)',
				NodeBegin : 'function(long flowlogId, String flowId, int flowVersionId, String nodeId, String dataType, long dataId, User user)',
				NodeEnd : 'function(long flowlogId, String flowId, int flowVersionId, String nodeId, String dataType, long dataId, User user)',
				BeforeSubmit : 'function(String action, long flowlogId, String flowId, int flowVersionId, String dataType, long dataId, User user)',
				BeforeConfirm : 'function(long flowlogId, String flowId, int flowVersionId, String dataType, long dataId, User user)'
			}
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '流程图',
		jspUrl : 'designer/flow.editor'
	}]
});
//=================================================================
//	�ļ�����FlowFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'FlowFolder',
	// 名称
	caption : '流程节点列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['node']
});
//=================================================================
//	�ļ�����FlowRole.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'flowrole',
	// 名称
	caption : '流程角色',
	//前缀
	prefix : 'fr',
	// 是否设计对象
	isDesignObject : true,
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����FlowRoleFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'FlowRoleFolder',
	// 名称
	caption : '流程角色列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['flowrole']
});
//=================================================================
//	�ļ�����Form.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'form',
	// 前缀
	prefix : 'frm',
	// 名称
	caption : '表单',
	//前缀
	prefix : 'frm',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['formtablelayout', 'formpositionlayout'],
	// 属性定义
	properties : [{
		name : 'f_i_table',
		text : '数据表',
		editor : 'designobject',
		editorCfg : {
			type : ['table'],
			scope : 'app'
		},
		description : '指定表单对应的数据表。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '表单布局事件，定义事件代码。',
		editorCfg : {
			server : {
				QueryOpen : 'function(long documentid, JsonObject data)',//打开前
				QuerySave : 'function(long documentid, JsonObject olddata, JsonObject newdata)',//保存前
				PostSave : 'function(long documentid, JsonObject olddata, JsonObject newdata)',//保存后
				QueryDelete : 'function(long documentid, long dataid)',//删除前
				PostDelete : 'function(long documentid, long dataid)',//删除后
				PostDataLoad : 'function(long documentid, JsonObject olddata)'//装载数据后
			},
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '输入域',
		jspUrl : 'designer/objectgrid.inputfield.editor',
		params : {type : 'inputfield'}
	}]
});
//=================================================================
//	�ļ�����FormFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'FormFolder',
	// 名称
	caption : '表单列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['form']
});
//=================================================================
//	�ļ�����FormPositionLayout.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'formpositionlayout',
	// 名称
	caption : '背景布局',
	//前缀
	prefix : 'fpl',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_width',
		text : '背景宽度',
		editor : 'number',
		defaultValue : 500,
		description : '指定表单背景图片像素宽度，即界面宽度。'
	},{
		name : 'f_height',
		text : '背景高度',
		editor : 'number',
		defaultValue : 600,
		description : '指定表单背景图片像素高度度，即界面高度。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '表单布局事件，定义事件代码。',
		editorCfg : {
			server : {
				beforeLoad : 'function(TableForm form, JsonObject params, FormContext context)',
				load : 'JsonObject function(TableForm form, JsonObject params, FormContext context)',
				afterLoad : 'function(TableForm form, JsonObject params, FormContext context, JsonObject data)',
				beforeSubmit : 'boolean function(TableForm form, JsonObject oldData, JsonObject submitData, JsonObject errors, FormContext context)',
				submit : 'JsonObject function(TableForm form, JsonObject oldData, JsonObject submitData, FormContext context)',
				afterSubmit : 'function(TableForm form, JsonObject oldData, JsonObject newData, FormContext context)',
				beforeSubmitPost : 'boolean function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				submitPost : 'JsonObject function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				afterSubmitPost : 'function(TableForm form, JsonObject oldData, JsonObject newData, Map<String, File> files, FormContext context)',
				beforeDelete : 'boolean function(TableForm form, JsonObject params, FormContext context)',
				delete : 'function(TableForm form, JsonObject params, FormContext context)',
				afterDelete : 'function(TableForm form, JsonObject params, JsonObject delData, FormContext context)'
			},
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '表单布局',
		jspUrl : 'designer/form.positionlayout.editor'
	},{
		title : '表单操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});
//=================================================================
//	�ļ�����FormTableLayout.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'formtablelayout',
	// 名称
	caption : '表格布局',
	//前缀
	prefix : 'ftl',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_cols',
		text : '列数',
		editor : 'none',
		defaultValue : 1,
		description : '布局列数，自动维护。'
	},{
		name : 'f_rows',
		text : '行数',
		editor : 'none',
		defaultValue : 0,
		description : '布局行数，自动维护。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '表单布局事件，定义事件代码。',
		editorCfg : {
			server : {
				beforeLoad : 'function(TableForm form, JsonObject params, FormContext context)',
				load : 'JsonObject function(TableForm form, JsonObject params, FormContext context)',
				afterLoad : 'function(TableForm form, JsonObject params, FormContext context, JsonObject data)',
				beforeSubmit : 'boolean function(TableForm form, JsonObject oldData, JsonObject submitData, JsonObject errors, FormContext context)',
				submit : 'JsonObject function(TableForm form, JsonObject oldData, JsonObject submitData, FormContext context)',
				afterSubmit : 'function(TableForm form, JsonObject oldData, JsonObject newData, FormContext context)',
				beforeSubmitPost : 'boolean function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				submitPost : 'JsonObject function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				afterSubmitPost : 'function(TableForm form, JsonObject oldData, JsonObject newData, Map<String, File> files, FormContext context)',
				beforeDelete : 'boolean function(TableForm form, JsonObject params, FormContext context)',
				delete : 'function(TableForm form, JsonObject params, FormContext context)',
				afterDelete : 'function(TableForm form, JsonObject params, JsonObject delData, FormContext context)'
			},
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '表单布局',
		jspUrl : 'designer/form.tablelayout.editor'
	},{
		title : '表单操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});
//=================================================================
//	�ļ�����Identity.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'useridentity',
	// 名称
	caption : '文档身份',
	//前缀
	prefix : 'uid',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_judge_source_type',
		text : '身份判定源类型',
		editor : 'combo',
		editorCfg : {
			editable : true
		},
		store:[['User','当前用户'],['UserId','用户ID'],['UserCaption','用户姓名'],['UserName','登录名'],['OrgId','机构ID'],['OrgCaption','机构名称'],['CompanyId','单位ID'],['CompanyCaption','单位名称'],['DeptId','部门ID'],['DeptCaption','部门名'],['RoleId','角色ID'],['RoleName','角色名'],['Expression','组织机构表达式'],['ModuleRole','模块角色'],['Custom', '自定义']],
		defaultValue : 'User',
		description : '设定身份判定源数据类型。'
	}, {
		name : 'f_judge_source',
		text : '判定源',
		editor : 'string',
		description : '身份判定源参数。'
	}, {
		name : 'f_judge_operator',
		text : '判定运算',
		editor : 'combosimple',
		store:['Equals','MemberOf','IncludeMember','LeaderOf','DirectLeaderOf','UnderlingOf','DirectUnderlingOf', 'SameDeptartmentOf', 'Custom'],
		defaultValue : 'Equals',
		description : '选择判定运算。'
	}, {
		name : 'f_judge_target_type',
		text : '身份判定目标类型',
		editor : 'combo',
		editorCfg : {
			editable : true
		},
		store:[['User','当前用户'],['UserId','用户ID'],['UserCaption','用户姓名'],['UserName','登录名'],['OrgId','机构ID'],['OrgCaption','机构名称'],['CompanyId','单位ID'],['CompanyCaption','单位名称'],['DeptId','部门ID'],['DeptCaption','部门名'],['RoleId','角色ID'],['RoleName','角色名'],['Expression','组织机构表达式'],['ModuleRole','模块角色'],['Custom', '自定义']],
		defaultValue : 'User',
		description : '设定身份判定目标数据类型。'
	}, {
		name : 'f_judge_target',
		text : '判定源',
		editor : 'string',
		description : '身份判定目标参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_judge_source_type':{width:120},
		'f_judge_source':{width:150},
		'f_judge_operator':{width:120},
		'f_judge_target_type':{width:120},
		'f_judge_target':{width:150},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����InputField.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'inputfield',
	// 名称
	caption : '输入域',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '输入字段排序号。'
	}, {
		name : 'f_i_dbfield',
		text : '数据字段',
		editor : 'designobject',
		editorCfg : {
			type : ['dbfield'],
			scope : 'parent.f_i_table'
		},
		renderer : function(v){
			if(v != null && typeof v == 'object'){
				return v.data;
			}
			return v;
		},
		description : '按钮图标样式名。'
	}, {
		name : 'f_inputtype',
		text : '输入类型',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['none','display','boolean','date', 'time', 
		         'datetime', 'number','text','textarea','combo',
		         'combolist','combotree','check','radio',
		         'organization','file','image','html', 'radiocombo'],
		defaultValue : 'none',
		description : '指定输入域类型。'
	}, {
		name : 'f_defaultvalue',
		text : '默认值',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['|userid|','|userlogin|','|username|','|companynane|', '|companyid|', '|deptid|', '|deptname|','|date|','|time|'],
		defaultValue : 'none',
		//editor : 'string',
		description : '设置字段默认值。'
	}, {
		name : 'f_allownull',
		text : '允许为空',
		editor : 'boolean',
		defaultValue : true,
		description : '字段是否允许输入为空。'
	}, {
		name : 'f_maxlength',
		text : '字段长度',
		editor : 'number',
		defaultValue : 0,
		description : '设置字段允许输入最大长度。'
	}, {
		name : 'f_validator',
		text : '合法性验证',
		editor : 'text',
		description : '设置字段合法性验证函数，提交时自动调用。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'displayCfg',type:'object',cfg:{},value:{}},
		       		{name:'editCfg',type:'object',cfg:{},value:{}},
		       		{name:'store',type:'array',cfg:{},value:[]},
		       		{name:'directFn',type:'string',cfg:{},value:''},
		       		{name:'dictionary',type:'string',cfg:{},value:''},
		       		{name:'sdictionary',type:'string',cfg:{},value:''},
		       		{name:'jspUrl',type:'string',cfg:{},value:''},
		       		{name:'renderer',type:'boolean',cfg:{},value:true},
		       		{name:'valueField',type:'string',cfg:{},value:''},
		       		{name:'isDefault',type:'boolean',cfg:{},value:true}],
		description : '输入域的扩展属性，设置该输入域的一些基本默认属性。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_i_dbfield':{width:120},
		'f_inputtype':{width:100},
		'f_allownull':{width:80},
		'f_maxlength':{width:80},
		'f_defaultvalue':{width:120},
		'f_validator':{width:150},
		'f_properties':{width:120},
		'f_description':{flex:1}
	}
});
//=================================================================
//	�ļ�����Layout.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'layout',
	// 名称
	caption : '布局',
	//前缀
	prefix : 'lyo',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '页面图标样式名。'
	}, {
		name : 'f_type',
		text : '布局类型',
		editor : 'combo',
		store : [['Border', 'Border布局'],['Tabs', '多标签布局'],['Column', '列布局'],['Table', '表格布局'],['VBox', '垂直布局'],['HBox', '水平布局'],['Position', '绝对位置布局'],['Anchor', '伸缩面板布局']],
		defaultValue : 'None',
		description : '指定布局页面的布局类型。'
	},{
		name : 'f_layout_config',
		text : '布局配置参数',
		editor : 'json',
		description : '设置布局配置参数。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor', {
		title : '布局元素',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'layoutitem'}
	}]
});
//=================================================================
//	�ļ�����LayoutFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'LayoutFolder',
	// 名称
	caption : '布局列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['layout']
});
//=================================================================
//	�ļ�����LayoutItem.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'layoutitem',
	// 名称
	caption : '布局元素',
	//前缀
	prefix : 'lyoi',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '视图按钮排序号。'
	},{
		name : 'f_i_page',
		text : '页面对象',
		editor : 'designobject',
		editorCfg : {
			type : ['view', 'treepage', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout'],
			scope : 'module'
		},
		description : '设置布局页面对象。'
	},{
		name : 'f_layout_config',
		text : '布局配置参数',
		editor : 'json',
		initialValue :[{name:'region',type:'string',cfg:{},value:''},
		       		{name:'layout',type:'string',cfg:{},value:''},
		       		{name:'split',type:'boolean',cfg:{},value:true},
		       		{name:'collapseMode',type:'string',cfg:{},value:'mini'},
		       		{name:'autoScroll',type:'boolean',cfg:{},value:true}],
		description : '设置布局配置参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_i_page':{width:150},
		'f_layout_config':{width:150},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����Module.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'module',
	// 名称
	caption : '模块',
	//前缀
	prefix : 'm',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '设置模块图标样式。'
	},{
		name : 'f_type',
		text : '界面类型',
		editor : 'combo',
		store : [['默认', 'default'],['JspPage', 'JSP页面']],
		defaultValue : 'default',
		description : '选择模块界面类型，默认或打开JSP页面。'
	},{
		name : 'f_i_startpage',
		text : '起始页面',
		editor : 'designobject',
		editorCfg : {
			type : ['view', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout']
		},
		description : '起始页面，选择页面对象作为模块打开时的默认页面。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '模块事件，定义模块事件代码。',
		editorCfg : {
			client : {
				beforeOpen : 'boolean function(mData, mCmp)',
				afterOpen : 'function(mData, mCmp)',
				active : 'function(mCmp)',
				close : 'function(mCmp)',
			},
			extJs : ['modulePanel', 'moduleMenuTree', 'moduleWorkspace']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����ModuleAuthorities.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ModuleAuthorities',
	// 名称
	caption : '模块权限',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['designer/module.authorities.editor']
});
//=================================================================
//	�ļ�����ModuleFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ModuleFolder',
	// 名称
	caption : '模块列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['module']
});
//=================================================================
//	�ļ�����ModuleMenu.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'menu',
	// 名称
	caption : '模块菜单',
	//前缀
	prefix : 'menu',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['menu'],
	// 属性
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '菜单排序号。'
	},{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	}, {
		name : 'f_script',
		text : 'JS脚本',
		editor : 'jsarea',
		description : '该按钮点击时执行的客户端脚本。'
	}, {
		name : 'f_group_type',
		text : '分组类型',
		editor : 'combo',
		store : [['None', '无分组'],['StaticDictionary', '静态字典'],['Dictionary', '数据字典'],['Organization', '组织机构'],['QueryData', '检索数据'],['ViewGroup', '视图分组'],['Custom', '自定义']],
		defaultValue : 'None',
		description : '指定菜单下级节点分组类型。'
	}, {
		name : 'f_group_arguments',
		text : '分组参数',
		editor : 'json',
		initialValue :[{name:'dictionaryKey',type:'string',cfg:{},value:''},
		       		{name:'orgCfg',type:'object',cfg:{},value:{},
		       		children:[{name:'listOrg',type:'boolean',cfg:{},value:true},
		       		{name:'listUser',type:'boolean',cfg:{},value:false}]},
		       		{name:'textField',type:'string',cfg:{},value:''},
		       		{name:'valueField',type:'string',cfg:{},value:''},
		       		{name:'isShowCount',type:'boolean',cfg:{},value:true},
		       		{name:'paramName',type:'string',cfg:{},value:''},
		       		{name:'isMultiLevel',type:'boolean',cfg:{},value:true},
		       		{name:'query',type:'designobject',cfg : {type:['query'],scope:'app'},value:{}},
		       		{name:'view',type:'designobject',cfg : {type:['view'],scope:'app'},value:{}}],
		description : '指定分组参数。'
	}, {
		name : 'f_action_type',
		text : '动作类型',
		editor : 'combo',
		store : [['None', '无动作'],['Script', '执行脚本'],['PageObject', '打开页面'],['Hyperlink', '超链接'],['ModuleMenu', '打开菜单']],
		defaultValue : 'None',
		description : '指定菜单动作类型'
	}, {
		name : 'f_action_arguments',
		text : '动作目标',
		editor : 'json',
		initialValue : [{name:'url',type:'string',cfg:{},value:''},
		                {name:'page',type:'designobject',cfg : {
		        			type : ['view', 'treepage', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout'],
		        			scope : 'module'},value:{}},
		                {name:'menu',type:'designobject',cfg : {type:['menu'],scope:'module'},value:{}},
		                {name:'params',type:'object',cfg:{},value:{}}],
		description : '配合动作类型设定目标内容：<br>NONE ： 无效<br>超链接 ： 目标设定为菜单打开的url路径<br>打开页面： 目标为页面标识<br>执行脚本： 目标为可运行的js脚本<br>打开菜单： 目标为菜单标识'
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'isDefault',type:'boolean',cfg:{},value:true}],
		description : '模块菜单的扩展属性，设置该菜单的一些基本默认属性。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_name':{width:100, header:'菜单名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:100},
		'f_script':{width:150},
		'f_group_type':{width:80},
		'f_group_arguments':{width:120},
		'f_action_type':{width:80},
		'f_action_arguments':{width:120},
		//'f_events':{width:120},
		'f_properties':{width:200},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor', {
		title : '下级菜单',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'menu'}
	}]
});
//=================================================================
//	�ļ�����ModuleMenuFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ModuleMenuFolder',
	// 名称
	caption : '模块菜单列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['menu'],
	// 编辑器定义
	editors : [{
		title : '模块菜单',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'menu'}
	}]
});
//=================================================================
//	�ļ�����ModuleRole.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'modulerole',
	// 名称
	caption : '模块角色',
	//前缀
	prefix : 'mr',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['modulerole'],
	// 属性
	properties : [],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����ModuleRoleFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ModuleRoleFolder',
	// 名称
	caption : '模块角色列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['modulerole']
});
//=================================================================
//	�ļ�����Node.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'node',
	// 名称
	caption : '流程节点',
	//前缀
	prefix : 'nd',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['route'],
	properties : [{
		name : 'f_type',
		text : '节点类型',
		editor : 'combo',
		store : [['StartNode', '开始节点'],['ForkNode', '分支节点'],
		         ['JoinNode', '合并节点'],['ProcessNode', '办理节点'],
		         ['ScriptNode', '脚本计算节点'],['SubFlowNode', '子流程节点'],
		         ['EndNode', '结束节点']],
		//defaultValue : 'ProcessNode',
		description : '流程节点的类型，根据类型可以找出对应的节点图形。'
	},{
		name : 'f_process_type',
		text : '节点办理类型',
		editor : 'combo',
		store : [['SingleProcess', '单人办理'],['SingleRequestProcess', '申请办理'],
		         ['MultiAyncProcess', '多人顺序办理'],['MultiParallelProcess', '多人并行办理']],
		//defaultValue : 'SingleProcess',
		description : '流程节点的办理类型。'
	},{
		name : 'f_i_processors',
		text : '节点办理人',
		editor : 'checkcomb',
		editorCfg : {
			type : 'flowrole'
		},
		description : '流程节点的办理人（仅支持流程角色）。'
	},{
		name : 'f_route_merge',
		text : '合并方式',
		editor : 'combo',
		store : [['InterSection', '交集'],['OuterSection', '并集']],
		//defaultValue : 'InterSection',
		description : '路由关系结果与节点办理人合并方式。'
	},{
		name : 'f_process_due',
		text : '办理过期时间',
		editor : 'number',
		description : '办理过期时间（时间长度，以小时为单位）。'
//	},{
//		name : 'f_allow_forward',
//		text : '允许转办',
//		editor : 'boolean',
//		defaultValue : false,
//		description : '在此节点，是否允许转给其他人办理。'
//	},{
//		name : 'f_allow_deputation',
//		text : '允许代办',
//		editor : 'boolean',
//		defaultValue : false,
//		description : '在此节点，是否允许让其他人代办理。'
	},{
		name : 'f_allow_cancel',
		text : '允许撤销',
		editor : 'boolean',
		defaultValue : false,
		description : '当前节点是否允许流程发起人或管理员撤销流程办理。'
	},{
		name:'f_react_mode',
		text:'流程提交交互模式',
		editor : 'combo',
		store : [['Wizard', '流程向导'],['RouteButton', '路由按钮']],
		defaultValue:'Wizard',
		description:'流程提交交互模式：流程向导模式，按步骤设置目标和办理人后提交；路由按钮模式，路由名称作为文档按钮，直接提交；'
	},{
		name : 'f_script',
		text : '节点计算脚本',
		editor : 'textarea',
		description : '计算节点脚本返回从节点出发的某一条路由标识符，用以控制流程流向'
	},{
		name : 'f_events',
		text : '节点事件',
		editor : 'events',
		description : '节点事件的定义。',
		editorCfg : {
			server : {
				Started : 'function(String nodeId, long flowlogId, long nodelogId, String dataType, long dataId, User user)',
				Completed : 'function(String nodeId, long flowlogId, long nodelogId, String dataType, long dataId, User user)',
				Canceled : 'function(String nodeId, long flowlogId, long nodelogId, String dataType, long dataId, User user)'
			}
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����PageFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'PageFolder',
	// 名称
	caption : '页面列表',
	// 是否设计对象
	isDesignObject : false
});
//=================================================================
//	�ļ�����Panel.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'panel',
	// 名称
	caption : '文档标签',
	//前缀
	prefix : 'p',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '页面图标样式名。'
	}, {
		name : 'f_type',
		text : '标签类型',
		editor : 'combosimple',
		store : ['Form','View','Custom'],
		defaultValue : 'Form',
		description : '指定文档标签类型。'
	},{
		//name : 'f_i_page',
		name : 'f_i_page',
		text : '标签页面',
		editor : 'designobject',
		editorCfg : {
			//returnType : 'string',
			type : ['view', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout'],
			scope : 'module'
		},
		description : '指定标签对应的页面对象。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'cfg',type:'object',cfg:{},value:{}},
		               {name:'idParamName',type:'string',cfg:{},value:''}],
		description : '文档标签的扩展属性，设置该文档标签的一些基本默认属性。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '页面事件，定义标签页面事件代码。',
		editorCfg : {
			server : {
				beforeLoad : 'function()',
				load : 'function()',
				afterLoad : 'function()',
				beforeSubmit : 'function()',
				submit : 'function()',
				afterSubmit : 'function()',
			},
			client : {
				beforeOpen : 'function()',
				afterOpen : 'function()',
				active : 'function()',
				close : 'function()'
			},
			extJs : ['panel']
		}
	},{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '标签页的排序号。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:100},
		'f_type':{width:70},
		'f_i_page':{width:120},
		'f_parameters':{width:200},
		'f_events':{width:150},
		'f_properties':{width:150},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����Parameter.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'parameter',
	// 名称
	caption : '参数',
	// 是否设计对象
	isDesignObject : true,
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����Portlet.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'portlet',
	// 名称
	caption : '门户栏目',
	//前缀
	prefix : 'ptl',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '设置栏目图标样式。'
	},{
		name : 'f_i_page',
		text : '对应页面',
		editor : 'designobject',
		editorCfg : {
			type : ['view', 'formtablelayout', 'formpositionlayout', 'urlpage', 'layout']
		},
		description : '对应页面，选择页面对象作为桌面栏目对应的默认页面。'
	},{
		name : 'f_refresh_interval',
		text : '刷新间隔（秒）',
		editor : 'number',
		defaultValue : 30,
		description : '栏目自动刷新的时间间隔。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����PortletFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'PortletFolder',
	// 名称
	caption : '桌面栏目列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['portlet']
});
//=================================================================
//	�ļ�����Query.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'query',
	// 名称
	caption : '数据检索',
	//前缀
	prefix : 'q',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['datagroup'],
	// 属性定义
	properties : [{
		name : 'f_i_ds',
		text : '数据源',
		editor : 'designobject',
		editorCfg : {
			type : 'datasource'
		},
		description : '指定查询对应的数据源对象。'
	}, {
		name : 'f_from',
		text : 'from语句',
		editor : 'text',
		description : '查询SQL的from子句。'
	}, {
		name : 'f_where',
		text : 'where语句',
		editor : 'text',
		description : '查询SQL的where子句。'
	}, {
		name : 'f_groupby',
		text : 'groupby语句',
		editor : 'text',
		description : '查询SQL的groupby子句。'
	}, {
		name : 'f_orderby',
		text : 'orderby语句',
		editor : 'text',
		description : '查询SQL的orderby子句。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor', 
	{
		title : '查询参数',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'queryparameter'}
	}]
});
//=================================================================
//	�ļ�����QueryField.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryfield',
	// 名称
	caption : '查询字段',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '输入字段排序号。'
	}, {
		name : 'f_inputtype',
		text : '输入类型',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		store : ['none','display','boolean','number','text','textarea','combo','check','radio','file','image','html'],
		defaultValue : 'none',
		description : '指定输入域类型。'
	}, {
		name : 'f_defaultvalue',
		text : '默认值',
		editor : 'string',
		description : '设置字段默认值。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_inputtype':{width:120},
		'f_defaultvalue':{width:120},
		'f_properties':{width:150},
		'f_description':{flex:1}
	}
});
//=================================================================
//	�ļ�����QueryFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'QueryFolder',
	// 名称
	caption : '数据检索列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['query']
});
//=================================================================
//	�ļ�����QueryForm.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryform',
	// 名称
	caption : '查询表单',
	//前缀
	prefix : 'qfrm',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['queryfield'],
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_cols',
		text : '表单列数',
		editor : 'none',
		defaultValue : 1,
		description : '设置查询表单布局的总列数，自动维护。'
	},{
		name : 'f_rows',
		text : '表单行数',
		editor : 'none',
		defaultValue : 0,
		description : '设置查询表单布局的总行数，自动维护。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '查询表单事件，定义查询表单事件代码。',
		editorCfg : {
			server : {
				beforeLoad : 'function(TableForm form, JsonObject params, FormContext context)',
				load : 'JsonObject function(TableForm form, JsonObject params, FormContext context)',
				afterLoad : 'function(TableForm form, JsonObject params, FormContext context, JsonObject data)',
				beforeSubmit : 'boolean function(TableForm form, JsonObject oldData, JsonObject submitData, JsonObject errors, FormContext context)',
				submit : 'JsonObject function(TableForm form, JsonObject oldData, JsonObject submitData, FormContext context)',
				afterSubmit : 'function(TableForm form, JsonObject oldData, JsonObject newData, FormContext context)',
				beforeSubmitPost : 'boolean function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				submitPost : 'JsonObject function(TableForm form, JsonObject oldData, Map<String, String> submitData, Map<String, File> files, JsonObject errors, FormContext context)',
				afterSubmitPost : 'function(TableForm form, JsonObject oldData, JsonObject newData, Map<String, File> files, FormContext context)',
				beforeDelete : 'boolean function(TableForm form, JsonObject params, FormContext context)',
				delete : 'function(TableForm form, JsonObject params, FormContext context)',
				afterDelete : 'function(TableForm form, JsonObject params, JsonObject delData, FormContext context)'
			},
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '查询输入域',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'queryfield'}
	},{
		title : '查询表单布局',
		jspUrl : 'designer/form.tablelayout.editor'
	}]
});
//=================================================================
//	�ļ�����QueryFormFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'QueryFormFolder',
	// 名称
	caption : '查询表单列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['queryform']
});
//=================================================================
//	�ļ�����QueryFormLayout.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryformlayout',
	// 名称
	caption : '表单布局',
	// 是否设计对象
	isDesignObject : true,
	// 编辑器定义
	editors : ['designer/properties.editor', 
	           'designer/queryformlayout.editor']
});
//=================================================================
//	�ļ�����QueryParameter.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'queryparameter',
	// 名称
	caption : '查询参数',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_sql',
		text : '查询语句',
		editor : 'text',
		description : '查询条件语句，语句中的|value|为参数值。'
	}, {
		name : 'f_nullsql',
		text : '空值查询语句',
		editor : 'text',
		description : '当查询参数值为空时的查询条件语句。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:150},
		'f_name':{width:150, header:'参数名'},
		'f_caption':{width:120, header:'中文名'},
		'f_sql':{width:200},
		'f_nullsql':{width:200},
		'f_description':{flex:1}
	}
});
//=================================================================
//	�ļ�����Reference.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'Reference',
	// 名称
	caption : '外部引用',
	// 是否设计对象
	isDesignObject : false,
	// 编辑器定义
	editors : ['designer/reference.editor']
});
//=================================================================
//	�ļ�����Root.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'root',
	// 名称
	caption : '根节点',
	// 是否设计对象
	isDesignObject : false
});
//=================================================================
//	�ļ�����Route.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'route',
	// 名称
	caption : '路由',
	//前缀
	prefix : 'rt',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['relation'],
	
	//属性
	properties : [{
		name : 'f_rbuton_caption',
		text : '路由按钮名称',
		editor : 'text',
		defaultValue : false,
		description : '路由作为操作输出时的操作名称'
	},{
    	name : 'f_allow_return', 
    	text : '允许退回', 
    	editor : 'boolean', 
    	defaultValue : true,
    	description : '是否允许该条路由的退回办理操作。'
    },{
    	name : 'f_allow_takeback', 
    	text : '允许拿回', 
    	editor : 'boolean', 
    	defaultValue : true,
    	description : '是否允许该条路的上一办理人进行拿回操作。'
    },{
    	name : 'f_events', 
    	text : '路由事件', 
    	editor : 'events', 
    	description : '定义路由事件。',
    	editorCfg : {
    		server : {
    			Backward : 'function(long flowlogId, String flowId, int flowVersionId, String routeId, String dataType, String dataId, User user)',
    			Forward : 'function(long flowlogId, String flowId, int flowVersionId, String routeId, String dataType, String dataId, User user)',
    			Validate : 'function(long flowlogId, String flowId, int flowVersionId, String routeId, String dataType, String dataId, User user)'
			}
		},
		renderer : function(v){
			if(typeof v == 'object'){
				return Asc.extension.EventsEditor.getEventsAbs(v);;
			}
			return v;
		}
    }],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '路由关系',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'relation'}
	}]
});
//=================================================================
//	�ļ�����RouteRelation.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'relation',
	// 名称
	caption : '路由关系',
	//前缀
	prefix : 'rtl',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	//childrenType : ['route'],
	properties : [{
    	name:'f_type', 
    	text:'关系类型', 
    	editor:'combo',  
    	editorCfg:{
    		editable:false
    	},
    	store:[['1','流程启动者'],['2','目标节点历史办理人'],['3','同部门人员'],['4','直接部门领导'],['5','所有上级领导'],['6','上一节点办理人'],['7','所有已办理人员'],['8','直接下属'],['9','所有下属'],['10','分管领导'],['11','流程管理员'],['12','流程读者'],['0','自定义类型']],
    	defaultValue : 'none',
    	description:'路由关系类型。'
    },{
    	name:'f_source', 
    	text:'关系源', 
    	editor:'combo',  
    	editorCfg:{
    		editable:false
    	},
    	store:[['0','按当前办理人'],['1','按流程启动者'],['2','按上一主办人']],
    	defaultValue : 'none',
    	description:'关系计算相关类型，0：按流程启动者计算关系；1：按当前办理人计算关系；2：按上一办理人计算关系。'
    },{
    	name:'f_arguments', 
    	text:'解析参数', 
    	editor:'string', 
    	description:'定义路由关系解析参数，自定类型有效。'
    },{
    	name:'f_merge_type', 
    	text:'结果计算', 
    	editor:'combo', 
    	editorCfg:{
    		editable:false
    	},
    	store:[['0','交集'],['1','并集']],
    	defaultValue : 'none',
    	description:'各计算关系之间的结果合并方式，intersection：交集；outersection：并集。'
    }],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '路由关系',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'relation'}
	}],
	propertyColumns : {
		'f_key':{width:100, header:'Key'},
		'f_name':{width:150, header:'英文名'},
		'f_caption':{width:150, header:'中文名'},
		'f_source':{width:100, header:'关系源'},
		'f_type':{width:180, header:'关系类型'},
		'f_merge_type':{width:80, header:'结果计算'},
		'f_arguments':{width:150, header:'解析参数'}
	}
});
//=================================================================
//	�ļ�����ServiceFlow.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'serviceflow',
	// 名称
	caption : 'ESB业务流',
	//前缀
	prefix : 'sf',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['sfnode'],
	//属性定义
	properties : [{
		name : 'f_version',
		text : '业务流版本',
		editor : 'string',
		defaultValue : '1.0',
		description : '定义业务流的版本。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'textarea',
		description : '设置业务流的事件脚本。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����SfNode.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'sfnode',
	// 名称
	caption : '节点',
	//前缀
	prefix : 'sfnode',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['sfroute'],
	//属性定义
	properties : [{
		name : 'f_type',
		text : '节点类型',
		editor : 'combo',
		store : [['StartNode', '开始节点'],['MessageNode', '消息节点'],
		         ['ServiceNode', '服务节点'],['ScriptNode', '脚本判断节点'],
		         ['EndNode', '结束节点']],
		//defaultValue : 'ProcessNode',
		description : '业务流节点的类型，根据类型可以找出对应的节点图形。'
	},{
		name : 'f_app_key',
		text : '应用标识',
		editor : 'string',
		description : '业务流节点的应用标识，需要填写。'
	},{
		name : 'f_message_key',
		text : '消息标识',
		editor : 'string',
		description : '业务流节点的消息标识，需要填写。'
	},{
		name : 'f_service_key',
		text : '服务标识',
		editor : 'String',
		description : '业务流节点的服务标识，需要填写。'
	},{
		name : 'f_params',
		text : '参数映射',
		editor : 'json',
		description : '节点类型为服务调用节点的时候起作用，用来映射向服务传递的参数。'
	},{
		name : 'f_i_message_src_node',
		text : '消息来源节点',
		editor : 'designobject',
		editorCfg : {
			type : ['sfnode'],
			scope : 'esbflownode'
		},
		description : '只定义该消息节点的消息来源节点。'
	},{
		name : 'f_script',
		text : '节点计算脚本',
		editor : 'textarea',
		description : '节点计算脚本代码'
	},{
		name : 'f_events',
		text : '节点事件',
		editor : 'textarea',
		description : '节点事件的定义。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����SfRoute.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'sfroute',
	// 名称
	caption : '路由',
	//前缀
	prefix : 'sfroute',
	// 是否设计对象
	isDesignObject : true,
	//属性定义
	properties : [{
		name : 'f_i_dest_node',
		text : '目标节点',
		editor : 'designobject',
		editorCfg : {
			type : ['sfnode'],
			scope : 'esbflow'
		},
		description : '定义该路由的目标节点。'
	},{
		name : 'f_type',
		text : '路由类型',
		editor : 'combo',
		store : [['CommonRoute', '一般路由'],['ErrorRoute', '异常路由']],
		description : '定义路由的类型。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'textarea',
		description : '设置业务流的事件脚本。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����Shortcut.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'shortcut',
	// 名称
	caption : '快捷方式',
	//前缀
	prefix : 'sct',
	// 是否设计对象
	isDesignObject : true,
	//属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	}, {
		name : 'f_big_icon',
		text : '桌面显示的大图标',
		editor : 'string',
		description : '桌面上的快捷方式显示的大图标。'
	}, {
		name : 'f_script',
		text : 'JS脚本',
		editor : 'jsarea',
		description : '该快捷方式点击时执行的客户端脚本。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����ShortcutFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ShortcutFolder',
	// 名称
	caption : '快捷方式列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['shortcut']
});
//=================================================================
//	�ļ�����State.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'state',
	// 名称
	caption : '文档状态',
	//前缀
	prefix : 'st',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_isdefault',
		text : '默认状态',
		editor : 'boolean',
		defaultValue : false,
		description : '标识当前状态是否默认状态，创建时设定。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:120},
		'f_name':{width:120, header:'英文名'},
		'f_caption':{width:120, header:'中文名'},
		'f_isdefault':{width:80, header:'默认状态'},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����SubState.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'extstate',
	// 名称
	caption : '文档辅助状态',
	//前缀
	prefix : 'est',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_left',
		text : '左操作值',
		editor : 'string',
		description : '子状态的左操作值，|field|或普通string值。'
	}, {
		name : 'f_operator',
		text : '操作符',
		editor : 'combo',
		store : [['Equal','='],['More','>'],['Less','<'],['MoreOrEqual','>='],['LesOrEqual','<='],['Include','包含'],['Custom','自定义']],
		defaultValue : 'equal',
		description : '设定左右操作值匹配计算的操作符。'
	}, {
		name : 'f_right',
		text : '右操作值',
		editor : 'string',
		description : '子状态的右操作值，|field|或普通string值。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_left':{width:200},
		'f_operator':{width:80},
		'f_right':{width:200},
		'f_properties':{width:150},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});
//=================================================================
//	�ļ�����Table.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'table',
	// 名称
	caption : '数据表',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_i_ds',
		text : '数据源',
		editor : 'designobject',
		editorCfg : {
			type : 'datasource',
			scope : 'app'
		},
		description : '指定数据表对应的数据源对象。'
	}],
	// 编辑器定义
	editors : [
		'designer/properties.editor', 
	{
		title : '字段列表',
		jspUrl : 'designer/dbfield.editor',
		params : {type : 'dbfield'}
	}]
});
//=================================================================
//	�ļ�����TableFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'TableFolder',
	// 名称
	caption : '数据表列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['table']
});
//=================================================================
//	�ļ�����TreeNode.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'treenode',
	// 名称
	caption : '树状页面节点',
	//前缀
	prefix : 'treenode',
	// 是否设计对象
	isDesignObject : true,
	// 可添加的子节点类型
	childrenType : ['treenode'],
	// 属性
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '节点排序号。'
	},{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '节点图标样式名。'
	}, {
		name : 'f_group_type',
		text : '分组类型',
		editor : 'combo',
		store : [['None', '无分组'],['StaticDictionary', '静态字典'],['Dictionary', '数据字典'],['Organization', '组织机构'],['QueryData', '检索数据'],['ViewGroup', '视图分组'],['Custom', '自定义']],
		defaultValue : 'None',
		description : '指定菜单下级节点分组类型。'
	}, {
		name : 'f_group_arguments',
		text : '分组参数',
		editor : 'json',
		initialValue :[{name:'dictionaryKey',type:'string',cfg:{},value:''},
			       		{name:'orgCfg',type:'object',cfg:{},value:{},
			       		children:[{name:'listOrg',type:'boolean',cfg:{},value:true},
			       		{name:'listUser',type:'boolean',cfg:{},value:false}]},
			       		{name:'textField',type:'string',cfg:{},value:''},
			       		{name:'valueField',type:'string',cfg:{},value:''},
			       		{name:'isShowCount',type:'boolean',cfg:{},value:true},
			       		{name:'paramName',type:'string',cfg:{},value:''},
			       		{name:'isMultiLevel',type:'boolean',cfg:{},value:true},
			       		{name:'query',type:'designobject',cfg : {type:['query'],scope:'app'},value:{}},
			       		{name:'view',type:'designobject',cfg : {type:['view'],scope:'app'},value:{}}],
		description : '指定分组参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_name':{width:100, header:'节点名'},
		'f_caption':{width:100, header:'中文名'},
		'f_icon':{width:100},
		'f_group_type':{width:80},
		'f_group_arguments':{width:120},
		'f_properties':{width:200},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor', {
		title : '下级节点',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'treenode'}
	}]
});
//=================================================================
//	�ļ�����TreePage.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'treepage',
	// 名称
	caption : '树状页面',
	//前缀
	prefix : 'tree',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '图标样式名。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '节点定义',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'treenode'}
	},{
		title : '右键操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});
//=================================================================
//	�ļ�����TreePageFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'TreeFolder',
	// 名称
	caption : 'Tree页面',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['treepage']
});
//=================================================================
//	�ļ�����UrlPage.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'urlpage',
	// 名称
	caption : 'URL页面',
	//前缀
	prefix : 'up',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '页面图标样式名。'
	},{
		name : 'f_use_proxy',
		text : '使用JSP代理',
		editor : 'boolean',
		defaultValue : true,
		description : '是否使用默认JSP页面代理（JSP页面有效）。'
	},{
		name : 'f_url',
		text : '页面URL路径',
		editor : 'string',
		description : '设置页面的url路径。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	},{
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '页面事件，定义URL页面事件代码。',
		editorCfg : {
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '页面操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	}]
});
//=================================================================
//	�ļ�����UrlPageFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'UrlPageFolder',
	// 名称
	caption : 'URL页面',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['urlpage']
});
//=================================================================
//	�ļ�����View.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'view',
	// 名称
	caption : '视图页面',
	//前缀
	prefix : 'v',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_icon',
		text : '图标',
		editor : 'string',
		description : '菜单图标样式名。'
	},{
		name : 'f_i_dbquery',
		text : '数据查询',
		editor : 'designobject',
		editorCfg : {
			type : 'query',
			scope : 'app'
		},
		description : '设置绑定到视图的数据库查询对象。'
	},{
		name : 'f_type',
		text : '视图类型',
		editor : 'combo',
		store : [['Normal', '普通视图'],['WeekView', '周视图'],['MonthView', '月视图'],['Custom', '自定义']],
		defaultValue : 'Normal',
		description : '指定视图界面类型，自定义视图可指定目标文件。'
	},{
		name : 'f_target',
		text : '视图目标',
		editor : 'string',
		description : '视图界面对应的解析程序URL路径，当视图类型为自定义时生效'
	},{
		name : 'f_i_form',
		text : '编辑表单',
		editor : 'designobject',
		editorCfg : {
			type : 'form',
			scope : 'module'
		},
		description : '指定行编辑对应的编辑表单，当视图为行编辑时有效。'
	},{
		name : 'f_i_documenttype',
		text : '文档类型',
		editor : 'designobject',
		editorCfg : {
			type : 'documenttype',
			scope : 'app'
		},
		description : '设置绑定到视图的数据库查询对象。'
	},{
		name : 'f_selectmode',
		text : '行选择类型',
		editor : 'combo',
		store : [['SingleSelect', '单行选择'],['MultiSelect', '多行选择']],
		defaultValue : 'SingleSelect',
		description : '视图行选择类型，指定是否为多行选择。'
	},{
		name : 'f_pagesize',
		text : '每页显示记录数',
		editor : 'number',
		defaultValue : 30,
		description : '设定列表每页显示记录数量，0为显示所有，不分页。'
	},{
		name : 'f_querytype',
		text : '查询类型',
		editor : 'combo',
		store : [['None', 'NONE'],['Column', '字段查询'],['Form', '表单查询']],
		defaultValue : 'Column',
		description : '设定查询类型，字段查询为默认生成查询界面，表单查询则由外部查询表单提供查询界面。'
	},{
		name : 'f_vague_query',
		text : '模糊查询',
		editor : 'boolean',
		defaultValue : true,
		description : '设定是否支持模糊查询，在所有字符字段中模糊检索。'
	},{
		name : 'f_parameters',
		text : '页面参数',
		editor : 'json',
		defaultValue : true,
		description : '设定页面访问参数。'
	},{
		name : 'f_has_controller',
		text : '页面控制器',
		editor : 'boolean',
		description : '是否定义页面控制器，按照命名规则编写控制器js文件。'
	}, {
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'cfg',type:'object',cfg:{},value:{}}],
		description : '视图的扩展属性，设置该视图的一些基本默认属性。'
	}, {
		name : 'f_events',
		text : '事件脚本',
		editor : 'events',
		description : '视图事件，定义视图事件代码。',
		editorCfg : {
			server : {
				BeforeDataLoad : 'function(View view, JsonObject params, ViewContext context, User user)',
				DataLoad : 'JsonArray function(View view, JsonObject params, ViewContext context, User user)',
				AfterDataLoad : 'function(View view, JsonObject params, JsonArray results, ViewContext context, User user)',
				ColumnDataParse : 'JsonPrimitive function(ViewColumn column, JsonPrimitive data, ViewContext context, User user)'
			},
			client : {
				beforeOpen : 'boolean function(pData, pCmp)',
				afterOpen : 'function(pData, pCmp)',
				active : 'function(pCmp)',
				close : 'function(pCmp)',
				beforeLoad : 'boolean function(pCmp, params)',
				afterDataLoad : 'function(pCmp, params)'
			},
			extJs : ['panel', 'form', 'grid', 'store']
		}
	}],
	// 编辑器定义
	editors : ['designer/properties.editor',{
		title : '列定义',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'viewfield'}
	},{
		title : '视图操作',
		jspUrl : 'designer/objecttreegrid.editor',
		params : {type : 'button'}
	},{
		title : '查询条件',
		jspUrl : 'designer/objectgrid.editor',
		params : {type : 'viewqueryparameter'}
	}]
});
//=================================================================
//	�ļ�����ViewColumn.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'viewfield',
	// 名称
	caption : '视图字段',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '视图按钮排序号。'
	}, {
		name : 'f_asname',
		text : '字段别名',
		editor : 'string',
		description : 'SQL语句中的别名。'
	}, {
		name : 'f_type',
		text : '列类型',
		editor : 'combosimple',
		store : ['None','String','Integer','Float','Date','Datetime','Icon','File','Dictionary','JsonObject','Custom'],
		defaultValue : 'String',
		description : '视图列类型。'
	}, {
		name : 'f_renderer',
		text : '渲染脚本',
		editor : 'textarea',
		description : '字段渲染脚本函数，自定义字段显示。'
	}, {
		name : 'f_query',
		text : '查询方式',
		editor : 'combosimple',
		store : ['None','String','Dictionary','Date','Number'],
		defaultValue : 'None',
		description : '设置列查询方式。'
	}, {
		name : 'f_orderable',
		text : '支持排序',
		editor : 'boolean',
		defaultValue : true,
		description : '设置列是否支持排序。'
	}, {
		name : 'f_width',
		text : '列宽',
		editor : 'number',
		defaultValue : 120,
		description : '设置列显示宽度。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		initialValue :[{name:'dictionary',type:'string',cfg:{},value:''},
		            {name:'sdictionary',type:'string',cfg:{},value:''},
		       		{name:'cfg',type:'object',cfg:{},value:{},
		       		children:[{name:'xtype',type:'string',cfg:{},value:''},
		       		{name:'decimalPrecision',type:'number',cfg:{},value:0}
		       		]}],
		description : '列定义的扩展属性，设置该列的一些基本默认属性。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_asname':{width:100},
		'f_type':{width:80},
		'f_renderer':{width:150},
		'f_query':{width:100},
		'f_orderable':{width:80},
		'f_width':{width:80},
		'f_properties':{width:200},
		'f_description':{flex:1}
	}
});
//=================================================================
//	�ļ�����ViewFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'ViewFolder',
	// 名称
	caption : '视图列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['view']
});
//=================================================================
//	�ļ�����ViewQueryParameter.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'viewqueryparameter',
	// 名称
	caption : '查询参数',
	//前缀
	prefix : 'vqp',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [,{
		name : 'f_type',
		text : '条件类型',
		editor : 'combo',
		store : [['Static', '静态条件'],['Parameters', '参数条件']],
		defaultValue : 'Parameters',
		description : '选择查询条件类型，静态条件始终执行，参数条件根据视图参数选择执行。'
	},{
		name : 'f_sql',
		text : '查询语句',
		editor : 'text',
		description : '查询条件语句，语句中的|value|为参数值。'
	}, {
		name : 'f_nullsql',
		text : '空值查询语句',
		editor : 'text',
		description : '当查询参数值为空时的查询条件语句。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor'],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:150},
		'f_name':{width:150, header:'条件名'},
		'f_caption':{width:120, header:'中文名'},
		'f_type':{width:80, header:'条件类型'},
		'f_sql':{width:200},
		'f_nullsql':{width:200},
		'f_description':{flex:1}
	}
});
//=================================================================
//	�ļ�����WorkFlowFolder.js
//=================================================================
AscApp.ClassManager.registerClass({
	// 类型
	type : 'WorkFlowFolder',
	// 名称
	caption : '流程列表',
	// 是否设计对象
	isDesignObject : false,
	// 可添加的子节点类型
	childrenType : ['flow']
});