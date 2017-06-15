AscApp.ClassManager.registerClass({
	// 类型
	type : 't_asc_user',
	// 名称
	caption : '用户信息',
	// 属性定义
	properties : [{
		name : 'id',
		text : 'ID',
		editor : 'none',
		description : '数据表记录ID（唯一）。'
	}, {
		name : 'f_name',
		text : '登录名',
		editor : 'string',
		allowBlank: false,
		editorCfg : {
			allowEmpty : true
		},
		description : '用户登录名。'
	}, {
		name : 'f_caption',
		text : '姓名',
		editor : 'string',
		allowBlank: false,
		description : '用户姓名。'
	}, {
		name : 'f_gender',
		text : '性别',
		editor : 'combo',
		defaultValue: 0,
		store : [[0, '女'], [1, '男']],
		description : '用户的性别。'
	}, {
		name : 'f_type',
		text : '类型',
		editor : 'combo',
		allowBlank: false,
		defaultValue: 0,
		store : [[0, '普通用户'], [1, '运维管理人员'], [2, '开发人员']],
		description : '用户类型。'
	}, {
		name : 'f_state',
		text : '状态',
		editor : 'combo',
		allowBlank: false,
		defaultValue: 0,
		store : [[0, '正常'], [1, '被锁定'], [2, '退休，离职'], [3, '标记删除']],
		description : '用户状态。'
	}, {
		name : 'f_email',
		text : 'E-mail',
		editor : 'string',
		description : '用户邮箱。'
	}, {
		name : 'f_birthdate',
		text : '出生日期',
		editor : 'date',
		renderer: function(value) {
			if(typeof value == 'string' && value.length == 19){
  	  			return Ext.util.Format.dateRenderer('Y-m-d')(Ext.Date.parse(value, 'Y-m-d H:i:s'));
  	  		}
			return value;
		},
		description : '出生日期。'
	}, {
		name : 'f_birthplace',
		text : '出生地',
		editor : 'string',
		description : '出生地。'
	}, {
		name : 'f_post',
		text : '职务',
		editor : 'string',
		description : '职务。'
	}, {
		name : 'f_hire_date',
		text : '入职年月',
		editor : 'date',
		renderer: function(value) {
			if(typeof value == 'string' && value.length == 19){
  	  			return Ext.util.Format.dateRenderer('Y-m-d')(Ext.Date.parse(value, 'Y-m-d H:i:s'));
  	  		}
			return value;
		},
		description : '入职年月。'
	}, {
		name : 'f_cellphone',
		text : '联系方式',
		editor : 'string',
		description : '用户联系方式。'
	}, {
		name : 'f_work_phone',
		text : '单位固话',
		editor : 'string',
		description : '单位固话。'
	}, {
		name : 'f_speciality',
		text : '技能特长',
		editor : 'text',
		description : '技能特长。'
	}, {
		name : 'f_company_caption',
		text : '默认单位',
		editor : 'none',
		description : '用户默认单位。',
		store : [[0, '无单位']]
	}, {
		name : 'f_dept_id',
		text : '默认部门',
		editor : 'combo',
		description : '用户默认部门 。'
		//store : [[0, '无部门']]
	}, {
		name : 'orgs',
		text : '所在机构',
		editor : 'none',
		defaultValue: 0,
		store : [],
		description : '用户所在机构列表。'
	}, {
		name : 'roles',
		text : '所有角色',
		editor : 'none',
		defaultValue: 0,
		store : [],
		description : '用户所有角色列表。'
	},{
		name : 'f_note',
		text : '备注',
		editor : 'text',
		description : '说明信息。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'id':{width:40},
		'f_name':{width:80},
		'f_caption':{width:70},
		'f_gender':{width:40},
		'f_type':{width:100},
		'f_state':{width:80},
		'f_email':{width:120},
		'f_company_caption':{width:120},
		'f_dept_id':{width:120},
		'f_cellphone':{width:100}
	}
});