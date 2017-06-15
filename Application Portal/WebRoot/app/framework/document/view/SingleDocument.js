// 单页面文档容器对象
Ext.define('Asc.framework.document.view.SingleDocument', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscSingleDocument',
	
	plugins : [{ptype : 'AscDocumentHandler'}],
	
	type : 'document',
	
	border : false,
	
	bodyPadding : 5,
	
	autoScroll : true,
	
	opener : undefined,
	
	addPanel : function(page){
		var title = page.title;
		page.autoScroll = false;
		page.title= undefined;
		this.add({
			xtype : 'fieldset',
			itemId : page.itemId,
			title : title,
			items : page
		});
	},
	// 设置激活标签
	setActivePage : function(index){
		
	},
	// 清空所有标签
	clearPanels : function(){
		this.removeAll();
		this.doLayout();
	},
	// 获得文档标签对象
	getPanel : function(itemId){
		var item = this.getComponent(itemId);
		if(Ext.isDefined(item)){
			return item.items.get(0);
		}
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