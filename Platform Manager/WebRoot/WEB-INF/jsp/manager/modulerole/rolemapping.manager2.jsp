<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	
	panel.selectType = 'cdru';
	panel.range = '';
	
	// 选择范围数
	var cdrTreePanel = Ext.create("Ext.container.Container", {
		title : '选择范围',
		region : 'north',
		split : true,
		collapsible: true,
		border : false,
		layout: 'fit',
		flex : 50
	});
	(function(panel, wrapPanel, mainPanel){
		<%@ include file="CDR.tree2.js" %>
	})(cdrTreePanel, panel, panel);
	
	// 备选列表
	var toSelectPanel = Ext.create("Ext.container.Container", {
		title : '备选列表',
		region : 'center',
		collapseMode : 'header',
		split : true,
		collapsible: true,
		border : false,
		layout: 'fit',
		flex : 50
	});
	(function(panel, wrapPanel, mainPanel){
		<%@ include file="toselect.grid2.js" %>
	})(toSelectPanel, panel, panel);
	
	// 关联列表
	var relativePanel = Ext.create("Ext.container.Container", {
		title: '关联列表',
		region: 'west',
		split : true,
		width: 300,
		layout: 'fit',
		border: false
	});
	(function(panel, wrapPanel, mainPanel){
		<%@ include file="relative.grid2.js" %>
	})(relativePanel, panel, panel);
	
	// 权限配置列表
	var mappingPanel = Ext.create("Ext.container.Container", {
		title: '权限配置列表',
		region: 'center',
		layout: 'fit',
		border: false
	});
	(function(panel, wrapPanel, mainPanel){
		<%@ include file="modulerolemapping.grid2.js" %>
	})(mappingPanel, panel, panel);
	
 	var selectRange, // 选中的选择范围
 		selectedKey,
 		relativeKey,
 		appId, 
 		isAll = false;
 	
 	panel.getSelectRange = function() {
 		return selectRange;
 	};
 	
 	panel.getRelativeKey = function() {
 		return relativeKey;
 	};
 	
 	panel.isAll = function() {
 		return isAll;
 	}
 	
 	// 加载配置表格
 	var loadMappingGrid = function() {
 		if(relativeKey && appId) {
	 		mappingPanel.loadMappingGrid(relativeKey, appId, isAll);
 		} else {
 			mappingPanel.clearMappingGrid();
 		}
 	};
	
	// 选择范围树选中时重新载入备选列表
 	cdrTreePanel.on('selectionchange', function(tree, selected, opt) {
 		if(selected.length === 0) {
 			selectRange = null;
 			toSelectPanel.clear();
 		} else {
 			selectRange = selected[0].get('key');
 			toSelectPanel.load(selectRange);
 		}
 	});
	
 	toSelectPanel.on('selectionchange', function(grid, selected, opt) {
 		if(selected.length === 0) {
 			selectedKey = null;
 			relativePanel.clear();
 		} else {
 			selectedKey = selected[0].get('key');
 			relativePanel.load(selectedKey);
 		}
 	});
 	
 	relativePanel.on('selectionchange', function(grid, selected, opt) {
 		if(selected.length === 0) {
 			relativeKey = null;
 			isAll = false;
 		} else {
			var key = selected[0].get("key");
 	 		if(key === "ALL") {
 	 			relativeKey = selectedKey;
 	 			isAll = true;
 	 			mappingPanel.clearManageUnit(); // 清空管理单位
 	 		} else {
 	 			relativeKey = key;
 	 			isAll = false;
 	 			mappingPanel.loadManageUnit(relativeKey); // 载入管理单位
 	 		}
 		}
 		loadMappingGrid();
 	});
 	
 	mappingPanel.on('applicationchange', function(com, applicationId) {
 		if(applicationId > 0) {
 			appId = applicationId;
 			loadMappingGrid();
 		} else {
 			appId = null;
 		}
 		loadMappingGrid();
 	});
	// 保存记录
	panel.doApply = panel.doSave = function() {
		mappingPanel.doApply();
	};

	
	// 显示界面
	panel.add({
		layout : 'border',
		border : false,
		items : [{
			xtype: 'container',
			region: 'west',
			layout: 'border',
			split : true,
			width: 300,
			items: [cdrTreePanel, toSelectPanel]
		}, relativePanel, mappingPanel]
	});
});
</script>