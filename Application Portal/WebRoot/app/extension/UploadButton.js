// use swfupload flash
Ext.define('Asc.extension.UploadButton', {
	// 指定基类
	extend : 'Ext.ux.upload.Button',
	// 设置别名
	alias : 'widget.AscUploadButton',
	
	requires: ['Ext.ux.upload.Button', 
	           'Ext.ux.upload.plugin.Window'],
	
	constructor: function(cfg){
		var me = this;
		var config = Ext.apply({}, cfg);
		config.uploader = Ext.apply({}, cfg.uploader);
		Ext.apply(config.uploader, {
			browse_button : config.id || Ext.id(me)
		});
		if(Ext.isDefined(config.uploader.multipart_params)){
			Ext.applyIf(config.uploader.multipart_params, {
				actionId : config.name
			});
		}else{
			config.uploader.multipart_params = {
				actionId : config.name
			};
		}
		// 根据AppId计算url
		var appManager = AscApp.getController('AscAppManager');
		Ext.applyIf(config.uploader, {
			url : '/upload',
			autoStart: true,
			max_file_size: '128mb',
			multi_selection: true
		});
		config.uploader.url = appManager.getAppUrl(config.appKey) + config.uploader.url;
		Ext.applyIf(config, {
			plugins: [{
				ptype: 'ux.upload.window',
				title: 'Upload',
				width: 520,
				height: 350
			}]
		});
		me.callParent([config]);
	}
});