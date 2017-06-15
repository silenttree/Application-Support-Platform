Ext.define('Asc.common.ActionManager', {
	// 构造函数
	constructor : function(){
		// 装载节点类注册信息
		Ext.Loader.loadScript({
			url : this.url
		});
	},
	// 装载类描述文件路径
	url : 'runtime/js/asc.manager.actions.js',
	// 按钮集合
	actions : {},
	// 函数集合
	functions : {},
	// 注册个功能函数
	addFunction : function(key, fn){
		this.functions[key] = fn;
	},
	// 删除功能函数
	delFunction : function(key){
		delete this.functions[key];
	},
	// 获得功能函数
	getFunction : function(key){
		return this.functions[key];
	},
	// 注册动作
	addAction : function(key, action){
		this.actions[key] = Ext.create('Ext.Action', action);
	},
	// 获得动作对象
	getAction : function(key){
		return this.actions[key];
	},
	// 删除动作
	delAction : function(key){
		delete this.actions[key];
	},
	// 设置动作不可用
	disableAction : function(key){
		if(Ext.isDefined(key)){
			if(this.actions[key].initialConfig.alwaysEnable!==true){
				this.actions[key].disable();
			}
		}else{
			for(var a in this.actions){
				this.disableAction(a);
			}
		}
	},
	// 设置动作可用
	enableAction : function(key){
		if(Ext.isDefined(key)){
			this.actions[key].enable();
		}else{
			for(var a in this.actions){
				this.actions[a].enable();
			}
		}
	},
	// 运行功能函数
	runFunction : function(key, scope, args){
		if(this.functions[key]){
			this.functions[key].apply(scope, args);
		}else{
			Asc.common.Message.log('动作[' + key + ']未注册', scope, 'log');
		}
	},
	// 处理函数
	handler : function(action){
		var key;
		if(action.key){
			key = action.key;
		}else if(action.name){
			key = (action.group || 'default') + '.' + action.name;
		}
		if(Ext.isDefined(key)){
			AscApp.ActionManager.runFunction(key, action, [action]);
		}
	}
});