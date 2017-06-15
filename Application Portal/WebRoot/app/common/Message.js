Ext.define('Asc.common.Message', {
	requires : [
		'Ext.ux.window.Notification'
	],
	statics : {
		defaultCfg : {
			position: 'br',
			cls: 'ux-notification-light',
			spacing: 20,
			autoCloseDelay: 4000,
			slideInDuration: 800,
			slideBackDuration: 1500,
			slideInAnimation: 'bounceOut',
			slideBackAnimation: 'easeIn',
			manager : 'asc-desktop-workspace'
		},
		// 消息
		showInfo : function(info, cfg){
			var infoCfg = Ext.apply({
				iconCls : 'ux-notification-icon-info',
				title : '提示信息',
				html: info
			}, this.defaultCfg)
			Ext.create('widget.uxNotification', Ext.apply(infoCfg, cfg)).show();
		},
		// 警告
		showAlarm : function(info, cfg){
			var alarmCfg = Ext.apply({
				iconCls : 'ux-notification-icon-alarm',
				title : '警告信息',
				html: info
			}, this.defaultCfg)
			Ext.create('widget.uxNotification', Ext.apply(alarmCfg, cfg)).show();
		},
		// 错误
		showError : function(info, cfg){
			var errorCfg = Ext.apply({
				iconCls : 'ux-notification-icon-error',
				title : '错误信息',
				html: info
			}, this.defaultCfg)
			Ext.create('widget.uxNotification', Ext.apply(errorCfg, cfg)).show();
		},
		// 显示屏蔽
		showMask : function(msg){
			Ext.getBody().mask(msg);
		},
		// 隐藏屏蔽
		hideMask : function(){
			Ext.getBody().unmask();
		},
		// 确认（显示是/否）
		showConfirm : function(title, msg, fn, scope){
			Ext.MessageBox.confirm(title, msg, fn, scope);
		},
		// 输出日志
		log : function(info, src, level){
			if(window.console){
				console.log('[' + Ext.Date.format(new Date(),'H:i:s u') + '] —— ' + info);
			}
		},
		// 输出错误
		error : function(info, src, level){
			if(window.console){
				console.error('[' + Ext.Date.format(new Date(),'H:i:s u') + '] —— ' + info);
			}
		}
	}
});