
// 输入支付密码
AscApp.ActionManager.addFunction('cfs.inputPayPassword', function(showAgain, callback, scope){
	Ext.require('Ext.ux.IFrame', function(){
		var win = Ext.create('Ext.Window', {
			title : '请输入支付密码',
			width : 360,
			height : 180,
			resizable : false,
			modal: true,
			layout : 'fit',
			items : {
				xtype : 'uxiframe', 
				padding : 5, 
				src : AscApp.jspPageProxy + "?url=cfs/icardpay.ras" + (showAgain ? '.again' : '')
			},
			buttons : [{
				text : '确定',
				handler : function(){
					var iframe = win.items.get(0);
					var domIframe = iframe.getFrame();
					var doc = domIframe.contentWindow;
					var result = doc.getRsaPassword();
					if(result.successed){
						callback.call(scope || win, result.message);
						win.close();
					}else{
						Asc.common.Message.showInfo(result.message);
					}
				}
			}, {
				text : '取消',
				handler : function(){
					win.close();
				}
			}]
		})
		win.show();
	}); 
});

Ext.form.field.ComboBox.prototype.findRecordByValue = function(value){
	return this.findRecord(this.valueField + '', value + '');
}
//打开监控平台工具
AscApp.ActionManager.addFunction('sys.monitor', function(){
	window.open('http://10.3.4.15/portal/monitor.jsp');
});