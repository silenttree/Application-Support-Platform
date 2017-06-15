Ext.define('Asc.framework.workflow.view.Confirmation', {
	// 指定基类
	extend : 'Ext.window.Window',
	// 设置别名
	alias : 'widget.AscConfirmation',
	// 标题
	title : '提交流程办理确认信息',
	
	height : 450,
	
	width : 540,
	
	modal : true,
	
	layout : 'fit',
	
	resizable : false,
	
	maximizable : false,
	
	minimizable : false,
	// 初始化界面	
	initComponent: function() {
		var me  = this;
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
		var infoFields = [{
			xtype : 'displayfield',
			fieldLabel : '&nbsp;&nbsp;办理节点',
			value : this.context.nodeCaption
		}, {
			xtype : 'displayfield',
			fieldLabel : '&nbsp;&nbsp;办理动作',
			value : this.context.actionCaption
		}];
		if(this.context.routeCaption){
			infoFields.push({
				xtype : 'displayfield',
				fieldLabel : '&nbsp;&nbsp;目标路由',
				value : this.context.routeCaption
			});
		}
		for(target in this.context.targets){
			if(target.nodeCaption){
				infoFields.push({
					xtype : 'displayfield',
					fieldLabel : '&nbsp;&nbsp;目标节点',
					value : target.nodeCaption
				});
			}
			if(target.processors){
				infoFields.push({
					xtype : 'displayfield',
					fieldLabel : '&nbsp;&nbsp;办理人员',
					value : target.processorCaptions
				});
			}
		}
		this.items = {
			xtype : 'form',
			defaults : {
				margin : 10,
				renderer : function(val, field) {
					return '<b>' +  val + '</b>';
				}
			},
			items : infoFields
		}
		this.buttons = [{
			text : '确定',
			iconCls : 'icon-sys-confirm',
			handler : function(){
				var params = {clientAction : 3};
				var flowManager = AscApp.getController('AscWorkflowManager');
				flowManager.submitFlow(me.context, params, function(){
					me.close();
				}, me);
			}
		},{
			text : '取消',
			iconCls : 'icon-sys-cancel',
			handler : function(){
				var flowManager = AscApp.getController('AscWorkflowManager');
				flowManager.cancelSubmitFlow(me.context);
				me.close();
			}
		}];
		this.callParent();
	}
});