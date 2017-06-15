<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String type = request.getParameter("type");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	panel.record = "";
	
	var formpanel = Ext.create('Ext.form.Panel', {
	    bodyPadding: 5,
	    width: 350,
	    layout: 'anchor',
	    defaults: {
	        anchor: '100%'
	    },
	    defaultType: 'displayfield',
	    items: [{
	        fieldLabel: 'ID',
	        name: 'id',
	        editable : false,
	        value : record.data.id
	    },{
	        fieldLabel: '姓名',
	        name: 'caption',
	        editable : false,
	        value : record.data.f_caption
	    },{
	        fieldLabel: '所在单位',
	        name: 'company',
	        editable : false,
	        value : record.data.f_company_caption
	    },{
	        fieldLabel: '所在部门',
	        name: 'dept',
	        editable : false,
	        value : record.data.f_dept_caption
	    },{
	    	xtype : 'treecombo',
	        fieldLabel: '变更机构',
	        name: 'last',
	        allowBlank : false
	    }]
	});
	
	var treepanel = Ext.create('Ext.tree.Panel', {
		border : false,
		height : 250,
		width : 350,
		autoScroll : true,
		store : new Ext.data.TreeStore({
			root : {
				id : 0,
				iconCls : 'icon-manager-root',
				text : '组织机构',
				expanded : true
			},
			proxy : {
				type : 'direct',
				directFn : OrganizationDirect.loadOrganizationNodes,
				reader : {
					type: 'json',
					root : 'datas',
					totalProperty : 'totals',
					successProperty : 'successed',
					messageProperty : 'message'
				}
			}
		})
	});
	//变更组织
	var win = new Ext.Window({
		width : 350,
		height : 200,
		modal : true,
		resizable : false,
		iconCls : 'icon-cross-common-view',
		title : '变更机构',
		layout : 'fit',
		items : treepanel,
		buttons : [{
			text : '保存',
			iconCls : 'icon-cross-btn-save',
			handler : function(){
				var userId = formpanel.getForm().findField('id').getValue();
				var orgId = formpanel.getForm().findField('last').getValue();
				if(orgId){
					OrganizationDirect.updateUserOrg(userId.toString(), orgId.toString(), function(result, e){
						if(result && result.success){
							win.close();
							panel.refresh();
						}
					});
				}
			}
		}, {
			text : '关闭',
			iconCls : 'icon-cross-btn-close',
			handler : function(){
				win.close();
			}
		}]
	});
	
	panel.test = function(){
		alert(111);
	}
	
	// 装载并显示组织树
	panel.add({
		layout : 'border',
		border : false,
		items : [win]
	});
	panel.doLayout();
});
</script>