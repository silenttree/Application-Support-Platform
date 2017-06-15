<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script type="text/javascript">
	Ext.onReady(function(){
		var panel = Ext.getCmp('<%=panelid%>');
<%--		Ext.require('Asc.common.CdruEditor');
		Ext.require('Asc.common.CdruSelectorPanel');
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
				editor : Ext.create('Asc.common.CdruEditor', {
					
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
		panel.add(aGrid);
	--%>
	});
</script>

<table height=100%>
	<tr>
		<td width=80 align=center><image src="resources/images/logo-asc.png"/></td>
		<td>
			铁路物资综合电子商务服务平台，是支撑铁路物资综合业务的综合服务平台，架构于ASC（Application Support Center）应用支撑中心。
		</td>
	</tr>
	<tr>
		<td colspan=2 align=center>
			<span><a href="http://www.crmec.com.cn/" target="_blank">中铁物总电子商务技术有限公司 版权所有</a></span>
			<br>
			<span>CRM E-commerce tech.Co.Ltd All Rights Reserved.</span>
		</td>
	</tr>
</table>
