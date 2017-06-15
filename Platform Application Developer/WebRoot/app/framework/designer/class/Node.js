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