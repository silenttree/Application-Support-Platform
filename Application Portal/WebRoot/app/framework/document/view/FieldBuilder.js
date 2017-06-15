Ext.define('Asc.framework.document.view.FieldBuilder', {
	// 别名
	alternateClassName: 'Asc.FieldBuilder',
	// 设置引用类
	requires : ['Asc.extension.editor.DisplayField',
	            'Asc.extension.editor.CdruEditor',
	            'Asc.extension.editor.DateTimeField'],
	
	statics : {
		// 输出权限标志位
		INPUT : 0,
		// 显示权限标志位
		READ : 1,
		// 编辑权限标志位
		EDIT : 2,
		// 扩展权限标志位
		EXTEND : 3,
		// 根据权限模式判断是否包含标志位
		hasAuth : function(mode, flag){
			if(Ext.isDefined(mode)){
				return mode.substring(flag, flag + 1) == '1';
			}else{
				return true;
			}
		},
		// 获得字段输入域
		getInputItem : function(appKey, fieldCfg, mode, fieldPrefix){
			if(this.hasAuth(mode, this.EDIT)){
				return this.getEditField(appKey, fieldCfg, fieldPrefix);
			}else if(this.hasAuth(mode, this.READ)){
				return this.getDisplayField(appKey, fieldCfg, fieldPrefix, this.hasAuth(mode, this.INPUT));
			}else if(this.hasAuth(mode, this.INPUT)){
				// 集中处理输出字段
				//return this.getHiddenField(appKey, fieldCfg, fieldPrefix);
			}
		},
		getHiddenItem : function(appKey, fieldCfg, mode, fieldPrefix){
			if(this.hasAuth(mode, this.INPUT)){
				// 集中处理输出字段
				return this.getHiddenField(appKey, fieldCfg, fieldPrefix);
			}
		},
		// 隐藏字段
		getHiddenField : function(appKey, fieldCfg, fieldPrefix){
			var item = {
				name : fieldPrefix + fieldCfg.f_key,
				xtype : 'hiddenfield'
			}
			return item;
		},
		// 显示字段
		getDisplayField : function(appKey, fieldCfg, fieldPrefix, submitValue){
			var item = {
				xtype : 'ascdisplayfield',
				anchor : '-10',
				fieldLabel : '&nbsp;&nbsp;' + fieldCfg.f_caption,
				name : fieldPrefix + fieldCfg.f_key,
				submitValue : submitValue
			};
			switch(fieldCfg.f_inputtype){
			case 'none':
				return this.getHiddenField(appKey, fieldCfg, fieldPrefix);
			case 'boolean':
			case 'number':
			case 'text':
			case 'display':
			case 'textarea':
			case 'date':
			case 'time':
			case 'datetime':
				//item.inputId = fieldPrefix + fieldCfg.f_key;
				break;
			case 'combo':
			case 'check':
			case 'radio':
				item.renderer = this.getStoreRenderer(appKey, fieldCfg, fieldPrefix);
				break;
			case 'organization':
				break;
			case 'file':
				break;
			case 'image':
				break;
			case 'html':
				break;
			default:
				break;
			}
			if(Ext.isDefined(fieldCfg.f_properties)){
				Ext.apply(item, fieldCfg.f_properties.displayCfg);
			}
			return item;
		},
		// 编辑字段
		getEditField : function(appKey, fieldCfg, fieldPrefix){
			var item = {
				xtype : 'textfield',
				anchor : '-10',
				fieldLabel : '&nbsp;&nbsp;' + fieldCfg.f_caption,
				name : fieldPrefix + fieldCfg.f_key
			};
			switch(fieldCfg.f_inputtype){
			case 'none':
				return this.getHiddenField(appKey, fieldCfg, fieldPrefix);
			case 'display':
				item =  this.getDisplayField(appKey, fieldCfg, fieldPrefix);
				item.submitValue = true;
				break;
			case 'boolean':
				Ext.apply(item, {
					xtype : 'combo',
					eidtable : false,
					store : [{text : '是', value : 1}, {text : '否', value : 0}],
					value : 1
				});
				break;
			case 'date':
				Ext.apply(item, {
					xtype : 'datefield',
					format : 'Y-m-d',
					editable : false,
					altFormats : 'Y-m-d|Y-m-d H:i:s'
				});
				break;
			case 'time':
				Ext.apply(item, {
					xtype : 'timefield',
					format : 'H:i:s'
				});
				break;
			case 'datetime':
				/*
				Ext.apply(item, {
					xtype: 'fieldcontainer',
					combineErrors: true,
					layout: 'hbox',
					defaults: {
						flex: 1,
						hideLabel: true
					},
					items: [{
						xtype	 : 'datefield',
						name	  : item.name + '_DATE',
						margin: '0 5 0 0',
						format : 'Y-m-d'
					},{
						xtype	 : 'timefield',
						name	  : item.name + '_TIME',
						format : 'H:i:s'
					}]
				});
				*/
				Ext.apply(item, {
					xtype : 'ascdatetimefield',
					format : 'Y-m-d H:i:s',
					editable : false,
					altFormats : 'Y-m-d|Y-m-d H:i:s'
				});
				break;
			case 'number':
				Ext.apply(item, {
					xtype : 'numberfield',
					decimalPrecision : 0
				});
				break;
			case 'text':
				break;
			case 'textarea':
				Ext.apply(item, {
					xtype : 'textareafield'
				});
				break;
			case 'combo':
				Ext.apply(item, {
					xtype : 'combo',
					store : this.getEditorStore(appKey, fieldCfg, fieldPrefix),
					displayField : 'value',
					valueField : 'key',
					minChars: 1,
					listConfig: {
					loadingText: 'Searching...',
					emptyText: 'No matching posts found.',
						 getInnerTpl: function() {
							return '{value}';
						} 
					}
				});
				break;
			case 'check':
				Ext.apply(item, {
					xtype : 'checkgroup'
				});
				break;
			case 'radio':
				Ext.apply(item, {
					xtype : 'radiogroup'
				});
				break;
			case 'organization':
				Ext.apply(item, {
					xtype : 'AscCdruEditor',
					callback : function(v, dv){
						if(fieldCfg.f_properties && fieldCfg.f_properties.valueField){
							var form = this.up('form');
							if(form){
								var bf = form.getForm();
								var vField = bf.findField(fieldPrefix + fieldCfg.f_properties.valueField);
								if(vField){
									vField.setValue(v);
								}
							}
						}
					}
				});
				break;
			case 'file':
				Ext.apply(item, {
					xtype : 'filefield'
				});
				break;
			case 'image':
				break;
			case 'html':
				Ext.apply(item, {
					xtype : 'htmleditor'
				});
				break;
			default:
				break;
			}
			if(Ext.isDefined(fieldCfg.f_properties)){
				Ext.apply(item, fieldCfg.f_properties.editCfg);
			}
			return item;
		},
		// 获得combo类字段的store
		getEditorStore : function(appKey, fieldCfg, fieldPrefix){
			if(Ext.isDefined(fieldCfg.f_properties)){
				var cfg = fieldCfg.f_properties
				// 自定义store
				if(Ext.isDefined(cfg.store)){
					return cfg.store;
				}
				// 自定义DirectStore
				if(Ext.isDefined(cfg.directFn)){
					var appManager = AscApp.getController('AscAppManager');
					var fn = appManager.getEngineDirectFn(appKey, cfg.directFn);
					return Ext.apply({
						proxy : {
							type : 'direct',
							extraParams : {
								action : 'load',
								params : {}
							},
							paramOrder : 'action params',
							reader : {
								type: 'json',
								root : 'datas'
							},
							directFn : fn
						},
						fields : ['key', 'value']
					},  cfg.storeCfg);
				}
				// 字典store
				if(Ext.isDefined(cfg.dictionary)){
					var dicManager = AscApp.getController('AscDictionaryManager');
					return dicManager.getDictionaryStore(appKey, cfg.dictionary, cfg.storeCfg);
				}
				// 静态字典store
				if(Ext.isDefined(cfg.sdictionary)){
					var dicManager = AscApp.getController('AscDictionaryManager');
					return dicManager.getStaticDictionaryStore(appKey, cfg.sdictionary, cfg.storeCfg);
				}
				// Jsp字典store
				if(Ext.isDefined(cfg.jspUrl)){
					var appManager = AscApp.getController('AscAppManager');
					return Ext.apply({
						proxy : {
							type: 'ajax',
							url : appManager.getAppUrl(appKey) + '/' + AscApp.jspPageProxy,
							params : {
								url : cfg.jspUrl
							},
							reader : {
								type: 'json',
								root : 'datas'
							}
						},
						fields : ['key', 'value']
					},  cfg.storeCfg);
				}
			}
		},
		// 获得字典之类的字段的自动转换函数
		getStoreRenderer : function(appKey, fieldCfg, fieldPrefix){
			if(Ext.isDefined(fieldCfg.f_properties)){
				var cfg = fieldCfg.f_properties;
				if(!cfg.renderer){
					return;
				}
				// 自定义renderer
				if(cfg.renderer !== true){
					eval('var renderer = ' + cfg.renderer);
					return renderer;
				}
				// 自定义DirectStore
				if(Ext.isDefined(cfg.directFn)){
					// TODO 自定义字典Direct解析
					return function(v, field){
						var appManager = AscApp.getController('AscAppManager');
						var fn = appManager.getEngineDirectFn(appKey, cfg.directFn);
						fn.call(field, 'renderer', {value : v}, function(result, e){
							if(result && result.success){
								this.setRawValue(result.text);
							}
						}, field);
					}
				}
				// 字典store
				if(Ext.isDefined(cfg.dictionary)){
					var dicManager = AscApp.getController('AscDictionaryManager');
					return dicManager.getDictionaryRenderer(appKey, cfg.dictionary, cfg.storeCfg);
				}
				// 静态字典store
				if(Ext.isDefined(cfg.sdictionary)){
					var dicManager = AscApp.getController('AscDictionaryManager');
					return dicManager.getStaticDictionaryRenderer(appKey, cfg.sdictionary, cfg.storeCfg);
				}
				// Jsp字典store
				if(Ext.isDefined(cfg.jspUrl)){
					// TODO ajax 调用返回值
				}
			}
		}
	}
});