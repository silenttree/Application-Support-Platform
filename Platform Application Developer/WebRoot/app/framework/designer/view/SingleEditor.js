// 页面上部标题区界面
Ext.define('Asc.framework.designer.view.SingleEditor', {
	
	extend: 'Asc.extension.JspPanel',
	
	alias: 'widget.AscDesignerSingleEditor',
    // 设置引用类
	requires: [],
	
	title : '对象编辑器',
	
	layout : 'fit',
	
	autoScroll : true,
	
	closable : true,
	
	border : false,
	
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
	}]
});