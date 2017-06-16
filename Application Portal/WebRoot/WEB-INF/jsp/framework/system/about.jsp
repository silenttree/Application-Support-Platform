<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script language=javascript>
Ext.onReady(function(){
		var panel = Ext.getCmp('<%=panelid%>');
		<%--	Ext.require('Asc.common.WorkFlowUserSelector');
		var selector = Ext.create('Asc.common.WorkFlowUserSelector', {
			intelligentUsers : 'U_1;U_15;U_16;U_17',
			selectedUsers : 'U_1',
			singleSelect : false,
			candidate : true
		});
		panel.add(selector);
	--%>
	<%--
	Ext.require('Asc.extension.editor.CdruEditor');
	Ext.require('Asc.extension.editor.CdruSelectorPanel');
	var aStore = Ext.create('Ext.data.Store', {
		storeId : 'simpsonsStore',
		fields : [ 'name', 'email', 'phone', 'auth_expression', 'auth_display' ],
		data : {
			'items' : [ {
				'name' : 'Lisa',
				"email" : "lisa@simpsons.com",
				"phone" : "555-111-1224"
			}, {
				'name' : 'Bart',
				"email" : "bart@simpsons.com",
				"phone" : "555-222-1234"
			}, {
				'name' : 'Homer',
				"email" : "home@simpsons.com",
				"phone" : "555-222-1244"
			}, {
				'name' : 'Marge',
				"email" : "marge@simpsons.com",
				"phone" : "555-222-1254"
			} ]
		},
		proxy : {
			type : 'memory',
			reader : {
				type : 'json',
				root : 'items'
			}
		}
	});

	var aGrid = Ext.create('Ext.grid.Panel', {
		title : 'test',
		store : aStore,
		columns : [ {
			text : 'Name',
			dataIndex : 'name'
		}, {
			text : 'Email',
			dataIndex : 'email'
		}, {
			text : 'Phone',
			dataIndex : 'phone'
		}, {
			text : 'auth_expression',
			dataIndex : 'auth_expression',
			flex : 1
		}, {
			text : 'auth_display',
			dataIndex : 'auth_display',
			flex : 1,
			editor : Ext.create('Asc.extension.editor.CdruEditor', {
				
			})
		} ],
		selType : 'cellmodel',
		plugins : [ Ext.create('Ext.grid.plugin.CellEditing', {
			clicksToEdit : 1,
			listeners : {
				beforeedit : function(editor, e) {
					var picker = e.column.getEditor();
					// 设置初始值
					picker.getValueFn = function(){
						return e.record.get('auth_expression');
					}
					picker.setCallback(function(authExpression, authDisplay) {
						e.record.set('auth_expression', authExpression);
					});
				}
			}
		}) ],
	});
	panel.add(aGrid);--%>
});
</script>
	<%--
<table height=100%>
	<tr>
		<td width=80 align=center><image src="resources/images/logo-asc.png"/></td>
		<td>
			
		</td>
	</tr>
	<tr>
		<td colspan=2 align=center>
			<span><a href="http://www.crmec.com.cn/" target="_blank"></a></span>
			<br>
			<span></span>
		</td>
	</tr>
</table> 
--%>