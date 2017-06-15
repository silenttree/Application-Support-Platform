Ext.define('Asc.framework.workflow.view.RouteSelector', {
	// 指定基类
	extend : 'Ext.window.Window',
	// 设置别名
	alias : 'widget.AscRouteSelector',
	// 标题
	title : '请选择流程提交路由',
	
	height : 450,
	
	width : 540,
	
	modal : true,
	
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	
	resizable : false,
	
	maximizable : false,
	
	minimizable : false,

	shouldCommit : false,
	// 初始化界面	
	initComponent: function() {
		var me = this;
		/* 上下文信息结构
		{
			appKey : doc.appKey,
			documentId : doc.documentId,
			dataId : doc.dataId,
			action : 'CompleteUserProcess',
			actionCaption : '办理完毕',
			clientAction : 0,
			nodeId : 'srcnodekey',
			nodeCaption : '拟稿',
			routeId : 'routekey',
			routeCaption : '提交核稿',
			targetNodeId : 'nodekey',
			targetNodeCaption : '核稿',
			targets : [{
				nodeId : 'nodekey',
				nodeCaption : '核稿',
				processors : 'U_12',
				processorCaptions : '张三'
			}]
		}
		*/
		// 信息提示
		var info = {
			layout : 'form',
			height : 60,
			border : false,
			items : [{
				xtype : 'displayfield',
				fieldLabel : '&nbsp;&nbsp;办理节点',
				value : this.context.nodeCaption
			}, {
				xtype : 'displayfield',
				fieldLabel : '&nbsp;&nbsp;办理动作',
				value : this.context.actionCaption
			}]
		}
		// 路由数据
		var store = Ext.create('Ext.data.Store',{
			fields : ['id', 'caption', 'description'],
			data : this.context.routes
		});
		// 路由选择表格
		var grid = Ext.create('Ext.grid.Panel',{
			flex : 1,
			border : false,
			store : store,
			columns : [
				{xtype: 'rownumberer'},
				{ text: '路由名称', dataIndex: 'caption', width : 100},
				{ text: '路由描述',  dataIndex: 'description', flex: 1}
			]
		});
		// 界面组织
		this.items = [info, grid];
		// 操作按钮
		this.buttons = [{
			text : '确定',
			iconCls : 'icon-sys-confirm',
			handler : function(){
				var routes = grid.getSelectionModel().getSelection();
				if(routes.length == 0){
					Asc.common.Message.showError('请选择流程提交路由！');
					return;
				}
				me.shouldCommit = true;
				var routeId = routes[0].get('id');
				var flowManager = AscApp.getController('AscWorkflowManager');
				flowManager.submitFlow(me.context, {
					clientAction : 1,
					routeId : routeId
				}, function(){
					me.close();
				}, me);
			}
		},{
			text : '取消',
			iconCls : 'icon-sys-cancel',
			handler : function(){
				me.shouldCommit = false;
				me.close();
			}
		}];
		
		me.on('beforeclose', function(win, opts){
			if(!me.shouldCommit) {		
				var flowManager = AscApp.getController('AscWorkflowManager');
				flowManager.cancelSubmitFlow(me.context);
			}
			return true;
		});
		this.callParent();


	}
});