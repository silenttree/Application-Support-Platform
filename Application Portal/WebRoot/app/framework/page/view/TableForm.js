Ext.define('Asc.framework.page.view.TableForm', {
	// 指定基类
	extend : 'Ext.form.Panel',
	// 设置别名
	alias : 'widget.AscTableForm',
	// 设置引用类
	requires : ['Asc.framework.document.view.FieldBuilder'],
	
	bodyPadding: '5 5 5 0',
    
	autoScroll : true,
	
	border : false,
	// 应用标识
	appKey : undefined,
	// 模块ID
	moduleId : undefined,
	// 视图key
	pageId : undefined,
	// 页面参数
	initParams : undefined,
	
	waitMsgTarget : Ext.getBody(),
	// 重载构造函数
	constructor : function(appKey, moduleId, pageData, params, context, cfg){
		var me = this;
		me.appKey = appKey;
		me.moduleId = moduleId;
		me.pageId = pageData.id;
		var config = {};
		// 初始化基础属性
		config.title = pageData.title;
		config.iconCls = pageData.iconCls;
		var inputs = Ext.clone(pageData.layout);
		var fields = pageData.fields;
		// 设置域名称前缀（用于标签表单页面）
		var fieldPrefix = '';
		if(Ext.isDefined(context) && Ext.isDefined(context.panelKey)){
			fieldPrefix = context.panelKey + '.';
		}
		this.fieldPrefix = fieldPrefix;
		// 处理隐藏字段
		var hiddens = [{
			xtype : 'hiddenfield',
			name : 'pageId',
			value : pageData.id
		},{
			xtype : 'hiddenfield',
			name : 'pageId.prefix',
			value : fieldPrefix
		}];
		for(n in fields){
			var item;
			var fieldId = fields[n].id;
			var mode = pageData.mode[fieldId];
			if(Ext.isDefined(context.authorities) && Ext.isDefined(context.authorities[fieldId])){
				mode = context.authorities[fieldId];
			}
			item = Asc.FieldBuilder.getHiddenItem(appKey, fields[n], mode, fieldPrefix);
			if(Ext.isDefined(item)){
				hiddens.push(item);
			}else if(n == 'ID'){
				// 如果是ID字段，确保字段被输出
				item = Asc.FieldBuilder.getInputItem(appKey, fields[n], mode, fieldPrefix);
				if(!item){
					hiddens.push(Asc.FieldBuilder.getHiddenField(appKey, fields[n], fieldPrefix));
				}
			}
		}
		// 处理显示/编辑字段
		for(n in inputs){
			if(Ext.isDefined(inputs[n].field)){
				var fieldId = fields[inputs[n].field].id;
				var mode = pageData.mode[fieldId];
				if(Ext.isDefined(context.authorities) && Ext.isDefined(context.authorities[fieldId])){
					mode = context.authorities[fieldId];
				}
				var item = Asc.FieldBuilder.getInputItem(appKey, 
						fields[inputs[n].field], 
						mode, fieldPrefix);
				Ext.apply(inputs[n], {
					layout : 'form',
					anchor : '100%',
					width : '100%',
					padding : 1,
					items : item || {}
				});
			}
		}
		// 以Table布局放置字段
		config.items = hiddens;
		config.items.push({
			layout : {
				type : 'table',
				columns : pageData.cols,
				tableAttrs : {
		            style: {
		                width: '100%'
		            }
				},
				tdAttr : {
					width : (100 / pageData.cols) + '%'
				},
				trAttr : {
					height : 22
				}
			},
			border : false,
			defaults : {
				border : false,
				anchor : '100%',
				width : '100%',
				labelStyle : {
					padding : '0 0 0 5',
					labelWidth : pageData.labelWidth || 80
				}
			},
			items : inputs
		});
		// 获得文档提交Direct函数
		var appManager = AscApp.getController('AscAppManager');
		var getLoadFormFn = appManager.getEngineDirectFn(appKey, 'loadForm');
		// 处理表单装载
		config.api = {
			load : getLoadFormFn
		};
		config.baseParams = {
			pageId : pageData.id,
			id : 0,
			prefix : fieldPrefix,
			iniValues : (context && context.iniValues) ? context.iniValues : {}
		};
		config.paramOrder = ['pageId', 'id', 'prefix', 'iniValues'];
		Ext.apply(config, pageData.cfg);
		Ext.apply(config, cfg);
		// 帮助文档的tool
		if(pageData.helpDocKey && pageData.helpDocKey.trim() !== "") {
			if(!config.tools) {
				config.tools = [];
			}
			config.tools.push({
				type: 'help',
				handler: function() {
					AscApp.getAscDesktop().openDocWin(pageData.helpDocKey.trim());
				}
			});
		}
        me.callParent([config]);
	},
	// 执行刷新
	doRefresh : function(params){
		if(params && params.iniValues){
			Ext.apply(params.iniValues, this.baseParams.iniValues);
		}
		var waitMsgTarget = this.waitMsgTarget;
		waitMsgTarget.mask('正在装载数据，请稍候...');
		// 刷新设置持久参数
		this.getForm().load({
			params : params,
			success : function(){
				Ext.defer(waitMsgTarget.unmask, 50, waitMsgTarget);
			},
			failure : function(){
				Ext.defer(waitMsgTarget.unmask, 50, waitMsgTarget);
			}
		});
	}
});