Ext.define('Asc.framework.system.view.PreferencesEditor', {
	// 指定基类
	extend : 'Ext.panel.Panel',
	// 设置别名
	alias : 'widget.AscPreferencesEditor',

	layout : 'border',
	
	border : false,
	
	bbar : [{
		text : '应用',
		name : 'apply',
		scale : 'medium',
		iconCls : 'icon-sys-apply'
	}, {
		text : '保存',
		name : 'save',
		scale : 'medium',
		iconCls : 'icon-sys-save'
	},'->', {
		text : '关闭',
		name : 'close',
		scale : 'medium',
		iconCls : 'icon-sys-close'
	}],

	items : [{
		region : 'north',
		border : false,
		bodyCls : 'asc-system-preferences-editor-title-body',
		data : {
			title : ''
		},
		tpl : '{title}',
		height : 35
	}, {
		region : 'center',
		layout : 'fit'
	}],
	// 获得编辑界面对象
	getEditorPanel : function(){
		return this.items.get(1);
	},
	// 恢复编辑界面
	clearEditor : function(){
		this.disable();
		this.setEditorTitle('');
		this.getEditorPanel().removeAll();
	},
	// 设置标题
	setEditorTitle : function(title){
		this.items.get(0).update({title : title});
	},
	// 装载界面
	loadEditor : function(){
		var url = this.node.get('url');
		var editor = Ext.create('Asc.extension.JspPanel', {
			jspUrl : url
		});
		var panel = this.getEditorPanel();
		panel.add(editor);
		panel.doLayout();
	},
	// 应用按钮
	doApply : function(){
		var panel = this.getEditorPanel();
		var editor = panel.items.get(0);
		if(editor && editor.fnApply){
			editor.fnApply.call(editor);
		}
	},
	
	//保存按钮
	doSave: function(){
		var userKey = AscApp.user.f_caption,
			preferences;
		this.doApply();
		preferences = AscApp.getPreferences();
		PortalAppDirect.savePreferences(userKey, preferences, function(result, e) {
			if(result && result.success){
				Asc.common.Message.showInfo('配置保存成功');
			}else{
				Asc.common.Message.showError('配置保存失败');
			}
		});
	}
});