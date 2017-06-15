AscApp.ClassManager.registerClass({
	// 类型
	type : 'useridentity',
	// 名称
	caption : '文档身份',
	//前缀
	prefix : 'uid',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_judge_source_type',
		text : '身份判定源类型',
		editor : 'combo',
		editorCfg : {
			editable : true
		},
		store:[['User','当前用户'],['UserId','用户ID'],['UserCaption','用户姓名'],['UserName','登录名'],['OrgId','机构ID'],['OrgCaption','机构名称'],['CompanyId','单位ID'],['CompanyCaption','单位名称'],['DeptId','部门ID'],['DeptCaption','部门名'],['RoleId','角色ID'],['RoleName','角色名'],['Expression','组织机构表达式'],['ModuleRole','模块角色'],['Custom', '自定义']],
		defaultValue : 'User',
		description : '设定身份判定源数据类型。'
	}, {
		name : 'f_judge_source',
		text : '判定源',
		editor : 'string',
		description : '身份判定源参数。'
	}, {
		name : 'f_judge_operator',
		text : '判定运算',
		editor : 'combosimple',
		store:['Equals','MemberOf','IncludeMember','LeaderOf','DirectLeaderOf','UnderlingOf','DirectUnderlingOf', 'SameDeptartmentOf', 'Custom'],
		defaultValue : 'Equals',
		description : '选择判定运算。'
	}, {
		name : 'f_judge_target_type',
		text : '身份判定目标类型',
		editor : 'combo',
		editorCfg : {
			editable : true
		},
		store:[['User','当前用户'],['UserId','用户ID'],['UserCaption','用户姓名'],['UserName','登录名'],['OrgId','机构ID'],['OrgCaption','机构名称'],['CompanyId','单位ID'],['CompanyCaption','单位名称'],['DeptId','部门ID'],['DeptCaption','部门名'],['RoleId','角色ID'],['RoleName','角色名'],['Expression','组织机构表达式'],['ModuleRole','模块角色'],['Custom', '自定义']],
		defaultValue : 'User',
		description : '设定身份判定目标数据类型。'
	}, {
		name : 'f_judge_target',
		text : '判定源',
		editor : 'string',
		description : '身份判定目标参数。'
	}],
	// 列表编辑字段
	propertyColumns : {
		'f_key':{width:100},
		'f_name':{width:100, header:'英文名'},
		'f_caption':{width:100, header:'中文名'},
		'f_judge_source_type':{width:120},
		'f_judge_source':{width:150},
		'f_judge_operator':{width:120},
		'f_judge_target_type':{width:120},
		'f_judge_target':{width:150},
		'f_properties':{width:120},
		'f_description':{flex:1}
	},
	// 编辑器定义
	editors : ['designer/properties.editor']
});