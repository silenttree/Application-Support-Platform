<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
	String contextPath = request.getContextPath();
%>
<script>
	Ext.onReady(function() {
		var contextPath = '<%=contextPath%>'; 
		var panel = Ext.getCmp('<%=panelid%>');
		
		var store = Ext.create('Ext.data.TreeStore', {
			clearRemovedOnLoad: true,
			root: {
		        expanded: true,
		        children: []
		    },
			proxy: {
				type : 'direct',
				directFn : PortalAppDirect.getPortlets,
				paramOrder : ['appIdsStr']
			},
			sorters: [{
				property : 'f_key'
			}],
			fields: ['key', 'f_app_key', 'f_caption', 'f_icon', 'f_key', 'f_name', 'f_refresh_interval'],
			listeners: {
				load: function(store, node, records) {
					var rootNode = store.getRootNode();
					var userPortlets = AscApp.getPreferences().portlets;
					checkSelected(rootNode, userPortlets);
				},
				beforeload: function(store, operation) {
					// 将应用列表传入到direct方法中
					var appIdsStr = '',	//应用的id组成的字符串，中间用分号分隔
						appManager = AscApp.getController('AscAppManager'),
						appStore = appManager.getStore('Applications');
					appStore.each(function(rec) {
						if(rec.get('f_is_init')){
							//判断已经初始化完成的应用加入到参数中
							appIdsStr += rec.get('id') + ';';
						}
						return true;
					});
					operation.params = {};
					operation.params.appIdsStr = appIdsStr;
				}
			}
		});
		
		var checkSelected = function(node, selectedrecs) {
			if(selectedrecs && selectedrecs.length !== 0){
				selectedrecs.every(function(val) {
					if(node.get('key') === val.id) {
						node.set('checked', true);
					}
					return true;
				});
			}
			if (node.hasChildNodes()) {
				node.childNodes.every(function(child) {
					checkSelected(child, selectedrecs);
					return true;
				});
			}
		};
		
		var grid = Ext.create('Ext.tree.Panel', {
			rootVisible: false,
			store: store,
			border: false,
			selModel: {mode: 'SIMPLE'},
			columns: [
				{xtype: 'treecolumn', text: '桌面栏目', dataIndex: 'f_caption', flex: 1}
			]
		});
		
		panel.fnApply = function() {
			var index,
				originalPortlets = AscApp.getPreferences().portlets,
				selectedRecs = grid.getChecked(),
				toDeleteKeys = [];
			// 找出需要删除的
			originalPortlets.every(function(portlet) {
				// 判断要不要保留
				var shouldKeep = false;
				selectedRecs.every(function(rec) {
					if(rec.get('key') === portlet.id) {
						// 在已选的记录里面找到了相应的记录，打上标记，退出遍历
						shouldKeep = true;
						return false;
					} else{
						// 继续遍历
						return true; 
					}
				});
				if(!shouldKeep){
					toDeleteKeys.push(portlet.id);
				}
				return true;
			});
			
			// 先删除掉需要删除的
			toDeleteKeys.every(function(id) {
				AscApp.removePortlet(id);
				return true;
			});
			
			// 再添加桌面栏目
			selectedRecs.every(function(val) {
				AscApp.addPortlet({
					id : val.get('key'),
					appKey : val.get('f_app_key'),
					portletId : val.get('f_key')
				});
				return true;
			});
		};
		panel.add(grid);
		grid.getStore().load();
	});
</script>