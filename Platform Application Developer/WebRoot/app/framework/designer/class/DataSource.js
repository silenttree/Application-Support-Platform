AscApp.ClassManager.registerClass({
	// 类型
	type : 'datasource',
	// 名称
	caption : '数据源',
	//前缀
	prefix : 'ds',
	// 是否设计对象
	isDesignObject : true,
	// 属性定义
	properties : [{
		name : 'f_dbtype',
		text : '数据库类型',
		editor : 'combosimple',
		store : ['oracle', 'mysql', 'mssql', 'db2'],
		defaultValue : 'oracle',
		description : '选择数据源对应的数据库类型。'
	}, {
		name : 'f_driverclass',
		text : '驱动类名',
		editor : 'combosimple',
		editorCfg : {
			editable : true
		},
		defaultValue : 'oracle.jdbc.driver.OracleDriver',
		store : ['oracle.jdbc.driver.OracleDriver'],
		description : '选择或输入数据源对应的驱动类名称。'
	},{
		name : 'f_url',
		text : '访问URL',
		editor : 'string',
		defaultValue : 'jdbc:oracle:thin:@localhost:1521:orcl',
		description : 'JDBC数据库访问路径定义，例：jdbc:oracle:thin:@localhost:1521:orcl。'
	},{
		name : 'f_username',
		text : '用户名',
		editor : 'string',
		description : '数据库访问用户名。'
	},{
		name : 'f_password',
		text : '密码',
		editor : 'string',
		description : '数据库访问密码。'
	}, {
		name : 'f_dbcp_type',
		text : '连接池类型',
		editor : 'combosimple',
		store : ['DBCP', 'BONECP', 'DRUID'],
		defaultValue : 'DBCP',
		description : '指定数据库连接池管理类型。'
	}, {
		name : 'f_maxconns',
		text : '最大连接数',
		editor : 'number',
		defaultValue : 100,
		description : '设定数据库连接池的最大连接数。'
	}, {
		name : 'f_minconns',
		text : '最小连接数',
		editor : 'number',
		defaultValue : 10,
		description : '设定数据库连接池的最小连接数。'
	}, {
		name : 'f_initconns',
		text : '初始连接数',
		editor : 'number',
		defalutValue : 30,
		description : '设定数据库连接池初始连接数。'
	}, {
		name : 'f_stepconns',
		text : '连接池步长',
		editor : 'number',
		defaultValue : 10,
		description : '设定数据库连接池每次创建的连接数。'
	}],
	// 编辑器定义
	editors : ['designer/properties.editor']
});