Ext.define('Asc.framework.desktop.view.PasswordChanger', {
	extend : 'Ext.window.Window',
	alias : 'widget.passwordChanger',
	iconCls : 'icon-sys-password',
	title : '修改密码',
	layout : 'fit',
	autoShow : true,
	modal : true,
    
	items : [{
		xtype : 'form',
		padding : '5 10 0 10', 
		frame : true,
		items : [{
			xtype: 'textfield',
			name : 'oldPassword',
			fieldLabel: '原始密码',
			inputType : 'password',
			validator : function(val){
				if(Ext.isEmpty(val)){
					return '原始密码不能为空';
				}
				return true;
			}
		},{
			xtype: 'textfield',
			name : 'newPassword',
			fieldLabel: '新密码',
			inputType : 'password',
			validator : function(val){
				if(Ext.isEmpty(val)){
					return '新密码不能为空';
				}
				return true;
			}
		},{
			xtype: 'textfield',
			name : 'newPassword2',
			fieldLabel: '确认新密码',
			inputType : 'password',
			validator : function(val){
				if(Ext.isEmpty(val)){
					return '请再次输入新密码';
				}
				var form = this.up('window').down('form');
				var newPw = form.down('textfield[name="newPassword"]').getValue();
				if(val != newPw){
					return '两次输入的新密码必须一致';
				}
				return true;
			}
		}]
	}],
	
	buttons : [{
		text : '确定',
		iconCls : 'icon-sys-accept',
		name : 'okButton'
	},{
		text : '取消',
		iconCls : 'icon-sys-closeall',
		name : 'cancelButton'
	}],
	
	initComponent : function(){
		this.callParent(arguments);
		//获取确定和取消按钮
		var okButton = this.getDockedItems('toolbar')[0].child('[name="okButton"]');
		var cancelButton = this.getDockedItems('toolbar')[0].child('[name="cancelButton"]');
		//为确定和取消按钮加上handler
		okButton.setHandler(this.confirm.bind(this));
		cancelButton.setHandler(this.close.bind(this));
	},
	
	confirm : function(){
		var win = this;
		var form = win.down('form');
		if(form.isValid()){
			var oldPw = form.down('textfield[name="oldPassword"]').getValue();
			var newPw = form.down('textfield[name="newPassword"]').getValue();
			var newPw2 = form.down('textfield[name="newPassword2"]').getValue();
			win.setDisabled(true);
			PortalAppDirect.changePassword(oldPw,newPw,newPw2,function(result,e){
				if(result && result.success){
					Asc.common.Message.showInfo(result.msg);
					win.close();
				}else if(!result || !result.hasOwnProperty('msg')){
					Asc.common.Message.showError('密码修改失败');
					win.setDisabled(false);
				}else{
					Asc.common.Message.showError(result.msg);
					win.setDisabled(false);
				}
			});
		}
	}
});