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