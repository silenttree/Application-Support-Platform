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