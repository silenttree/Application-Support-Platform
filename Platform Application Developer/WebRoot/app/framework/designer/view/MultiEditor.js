// 多窗口编辑器
Ext.define('Asc.framework.designer.view.MultiEditor', {
	
	extend: 'Ext.tab.Panel',
	
	alias: 'widget.AscDesignerMultiEditor',
    // 设置引用类
	requires: [],
	
	tabPosition: 'left',
	
	border : false,
	
	title : '对象编辑器',
	
	closable : true,
	
	activateTab : 0,
	
	deferredRender : true,
	
	defaults : {
		xtype : 'AscJspPanel',
		layout : 'fit',
		border : false,
		autoScroll : true,
		closable : false,
		listeners : {
			render : function(){
				var index = this.ownerCt.items.indexOf(this);
				if(index > 0){
					AscApp.ActionManager.runFunction('designer.reloadEditor');
				}
			}
		}
	},
	
	bbar : [{
		text : '应用',
		iconCls : 'icon-sys-apply',
		scale : 'medium',
		group : 'designer',
		name : 'applyEditor'
	},{
		text : '保存',
		iconCls : 'icon-sys-save',
		scale : 'medium',
		group : 'designer',
		name : 'saveEditor'
	}, '->',{
		text : '刷新',
		iconCls : 'icon-sys-refresh',
		scale : 'medium',
		group : 'designer',
		name : 'reloadEditor'
	},{
		text : '关闭',
		iconCls : 'icon-sys-close',
		scale : 'medium',
		group : 'designer',
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