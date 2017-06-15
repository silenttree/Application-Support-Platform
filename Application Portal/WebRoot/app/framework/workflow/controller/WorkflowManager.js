Ext.define('Asc.framework.workflow.controller.WorkflowManager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscWorkflowManager',
	// 设置引用类
	requires : ['Asc.framework.workflow.model.NodeProcessLog',
	            'Asc.framework.workflow.view.Confirmation',
	            'Asc.framework.workflow.view.ProcessorSelector',
	            'Asc.framework.workflow.view.RouteSelector',
	            'Asc.framework.workflow.view.WorkflowButton',
	            'Asc.framework.workflow.view.WorkFlowUserSelector'],
	// 名称空间
	$namespace : 'Asc.framework.workflow',
	// 初始化函数
	init : function(){
		Asc.common.Message.log('WorkflowManager is loaded & initialise ', this, 'log');
		// 页面控制界面元素
		this.control({
			'[type=flowbutton]' : {
				click : this.actionHandler
			},
			'[type=flowbutton]>menuitem' : {
				click : this.actionHandler
			}
		});
	},
	// 处理流程动作
	actionHandler : function(btn){
		var doc = btn.up('[type=document]');
		var context = {
			appKey : doc.appKey,
			documentId : doc.documentId,
			userlogId : doc.userlogId,
			dataId : doc.dataId,
			action : btn.action,
			actionCaption : btn.text,
			afterSubmit : function(){
				doc.doRefresh();
			}
		}
		switch(btn.action){
		case 'CompleteUserProcess':
		case 'ReadComplete':
		case 'RequestProcess':
		case 'TakeBack':
		case 'Return':
		case 'Cancel':
		case 'Resume':
		case 'Forward':
			// 获得文档控制器
			var documentManager = AscApp.getController('AscDocumentManager');
			// 处理提交完毕后事件
			context.afterSubmit = function(){
				if(doc.opener && Ext.isFunction(doc.opener.doRefresh)){
					// 刷新文档打开页面
					doc.opener.doRefresh();
				}
				// 关闭文档
				documentManager.closeDocument(doc.appKey, doc.documentId, doc.dataId);
			}
			// 获得事件脚本
			var data = documentManager.getDocumentData(doc.appKey, doc.documentId);
			if(data && data.events && data.events.doSubmit){
				// 执行事件脚本
				eval(data.events.doSubmit);
			}else{
				// 提交文档保存后提交流程处理
				doc.doSubmit(function(){
					this.submitFlow(context, {clientAction : 0});
				}, this);
			}
			break;
		case 'Comment':
			// TODO 输入意见
			break;
		default :
			Asc.common.Message.showError('未定义流程动作：[' + btn.action + ']');
			break;
		}
	},
	// 提交流程处理
	submitFlow : function(context, params, fn, scope){
		var appKey = context.appKey;
		var documentId = context.documentId;
		var dataId = context.dataId;
		var userlogId = context.userlogId;
		var action = context.action;
		var actionCaption = context.actionCaption;
		// 调用Direct方法提交文档流程
		var appManager = AscApp.getController('AscAppManager');
		var submitDocumentFlowFn = appManager.getEngineDirectFn(appKey, 'submitDocumentFlow');;
		submitDocumentFlowFn(documentId, dataId, userlogId, action, params, function(result, e){
			if(result && result.success){
				if(Ext.isFunction(fn)){
					fn.call(scope || this);
				}
				// 装载模块数据
				this.handleSubmitResult(context, result);
			}else{
				Asc.common.Message.showError('提交文档流程处理' + actionCaption + '动作[' + appKey + '.' + documentId + ']失败！');
			}
		}, this);
	},
	// 取消流程提交操作
	cancelSubmitFlow : function(context){
		var appKey = context.appKey;
		var documentId = context.documentId;
		var dataId = context.dataId;
		var userlogId = context.userlogId;
		// 调用Direct方法取消提交文档流程
		var appManager = AscApp.getController('AscAppManager');
		var cancelSubmitDocumentFlowFn = appManager.getEngineDirectFn(appKey, 'cancelSubmitDocumentFlow');;
		cancelSubmitDocumentFlowFn(documentId, dataId, userlogId, function(result, e){
			if(result && result.success){
				Asc.common.Message.showInfo('已取消提交流程操作[' + context.actionCaption + ']！');
			}else{
				Asc.common.Message.showError('取消提交流程操作[' + context.actionCaption + ']失败！');
			}
		}, this);
	},
	// 处理流程提交返回结果
	handleSubmitResult : function(context, result){
		if(Ext.isDefined(result.clientAction)){
			context.clientAction = result.clientAction;
			switch(result.clientAction){
			case 1:		//'DefinitlyRoute':
				context.nodeId = result.nodeId;
				context.nodeCaption = result.nodeCaption;
				context.routes = result.routes;
				// TODO 选择路由
				Ext.create('Asc.framework.workflow.view.RouteSelector', {
					context : context
				}).show();
				break;
			case 2:		//'TargetNodeProcessors':
				context.targetRouteId = result.targetRouteId;
				context.targetRouteCaption = result.targetRouteCaption;
				context.targetNodeId = result.targetNodeId;
				context.targetNodeCaption = result.targetNodeCaption;
				context.processors = result.processors;
				// TODO 选择人员
				Ext.create('Asc.framework.workflow.view.ProcessorSelector', {
					context : context
				}).show();
				break;
			case 3:		//'Confirmation':
				context.nodeId = result.nodeId;
				context.nodeCaption = result.nodeCaption;
				context.routeId = result.routeId;
				context.routeCaption = result.routeCaption;
				context.targets = result.targets;
				// TODO 确认窗口
				Ext.create('Asc.framework.workflow.view.Confirmation', {
					context : context
				}).show();
				break;
			default :
				// 提交完毕后刷新文档
				if(Ext.isDefined(context.afterSubmit)){
					context.afterSubmit.call();
				}
				break;
			}
		}else{
			// 提交完毕后刷新文档
			if(Ext.isDefined(context.afterSubmit)){
				context.afterSubmit.call();
			}
		}
	}
});