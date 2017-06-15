Ext.define('Asc.common.ClassManager', {
	// 构造函数
	constructor : function(){
		// 装载节点类注册信息
		Ext.Loader.loadScript({
			url : this.url
		});
	},
	// 装载类描述文件路径
	url : 'runtime/js/asc.manager.classes.js',
	// 注册类集合
	classes : {},
	// 类属性缓存管理
	properties : {},
	// 默认配置
	deafultConfig : {
		// 类型
		type : 'default',
		// 名称
		caption : '运维管理',
		// 可添加的子节点类型
		childrenType : undefined,
		// 扩展属性
		properties : undefined,
		// 编辑界面
		editors : undefined,
		// 扩展功能
		extendActions : undefined
	},

	// 注册类
	registerClass : function(config){
		config.iconCls = ('icon-manager-' + config.type).toLowerCase();
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
	// 设置当前激活对象
	setActivateObject : function(obj){
		
	},
	getNodeId : function(type, key){
		return type + '-' + key;
	},
	// 获得类属性
	getProperties : function(type){
		if(!Ext.isDefined(this.properties[type])){
			var c = this.getClass(type);
			var properties = {};
			// 对象属性
			for(var i in c.properties){
				var p = c.properties[i];
				if(Ext.isDefined(properties[p.name])){
					Ext.apply(properties[p.name], p);
				}else{
					properties[p.name] = Ext.apply({}, p);;
				}
			}
			this.properties[type] = properties;
		}
		return this.properties[type];
	},
	// 获得类属性
	getPropertie : function(type){
		var c = this.getClass(type);
		var properties = {};
		// 对象属性
		for(var i in c.properties){
			var p = c.properties[i];
			if(Ext.isDefined(properties[p.name])){
				Ext.apply(properties[p.name], p);
			}else{
				properties[p.name] = Ext.apply({}, p);;
			}
		}
		this.properties[type] = properties;
		return this.properties[type];
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
	// 获得视图列
	getGridColumns : function(properties, propertyColumns){
		// 初始化列属性
		var columns = [{
			xtype: 'rownumberer',
			dataIndex : Ext.isDefined(properties['f_order']) ? 'f_order' : undefined
		}];
		for(var n in propertyColumns){
			var col = Ext.apply({
				dataIndex : properties[n].name,
				header : properties[n].text
			}, propertyColumns[n]);
			if(!Ext.isDefined(col.renderer)){
				var renderer = this.getPropertyRenderer(properties[n]);
				if(Ext.isDefined(renderer)){
					col.renderer = renderer;
				}
			}
			columns.push(col);
		}
		return columns;
	},
	// 获得存储字段
	getStoreFields : function(properties){
		var fields = [];
		for(var n in properties){
			var p = properties[n];
			fields.push({name : p.name});
		}
		return fields;
	},
	getPropertyEditor : function(p){
		var editor;
		switch(p.editor){
		case 'readOnly':
		case 'none':
			editor = new Ext.form.field.Display(Ext.apply({
				valueToRaw: function(value) {
					if(typeof value == 'object'){
						value = Ext.encode(value);
					}
					return '' + Ext.value(value, '');
				},
				getValue : function(){
					return this.value;
				}
			}, p.editorCfg));
			break;
		case 'date':
			editor = new Ext.form.field.Date(Ext.apply({
				format : 'Y-m-d', 
				altFormats : 'Y-m-d|Y-m-d H:i:s',
				selectOnFocus: true, 
				rawToValue : function(v){
					if(Ext.isDefined(v) && v != ''){
						return v + ' 00:00:00';
					}else{
						return v;
					}
					
				}
			}, p.editorCfg));
			break;
		case 'event':
		case 'string':
			editor = new Ext.form.field.Text(Ext.apply({selectOnFocus: true}, p.editorCfg));
			break;
		case 'text':
		case 'textarea':
			editor = new Asc.extension.TextEditor(p.editorCfg);
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
		case 'treecombox':
			editor = new Asc.extension.TreeCombox(Ext.apply({
				store: p.store,
				relationId : p.editorCfg['relationId'],
				dicType : p.editorCfg['dicType']
			},p.editotCfg));
			break;
		case 'gridcombox':
			editor = new Asc.extension.GridCombox(Ext.apply({
				store: p.store,
				relationId : p.editorCfg['relationId'],
				dicType : p.editorCfg['dicType']
			},p.editotCfg));
			break;
		case 'json':
			editor = new Asc.extension.JsonEditor(Ext.apply({appId:this.appId,key:this.key,initialValue:p.initialValue}),p.editorCfg);
			break;
		case 'designobject':
			editor = new Asc.extension.DesignObjectEditor(p.editorCfg);
			break;
		case 'events':
			editor = new Asc.extension.EventsEditor(Ext.apply({eventConf:p.editorCfg}),p.editorCfg);
			break;
		case 'cdru' :
			editor = new Asc.common.CdruEditor(p.editorCfg);
		}
		return editor;
	},
	getPropertyRenderer : function(p){
		var renderer;
		switch(p.editor){
		case 'combosimple':
		case 'combo':
		case 'treecombox':
		case 'gridcombox':
			renderer = function(v, metaData, record){
			for(var i in p.store){
				if(p.store[i][0] == v){
					return p.store[i][1];
				}
			}
			return v;
		}
			break;
		case 'designobject':
		case 'json':
			renderer = function(v, metaData, record){
				if(typeof v == 'object'){
					//if(Ext.Object.isEmpty(v)){
					//	return '';
					//}
					return  unescape(Ext.encode(v).replace(/\\u/gi, '%u'));
				}
				return v;
			}
			break;
		case 'date' :
			renderer = Ext.util.Format.dateRenderer('Y-m-d');
			break;
		}
		return renderer;
	}
});