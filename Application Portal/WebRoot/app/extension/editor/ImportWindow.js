Ext.define('Asc.extension.editor.ImportWindow', {
	config : {
		/**
		 * @cfg {string} appKey 
		 */
		appKey : '',
		/**
		 * @cfg {string} fileImportLogId
		 */
		fileImportLogId : ''
	},
	extend: 'Ext.window.Window',
	alias: 'widget.importwindow',
	title: '文件导入',
	width : 700,
	height : 500,
	modal : true,
	border : false,
	maximizable : false,
	minimizable : false,
	layout : 'border',
	initComponent: function() {
		var me = this;
		var appManager = AscApp.getController('AscAppManager');
	    me.queryOperationDataFn = appManager.getEngineDirectFn(me.appKey, 'getOperationList');
		
		var operationSelectPanel = Ext.create('Asc.extension.JspPanel', {
			region : 'center',
			header : false,
			split : false,
			width : '100%',
			height : '40%',
			border : false,
			appId : me.appKey,
			jspUrl : 'framework/fileimport/operation.select',
			params : {'fileImportLogId' : me.fileImportLogId, 'appKey' : me.appKey}
		});
		
		var operationDatasPanel = Ext.create('Asc.extension.JspPanel', {
			region : 'south',
			header : false,
			split : false,
			width : '100%',
			height : '60%',
			border : false,
			appId : me.appKey,
			jspUrl : 'framework/fileimport/operation.datas',
			params : {'fileImportLogId' : me.fileImportLogId, 'appKey' : me.appKey}
		});
		
		me.items = [operationSelectPanel, operationDatasPanel];
		me.callParent();
	},
	onSubmitSuccess : function(){
		this.items.get(1).refresh();
	}
});