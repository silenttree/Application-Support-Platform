<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = '<%=type%>';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[2].xtype = 'treecolumn';
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	panel.dicId = 0;
	panel.dataId = 0;
	panel.extproperty;
	var orderFieldName = 'f_order';

	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		split : true,
		border : false,
        collapseMode:'mini',
		width : 400,
		minWidth : 200,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/extproperty/extproperties.editor',
		params : {type : type}
	});
	
	// 初始化数据存储对象
	var store = new Ext.data.TreeStore({
		clearRemovedOnLoad : true,
		autoLoad : false,
		root : {
			id: 0,
	        expanded: true
	    },
	    fields : fields,
	    proxy : {
			type : 'direct',
			directFn : DictionaryDirect.loadDictionaryDataNodes,
			extraParams : {
				dicId : panel.dicId,
				dataId : panel.dataId
			},
			reader : {
				type: 'json',
				root : 'datas',
				totalProperty : 'totals',
				successProperty : 'successed',
				messageProperty : 'message'
			}
		},
		sorters : [{
			property : orderFieldName
		}],
		listeners : {
			'beforeload' : function(s, operation, eOpts){
				operation.params = {dicId : panel.dicId, dataId : panel.dataId};
			}
		}
	});
	
	var dicdatatree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		border : false,
		sortableColumns : false,
        useArrows: true,
        rootVisible: false,
	    viewConfig: {stripeRows: false},
		allowDeselect : true,
		split : true,
	    columns : columns,
	    width : 500,
		fields : fields,
		store : store,
 		plugins: [Ext.create('Asc.common.ExtPropertyData', {type: type,editor: editor})],
 		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				panel.insertRecord();
			}
		}, '-',{
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				panel.deleteRecord();
			}
		}, '-',{
			text : '上移',
			iconCls : 'icon-sys-moveup',
			handler : function(){
				panel.recordUp();
			}
		}, {
			text : '下移',
			iconCls : 'icon-sys-movedown',
			handler : function(){
				panel.recordDown();
			}
		}, '->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				dicdatatree.getSelectionModel().deselectAll();
				dicdatatree.getStore().reload();
			}
		}]
	});
	
	// 选中校验
	dicdatatree.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			editor.clearRecord();
			return false;
		}
	})
	// 选中
	dicdatatree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			editor.loadRecord(selected[0]);
		}else{
			editor.clearRecord();
		}
	})
	// 添加记录
	panel.insertRecord = function(){
		var SelRecs = dicdatatree.getSelectionModel().getSelection();
		var pRec;
		if(SelRecs.length == 0){
			pRec = dicdatatree.getRootNode();
			console.log(pRec);
		}else{
			//找到父记录
			pRec = SelRecs[0];
			if(!pRec.get('id')){
				Asc.common.Message.showError('添加失败：上级导航菜单必须先进行保存');
				return false;
			}
			pRec.set('leaf',false);
		}
		
		var recToAdd = AscApp.ClassManager.getPropertyDefaultValues(type);
		var addedNode = pRec.appendChild(recToAdd);
		addedNode.set('f_order', pRec.childNodes.length + 1);
		addedNode.set('f_parent_id', pRec.get('id'));
		addedNode.set('f_level', Number(pRec.get('f_level')) + 1);//lhy update :将pRec.get('f_level') 转化为Number类型，防止后台报错
		addedNode.set('parentId', pRec.get('id'));
		addedNode.set('leaf', true);
		pRec.expand();
		dicdatatree.getSelectionModel().select(addedNode);
	};
	
	panel.deleteNode = function(node){
		if(node.get('id') > 0){
			// 已存在的记录标记删除
			node.isDeleted = true;
			node.setDirty();
			if(node.childNodes.length > 0){
				for(var i = node.childNodes.length - 1; i >= 0; i--){
					panel.deleteNode(node.childNodes[0]);
				}
			}
		}else{
			// 新添加的记录直接删除
			var pNode = node.parentNode;
			var index = pNode.indexOf(node);
			pNode.removeChild(node);
			if(pNode.childNodes.length == 0){
				//设置父节点为叶子节点
				pNode.set('leaf', true);
			}
			for(var i = index; i < pNode.childNodes.length; i++){
				pNode.getChildAt(i).set(orderFieldName,pNode.getChildAt(i).get(orderFieldName) - 1);
			}
		}
	};
	
	// 删除记录
	panel.deleteRecord = function(){
		var records = dicdatatree.getSelectionModel().getSelection();
		if(records.length == 0){
			return;
		}
		var recToDel = records[0];
		
		if(recToDel.childNodes.length > 1){
			Ext.Msg.confirm('确认删除', '该菜单项有子菜单，确定级联删除吗？', function(btn){
			    if (btn == 'yes'){
			    	panel.deleteNode(recToDel);
			    }
			});
		}else{
			panel.deleteNode(recToDel);
		}
		
		dicdatatree.getSelectionModel().deselectAll();
	};
	
	//上移
	panel.recordUp = function(){
		var SelectedRecs = dicdatatree.getSelectionModel().getSelection();
		if(SelectedRecs.length == 0){
			return;
		}
		var selectedRec = SelectedRecs[0];
		//选中了第一个节点不做操作
		if(selectedRec.isFirst()){
			return;
		}
		//如果不是同级第一个节点
		
		var pRec = selectedRec.parentNode;
		var index = pRec.indexOf(selectedRec);
		var recordUp = pRec.getChildAt(index - 1);
		var order = selectedRec.get(orderFieldName);
		var orderUp = recordUp.get(orderFieldName);
		recordUp.set(orderFieldName, order);
		selectedRec.set(orderFieldName, orderUp);
		
		store.sort();
		dicdatatree.getSelectionModel().select(selectedRec);
		editor.loadRecord(selectedRec);
	};
	
	//下移
	panel.recordDown = function(){
		var records = dicdatatree.getSelectionModel().getSelection();
		if(records.length == 0){
			return;
		}
		var selectedRec = records[0];
		//选中了最后一个节点不做操作
		if(selectedRec.isLast()){
			return;
		}
		
		//如果不是同级最后一个节点
		var pRec = selectedRec.parentNode;
		var index = pRec.indexOf(selectedRec);
		var recordDown = pRec.getChildAt(index + 1);
		var order = selectedRec.get(orderFieldName);
		var orderDown = recordDown.get(orderFieldName);
		recordDown.set(orderFieldName, order);
		selectedRec.set(orderFieldName, orderDown);
		
		store.sort();
		dicdatatree.getSelectionModel().select(selectedRec);
		editor.loadRecord(selectedRec);
	}
	
	//点击字典重新加载grid和editor
	panel.reStore = function(dicId){
		panel.dicId = dicId;
		panel.dataId = dicId;
		//dicdatatree.reconfigure(store, columns);
		dicdatatree.getStore().load();
	}
	
	panel.delAll = function(){
		dicdatatree.getSelectionModel().deselectAll();
	}
	
	panel.reLoad = function(dicId){
		panel.dicId = dicId;
    	dicdatatree.getStore().load(panel.dicId);
	}
	
	// 保存记录
	panel.applys = function(){
		var insertRecs = dicdatatree.getStore().getNewRecords();
		var updateRecs = dicdatatree.getStore().getUpdatedRecords();
		if(insertRecs.length + updateRecs.length > 0){
			var deletes = [];
			var updates = [];
			for(var i in insertRecs){
				var record = insertRecs[i];
				updates.push(record.getData());
			}
			for(var i in updateRecs){
				var record = updateRecs[i];
				if(record.isDeleted){
					deletes.push({id : record.get('id')});
				}else{
					updates.push(Ext.apply({id : record.get('id')},record.getChanges()));
				}
			}
			DictionaryDirect.saveDictionaryData(panel.dicId, panel.dataId, {
				updates : updates,
				deletes : deletes
			}, function(result, e){
				if(result && result.success){
					if(result.sObjects && result.sObjects.length > 0){
						var str = '';
						for(var n in result.sObjects){
							str = str + '<br>[' + result.sObjects[n].id + ']';
						}
						Asc.common.Message.showInfo('共[' + result.sObjects.length + ']个对象提交成功：' + str);
					}
					if(result.fObjects && result.fObjects.length > 0){
						var str = '';
						for(var n in result.fObjects){
							str = str + '<br>[' + result.fObjects[n].id + ']';
						}
						Asc.common.Message.showError('共[' + result.fObjects.length + ']个对象提交失败：' + str);
					}
					panel.refresh();
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('保存对象操作失败！');
					}
				}
			});
		}
	}
	// 刷新列表
	panel.refresh = function(){
		dicdatatree.getStore().reload();
		dicdatatree.getSelectionModel().deselectAll();
	}
	// 装载并显示组织树
	panel.add({
		layout : 'border',
		border : false,
		items : [dicdatatree, editor]
	});
	panel.doLayout();
});
</script>