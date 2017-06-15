// 页面下部的页脚区界面
Ext.define('Asc.framework.desktop.view.UserPanel', {
	
	extend: 'Ext.container.ButtonGroup',
	
	alias: 'widget.AscDesktopUserPanel',
	
	frame : false,
	
	border : false,
	
	padding : '0 0 0 5',
	
	defaults : {
		listeners : {
			mouseover : function(){
				this.showMenu(true);
			}
		}
	},
	
    setUser : function(user){
		Asc.common.Message.log('初始化用户面板', this, 'log');
		/**
		 * 用户类型0：普通用户，1：运维管理人员，2：开发人员
		 */ 
		var hiddenUserType = function() {
			if(!arguments) {
				return true;
			}
			for(var i = 0; i < arguments.length; i++) {
				if(arguments[i] == user.f_type) {
					return true;
				}
			}
			return false;
		}
		var buttons = [{
			xtype : 'splitbutton',
			hidden: true,
			text : '快捷导航',
			iconCls : 'icon-sys-shortcut',
			arrowAlign : 'bottom',
			menu : [{
				text : '打开收藏夹', iconCls : 'icon-sys-open'
			},'-',{
				text : '常用模块',
				tooltip : '列出访问次数最多的10个模块',
				menu : [{
					text : '测试1'
				},{
					text : '测试2'
				},'-',{
					iconCls : 'icon-sys-clear',
					text : '清空列表'
				}]
			},{
				text : '最近访问',
				tooltip : '列出最近访问的10个模块',
				menu : [{
					text : '测试1'
				},{
					text : '测试2'
				},'-',{
					iconCls : 'icon-sys-clear',
					text : '清空列表'
				}]
			}]
			
		},{
			xtype : 'splitbutton',
			hidden : true,
			text : '窗口管理',
			iconCls : 'icon-sys-window',
			arrowAlign : 'bottom',
			menu : [{
				group : 'sys', name : 'closeAllWindow',text : '关闭所有', iconCls : 'icon-sys-closeall'
			}]
			
		},{
			xtype : 'splitbutton',
			//text : '<font color=blue>' + user.f_unit_caption + ' ' + user.f_caption + '</font>',
			text : '<font color=blue>' + user.f_caption + '</font>',
			iconCls : 'icon-sys-user',
			arrowAlign : 'bottom',
			menu : [{
				group : 'test', name : 'clearCache',text : '清空应用缓存', iconCls : 'icon-sys-empty', hidden: hiddenUserType()
			},{
				group : 'sys', name : 'buildIcons',text : '生成图标', iconCls : 'icon-sys-build', hidden: hiddenUserType(0)
			},{
				group : 'sys', name : 'buildActionsJs',text : '生成动作函数文件', iconCls : 'icon-sys-build', hidden: hiddenUserType(0)
			},{
				xtype: 'menuseparator', hidden: hiddenUserType(0, 1)
			},{
				group : 'sys', name : 'openPortal',text : '打开桌面', iconCls : 'icon-sys-open'
			},{
				group : 'sys', name : 'savePreferences', text : '保存桌面', iconCls : 'icon-sys-accept'
			},{
				group : 'sys', name : 'showPreferences', text : '界面配置', iconCls : 'icon-sys-preferences'
			},{
				group : 'sys', name : 'manager',text : '管理平台', iconCls : 'icon-sys-manager', hidden: hiddenUserType(0)
			},'-',{
				group : 'sys', name : 'helpDoc', text : '在线帮助', iconCls : 'icon-sys-book'
			},{
				group : 'sys', name : 'showAbout', text : '关于产品', iconCls : 'icon-sys-about'
			},{
				group : 'sys', name : 'userSetting',text : '用户设置', iconCls : 'icon-sys-setting', hidden: hiddenUserType(0, 1, 2)
			},{
				group : 'sys', name : 'changePassword',text : '修改密码', iconCls : 'icon-sys-password'
			},'-',{
				group : 'sys', name : 'logout', text : '退出系统', iconCls : 'icon-sys-logout'
			}]
		}];
    	this.add(buttons);
    	this.doLayout();
    }
});