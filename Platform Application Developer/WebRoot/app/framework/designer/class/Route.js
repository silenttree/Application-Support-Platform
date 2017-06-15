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