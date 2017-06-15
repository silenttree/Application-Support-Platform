Ext.define('Asc.framework.workflow.view.ProcessorSelector', {
	// 指定基类
	extend : 'Ext.window.Window',
	// 设置别名
	alias : 'widget.AscProcessorSelector',
	// 标题
	title : '请设置节点办理人',
	
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
		// 根据当前节点显示提示信息
		this.items = [{
			layout : 'form',
			height : 60,
			border : false,
			items : [{
				xtype : 'displayfield',
				fieldLabel : '&nbsp;&nbsp;办理节点',
				value : this.context.nodeCaption
			}, {
				xtype : 'displayfield',
				fieldLabel : '&nbsp;&nbsp;目标节点',
				value : this.context.targetNodeCaption
			}]
		}, {
			xtype : 'workflowuserselector',
			border : false,
			intelligentUsers : 'U_1;',			//this.context.processors,
			flex : 1
		}]
		this.buttons = [{
			text : '确定',
			iconCls : 'icon-sys-confirm',
			handler : function(){
				var flowManager = AscApp.getController('AscWorkflowManager');
				var params = {clientAction : 2};
				// 解析选择用户参数
				var selector = this.items.get(1);
				var processors = selector.getValue();
				if(processors == ''){
					Asc.common.ActionManager.showError('请选择办理人员！');
					return;
				}
				params.processors = processors;
				me.shouldCommit = true;
				// 提交流程办理
				flowManager.submitFlow(me.context, params, function(){
					me.close();
				}, me);
			},
			scope : this
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
		console.log(this.items);
		this.callParent();
	}
});