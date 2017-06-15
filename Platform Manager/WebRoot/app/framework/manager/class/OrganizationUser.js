AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_org_user',
	// 名称
	caption : '用户信息',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	},{
		name : 'f_user_id',
		text : '用户ID',
		editor : 'none',
		description : '数据表记录用户ID。'
	},{
		name : 'f_org_id',
		text : '机构ID',
		editor : 'none',
		description : '数据表记录机构ID。'
	},{
		name : 'f_isdefault',
		text : '默认机构',
		editor : 'combo',
		store : [[true, '是'], [false, '否']],
		description : '是否是用户的默认机构。'
	}, {
		name : 'f_name',
		text : '登录名',
		editor : 'string',
		editorCfg : {
			allowEmpty : true
		},
		description : '用户登录名。'
	}, {
		name : 'f_caption',
		text : '姓名',
		editor : 'string',
		description : '用户姓名。'
	}, {
		name : 'f_email',
		text : 'E-mail',
		editor : 'string',
		description : '用户邮箱。'
	}, {
		name : 'f_company_caption',
		text : '单位',
		editor : 'string',
		description : '用户所在单位名称。'
	}, {
		name : 'f_cellphone',
		text : '联系方式',
		editor : 'string',
		description : '用户联系方式。'
	}, {
		name : 'f_gender',
		text : '性别',
		editor : 'combo',
		defaultValue: 1,
		store : [[0, '女'], [1, '男']],
		description : '用户的性别。'
	},{
		name : 'f_type',
		text : '类型',
		editor : 'combo',
		defaultValue: 0,
		editable : false,
		store : [[0, '普通成员'], [1, '组织领导'], [2, '组织副职领导'], [3, '组织分管领导']],
		description : '说明信息。'
	},{
		name : 'f_order',
		text : '排序',
		editor : 'none',
		hidden: true,
		description : '排序。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_user_id':{width:60,hidden:true},
		'f_org_id':{width:60,hidden:true},
		'f_name':{width:80},
		'f_caption':{width:70},
		'f_gender':{width:40},
		'f_email' : {width:120},
		'f_company_caption' : {width:120},
		'f_cellphone' : {width:100},
		'f_type':{
			width:40,
			editor: new Ext.form.field.ComboBox({
                typeAhead: false,
        		editable : false,
        		defaultValue: 0,
                triggerAction: 'all',
                store: [
                    [0,'普通成员'],
                    [1,'组织领导'],
                    [2,'组织副职领导'],
                    [3,'组织分管领导']
                ]
            }),
			flex:1
		},
		'f_isdefault':{width:60}
	}
});