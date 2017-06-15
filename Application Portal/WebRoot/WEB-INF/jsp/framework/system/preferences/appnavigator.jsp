<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
%>
<script>
Ext.onReady(function () {
	var contextPath = '<%=contextPath%>';
	var panel = Ext.getCmp('<%=panelid%>');
	var appGroupText = Ext.create('Ext.form.field.Text', {
		value : Ext.util.Cookies.get('appGroup')
	});
	
	var appEntPanel = Ext.create('Ext.panel.Panel', {
		padding : '5px',
		border : false,
		items : [{
				xtype : 'fieldset',
				title : '设置应用组',
				items : [
					{
						layout : 'column',
						border : false,
						items : [{
								columnWidth : 0.3,
								border : false,
								layout : 'form',
								items : [{
										border : false,
										html : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;应用组：'
									}
								]
							}, {
								columnWidth : 0.7,
								border : false,
								layout : 'form',
								items : [{
			                		hideLabel : true,
			                		xtype : 'radiogroup',
			                        items : [appGroupText]
			                	}]
							}
						]
					}
				]
			}
		]
	});
	panel.fnApply = function() {
		console.log(appGroupText.getValue());
		Ext.util.Cookies.set('appGroup', appGroupText.getValue(), new Date(new Date().getTime()+(1000*60*60*24*1000)));
	};
	
	var appIdsStr = '',	//应用的id组成的字符串，中间用分号分隔
		appManager = AscApp.getController('AscAppManager'),
		appStore = appManager.getStore('Applications');
	appStore.each(function(rec) {
		//if(rec.get('f_is_init')){
			//判断已经初始化完成的应用加入到参数中
			appIdsStr += rec.get('id') + ';';
		//}
		return true;
	});
	console.log(appIdsStr);
	
	panel.add(appEntPanel);
});
</script>