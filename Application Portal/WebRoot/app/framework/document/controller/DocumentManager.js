Ext.define('Asc.framework.document.controller.DocumentManager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscDocumentManager',
	// 设置引用类
	requires : ['Asc.framework.document.view.TabsDocument', 
	            'Asc.framework.document.view.SingleDocument',
	            'Asc.framework.document.view.DocumentHandler'],
	// 名称空间
	$namespace : 'Asc.framework.document',
	// 模块定义
	documents : {},
	// 按钮脚本函数
	handlers : {},
	// 初始化函数
	init : function() {
		Asc.common.Message.log('DocumentManager is loaded & initialise ', this, 'log');
		// 页面控制界面元素
		this.control({
			'[type=docbutton]' : {
				click : this.buttonHandler
			},
			'[type=docbutton]>menuitem' : {
				click : this.buttonHandler
			}
		});
	},
	// 获得文档对象数据
	getDocumentData : function(appKey, documentId){
		if(!Ext.isDefined(this.documents[appKey])){
			this.documents[appKey] = {};
		}
		return this.documents[appKey][documentId];
	},
	// 设置页面数据
	setDocumentData : function(appKey, documentId, data){
		this.documents[appKey][documentId] = data;
		// 处理文档按钮
		for(n in data.buttons){
			var btn = data.buttons[n];
			try{
				eval('var fn = ' + btn.script);
				if(Ext.isFunction(fn)){
					this.handlers[n] = fn;
				}else{
					Asc.common.Message.log('文档按钮[' + n + ']脚本非执行函数');
				}
			}catch(err){
				// 
				Asc.common.Message.log('文档按钮[' + n + ']脚本定义语法错误');
			}
		}
	},
	// 装载模块数据
	loadDocumentData : function(appKey, documentId, fn, scope){
		var data = this.getDocumentData(appKey, documentId);
		if(!Ext.isDefined(data)){
			// 调用Direct方法请求文档对象数据
			var appManager = AscApp.getController('AscAppManager');
			var getDocumentFn = appManager.getEngineDirectFn(appKey, 'getDocument');
			getDocumentFn(documentId, function(result, e){
				if(result && result.success){
					// 装载模块数据
					this.setDocumentData(appKey, documentId, result.data);
					if(Ext.isFunction(fn)){
						fn.call(scope || this);
					}
					Asc.common.Message.log('装载文档[' + appKey + '.' + documentId + ']数据执行完毕！');
				}else{
					Asc.common.Message.showError('装载文档[' + appKey + '.' + documentId + ']失败！');
				}
			}, this);
		}else{
			return data;
		}
	},
	// 获得文档界面对象对象ID
	getDocumentId : function(appKey, documentId, dataId){
		return appKey + '_' + documentId + '_' + dataId;
	},
	// 获得文档主表SEQ
	getDocumentSeqId : function(appKey, documentId, fn, scope){
		// 获取文档数据
		var data = this.loadDocumentData(appKey, documentId, function(){
			this.getDocumentSeqId(appKey, documentId, fn, scope);
		}, this);
		if(!Ext.isDefined(data)){
			return;
		}
		if(!Ext.isDefined(data.tableName)){
			Asc.common.Message.showError('创建文档[' + appKey + '.' + documentId + ']失败，未定义数据表！');
			return;
		}
		// 调用Direct方法请求文档对象数据ID
		var appManager = AscApp.getController('AscAppManager');
		var getTableSeqFn = appManager.getEngineDirectFn(appKey, 'getTableSeq');;
		getTableSeqFn(data.tableName, function(result, e){
			if(result && result.success){
				if(Ext.isFunction(fn)){
					fn.call(scope || this, result.id);
				}
			}else{
				Asc.common.Message.showError('创建文档[' + appKey + '.' + documentId + ']获取ID失败！');
			}
		}, this);
	},
	// 获得文档界面对象
	getDocumentCmp : function(appKey, documentId, dataId, cfg){
		if(!cfg){
			cfg = {};
		}
		if(dataId <= 0){
			Asc.common.Message.showError('文档【' + documentId + '】数据ID未指定，打开失败！');
			return;
		}
		// 获得窗口
		var id = this.getDocumentId(appKey, documentId, dataId);
		var doc = Ext.getCmp(id);
		var docWrap;
		if(!Ext.isDefined(doc)){
			// 获取文档数据
			var data = this.loadDocumentData(appKey, documentId, function(){
				if(Ext.isFunction(cfg.fn)){
					cfg.fn.call(cfg.scope || this);
				}
			}, this);
			if(!Ext.isDefined(data)){
				return;
			}
			// 定义文档对象打开参数
			var config = {
				id : id,
				appKey : appKey,
				moduleId : data.moduleId,
				documentId : documentId,
				dataId : dataId || 0,
				userlogId : cfg.userlogId || 0,
				iniValues : cfg.iniValues
			};
			// 根据布局类型选择文档界面构造类
			switch(data.panelLayout){
			case 'Tabs':
				doc = Ext.create('Asc.framework.document.view.TabsDocument', config);
				break;
			case 'Single':
				doc = Ext.create('Asc.framework.document.view.SingleDocument', config);
				break;
			}
			// 输出隐藏参数域
			items = [doc, {
				xtype :'hiddenfield',
				name : 'dataId',
				value : dataId
			},{
				xtype :'hiddenfield',
				name : 'documentId',
				value : documentId
			},{
				xtype :'hiddenfield',
				name : 'userlogId',
				value : cfg.userlogId || 0
			}];
			// 获得文档提交Direct函数
			var appManager = AscApp.getController('AscAppManager');
			var getSubmitDocumentFn = appManager.getEngineDirectFn(appKey, 'submitDocument');
			docWrap = Ext.create('Ext.form.Panel', {
				border : false,
				api : {
					submit : getSubmitDocumentFn
				},
				defaults : {
					anchor : '100% 100%'
				},
				items : items,
				getDoc : function(){
					return this.items.get(0);
				}
			});
		}else{
			docWrap = doc.up('form');
		}
		return docWrap;
	},
	// 关闭文档对象
	closeDocument : function(appKey, documentId, dataId){
		var id = this.getDocumentId(appKey, documentId, dataId);
		var doc = Ext.getCmp(id);
		if(Ext.isDefined(doc)){
			doc.up('form').opener.closeDocument(doc);
		}else{
			Asc.common.Message.log('文档[' + appKey + '.' + documentId + ']未装载，关闭失败！');
		}
	},
	buttonHandler : function(btn){
		var doc = btn.up('[type=document]');
		if(Ext.isFunction(this.handlers[btn.name])){
			this.handlers[btn.name].call(btn, doc);
		}else{
			Asc.common.Message.log('按钮[' + btn.name + ']未定义操作函数！');
		}
	}
});