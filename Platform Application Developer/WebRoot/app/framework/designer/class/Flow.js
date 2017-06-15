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