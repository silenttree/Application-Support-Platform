Ext.define('Asc.framework.system.controller.System', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscSystem',
	// 设置引用类
	requires : [],
	// 名称空间
	$namespace : 'Asc.framework.system',
	
	stores : ['PreferencesTreeStore'],
	// 视图
	views : ['Preferences',
	         'PreferencesTree',
	         'PreferencesEditor'],
	// 设置引用
	refs: [{
		ref: 'Preferences',
		selector: 'AscPreferences'
	}, {
		ref: 'PreferencesTree',
		selector: 'AscPreferencesTree'
	}, {
		ref: 'PreferencesEditor',
		selector: 'AscPreferencesEditor'
	}],
	
	init : function() {
		Asc.common.Message.log('System is loaded & initialise ', this, 'log');
		// 控制导航菜单
		this.control({
			'AscPreferencesEditor button' : {
				click : this.actionHandler
			},
			'AscPreferencesTree' : {
				selectionchange : this.loadPreferencesNodeEditor
			},
			'AscPreferencesTree button' : {
				click : this.showTplWin
			}
		});
	},
	
	// 显示模板窗口
	showTplWin : function(btn) {
		var me = this,
			tplComboBox,
			tplStore,
			tplWin;
		tplStore = Ext.create('Ext.data.Store', {
			autoLoad : true,
			fields : ['key', 'preferences'],
			proxy : {
				type : 'direct',
				directFn : PortalAppDirect.findAllPreferenceTpl,
				reader : {
					type : 'json'
				}
			},
			sorters : [{
				property : 'key'
			}]
		});
		tplComboBox = Ext.create('Ext.form.ComboBox', {
			store : tplStore,
			fieldLabel : '选择模板',
			editable : false,
			displayField : 'key',
			valueField : 'preferences'
		});
		
		tplWin =  Ext.create('Ext.window.Window', {
			title : btn.text,
			modal : true,
			resizable : false,
			height : 91,
			width : 279,
			items : [tplComboBox],
			bbar : ['->', {
				text : '确定',
				handler : function() {
					if(tplComboBox.getRawValue() === ''){
						return;
					}
					if(btn.name === 'saveTpl'){
						// 保存模板
						me.saveTpl(tplComboBox.getRawValue(), tplComboBox.getValue());
					}else {
						// 加载模板
						me.loadTpl(tplComboBox.getRawValue(), tplComboBox.getValue());
					}
				}
			}, {
				text : '取消',
				handler : function() {
					tplWin.close();
				},
			}]
		});
		tplWin.show();
	},
	
	loadTpl : function(key, preferences) {
		var userKey = AscApp.getPreferences().key;
		var newPreference = Ext.apply({},preferences);
		newPreference.key = userKey;
		AscApp.initPreferences(newPreference);
	},
	
	saveTpl : function(key, preferences) {
		var newPreference = Ext.apply({},AscApp.getPreferences());
		newPreference.key = key;
		PortalAppDirect.savePreferencesTpl(key, newPreference, function(result, e) {
			if(result && result.success){
				Asc.common.Message.showInfo('配置模板保存成功');
			}else{
				Asc.common.Message.showError('配置模板保存失败');
			}
		});
	},
	
	// 处理按钮点击事件
	actionHandler : function(btn){
		var editor;
		switch(btn.name){
		case 'apply' : 
			editor = this.getPreferencesEditor();
			editor.doApply();
			break;
		case 'save':
			editor = this.getPreferencesEditor();
			editor.doSave();
			break;
		case 'close':
			console.log(this);
			btn.up('window').close();
			break;
		}
	},
	
	// 装载界面参数编辑
	loadPreferencesNodeEditor : function(sm, selected, eOpts){
		var editor = this.getPreferencesEditor();
		editor.clearEditor();
		if(selected.length > 0){
			var node = selected[0];
			if(node.get('url')){
				editor.node = node;
				editor.setEditorTitle(node.getPath('text').replace('/Root/', ''));
				editor.enable();
				editor.loadEditor();
			}
		}
	}
});