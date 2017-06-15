<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
		
	var editor = Ext.create('Ext.panel.Panel', {
		border : false,
		bodyPadding : 5,
		autoScroll : true,
		tbar : [{
			text : '刷新布局',
			handler : function(){
				panel.refresh();
			}
		}, {
			text : '重置布局',
			handler : function(){
				panel.resetLayout();
			}
		}, {
			text : '保存布局'
		}, '-', {
			text : '添加列',
			handler : function(){
				panel.insertCol();
			}
		}, {
			text : '添加行',
			handler : function(){
				panel.insertRow();
			}
		}, '-', {
			text : '删除列',
			handler : function(){
				panel.removeCol();
			}
		}, {
			text : '删除行',
			handler : function(){
				panel.removeRow();
			}
		}, {
			text : '设置测试',
			hidden : true,
			handler : function(){
				panel.formLayout.rows = panel.formLayout.rows + 1;
				panel.setTestLayout();
			}
		}, {
			text : '测试拆分',
			hidden : true,
			handler : function(){
				panel.splitLayoutCol(0, 0);
				panel.showLayout();
			}
		}, {
			text : '测试合并',
			hidden : true,
			handler : function(){
				panel.mergeLayoutCol(0, 1);
				panel.showLayout();
			}
		}]
	});
	
	panel.setTestLayout = function(){
		var layout = {
			cols : 4,
			rows : 4,
			layout : [{
				html : 'test1',
				colspan : 2
			},{
				html : 'test3',
				rowspan: 2
			},{
				html : 'test4',
				rowspan: 2
			},{
				html : 'test5'
			},{
				html : 'test6',
				rowspan: 3
			},{
				html : 'test7',
				rowspan : 2
			},{
				html : 'test9',
				rowspan: 2
			},{
				html : 'test10'
			},{
				html : 'test11'
			}]
		}
		panel.loadLayout(layout);
	}
	
	// 设置布局字段，已经添加过的字段，再次添加时，要清空已经存在的该布局字段
	panel.setFieldLayout = function(btn){
		var index = contextmenu.index;
		for(var i = 0; i < panel.formLayout.layout.length;i++){
			var layoutTemp = panel.formLayout.layout[i];
			if(Ext.isDefined(layoutTemp.field) && layoutTemp.field.indexOf(btn.key) >= 0){
				layoutTemp.field = '';
			}
		}
		panel.formLayout.layout[index].field = btn.key;
		panel.showLayout();
	}
	
	var contextmenu = Ext.create('Ext.menu.Menu', {
		items : [{
			id : panel.getId() + '-contextment',
			text : '设置字段',
			menu : {
				items : []
			}
		}, '-', {
			text : '右合并',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				var col = cell.maxCol + 1;
				if(col == panel.formLayout.cols){
					Asc.common.Message.showError("最右侧单元格无法执行向右合并操作");
					return;
				}
				panel.mergeLayoutCol(index, col);
				panel.showLayout();
			}
		}, {
			text : '左合并',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				var col = cell.minCol - 1;
				if(col < 0){
					Asc.common.Message.showError("最左侧单元格无法执行向左合并操作");
					return;
				}
				panel.mergeLayoutCol(index, col);
				panel.showLayout();
			}
		}, {
			text : '下合并',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				var row = cell.maxRow + 1;
				if(row == panel.formLayout.rows){
					Asc.common.Message.showError("最下方单元格无法执行向下合并操作");
					return;
				}
				panel.mergeLayoutRow(index, row);
				panel.showLayout();
			}
		}, {
			text : '上合并',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				var row = cell.minRow - 1;
				if(row < 0){
					Asc.common.Message.showError("最上方单元格无法执行向上合并操作");
					return;
				}
				panel.mergeLayoutRow(index, row);
				panel.showLayout();
			}
		}, '-', {
			text : '右拆分',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				if(cell.minCol == cell.maxCol){
					Asc.common.Message.showError("没有合并列无法进行右拆分");
					return;
				}
				panel.splitLayoutCol(index, cell.maxCol);
				panel.showLayout();
			}
		}, {
			text : '左拆分',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				if(cell.minCol == cell.maxCol){
					Asc.common.Message.showError("没有合并列无法进行右拆分");
					return;
				}
				panel.splitLayoutCol(index, cell.minCol);
				panel.showLayout();
			}
		}, {
			text : '下拆分',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				if(cell.minRow == cell.maxRow){
					Asc.common.Message.showError("没有行并列无法进行下拆分");
					return;
				}
				panel.splitLayoutRow(index, cell.maxRow);
				panel.showLayout();
			}
		}, {
			text : '上拆分',
			handler : function(){
				var index = contextmenu.index;
				var cell = panel.itemCells[index];
				if(cell.minRow == cell.maxRow){
					Asc.common.Message.showError("没有行并列无法进行下拆分");
					return;
				}
				panel.splitLayoutRow(index, cell.minRow);
				panel.showLayout();
			}
		}]
	});
	
	panel.on('destroy', function(){
		Ext.destroy(contextmenu);
	});
	var getCaption = function(items,key){
		for(var i = 0;i < items.length;i++){
			if(key == items[i].key){
				return items[i].text;
			}
		}
		return "";
	};
	// 显示布局
	panel.showLayout = function(){
		var layout = [];
		var items = contextmenu.items.getByKey(panel.getId() + '-contextment').menu.items.items;
		for(var i=0;i<panel.formLayout.layout.length;i++){
			layout.push({
				rowspan : panel.formLayout.layout[i].rowspan,
				colspan : panel.formLayout.layout[i].colspan,
				height : 40,
				html : Ext.isDefined(panel.formLayout.layout[i].field)?
						(panel.formLayout.layout[i].field + '<BR>' + getCaption(items,panel.formLayout.layout[i].field)):''
			});
		}
		for(var i=0;i<panel.formLayout.cols;i++){
			layout.push({
				width : 200
			});
		}
		editor.removeAll();
		var table = Ext.create('Ext.panel.Panel', {
			border : false,
			layout : {
				type : 'table',
				columns : panel.formLayout.cols,
				tableAttrs : {
					border : 1
				},
				tdAttrs : {
					valign : 'top',
					backgroundcolor : 'yellow'
				},
				trAttrs : {
					align : 'center'
				}
			},
			defaults : {
				padding : 2,
				border : false,
				style : {
					width : '100%',
					height : '100%'
				},
				bodyStyle : {
					width : '100%',
					height : '100%'
				}
			},
			items : layout
		});
		editor.add(table);
		editor.doLayout();
		table.body.on('contextmenu', function(e, t, eOpt){
			var dom;
			if(t.tagName == 'TD'){
				dom = Ext.DomQuery.select(':first-child', t)[0];
			}else{
				var el = Ext.fly(t).findParent('TD');
				dom = Ext.DomQuery.select(':first-child', el)[0];
			}
			var cell = Ext.getCmp(dom.id);
			contextmenu.index = table.items.indexOf(cell);
			contextmenu.showAt(e.getXY());
		});
	};
	// 按行合并单元格
	panel.mergeLayoutRow = function(index, row){
		var cell = panel.itemCells[index];
		// 判断是否允许执行
		if(row < 0 || row == panel.formLayout.rows || (row != cell.maxRow+1 && row != cell.minRow - 1)){
			return;
		}
		// 先拆分相邻单元格列
		for(var col = cell.minCol;col<cell.maxCol + 1;col++){
			var tIndex = panel.cellItems[row][col] - 1;
			panel.splitLayoutRow(tIndex, row);
		}
		// 左边界拆分
		var leftIndex = panel.cellItems[row][cell.minCol] - 1;
		var leftCell = panel.itemCells[leftIndex];
		if(leftCell.minCol < cell.minCol){
			panel.splitLayoutCol(leftIndex, cell.minCol);
		}
		// 右边界拆分
		var rightIndex = panel.cellItems[row][cell.maxCol] - 1;
		var rightCell = panel.itemCells[rightIndex];
		if(rightIndex.maxCol > cell.maxCol){
			panel.splitLayoutCol(rightIndex, cell.maxCol + 1);
		}
		// 刷新最新索引
		index = panel.cellItems[cell.minRow][cell.minCol] - 1;
		// 合并单元格
		var tCells = [];
		for(var col = cell.minCol;col<cell.maxCol + 1;col++){
			var tIndex = panel.cellItems[row][col] - 1;
			if(tCells.indexOf(tIndex) < 0){
				tCells.push(tIndex);
			}
		}
		// 将当前单元索引插入目标单元格索引列表中
		tCells.push(index);
		tCells.sort(function(a, b) {
		    return a - b;
		});
		// 设置合并后单元格属性（Index最小的为合格后的单元格）
		panel.formLayout.layout[tCells[0]].rowspan = cell.maxRow - cell.minRow + 2;
		panel.formLayout.layout[tCells[0]].colspan = panel.formLayout.layout[index].colspan || 1;
		panel.formLayout.layout[tCells[0]].field = panel.formLayout.layout[index].field;
		// 倒序删除单元格
		for(var i=tCells.length-1;i>0;i--){
			panel.formLayout.layout.splice(tCells[i], 1);
		}
		// 刷新合并后解析
		panel.setLayout();
		return index;
	};
	// 按列合并单元格
	panel.mergeLayoutCol = function(index, col){
		var cell = panel.itemCells[index];
		// 判断是否允许执行
		if(col < 0 || col == panel.formLayout.cols || (col != cell.maxCol+1 && col != cell.minCol - 1)){
			return;
		}
		// 先拆分相邻单元格
		for(var row = cell.minRow;row<cell.maxRow + 1;row++){
			var tIndex = panel.cellItems[row][col] - 1;
			panel.splitLayoutCol(tIndex, col);
		}
		// 上边界拆分
		var upIndex = panel.cellItems[cell.minRow][col] - 1;
		var upCell = panel.itemCells[upIndex];
		if(upCell.minRow < cell.minRow){
			panel.splitLayoutRow(upIndex, cell.minRow - 1);
		}
		// 下边界拆分
		var downIndex = panel.cellItems[cell.maxRow][col] - 1;
		var downCell = panel.itemCells[downIndex];
		if(downCell.maxRow > cell.maxRow){
			panel.splitLayoutRow(downIndex, cell.maxRow + 1);
		}
		// 刷新最新索引
		index = panel.cellItems[cell.minRow][cell.minCol] - 1;
		// 合并单元格
		var tCells = [];
		for(var row = cell.minRow;row<cell.maxRow + 1;row++){
			var tIndex = panel.cellItems[row][col] - 1;
			if(tCells.indexOf(tIndex) < 0){
				tCells.push(tIndex);
			}
		}
		// 将当前单元索引插入目标单元格索引列表中
		tCells.push(index);
		tCells.sort(function(a, b) {
		    return a - b;
		});
		// 设置合并后单元格属性（Index最小的为合格后的单元格）
		panel.formLayout.layout[tCells[0]].rowspan = panel.formLayout.layout[index].rowspan || 1;
		panel.formLayout.layout[tCells[0]].colspan = cell.maxCol - cell.minCol + 2;
		panel.formLayout.layout[tCells[0]].field = panel.formLayout.layout[index].field;
		// 倒序删除单元格
		for(var i=tCells.length-1;i>0;i--){
			panel.formLayout.layout.splice(tCells[i], 1);
		}
		// 刷新合并后解析
		panel.setLayout();
		return index;
	}
	
	// 按行拆分单元格
	panel.splitLayoutRow = function(index, row){
		// 获得目标位置单元格
		var cell = panel.itemCells[index];
		// 判断目标单元格是否需要拆分
		if(cell.minRow == cell.maxRow || (cell.minRow != row && cell.maxRow != row)){
			return false;
		}
		console.log('开始拆分行 index = ' + index + '  row = ' + row);
		// 查找新单元格目标位置
		var fRow = (row == cell.minRow ? row + 1 : row);
		for(var newIndex = index;newIndex < panel.formLayout.layout.length;newIndex++){
			var tCell = panel.itemCells[newIndex];
			if(tCell.minRow > fRow || (tCell.minRow == fRow && tCell.minCol > cell.maxCol)){
				break;
			}
		}
		// 修改原单元格合并信息
		/*
		if(row == cell.minRow){
			panel.formLayout.layout[index].rowspan = cell.maxRow - cell.minRow + 1;
		}else{
			panel.formLayout.layout[index].rowspan = 1;
		}
		*/
		panel.formLayout.layout[index].rowspan = row - cell.minRow == 0 ? 1 : row - cell.minRow;
		// 在新位置执行插入
		for(var i = panel.formLayout.layout.length;i >newIndex;i--){
			panel.formLayout.layout[i] = panel.formLayout.layout[i-1];
		}
		panel.formLayout.layout[newIndex] = {
			colspan : panel.formLayout.layout[index].colspan || 1,
			rowspan : cell.maxRow - row == 0 ? 1 : cell.maxRow - row
		}
		// 刷新拆分后解析
		panel.setLayout();
		panel.showLayout();
		return newIndex;
	}

	// 按列拆分单元格
	panel.splitLayoutCol = function(index, col){
		// 获得目标位置单元格
		var cell = panel.itemCells[index];
		// 判断目标单元格是否需要拆分
		if(cell.minCol == cell.maxCol || (cell.minCol != col && cell.maxCol != col)){
			return false;
		}
		console.log('开始拆分列 index = ' + index + '  col = ' + col);
		var newIndex = index + 1;
		// 修改原单元格合并信息
		panel.formLayout.layout[index].colspan = (col == cell.minCol ? 1 : cell.maxCol - cell.minCol);
		// 在新位置执行插入
		for(var i = panel.formLayout.layout.length;i >=newIndex;i--){
			panel.formLayout.layout[i] = panel.formLayout.layout[i-1];
		}
		panel.formLayout.layout[newIndex] = {
			colspan : col == cell.minCol ? 1 : cell.maxCol - col + 1,
			rowspan : panel.formLayout.layout[index].rowspan || 1
		}
		// 刷新拆分后解析
		panel.setLayout();
		panel.showLayout();
		return newIndex;
	}
	// 设置布局（解析布局）
	panel.setLayout = function(){
		// 单元格对应元素序号表
		panel.cellItems = [];
		// 元素对应单元格起止位置
		panel.itemCells = [];
		var rows=panel.formLayout.rows, cols = panel.formLayout.cols;
		for(var i=0;i<rows;i++){
			panel.cellItems[i] = [];
			for(var j=0;j<cols;j++){
				panel.cellItems[i][j] = 0;
			}
		}
		// 根据放置元素行列属性设置单元格内存
		var row = 0,col = 0;
		for(var index = 0;index<panel.formLayout.layout.length;index++){
			var cell = panel.formLayout.layout[index];
			var rowspan = cell.rowspan ? cell.rowspan : 1;
			var colspan = cell.colspan ? cell.colspan : 1;
			panel.itemCells[index] = {
				minRow : row,
				minCol : col,
				maxRow : row + rowspan - 1,
				maxCol : col + colspan - 1
			};
			for(var i=row;i<row + rowspan;i++){
				for(var j=col;j<col+ colspan;j++){
					panel.cellItems[i][j] = index + 1;
				}
			}
			do{
				col ++;
				if(col >= cols){
					row ++;
					col = 0;
				}
			}while(row < rows && panel.cellItems[row][col])
			if(index >= rows * cols){
				break;
			}
		}
		// 取出相同合并行列
		if(panel.removeSameMergeRow() || panel.removeSameMergeCol()){
			panel.setLayout();
		}
	}
	// 去除相同合并行
	panel.removeSameMergeRow = function(){
		var rows=panel.formLayout.rows, cols = panel.formLayout.cols;
		if(cols > 0){
			for(var i=0;i<rows;i++){
				// 查找是否存在相同合并行
				var hasUnmergeRow = false;
				for(var j=0;j<cols;j++){
					var index = panel.cellItems[i][j] - 1;
					var cell = panel.itemCells[index];
					if(cell.minRow == cell.maxRow){
						hasUnmergeRow = true;
						break;
					}
				}
				if(!hasUnmergeRow){
					// 去除合并行
					for(var j=0;j<cols;j++){
						var index = panel.cellItems[i][j] - 1;
						panel.formLayout.layout[index].rowspan--;
					}
					panel.formLayout.rows--;
					return true;
				}
			}
		}
	}
	// 去除相同合并列
	panel.removeSameMergeCol = function(){
		var rows=panel.formLayout.rows, cols = panel.formLayout.cols;
		if(rows > 0){
			for(var i=0;i<cols;i++){
				// 查找是否存在相同合并列
				var hasUnmergeCol = false;
				for(var j=0;j<rows;j++){
					var index = panel.cellItems[j][i] - 1;
					var cell = panel.itemCells[index];
					if(cell.minCol == cell.maxCol){
						hasUnmergeCol = true;
						break;
					}
				}
				if(!hasUnmergeCol){
					// 去除合并行
					for(var j=0;j<rows;j++){
						var index = panel.cellItems[j][i] - 1;
						panel.formLayout.layout[index].colspan--;
					}
					panel.formLayout.cols--;
					return true;
				}
			}
		}
	}
	// 重置布局（撤销所有合并）
	panel.resetLayout = function(cfg){
		var items = [];
		var cfg = panel.formLayout;
		for(var i=0;i<cfg.rows;i++){
			for(var j=0;j<cfg.cols;j++){
				items.push({});
			}
		}
		panel.formLayout.layout = items;
		panel.setLayout();
		panel.showLayout();
	}
	// 插入行
	panel.insertRow = function(cfg){
		panel.formLayout.rows++;
		for(var i=0;i<panel.formLayout.cols;i++){
			panel.formLayout.layout.push(cfg || {});
		}
		panel.setLayout();
		panel.showLayout();
	}
	// 删除行
	panel.removeRow = function(){
		// 获得要拆分的行索引
		var row = panel.formLayout.rows - 1;
		if(row < 0){
			Asc.common.Message.showError("没有可以删除的行！");
			return;
		}
		// 拆分最后一行
		for(var i=0;i<panel.formLayout.cols;i++){
			// 获得单元格索引
			var index = panel.cellItems[row][i] - 1;
			// 拆分出最后一行
			panel.splitLayoutRow(index, row);
		}
		// 查找需要删除的单元格索引
		var indexs = [];
		for(var i=0;i<panel.formLayout.cols;i++){
			// 获得单元格索引
			var index = panel.cellItems[row][i] - 1;
			if(indexs.indexOf(index) < 0){
				indexs.push(index);
			}
		}
		indexs.sort(function(a, b) {
		    return a - b;
		});
		for(var i=indexs.length;i>0;i--){
			panel.formLayout.layout.splice(indexs[i-1], 1);
		}
		panel.formLayout.rows --;
		panel.setLayout();
		panel.showLayout();
	}
	// 插入列
	panel.insertCol = function(cfg){
		var indexs = [];
		panel.formLayout.cols++;
		for(var i=0;i<panel.formLayout.rows;i++){
			// 查找新单元格目标位置
			var row = i;
			for(var index = 0;index < panel.formLayout.layout.length;index++){
				var cell = panel.itemCells[index];
				if(cell.minRow > row || (cell.minRow == row && cell.minCol > panel.formLayout.rows-1)){
					break;
				}
			}
			indexs.push(index + i);
		}
		for(var i=0;i<indexs.length;i++){
			// 在新位置执行插入
			for(var j = panel.formLayout.layout.length;j >indexs[i];j--){
				panel.formLayout.layout[j] = panel.formLayout.layout[j-1];
			}
			panel.formLayout.layout[indexs[i]] = cfg || {};
		}
		panel.setLayout();
		panel.showLayout();
	}
	// 删除列
	panel.removeCol = function(){
		// 获得要拆分的行索引
		var col = panel.formLayout.cols - 1;
		if(col == 0){
			Asc.common.Message.showError("只有一列布局，无法删除！");
			return;
		}
		// 拆分最后一列
		for(var i=0;i<panel.formLayout.rows;i++){
			// 获得单元格索引
			var index = panel.cellItems[i][col] - 1;
			// 拆分出最后一列
			panel.splitLayoutCol(index, col);
		}
		// 查找需要删除的单元格索引
		var indexs = [];
		for(var i=0;i<panel.formLayout.rows;i++){
			// 获得单元格索引
			var index = panel.cellItems[i][col] - 1;
			if(indexs.indexOf(index) < 0){
				indexs.push(index);
			}
		}
		indexs.sort(function(a, b) {
		    return a - b;
		});
		for(var i=indexs.length;i>0;i--){
			panel.formLayout.layout.splice(indexs[i-1], 1);
		}
		panel.formLayout.cols --;
		panel.setLayout();
		panel.showLayout();
	}
	// 装载布局（解析并显示）
	panel.loadLayout = function(cfg){
		panel.formLayout = cfg;
		if(!cfg.layout){
			panel.resetLayout();
		}else{
			panel.setLayout();
			panel.showLayout();
		}
	}
	// 刷新（从服务器获取数据）
	panel.refresh = function(){
		DeveloperAppDirect.loadObject(panel.object.appId, panel.object.key, function(result, e){
			if(result && result.success){
				var inputType = 'inputfield';
				var parentId = panel.object.key;
				if(panel.object.type == 'formtablelayout'){
					inputType = 'inputfield';
					parentId = result.object.f_i_parent.data;
				}else if(panel.object.type == 'queryform'){
					inputType = 'queryfield';
					parentId = panel.object.key;
				}
				//从服务器获取表单的输入域，加载该表单的输入域
				DeveloperAppDirect.loadObjects(panel.object.appId, parentId, inputType, function(result,e){
					if(result && result.success){
						
						//获取返回的数据
						var objs = result.datas;
					 	var items = [];
					 	//根据返回的数据，封装成右键菜单的items属性
						for(var i = 0;i < objs.length;i++){
							var temp = {
									text : objs[i].f_caption,
									key : objs[i].f_key,
									handler : panel.setFieldLayout
							};
							items.push(temp);
						}
						//把右键菜单的items属性加入到右键菜单中
						var menu = contextmenu.items.getByKey(panel.getId() + '-contextment').menu;
						menu.removeAll();
						menu.add(items);
						panel.showLayout();
					}
				});
				panel.loadLayout({
					cols : result.object.f_cols,
					rows : result.object.f_rows,
					layout : result.object.f_layout
				});
			}else{
				if(result && result.message){
					Asc.common.Message.showError(result.message);
				}else{
					Asc.common.Message.showError('界面布局装载失败！');
				}
			}
		});
	}
	// 获得数据
	panel.getLayoutValues = function(){
		return panel.formLayout;
	}
	
	panel.add(editor);
	panel.doLayout();
	
	panel.refresh();
});
</script>