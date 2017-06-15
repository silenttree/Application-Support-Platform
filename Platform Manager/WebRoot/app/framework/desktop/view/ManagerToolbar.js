// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.ManagerToolbar', {
	
	extend: 'Ext.container.ButtonGroup',
	
	alias: 'widget.AscDesktopManagerToolbar',
	
	frame : false,
	
	border : false,
	
	padding : '2 5 0 0',

    defaults: {
    	xtype : 'button',
        scale: 'medium'
    },

	items : [/*{
		text : '编译文件',
		iconCls : 'icon-sys-build',
		xtype : 'splitbutton',
		menu : {
			items : [{
				group : 'sys', name : 'buildIcons',text : '生成图标', iconCls : 'icon-sys-build'
			},{
				group : 'sys', name : 'buildClassesJs',text : '生成类文件', iconCls : 'icon-sys-build'
			},{
				group : 'sys', name : 'buildActionsJs',text : '生成功能函数文件', iconCls : 'icon-sys-build'
			}]
		}
	},{
		group : 'sys', name : 'helpDoc', text : '在线帮助', iconCls : 'icon-sys-book'
	},{
		group : 'sys', name : 'extjsDoc', text : 'Ext4手册', iconCls : 'icon-sys-extjslogo'
	},*/{
		group : 'sys', name : 'showAbout', text : '关于产品', iconCls : 'icon-sys-about'
	}]
});