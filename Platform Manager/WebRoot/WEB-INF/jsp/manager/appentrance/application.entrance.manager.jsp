<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_application_entrance';
	var typeClass = AscApp.ClassManager.getClass(type);
	// 获得对象属性列表
	var properties = AscApp.ClassManager.getProperties(type);
	// 初始化视图列
	var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
	columns[1].xtype = 'treecolumn';
	// 初始化存储字段
	var fields = AscApp.ClassManager.getStoreFields(properties);
	
	var orderFieldName = 'f_order';
	// 初始化数据存储对象
	var store = new Ext.data.TreeStore({
		clearRemovedOnLoad : true,
		autoLoad : false,
		root : {
			id: 0,
			f_level: 0,
	        expanded: true,
	        children: []
	    },
		proxy : {
			type : 'direct',
			directFn : AppEntDirect.loadAppEntranceTree
		},
		sorters : [{
			property : orderFieldName
		}],
		fields : fields
	});

	
	// 数据列表界面
	var grid = Ext.create('Ext.tree.Panel', {
		region : 'center',
		border : false,
		rowLines : true,
		sortableColumns : false,
        useArrows: true,
        rootVisible: false,
	    viewConfig: {stripeRows: true},
		allowDeselect : true,
	    columns : columns,
	    flex : 70,
		store : store,
		tbar : [{
			text : '添加',
			iconCls : 'icon-sys-add',
			handler : function(){
				grid.insertRecord();
			}
		}, '-',{
			text : '删除',
			iconCls : 'icon-sys-delete',
			handler : function(){
				grid.deleteRecord();
			}
		}, '-',{
			text : '上移',
			iconCls : 'icon-sys-moveup',
			handler : function(){
				grid.recordUp();
			}
		}, '-',{
			text : '下移',
			iconCls : 'icon-sys-moveDown',
			handler : function(){
				grid.recordDown();
			}
		} ,'->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.getStore().reload();
			}
		}]
	});
	
	// 属性编辑窗口
	var editor = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'east',
		split : true,
		border : false,
        collapseMode:'mini',
        flex : 30,
		minWidth : 200,
		title : '属性编辑窗口',
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/properties.editor',
		params : {type : type}
	});
	
	// 选中
	grid.on('beforeselect', function(g, record, index){
		// 被删除的记录不能选中
		if(record.isDeleted){
			editor.clearRecord();
			return false;
		}
	});
	// 选中
	grid.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			editor.loadRecord(selected[0]);
		}else{
			editor.clearRecord();
		}
	});
	
	// 添加记录
	grid.insertRecord = function(){
		var SelRecs = grid.getSelectionModel().getSelection();
		var pRec;
		if(SelRecs.length == 0){
			pRec = grid.getRootNode();
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
		addedNode.set('f_level', pRec.get('f_level') + 1);
		addedNode.set('parentId', pRec.get('id'));
		addedNode.set('leaf', true);
		pRec.expand();
		grid.getSelectionModel().select(addedNode);
	};
	
	grid.deleteNode = function(node){
		if(node.get('id') > 0){
			// 已存在的记录标记删除
			node.isDeleted = true;
			node.setDirty();
			if(node.childNodes.length > 0){
				for(var i = node.childNodes.length - 1; i >= 0; i--){
					grid.deleteNode(node.childNodes[0]);
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
	grid.deleteRecord = function(){
		var records = grid.getSelectionModel().getSelection();
		if(records.length == 0){
			return;
		}
		var recToDel = records[0];
		
		if(recToDel.childNodes.length > 1){
			Ext.Msg.confirm('确认删除', '该菜单项有子菜单，确定级联删除吗？', function(btn){
			    if (btn == 'yes'){
			    	grid.deleteNode(recToDel);
			    }
			});
		}else{
			grid.deleteNode(recToDel);
		}
		
		grid.getSelectionModel().deselectAll();
	};
	
	//上移
	grid.recordUp = function(){
		var SelectedRecs = grid.getSelectionModel().getSelection();
		if(SelectedRecs.length == 0){
			return;
		}
		var selectedRec = SelectedRecs[0];
		//选中了第一个节点不做操作
		if(selectedRec.isFirst()){
			return;
		}
		console.log(selectedRec);
		//如果不是同级第一个节点
		var pRec = selectedRec.parentNode;
		var index = pRec.indexOf(selectedRec);
		var recordUp = pRec.getChildAt(index - 1);
		var order = selectedRec.get(orderFieldName);
		var orderUp = recordUp.get(orderFieldName);
		recordUp.set(orderFieldName, order);
		selectedRec.set(orderFieldName, orderUp);
		
		store.sort();
		grid.getSelectionModel().select(selectedRec);
		editor.loadRecord(selectedRec);
	};
	
	//下移
	grid.recordDown = function(){
		var records = grid.getSelectionModel().getSelection();
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
		grid.getSelectionModel().select(selectedRec);
		editor.loadRecord(selectedRec);
	}
	
	// 刷新列表
	grid.refresh = function(){
		grid.getStore().reload();
		grid.getSelectionModel().deselectAll();
	};
	
	
	
	//重新编序号
	panel.reOrder = function(node){
		var idx,
			order;
		if(node.childNodes.length > 0){
			for(idx = 0, order = 1; idx < node.childNodes.length; idx++, order++){
				var record = node.getChildAt(idx);
				if(record.isDeleted){
					order--;
					continue;
				}
				if(record.get(orderFieldName) !== order){
					record.set(orderFieldName, order);
				}
				if(record.childNodes.length > 0){
					panel.reOrder(record);
				}
			}
		}
	};
	
	// 保存记录
	panel.doApply = panel.doSave = function(){
		panel.reOrder(grid.getStore().getRootNode());
		
		var insertRecs = grid.getStore().getNewRecords();
		var updateRecs = grid.getStore().getUpdatedRecords();
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
			AppEntDirect.SaveAppEnts({
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
					grid.refresh();
				}else{
					if(result && result.message){
						Asc.common.Message.showError(result.message);
					}else{
						Asc.common.Message.showError('保存对象操作失败！');
					}
				}
			});
		}
	};

	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [grid, editor]
	});
	panel.doLayout();
	// 装载数据
	grid.getStore().load();
	
});
</script>