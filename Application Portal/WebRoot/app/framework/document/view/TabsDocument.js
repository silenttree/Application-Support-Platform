// 多标签文档容器对象
Ext.define('Asc.framework.document.view.TabsDocument', {
	
	extend: 'Ext.tab.Panel',
	
	alias: 'widget.AscTabsDocument',
	
	plugins : [{ptype : 'AscDocumentHandler'}],
	
	border : false,
	
	type : 'document',
	
	deferredRender : false,
	
	opener : undefined,
	
	defaults : {
		closable : false
	},
	// 添加标签页面
	addPanel : function(page){
		this.add(page);
		this.doLayout();
	},
	// 设置激活标签
	setActivePage : function(index){
		this.setActiveTab(index);
	},
	// 清空所有标签
	clearPanels : function(){
		this.removeAll();
		this.doLayout();
	},
	// 获得文档标签对象
	getPanel : function(itemId){
		return this.getComponent(itemId);
	},
	// 获得文档表单对象
	getFormPanel : function(){
		return this.up('form');
	},
	// 获得文档处理器插件
	getHandler : function(){
		return this.findPlugin('AscDocumentHandler');
	},
	// 提交保存文档
	doSubmit : function(fn, scope){
		this.getHandler().submitDocument(fn, scope);
	},
	// 删除文档
	doDelete : function(fn, scope){
		this.getHandler().deleteDocument(fn, scope);
	},
	// 刷新文档
	doRefresh : function(){
		this.getHandler().reloadDocument();
	}
});