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