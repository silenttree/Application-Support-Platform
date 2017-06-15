<%@page contentType="text/html;charset=UTF-8"%>
<%
	String panelid = request.getParameter("panelid");
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	// 获取应用的key
	var appKey = panel.appId;
	var getBaseDirectFn = function(method) {
		return AscApp.getController('AscAppManager').getAppDirectFn(appKey,"BaseDataDirect" , method);
	};
	
	var listOrgsAttribute = getBaseDirectFn('listOrgsAttribute');
	
	var treePanel;		// 类型树
	var treeStore;		// 树的store
	var selectedCodes = ['013']; // 已选择的机构code
	var selectingCodes = [];// 正在选中的code
	
	var isEmpty = function(v){
		if(v == undefined || v == null || v == ''){
			return true;
		}else{
			return false;
		}
	};
	
	treeStore = Ext.create('Ext.data.TreeStore', {
		nodeParam : 'node',// 根节点的参数是parentId
		defaultRootId : 0,// 根节点的参数值是20
		autoLoad : false,
		fields : [{
            name : 'id',
            type : 'long'
        }, {
            name : 'orgcode',
            type : 'long'
        }, {
            name : 'orgshortname',
            type : 'string'
        }, {
            name : 'orgname',
            type : 'string'
        }, {
            name : 'iconCls',
            type : 'string',
            value : 'icon-sys-organization'
        }, {
        	name : 'checked',
        	type : 'boolean',
        	value : false
        }],
        proxy : {
			type : 'direct',
			directFn : listOrgsAttribute,
// 			paramOrder : ['QI'],
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		},
		listeners : {
			'load' : function(store, node, successful){
				checkboxSelected(node, node.get('checked'));
				if(node.hasChildNodes()){
					node.eachChild(function(child) {
						child.set('iconCls','icon-sys-organization');
						if(selectedCodes.indexOf(child.get('orgcode')) >= 0){
							child.set('checked',true);
// 							selectingCodes.push(child.get('orgcode'));
						}
					});
				}
			}
		}
// 	    root: {
// 	        expanded: true,
// 	        children: [
// 	            { text: "辙叉构造", code : 'DCLX', expanded: true, checked : false, children: [
// 	                { text: "可动心岔", code : 'KDXC', leaf: true, checked : false },
// 	                { text: "合金钢岔", code : 'HJGC', leaf: true, checked : false },
// 	                { text: "高猛钢岔", code : 'GMGC', leaf: true, checked : false },
// 	            ] }
// 	        ]
// 	    }
	});
	
	treePanel = Ext.create('Ext.tree.Panel', {
	    width: 150,
	    border : false,
	    useArrows: true,
	    store: treeStore,
	    rootVisible: false,
// 	    frame: true,
	    multiSelect : true,
	    border : false,
	    displayField : 'orgshortname',
	    listeners : {
	    	checkchange:function(node,checked,obj){
// 	    		if(node.get('checked')){
// 	    			selectingCodes.push(child.get('orgcode'));
// 	    		}
	    		checkboxSelected(node,checked);
// 	    		if(node.raw.code == 'DCLX'){
// 	    			return;
// 	    		}else{
// 	    			if(!node.parentNode.get('checked')){
// 		    			return;
// 	    			}
// 	    		}
// 	    		var checks = [];
// 	    		node.parentNode.eachChild(function(childnode) {
// 	    			if(childnode.get('checked')){ 
// 	    				checks.push(childnode.raw.code);
// 	    			}
// 	    		});
   			}
	    }
	});
	
	function checkboxSelected(node,checked){
	  setChildChecked(node,checked);
	  setParentChecked(node,checked);
	}
	//选择子节点树
	function setChildChecked(node,checked){
// 		node.expand();
		node.set('checked',checked);
		if(node.hasChildNodes()){
			node.eachChild(function(child) {
				setChildChecked(child,checked);
			});
		}
	}
		 //选择父节点树
	function setParentChecked(node,checked){
		node.set({checked:checked});
		var parentNode = node.parentNode;
		if(parentNode !=null){
			var flag = false;
			parentNode.eachChild(function(childnode) {
				if(childnode.get('checked')){ 
					 flag = true;
				}
			});
			if(checked == false){
			  if(!flag){
				  setParentChecked(parentNode,checked);
			   }
			}else{
			   if(flag){
				  setParentChecked(parentNode,checked);
				}
			}
		}
	}
	
	panel.okClick = function(p){
		return;
		panel.callback();
		panel.up().close();
	}
	
	panel.cancelClick = function(p){
		panel.up().close();
	}
	
	panel.add({
		xtype: 'panel',
		region: 'center',
		border : false,
		layout: 'fit',
		items: [treePanel],
// 		tbar: statusBar,
		bbar: ['->', {
			text: '确定',
			handler: panel.okClick,
			scope: panel
		}, '-', {
			text: '取消',
			handler: panel.cancelClick,
			scope: panel
		}]
	});
// 	treeStore.load({ 
// 			params : {
// 			params : {
// 				node : 0
// 			}
// 		}
// 	});
	panel.doLayout();
});
</script>