Ext.define('Asc.framework.manager.controller.Manager', {
	// 指定基类
	extend : 'Ext.app.Controller',
	// 设置别名
	alias : 'widget.AscManager',
	// 设置引用类
	requires : [],
	// 名称空间
	$namespace : 'Asc.framework.manager',
	// 数据模型
	models : [],
	// 数据存储
	stores : [],
	// 视图
	views : [],
	// 设置引用
    refs: [{
		ref : 'AscNavigator',
		selector: 'AscDesktopNavigator'
	},{
		ref : 'AscWorkspace',
		selector: 'AscDesktopWorkspace'
	}],
	// 初始化
	init : function() {
		Asc.common.Message.log('Manager is loaded & initialise ', this, 'log');
		
		this.control({
			'AscDesktopNavigator' : {
				itemdblclick : this.onNavigatorNodeDblclick,
				select : this.onNavigatorNodeSelect
			},
			'AscDesktopWorkspace > panel' : {
				activate : this.onEditorActivate
			},
			'AscDesktopWorkspace > panel button[group=manager]' : {
				click : AscApp.ActionManager.handler
			}
		});
	},
	// 重新装载子节点
	reloadNode : function(node, callback){
		var navigator = this.getAscNavigator();
		if(!node){
			node = navigator.getSelectionModel().getLastSelected();
		}
		if(!node || node == null || node.leaf){
			return;
		}
		navigator.getStore().load({
			node : node,
			callback : callback
		});
	},
	// 关闭编辑器
	closeEditor : function(navigatorNodeId){
		var ws = this.getAscWorkspace();
		if(navigatorNodeId){
			var editor = this.getAscWorkspace().down('[navigatorNodeId=' + navigatorNodeId + ']');
			ws.remove(editor);
		}else{
			ws.remove(ws.getActiveTab());
		}
	},
	// 打开编辑器
	openEditor : function(obj){
		if(!obj){
			obj = AscApp.Context.getActivateObject();
		}
		if(!obj){
			Asc.common.Message.log('open editor error object undefined', this, 'log');
			return;
		}
		var objClass = AscApp.ClassManager.getClass(obj.type);
		if(!objClass){
			Asc.common.Message.log('open editor error object type 【' + obj.type + '】 undefined', this, 'log');
			return;
		}
		if(!objClass.editors){
			Asc.common.Message.log('open editor error object type 【' + obj.type + '】 editors is undefined', this, 'log');
			return;
		}
		var ws = this.getAscWorkspace();
		// 查找编辑器是否已经打开
		var navigatorNodeId = AscApp.ClassManager.getNodeId(obj.type, obj.key);
		var editor = ws.down('[navigatorNodeId=' + navigatorNodeId + ']');
		if(Ext.isDefined(editor) && editor != null){
			// 激活已打开的编辑器
			this.getAscWorkspace().setActiveTab(editor);
		}else{
			// 查找节点
			var store = this.getAscNavigator().getStore();
			var node = store.getNodeById(navigatorNodeId);
			var title = node.get('text') + ' [' + node.get('type') + ' ' + node.get('key') + ']';
			// 解析编辑参数
			var items = [];
			for(var i=0;i<objClass.editors.length;i++){
				var e = objClass.editors[i];
				var item;
				if(typeof e == 'object'){
					item = Ext.apply({}, e);
				}else{
					item = {jspUrl : e};
				}
				items.push(Ext.apply(item, {
					object : Ext.apply({}, obj)
				}));
			}
			// 创建编辑器
			if(items.length > 1){
				// 创建多窗口编辑器
				editor = Ext.create('Asc.framework.manager.view.MultiEditor', {
					title : title,
					iconCls : objClass.iconCls,
					items : items
				});
			}else{
				// 创建单窗口编辑器
				editor = Ext.create('Asc.framework.manager.view.SingleEditor', Ext.apply(items[0],{
					title : title,
					iconCls : objClass.iconCls
				}));
			}
			// 设置参数
			Ext.apply(editor, {
				navigatorNodeId : navigatorNodeId,
				object : Ext.apply({}, obj)
			});
			// 显示编辑器
			ws.add(editor);
			ws.doLayout();
			ws.setActiveTab(editor);
		}
		return editor;
	},
	// 执行编辑窗口刷新按钮
	reloadEditor : function(){
		var ws = this.getAscWorkspace();
		var editor = ws.getActiveTab();
		if(editor.getXType() == 'AscManagerMultiEditor'){
			editor = editor.getActiveTab();
		}
		editor.removeAll();
		editor.getLoader().load();
	},
	// 执行编辑窗口应用按钮
	applyEditor : function(){
		var ws = this.getAscWorkspace();
		var editor = ws.getActiveTab();
		if(Ext.isDefined(editor.doApply)){
			editor.doApply();
		}else{
			Asc.common.Message.log('doapply editor error function undefined', this, 'log');
		}
	},
	// 执行编辑窗口保存按钮
	saveEditor : function(){
		this.applyEditor();
	},
	// 激活对象编辑器
	activateEditor : function(obj){
		var navigatorNodeId = AscApp.ClassManager.getNodeId(obj.type, obj.key);
		var editor = this.getAscWorkspace().down('[navigatorNodeId=' + navigatorNodeId + ']');
		if(Ext.isDefined(editor) && editor != null){
			this.getAscWorkspace().setActiveTab(editor);
		}
	},
	// 导航菜单节点
	selectNavigatorNode : function(navigatorNodeId){
		var sm = this.getAscNavigator().getSelectionModel();
		var store = this.getAscNavigator().getStore();
		var node = store.getNodeById(navigatorNodeId);
		if(node && node != null){
			this.getAscNavigator().expandPath(node.getPath());
			sm.select(node);
		}
	},
	// 装载页面
	loadEditorPanel : function(url, params, cfg){
		this.removeAll();
	},
	// 双击节点事件
	onNavigatorNodeDblclick : function(){
		this.openEditor();
	},
	// 递归关闭节点编辑窗口
	closeNodeEditor : function(node){
		if(node && node != null){
			node.eachChild(function(child){
				this.closeNodeEditor(child);
			}, this);
			this.closeEditor(node.getId());
		}
	},
	// 删除节点
	afterDeleteNode : function(nodeId){
		var tree = this.getAscNavigator();
		var store = this.getAscNavigator().getStore();
		var node = store.getNodeById(nodeId);
		// 关闭该节点及所有下级节点编辑器
		this.closeNodeEditor(node);
		// 刷新父节点
		if(node && node != null){
			var parent = node.parentNode;
			if(parent && parent != null){
				this.reloadNode(parent);
			}
			// 选中父节点
			this.selectNavigatorNode(parent.getId());
		}
	},
	// 激活节点（选中）
	onNavigatorNodeSelect : function(tree, record, item, index, e, eOpts){
		var obj = {
			type : record.get('type'),
			key : record.get('key')
		};
		// 激活上下文对象
		AscApp.Context.activateObject(obj);
		// 设置菜单动作可用
		AscApp.ClassManager.setActivateObject(obj);
		// 激活编辑器界面
		AscApp.getAscManager().activateEditor(obj);
	},
	// 激活容器
	onEditorActivate : function(editor){
		AscApp.Context.activateContainer(editor);
		// 获得上下文对象
		var obj = AscApp.Context.getActivateObject();
		// 选中节点
		AscApp.getAscManager().selectNavigatorNode(editor.navigatorNodeId);
	}
});