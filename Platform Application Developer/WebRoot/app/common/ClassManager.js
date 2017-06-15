Ext.define('Asc.common.ClassManager', {
	// 构造函数
	constructor : function(){
		// 装载节点类注册信息
		Ext.Loader.loadScript({
			url : this.url
		});
	},
	// 装载类描述文件路径
	url : 'runtime/js/asc.developer.classes.js',
	// 注册类集合
	classes : {},
	//当前页面的key
	key : '',
	//当前连接的appId
	appId : '',
	// 类属性缓存管理
	properties : {},
	// 默认配置
	deafultConfig : {
		// 类型
		type : 'default',
		// 名称
		caption : '设计对象',
		// 是否设计对象
		isDesignObject : true,
		// 可添加的子节点类型
		childrenType : undefined,
		// 扩展属性
		properties : undefined,
		// 编辑界面
		editors : undefined,
		// 扩展功能
		extendActions : undefined
	},
	// 设计对象默认属性
	defaultProperties : [{
		name : 'id',
		text : '标识',
		editor : 'none',
		description : '对象唯一标识，键值全路径。'
	},{
		name : 'f_i_parent',
		text : 'Parent',
		editor : 'none',
		renderer : function(v){
			if(v != null && typeof v == 'object'){
				//把字符中含有Unicode码转化成中文字符
				return unescape(Ext.encode(v).replace(/\\u/gi, '%u'));
			}
			return v;
		},
		description : '父对象标识。'
	},{
		name : 'f_class',
		text : 'Class',
		editor : 'none',
		description : '对象的类型标识。'
	},{
		name : 'f_key',
		text : 'Key',
		editor : 'none',
		description : '对象键值，同一父对象下唯一标识对象。'
	},{
		name : 'f_name',
		text : '命名',
		editor : 'string',
		description : '对象命名，一般为对象的英文名称，建议与键值保持一致。'
	},{
		name : 'f_caption',
		text : '名称',
		editor : 'string',
		description : '对象名称，一般为对象的中文名称。'
	},{
		name : 'f_description',
		text : '说明',
		editor : 'textarea',
		description : '对象说明，说明该对象的定义、用途等描述信息。'
	},{
		name : 'f_properties',
		text : '扩展属性',
		editor : 'json',
		description : '扩展属性，对象可设置的Json结构的可扩展、多级属性定义。。'
	}],
	// 注册类
	registerClass : function(config){
		config.iconCls = ('icon-designer-' + config.type).toLowerCase();
		this.classes[config.type] = config;
	},
	// 判断是否存在类
	hasClass : function(type){
		return Ext.isDefined(this.classes[type]);
	},
	// 获得类定义
	getClass : function(type){
		return this.classes[type];
	},
	// 获得类属性
	getProperties : function(type){
		if(!Ext.isDefined(this.properties[type])){
			var c = this.getClass(type);
			var properties = {};
			if(Ext.isDefined(c)){
				if(c.isDesignObject){
					// 设计对象公共属性
					for(var i in this.defaultProperties){
						var p = this.defaultProperties[i];
						properties[p.name] = Ext.apply({}, p);
					}
				}
				// 对象属性
				for(var i in c.properties){
					var p = c.properties[i];
					if(Ext.isDefined(properties[p.name])){
						Ext.apply(properties[p.name], p);
					}else{
						properties[p.name] = Ext.apply({}, p);;
					}
				}
			}else{
				Asc.common.Message.log('设计对象类型  [' + type + '] 未定义', this, 'log');
			}
			this.properties[type] = properties;
		}
		return this.properties[type];
	},
	getPropertyEditor : function(p){
		var editor;
		switch(p.editor){
		case 'readOnly':
		case 'none':
			editor = new Ext.form.field.Display(Ext.apply({
				valueToRaw: function(value) {
					if(typeof value == 'object'){
						value = unescape(Ext.encode(value).replace(/\\u/gi, '%u'));
					}
					return '' + Ext.value(value, '');
				},
				getValue : function(){
					return this.value;
				}
			}, p.editorCfg));
			break;
		case 'date':
			editor = new Ext.form.field.Date(Ext.apply({format : 'Y-m-d', selectOnFocus: true}, p.editorCfg));
			break;
		case 'event':
		case 'string':
			editor = new Ext.form.field.Text(Ext.apply({selectOnFocus: true}, p.editorCfg));
			break;
		case 'text':
		case 'textarea':
			editor = new Asc.extension.TextEditor(p.editorCfg);
			break;
		case 'jsarea':
			editor = new Asc.extension.JsEditor(p.editorCfg);
			break;
		case 'number':
			editor = new Ext.form.field.Number(Ext.apply({selectOnFocus: true}, p.editorCfg));
			break;
		case 'boolean':
			editor = new Ext.form.field.ComboBox(Ext.apply({
					editable: false,
					store: [[ true, 'true' ], [false, 'false' ]]
			}, p.editorCfg));
			break;
		case 'combo':
		case 'combosimple':
			editor = new Ext.form.field.ComboBox(Ext.apply({
				editable: false,
				store: p.store
			}, p.editorCfg));
			break;
		case 'json':
			editor = new Asc.extension.JsonEditor(Ext.apply({
					appId:this.appId,
					key:this.key,
					initialValue:p.initialValue
				}),p.editorCfg);
			break;
		case 'designobject':
			editor = new Asc.extension.DesignObjectEditor(Ext.apply({
					appId:this.appId,
					key:this.key,
					returnType:p.editorCfg['returnType'],
					type:p.editorCfg['type'],
					scope:p.editorCfg['scope']
			}),p.editorCfg);
			break;
		case 'events':
			editor = new Asc.extension.EventsEditor(Ext.apply({eventConf:p.editorCfg}),p.editorCfg);
			break;
		case 'checkcomb':
			editor = new Asc.extension.CheckCombEditor(Ext.apply({
				appId:this.appId,
				key:this.key,
				type:p.editorCfg['type']
			}),p.editorCfg);
			break;
		}
		return editor;
	},
	getPropertyRenderer : function(p){
		var renderer;
		switch(p.editor){
		case 'designobject':
		case 'json':
			renderer = function(v){
				if(typeof v == 'object'){
					return  unescape(Ext.encode(v).replace(/\\u/gi, '%u'));
				}
				return v;
			}
			break;
		case 'combosimple':
		case 'combo':
			renderer = function(v, metaData, record){
				for(var i in p.store){
					if(p.store[i][0] == v){
						return p.store[i][1];
					}
				}
				return v;
			}
			break;
		case 'events':
			renderer = function(v){
				if(typeof v == 'object'){
					return Asc.extension.EventsEditor.getEventsAbs(v);
				}
				return v;
			}
			break;
		case 'checkcomb':
			renderer = function(v){
				var showValue = '';
  				if(typeof v == 'object'){
  					if(Ext.isDefined(v['data'])){
  		    			for(var i in v['data']){
  		    				showValue = showValue + v['data'][i] + ';';
  		    			}
  					}else{
  		    			for(var i in v){
  		    				showValue = showValue + v[i] + ';';
  		    			}
  					}
        		}else{
        			showValue = v;
        		}
        		return showValue;
			}
			break;
		}
		return renderer;
	},
	// 获得类属性默认值
	getPropertyDefaultValues : function(type){
		var values = {};
		var properties = this.getProperties(type);
		if(Ext.isDefined(properties)){
			for(var n in properties){
				if(Ext.isDefined(properties[n].defaultValue)){
					values[n] = properties[n].defaultValue;
				}
			}
		}
		return values;
	},
	// 设置当前激活对象
	setActivateObject : function(obj){
		this.key = obj.key;
		this.appId = obj.appId;
		var am = AscApp.ActionManager;
		// 设置所有按钮不可用
		am.disableAction();
		// 清空添加按钮菜单项
		var addMenu = Ext.menu.Manager.get('mnuAddDesignObject');
		addMenu.removeAll();
		// 判断可用按钮
		if(Ext.isDefined(obj)){
			am.enableAction('reloadNavigatorSelectedNode');
			var objClass = this.getClass(obj.type);
			if(Ext.isDefined(objClass)){
				if(Ext.isDefined(objClass.editors)){
					am.enableAction('openEditor');
				}
				if(objClass.isDesignObject){
					// 设计对象允许拷贝、更名、删除操作
					am.enableAction('copyObject');
					am.enableAction('renameObject');
					am.enableAction('delObject');
				}
				// 设置可添加的下级菜单
				if(objClass.childrenType && objClass.childrenType.length > 0){
					// 添加子节点菜单
					am.enableAction('addObject');
					for(var n in objClass.childrenType){
						var childClass = this.getClass(objClass.childrenType[n]);
						if(Ext.isDefined(childClass)){
							addMenu.add({
								text : childClass.caption,
								iconCls : childClass.iconCls,
								group : 'designer',
								name : 'addObject',
								type : childClass.type
							});
						}
					}
				}
				switch(obj.type){
				case 'root':
					am.enableAction('connection');
					am.enableAction('registerAppConn');
					break;
				case 'appconn':
					am.enableAction('connection');
					am.enableAction('unregisterAppConn');
					am.enableAction('updateDesigns');
					am.enableAction('submitDesigns');
					break;
				}
			}else{
				Asc.common.Message.log('未定义设计对象类型【' + obj.type + '】', this, 'log');
			}
		}
	},
	getNodeId : function(appId, type, key){
		return appId + '-' + type + '-' + key;
	}
});