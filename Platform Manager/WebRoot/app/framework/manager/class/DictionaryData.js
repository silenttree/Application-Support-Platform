AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_dictionary_data',
	// 名称
	caption : '数据管理',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_key',
		text : '数据标识',
		editor : 'string',
		description : '数据标识。'
	}, {
		name : 'f_value',
		text : '名称',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '数据值。'
	}, {
		name : 'f_shortcode',
		text : '短代码',
		editor : 'string',
		description : '数据短代码。'
	}, {
		name : 'f_state',
		text : '状态',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '启用'], [1, '禁用']],
		description : '数据状态。'
	}, {
		name : 'f_level',
		text : '级别',
		editor : 'none',
		defaultValue: 0,
		description : '数据级别。'
	}, {
		name : 'f_order',
		text : '排序',
		editor : 'none',
		defaultValue: 0,
		description : '数据排序。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40,hidden:true},
		'f_key':{
			width:120,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_value':{
			width:100,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_shortcode':{
			width:80,
			editor: new Ext.form.field.Text({
                typeAhead: true
            })
		},
		'f_state':{
			width:40,
			editor: new Ext.form.field.ComboBox({
                typeAhead: false,
        		editable : false,
        		defaultValue: 0,
                triggerAction: 'all',
                store: [
                    [0,'启用'],
                    [1,'禁用']
                ]
            })
		},
		'f_level':{width:40},
	}
});