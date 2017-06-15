AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_database_sync_table',
	// 名称
	caption : '内外网交换配置',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_database_sync_id',
		text : '内外网交换ID',
		editor : 'none',
		description : '内外网交换ID。'
	}, {
		name : 'f_tablename',
		text : '表名',
		editor : 'string',
		description : '需要交换的表名。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_database_sync_id':{width:120,hidden:true},
		'f_tablename':{width:300}
	}
});