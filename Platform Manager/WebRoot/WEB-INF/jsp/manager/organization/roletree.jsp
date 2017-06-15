<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	var type = 't_asc_role';
	panel.orgId = 0;
	panel.roleId = 0;

	// 角色用户窗口
	var grid = Ext.create('Asc.extension.JspPanel', {
		disabled : true,
		layout : 'fit',
		region : 'center',
		header: false,
		split : true,
		border : false,
		iconCls : 'icon-manager-property',
		jspUrl : 'manager/organization/roleusergrid',
		params : {type : type}
	});
	
	panel.ds = true;
	var roletree = Ext.create('Ext.tree.Panel', {
		region : 'west',
		width : 230,
		minWidth : 150,
		split : true,
		border : false,
        collapseMode:'mini',
		store : Ext.create('Ext.data.TreeStore', {
			clearOnLoad : true,
			clearRemovedOnLoad : true,
			root : {
				iconCls : 'icon-manager-rolemanager',
				text : '角色列表',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : OrganizationDirect.loadRoleNodes,
				extraParams : {
					orgId : panel.orgId
				}
			},
			listeners : {
				'beforeload' : function(s, operation, eOpts){
					operation.params = {orgId : panel.orgId};
				}
			},
			autoLoad: false
		}),
		tbar : ['->', {
			text : '刷新',
			iconCls : 'icon-sys-refresh',
			handler : function(){
				grid.desAll();
				roletree.getSelectionModel().deselectAll();
				roletree.getRootNode().removeAll(false);
				roletree.getStore().load();
			}
		}]
	});
	// 选中校验
	roletree.on('beforeselect', function(g, record, index){
		// 跟节点不能选中
		if(record.get('id') == 0 || record.get('id') == 'root' || record.get('text') == '机构角色'){
			grid.desAll();
			grid.disable();
			return false;
		}
	})
	// 选中对象树节点
	roletree.on('selectionchange', function(g, selected){
		if(selected.length > 0){
			panel.roleId = selected[0].get('id');
			grid.enable();
			//动态更新store
			OrganizationDirect.findRoleUsers(panel.roleId,panel.orgId,function(result,e){
				if(result&&result.success){
					console.log(result.datas);
					var gridtype = 't_asc_user';
					// 获得对象属性列表
					var properties = AscApp.ClassManager.getProperties(gridtype);
					var array = [];
					for(var i=0;i<result.datas.length;i++){
						array.push([result.datas[i].f_dept_id,result.datas[i].f_dept_caption]);
					}
					properties.f_dept_id.store = array;
					grid.reStore(panel.orgId, panel.roleId);
				}
			});
			
		}else{
			grid.disable();
		}
	})
	
	panel.delAll = function(){
		roletree.getSelectionModel().deselectAll();
	}
	
	panel.reLoad = function(orgId){
		panel.orgId = orgId;
		roletree.getStore().reload();
	}
	
	panel.apply = function(){
		grid.apply();
	}
	// 装载并显示组织树
	panel.add({
		layout : 'border',
		border : false,
		items : [roletree, grid]
	});
	panel.doLayout();
});
</script>