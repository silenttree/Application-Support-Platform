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