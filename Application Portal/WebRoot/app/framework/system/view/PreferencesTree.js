Ext.define('Asc.framework.system.view.PreferencesTree', {
	// 指定基类
	extend : 'Ext.tree.Panel',
	// 设置别名
	alias : 'widget.AscPreferencesTree',
	
	rootVisible: false,
	
	autoScroll : true,
	
    useArrows: true,
	
	store : 'PreferencesTreeStore',
	
	tbar: [{
		name: 'loadTpl',
		text: '装载模板'
	}, {
		name: 'saveTpl',
		text: '保存模板'
	}]
});