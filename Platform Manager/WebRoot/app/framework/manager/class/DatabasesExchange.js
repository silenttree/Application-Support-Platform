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