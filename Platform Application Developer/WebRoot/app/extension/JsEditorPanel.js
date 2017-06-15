Ext.define('Asc.extension.JsEditorPanel', {
	// 指定基类
	extend : 'Ext.panel.Panel',
	// 设置别名
	alias : 'widget.AscJsEditorPanel',
	autoScroll : true,
	border : false,
	// 重载构造函数
	constructor : function(config){
		var me = this;
		// 获得ID
		me.getId();
		me.callParent([config]);
		me.on('afterlayout', function(panel, layout, eOpts){
			me.createEditor();
		});
		me.on('resize', function(panel, layout, eOpts){
			if(me.editor){
				me.editor.resize();
			}
		});
	},
	createEditor : function(){
		var me = this;
		if(me.hasEditor){
			return;
		}
		me.hasEditor = true;
		var themeStore = Ext.create('Ext.data.Store', {
		    fields: ['name', 'theme'],
		    data : [
		        {"name":"ambiance", "theme":"ambiance"},
		        {"name":"chaos", "theme":"chaos"},
		        {"name":"chrome", "theme":"chrome"},
		        {"name":"clouds", "theme":"clouds"},
		        {"name":"clouds_midnight", "theme":"clouds_midnight"},
		        {"name":"cobalt", "theme":"cobalt"},
		        {"name":"crimson_editor", "theme":"crimson_editor"},
		        {"name":"dawn", "theme":"dawn"},
		        {"name":"dreamweaver", "theme":"dreamweaver"},
		        {"name":"eclipse", "theme":"eclipse"},
		        {"name":"github", "theme":"github"},
		        {"name":"idle_fingers", "theme":"idle_fingers"},
		        {"name":"iplastic", "theme":"iplastic"},
		        {"name":"katzenmilch", "theme":"katzenmilch"},
		        {"name":"kr_theme", "theme":"kr_theme"},
		        {"name":"kuroir", "theme":"kuroir"},
		        {"name":"merbivore", "theme":"merbivore"},
		        {"name":"merbivore_soft", "theme":"merbivore_soft"},
		        {"name":"mono_industrial", "theme":"mono_industrial"},
		        {"name":"monokai", "theme":"monokai"},
		        {"name":"pastel_on_dark", "theme":"pastel_on_dark"},
		        {"name":"solarized_dark", "theme":"solarized_dark"},
		        {"name":"solarized_light", "theme":"solarized_light"},
		        {"name":"sqlserver", "theme":"sqlserver"},
		        {"name":"terminal", "theme":"terminal"},
		        {"name":"textmate", "theme":"textmate"},
		        {"name":"tomorrow", "theme":"tomorrow"},
		        {"name":"tomorrow_night", "theme":"tomorrow_night"},
		        {"name":"tomorrow_night_blue", "theme":"tomorrow_night_blue"},
		        {"name":"tomorrow_night_bright", "theme":"tomorrow_night_bright"},
		        {"name":"tomorrow_night_eighties", "theme":"tomorrow_night_eighties"},
		        {"name":"twilight", "theme":"twilight"},
		        {"name":"vibrant_ink", "theme":"vibrant_ink"},
		        {"name":"xcode", "theme":"xcode"}
		    ]
		});
		//模板选择
		var themeComboBox = me.themeComboBox = Ext.create('Ext.form.ComboBox', {
		    fieldLabel: '选择模板',
		    store: themeStore,
		    queryMode: 'local',
		    labelAlign: 'right',
		    labelWidth: 60,
		    width: 260,
		    displayField: 'name',
		    valueField: 'theme',
		    listeners : {
				select : {
					fn : function (combo, records, eOpts) {
						editor.setTheme("ace/theme/"+combo.getValue());
						if(typeof me.onThemeSelect == 'function'){
							me.onThemeSelect();
						}
					}
				}
			}
		});
		//编译信息结果展示
		var infoDisplay = me.infoDisplay = Ext.create('Ext.form.field.Display', {
			value : ''
		});
		me.addDocked({
		    xtype: 'toolbar',
		    dock: 'bottom',
		    items: [
		        themeComboBox, '->', infoDisplay
		    ]
		});
		var editorId = 'editor' + (new Date()).getTime();
		me.update('<div id="'+editorId+'" class="ace_editor" style="height:100%;"></div>');
		var editor = ace.edit(editorId);
		me.editor = editor;
		editor.getSession().setMode("ace/mode/javascript");
	    editor.setOptions({
	        enableBasicAutocompletion: true,
	        enableSnippets: true,
	        enableLiveAutocompletion: true
	    });
	    editor.on('change', function() {
	        if(me.hasEditorError()){
	        	infoDisplay.update('编辑器内容正确！');
	        }else{
	        	infoDisplay.update('<span style="color:#e43c59;">编辑器内容有误！</span>');
	        }
	        if(typeof me.onChange == 'function'){
				me.onChange();
			}
	    });
	    editor.on('focus', function(event, editor) {
//	         debugger;
	    });

	    editor.on('blur', function(event, editor) {
//	        debugger;
	    });
	    editor.commands.addCommand({
		    name: "fullscreen",
		    bindKey: {win: "Ctrl-Enter", mac: "Command-Enter"},
		    exec: function(editor) {
		    	if(typeof me.onCtrlCenter == 'function'){
						me.onCtrlCenter();
				}
		    }
		});
	    if(me.value){
	    	editor.setValue(me.value);
	    	editor.selection.clearSelection();
	    }
//	    editor.focus();
//	    me.on('show', function(){debugger;
//	    	editor.focus();
//	    });
	},
	getValue : function(){
		if(this.editor){
			return this.editor.getValue();
		}
		return null;
	},
	setValue : function(value){
		this.value = value;
		if(this.editor){
			this.editor.setValue(value);
//			this.editor.renderer.updateFull();
		}
	},
	setEditorTheme : function(theme){
		if(this.editor){
			this.editor.setTheme('ace/theme/'+theme);
		}
	},
	hasEditorError : function(){
		if(this.editor){
			var annotations = this.editor.getSession().getAnnotations();
		    for (var aid = 0, alen = annotations.length; aid < alen; ++aid) {
		        if (annotations[aid].type === 'error') {
		            return true;
		        }
		    }
		}
	    return false;
	},
	onChange : function(){
		
	},
	onCtrlCenter : function(){//ctrl + enter键盘事件
		
	},
	//背景选择事件
	onThemeSelect : function(){
		
	},
	setThemeValue : function(value){
		this.themeComboBox.setValue(value);
	},
	getThemeValue : function(){
		return this.themeComboBox.getValue();
	},
	getCursorPosition : function(){
		return this.editor.getCursorPosition();
	},
	focusCursorPosition : function(row, column){
		this.editor.gotoLine(row, column, true);
	},
	focusEditor : function(){
		if(!this.editor.isFocused()){
			var me = this;
			setTimeout(function(){
				me.editor.focus();
			}, 100);
//			this.editor.navigateFileStart();
		}
	},
	clearSelection : function(){
		this.editor.selection.clearSelection();
	}
});