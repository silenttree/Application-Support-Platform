AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_org',
	// 名称
	caption : '组织机构',
	// 属性定义
	properties : [
	/*{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_no',
		text : '组织编码',
		editor : 'string',
		description : '组织唯一编码，不可重复。'
	}, {
		name : 'f_caption',
		text : '组织名称',
		editor : 'string',
		description : '指定按钮对象xtype，用于生成按钮界面对象的类引用名。'
	}, {
		name : 'f_type',
		text : '类型',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '单位'], [1, '部门']],
		description : '组织的类型。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	},{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		hidden: true,
		description : '排序。'
	},*/
	// 为伤损项目增加如下属性
	{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_no',
		text : '组织编码',
		editor : 'string',
		description : '组织唯一编码，不可重复。'
	}, {
		name : 'f_code',
		text : '机构编码',
		editor : 'string',
		allowBlank: false,
		description : '机构编码'
	}, {
		name : 'f_caption',
		text : '机构全称',
		editor : 'string',
		allowBlank: false,
		description : '机构全称'
	}/*, {
		name : 'f_orgtype',
		text : '机构类型',
		editor : 'combo',
		defaultValue: '',
		description : '机构类型（总公司、路局、工务段）',
		store : []
	}*//*, {
		name : 'f_level',
		text : '机构层级',
		editor : 'string',
		editable : false,
		description : '机构层级'
	}*/, {
		name : 'f_shortname',
		text : '机构简称',
		editor : 'string',
		allowBlank: false,
		description : '机构简称'
	}, {
		name : 'f_inputcode',
		text : '机构输入码',
		editor : 'string',
		description : '机构输入码'
	}, {
		name : 'f_found_time',
		text : '成立时间',
		editor : 'date',
		renderer: function(value) {
			if(typeof value == 'string' && value.length == 19){
  	  			return Ext.util.Format.dateRenderer('Y-m-d')(Ext.Date.parse(value, 'Y-m-d H:i:s'));
  	  		}
			return value;
		},
		description : '成立时间'
	}, /*{
		name : 'f_amount_gx',
		text : '干线数',
		editor : 'number',
		description : '干线数'
	}, {
		name : 'f_amount_zx',
		text : '正线数',
		editor : 'number',
		description : '正线数'
	},*/ {
		name : 'f_address',
		text : '机构地址',
		editor : 'string',
		description : '机构地址'
	}, {
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}, {
		name : 'f_order',
		text : '排序',
		editor : 'none',
		hidden: true,
		description : '排序。'
	}
	// 增加结束
	],
	// 列表编辑字段
	propertyColumns : {
/*		'id':{width:40},
		'f_no':{width:100},
		'f_caption':{width:120},
		'f_type':{width:50},
		'f_note':{flex:1}*/
		'id':{width:40},
		'f_no':{width:60},
		'f_code':{width:60},
		'f_caption':{width:100},
		//'f_orgtype':{width:60},
		//'f_level':{width:60},
		'f_shortname':{width:100},
		'f_inputcode':{width:100},
		'f_found_time':{width:100},
		//'f_amount_gx':{width:50},
		//'f_amount_zx':{width:50},
		'f_address':{width:100},
		'f_note':{flex:1}
	}
});