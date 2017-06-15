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