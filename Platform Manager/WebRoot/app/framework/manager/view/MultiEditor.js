// 多窗口编辑器
Ext.define('Asc.framework.manager.view.MultiEditor', {
	
	extend: 'Ext.tab.Panel',
	
	alias: 'widget.AscManagerMultiEditor',
    // 设置引用类
	requires: [],
	
	tabPosition: 'left',
	
	border : false,
	
	title : '对象编辑器',
	
	closable : true,
	
	activateTab : 0,
	
	deferredRender : false,
	
	defaults : {
		xtype : 'AscJspPanel',
		layout : 'fit',
		border : false,
		autoScroll : true,
		closable : false
	},
	
	bbar : [{
		text : '应用',
		iconCls : 'icon-sys-apply',
		scale : 'medium',
		group : 'manager',
		name : 'applyEditor'
	},{
		text : '保存',
		iconCls : 'icon-sys-save',
		scale : 'medium',
		group : 'manager',
		hidden : true,
		name : 'saveEditor'
	}, '->',{
		text : '刷新页面',
		iconCls : 'icon-sys-refresh',
		scale : 'medium',
		group : 'manager',
		name : 'reloadEditor'
	},{
		text : '关闭',
		iconCls : 'icon-sys-close',
		scale : 'medium',
		group : 'manager',
		name : 'closeEditor'
	}],
	// 执行应用按钮
	doApply : function(){
		var panel = this.getActiveTab();
		if(panel && panel != null && panel.doApply){
			panel.doApply();
		}
	},
	// 执行保存按钮
	doSave : function(){
		this.doApply();
	}
});