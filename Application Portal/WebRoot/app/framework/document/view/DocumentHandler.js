Ext.define('Asc.framework.document.view.DocumentHandler', {
	// 指定基类
	extend: 'Ext.AbstractPlugin',
	// 设置别名
	alias : 'plugin.AscDocumentHandler',
	
	init : function(doc){
		this.doc = doc;
        var me = this;
		// 添加文档操作插件
		var bbar = Ext.create('Ext.toolbar.Toolbar', {
			dock : 'bottom',
			defaults : {
				iconAlign : 'left'
			},
			items : ['->', {
				text : '刷新文档',
				iconCls : 'icon-sys-refresh',
				handler : function(){
					me.reloadDocument();
				}
			}, {
				text : '关闭',
				iconCls : 'icon-sys-close',
				handler : function(){
					// 获得文档控制器
					var documentManager = AscApp.getController('AscDocumentManager');
					// 关闭文档
					documentManager.closeDocument(doc.appKey, doc.documentId, doc.dataId);
				}
			}]
		});
		doc.addDocked(bbar);
		doc.on('render', function(){
			me.reloadDocument();
		})
	},
	// 重新装载文档界面对象
	reloadDocument : function(){
		var doc = this.doc;
		// 清空标签
		doc.clearPanels();
		// 清空按钮
		var bbar = doc.getDockedItems('toolbar[dock="bottom"]')[0];
		var docBtns = bbar.query('[type=docbutton]');
		for(n in docBtns){
			bbar.remove(docBtns[n]);
		}
		var flowBtns = bbar.query('[type=flowbutton]');
		for(n in flowBtns){
			bbar.remove(flowBtns[n]);
		}
		bbar.doLayout();
		// 调用Direct方法请求文档界面元素
		var appManager = AscApp.getController('AscAppManager');
		var getDocumentObjectsFn = appManager.getEngineDirectFn(doc.appKey, 'getDocumentUIObjects');
		getDocumentObjectsFn(doc.documentId, doc.dataId, doc.userlogId, function(result, e){
			if(result && result.success){
				// 显示文档界面元素
				this.renderDocument(result.data);
			}else{
				Asc.common.Message.showError('请求文档界面元素[' + doc.documentId + ' - ' + doc.dataId + ']失败！');
			}
		}, this);
	},
	// (private)显示文档界面元素
	renderDocument : function(data){
		var doc = this.doc;
		// 记录文档状态
		doc.state = data.state;
		// 记录文档权限
		doc.authorities = data.authorities;
		// 装载标签
		this.addPanels(data.panels);
		// 装载文档按钮
		this.addButtons(data.buttons);
		// 装载流程按钮
		this.addFlowButtons(data.flowButtons);
	},
	// 提交文档
	submitDocument : function(fn, scope){
		var doc = this.doc;
		// 处理数据校验
		doc.getFormPanel().getForm().submit({
			clientValidation: true,
			waitMsg : '正在保存文档数据，请稍候...', 
			success : function(f,a){
				// 刷新文档
				this.doRefresh();
				if(Ext.isFunction(fn)){
					fn.call(scope || this);
				}else if(doc.opener && Ext.isFunction(doc.opener.doRefresh)){
					// 刷新文档打开页面
					doc.opener.doRefresh();
				}
			},
			failure : function(f, a){
				Asc.common.Message.showError('保存文档操作失败，' + a.response);
			},
			scope : doc
		});
	},
	// 删除文档
	deleteDocument : function(fn, scope){
		var doc = this.doc;
		Ext.MessageBox.confirm('危险操作提示', '当前文档，该操作不可恢复，您确定吗？', function(btn){
			if(btn == 'yes'){
				// 调用Direct方法请求文档界面元素
				var appManager = AscApp.getController('AscAppManager');
				var getDocumentObjectsFn = appManager.getEngineDirectFn(doc.appKey, 'deleteDocument');
				getDocumentObjectsFn(doc.documentId, doc.dataId, function(result, e){
					if(result && result.success){
						// 获得文档控制器
						var documentManager = AscApp.getController('AscDocumentManager');
						// 关闭文档
						documentManager.closeDocument(doc.appKey, doc.documentId, doc.dataId);
						if(Ext.isFunction(fn)){
							fn.call(scope || this);
						}else if(doc.opener && Ext.isFunction(doc.opener.doRefresh)){
							// 刷新文档打开页面
							doc.opener.doRefresh();
						}
					}else{
						Asc.common.Message.showError('删除文档数据[' + doc.documentId + ' - ' + doc.dataId + ']失败！');
					}
				}, doc);
			}
		}, this);
		
	},
	// (private)装载文档标签页面
	addPanels : function(panels){
		var doc = this.doc;
		var moduleId = doc.moduleId;
		var appKey = doc.appKey;
		var pageManager = AscApp.getController('AscPageManager');
		for(n in panels){
			var panel = panels[n];
			// 文档标签页面子对象ID用标签的id标识
			var itemId = panel.id;
			var page = doc.getPanel(itemId);
			if(!Ext.isDefined(page)){
				page = pageManager.getPageCmp(
						appKey, 
						moduleId, 
						panel.page, 
						Ext.apply({docPanelId : panel.id}, panel.params), 
						{authorities : doc.authorities, panelKey:panel.key, iniValues : doc.iniValues}, 
						function(){
							this.addPanels(panels);
						},
						this);
				if(!Ext.isDefined(page)){
					return;
				}
				// 文档标签配置
				var panelConfig = Ext.apply({
					itemId : itemId,
					type : 'docpanel'
				}, panel.cfg)
				Ext.apply(page, panelConfig);
				// 文档界面类实现接口函数
				doc.addPanel(page);
				// 装载完毕后驱动标签刷新
				if(Ext.isFunction(page.doRefresh)){
					page.doRefresh(panel.params);
				}else{
					page.panelParams = panel.params;
				}
			}
		}
		// 文档界面类实现接口函数
		doc.setActivePage(0);
	},
	// (private)装载文档按钮
	addButtons : function(buttons){
		var doc = this.doc;
		var appKey = doc.appKey;
		var documentId = doc.documentId;
		var bbar = doc.getDockedItems('toolbar[dock="bottom"]')[0];
		for(n in buttons){
			bbar.insert(parseInt(n), 
				Ext.apply({type:'docbutton'}, buttons[n]));
		}
		doc.doLayout();
	},
	// (private)装载流程按钮
	addFlowButtons : function(buttons){
		var doc = this.doc;
		var appKey = doc.appKey;
		var documentId = doc.documentId;
		var bbar = doc.getDockedItems('toolbar[dock="bottom"]')[0];
		for(n in buttons){
			var btn = Ext.create('Asc.framework.workflow.view.WorkflowButton', buttons[n], this.doc);
			console.log(btn);
			bbar.insert(parseInt(n), btn);
		}
		doc.doLayout();
	}
});