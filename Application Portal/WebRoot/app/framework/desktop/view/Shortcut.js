// 页面上部标题区界面
Ext.define('Asc.framework.desktop.view.Shortcut', {
	
	extend: 'Ext.panel.Panel',
	
	alias: 'widget.AscDesktopShortcut',
    // 设置引用类
	requires: ['Ext.ux.button.BadgeButton'],
    
	autoScroll : false,
	
	padding : 5,
	
	border : false,
	
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	
	defaults : {
		xtype : 'button',
		padding : 3,
		margins : '5 0 10 5',
		iconAlign : 'top',
		scale : 'large',
		frame : false,
		border : false
	},
	// 添加快捷菜单
	addShortcut : function(shortcutData){
		var handler;
		if(shortcutData.script) {
			try{				
				eval('handler = ' + shortcutData.script + ';');
			} catch (e) {
				errMsg = '解析快捷按钮 [' + shortcutData.text + '] 的脚本时出现错误';
				Asc.common.Message.showError(errMsg);
			}
		}
		if(!handler) {
			handler = Ext.emptyFn;
		}
		this.add({
			appKey : shortcutData.appKey,
			key : shortcutData.key,
			text : shortcutData.text,
			iconCls : shortcutData.iconCls || 'icon-sys-shortcut32',
			cls : 'asc-btn-shortcut',
			tooltip : shortcutData.text,
			handler : handler
		});
	},
	// 移除快捷菜单
	removeShortcut : function(key){
		// 查找需要删除的快捷方式
		var shortcuts = Ext.ComponentQuery.query('AscDesktopShortcut > button[key=' + key + ']');
		if(shortcuts.length > 0){
			this.remove(shortcuts[0]);
		}
	},
	
	// 移除所有快捷菜单
	clearShortcuts : function(){
		// 查找需要删除的快捷方式
		var shortcuts = Ext.ComponentQuery.query('AscDesktopShortcut > button');
		if(shortcuts.length > 0){
			for(var i = shortcuts.length - 1; i >= 0 ; i--)
			this.remove(shortcuts[i]);
		}
	}
	
});