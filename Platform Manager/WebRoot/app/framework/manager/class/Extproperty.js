AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_ext_property',
	// 名称
	caption : '属性扩展',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_table_name',
		text : '表名',
		editor : 'none',
		defaultValue : 't_asc_dictionary_data',
		description : '扩展表名称。'
	}, {
		name : 'f_field_name',
		text : '属性标识',
		editor : 'string',
		description : '扩展属性标识。'
	}, {
		name : 'f_field_caption',
		text : '属性名称',
		editor : 'string',
		description : '扩展属性名称。'
	}, {
		name : 'f_field_type',
		text : '类型',
		editor : 'combo',
		defaultValue: 0,
		store: [
		        [0,'String'],
				[1,'Long'],
				[2,'Float']
		],
		description : '扩展属性类型。'
	}, {
		name : 'f_editor_type',
		text : '编辑类型',
		editor : 'combo',
		defaultValue: 0,
		store: [
				['readOnly','readOnly'],
				['none','none'],
				['date','date'],
				['event','event'],
				['text','text'],
				['textarea','textarea'],
				['number','number'],
				['boolean','boolean'],
				['combo','combo'],
				['combosimple','combosimple'],
				['treecombox','treecombox'],
				['gridcombox','gridcombox'],
				['json','json'],
				['designobject','designobject'],
				['events','events'],
				['cdru','cdru']
		],
		description : '扩展属性类型。'
	}, {
		name : 'f_length',
		text : '长度',
		editor : 'number',
		description : '扩展属性长度。'
	}, {
		name : 'f_nullable',
		text : '能否为空',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '否'], [1, '是']],
		description : '属性能否为空'
	}, {
		name : 'f_order',
		text : '排序',
		editor : 'none',
		description : '排序。'
	}, {
		name : 'f_config',
		text : '配置',
		editor : 'json',
		description : '配置。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:30,hidden:true},
		'f_field_name':{
			width:100,
			editor: new Ext.form.field.Text({
				typeAhead: true
			})
		},
		'f_field_caption':{
			width:100,
			editor: new Ext.form.field.Text({
				typeAhead: true
			})
		},
		'f_field_type':{
			width:60,
			editor: new Ext.form.field.ComboBox({
				typeAhead: false,
				editable : false,
				defaultValue: 0,
				triggerAction: 'all',
				store: [
				        [0,'String'],
				        [1,'Long'],
				        [2,'Float']
				]
			})
		},
		'f_length':{
			width:60,
			editor: new Ext.form.field.Number({
				typeAhead: true
			})
		},
		'f_nullable':{
			flex:1,
			editor: new Ext.form.field.ComboBox({
				typeAhead: false,
				editable : false,
				defaultValue: 0,
				triggerAction: 'all',
				store: [
				        [0,'否'],
				        [1,'是']
				]
			})
		},
		'f_order':{flex:1,hidden:true}
	}
});