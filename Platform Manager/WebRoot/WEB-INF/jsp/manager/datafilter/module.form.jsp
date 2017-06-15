<%@page contentType="text/html;charset=UTF-8"%>
<%@page import="com.asc.commons.certification.AscUserCertification"%>
<%@page import="com.asc.commons.organization.entity.User"%>
<%
	String panelid = request.getParameter("panelid");
	User user = AscUserCertification.instance().getUser(request);
%>
<script>
Ext.onReady(function(){
	var panel = Ext.getCmp('<%=panelid%>');
	panel.applicationId = 0;
	
	var appId = Ext.create('Ext.form.Hidden',{
		fieldLabel : '应用标识 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_app_id',
		width : 240,
		allowBlank: false,
		padding : '0 10 0 0'
	});
	
	var id = Ext.create('Ext.form.Hidden',{
		xtype : 'numberfield',
		fieldLabel : 'id <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'id',
		width : 240,
		padding : '0 10 0 0'
	});
	
	var appTitle = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		fieldLabel : '应用名称 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_app_title',
		allowBlank: false,
		width : 490,
		padding : '0 10 0 0'
	});
	
	var moduleStore = Ext.create('Ext.data.Store', {
		fields:['key','value'],
		proxy:{
			type:'direct',
			directFn:DataFilterDirect.listModulesByAppId,
			paramOrder : ['QI'],
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		}
	});
	
	var moduleNameStore = Ext.create('Ext.data.Store', {
		fields:['key','value'],
		proxy:{
			type:'direct',
			directFn:DataFilterDirect.listModulesByAppId,
			paramOrder : ['QI'],
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		}
	});
	
	var moduleId = Ext.create('Ext.form.ComboBox',{
		store: moduleStore,
		fieldLabel : '模块标识 <font color=\'red\'>*</font>',
		editable:false,
	    queryMode: 'local',
	    displayField: 'key',
	    valueField: 'value',
		labelWidth : 100,
		labelAlign: 'left',
		allowBlank: false,
		name : 'f_module_title',
		width : 240,
		padding : '0 10 0 0',
		listeners: {
	        change:{
		    	fn:function(com,nv,ov,eopts){
		    		moduleTitle.setValue(com.getRawValue());
		    		objBtn.setDisabled(false);
		    		//objectStore.load({params:{QI:{appId:appId.getValue(),moduleId:com.getRawValue()}}});
		    	}
	        }
	    }
	});
	
	var objectStore = Ext.create('Ext.data.Store', {
		fields:['key','value'],
		proxy:{
			type:'direct',
			directFn:DataFilterDirect.listObjectsByAppIdAndModuleId,
			paramOrder : ['QI'],
			reader : {
				type: 'json',
				root : 'datas',
				messageProperty : 'message'
			}
		}
	});
	
	var moduleTitle = Ext.create('Ext.form.ComboBox',{
		store: moduleNameStore,
		fieldLabel : '模块名称 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		queryMode: 'local',
	    displayField: 'value',
	    valueField: 'key',
		name : 'f_module_id',
		readOnly:true,
		allowBlank: false,
		width : 240,
		padding : '0 10 0 0'
	});
	
	var functionName = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		fieldLabel : '功能名称 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_function_name',
		allowBlank: false,
		width : 240,
		padding : '0 10 0 0'
	});
	
	var objBtn = Ext.create('Ext.Button',{
		text : '对象标识符 <font color=\'red\'>*</font>',
		disabled : true,
		handler : function (){
			loadCkeckTree();
		}
	});
	var objectId = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		name : 'f_object_id',
		allowBlank: false,
		readOnly:true,
		width : 385,
		padding : '0 10 0 0',
		margin:'0 0 0 20'
	});
	
	var objectName = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		fieldLabel : '对象名称 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_object_name',
		readOnly : true,
		width : 490,
		padding : '0 10 0 0'
	});
	
	var tableName = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		fieldLabel : '数据表名称 ',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_tablename',
		width : 240,
		padding : '0 10 0 0'
	});
	
	var ueExp = Ext.create('Ext.form.Hidden',{
		xtype : 'textfield',
		fieldLabel : '用户表达式 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_ue_expressions',
		allowBlank: false,
		width : 240,
		padding : '0 10 0 0'
	});
	
	var ueBtn = Ext.create('Ext.Button',{
		text : '用户表达式 <font color=\'red\'>*</font>',
		handler : function (){
			panel.choice();
		}
	});
	
	var ueCaption = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		name : 'f_ue_captions',
		allowBlank: false,
		readOnly:true,
		width : 385,
		padding : '0 10 0 0',
		margin:'0 0 0 20'
		
	});
	
	var sqlWhere = Ext.create('Ext.form.TextArea',{
		xtype : 'textareafield',
		fieldLabel : 'SQL条件',
		labelWidth : 100,
		labelAlign: 'left',
		width:490,
		name : 'f_sql_where',
		padding : '0 10 0 0'
	});
	
	var createTime = Ext.create('Ext.form.Date',{
		xtype : 'datefield',
		fieldLabel : '创建时间 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_create_time',
		allowBlank: false,
		width : 240,
		format:'Y-m-d H:m:s',
		readOnly:true,
		value:new Date(),
		padding : '0 10 0 0',
	});
	
	var creatorId = Ext.create('Ext.form.Hidden',{
		xtype : 'textfield',
		fieldLabel : '创建者id <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_creator_id',
		allowBlank : false,
		width : 240,
		padding : '0 10 0 0'
	});
	
	var creatorName = Ext.create('Ext.form.Text',{
		xtype : 'textfield',
		fieldLabel : '创建人 <font color=\'red\'>*</font>',
		labelWidth : 100,
		labelAlign: 'left',
		name : 'f_creator_name',
		allowBlank : false,
		width : 240,
		padding : '0 10 0 0'
	});
	
	var fNote = Ext.create('Ext.form.TextArea',{
		xtype : 'textareafield',
		fieldLabel : '备注',
		labelWidth : 100,
		labelAlign: 'left',
		width:490,
		name : 'f_note',
		padding : '0 10 0 0'
	});

	var loadCkeckTree = function(){
		//checktree的model
		Ext.define('checktreeModel',{
			extend : 'Ext.data.Model',
	 		fields : [
				{name: 'type', type: 'string'},
	 		    {name: 'text', type: 'string'},
	 		    {name: 'name',  type: 'string'},
	 		    {name: 'caption', type : 'string'}
	 		]
		});
		//checktree的store
		var store = Ext.create('Ext.data.TreeStore', {
			model : 'checktreeModel',
			root : 'data',
			proxy : {
				type : 'direct',
				directFn : DataFilterDirect.listObjectsByAppIdAndModuleId,
				extraParams : {
					appId : appId.getValue(),
					moduleId : moduleTitle.getValue(),
				},
				paramOrder : 'appId moduleId',
				paramsAsHash : true,
				sorters : [{
					property : 'type',
					direction : 'ASC'
				},{
					property : 'key',
					direction : 'ASC'
				}]
			},
			autoLoad: true
	    });
		//带有复选框的选择树
		var checktree = Ext.create('Ext.tree.Panel',{
			rootVisible : false,
			viewConfig : {
				forceFit : true,//当行大小变化时始终填充满
				stripeRows : true //每列是否是斑马线分开
			},
        	useArrows : true,
        	border : false,
        	store : store,
       		columns : [
   		        { xtype : 'treecolumn',text : 'Key',  dataIndex : 'text', width :'40%', flex : 1},
   		     	{ text : '类型', dataIndex : 'type', width :'15%'},
   		        { text : '英文名', dataIndex : 'name', width :'15%'},
   		        { text : '中文名', dataIndex : 'caption'}
   		    ],
   		 	listeners : {
		    	itemclick : function(tree, record, item, index, e){
		    		if(record.data.checked != null){
		    			for(var i = 0;i < tree.getChecked().length; i++) {
		    				tree.getChecked()[i].set('checked',false);
		    			}
		    		}
		    		
		    	}
		    }
		});
		var win = Ext.create('Ext.window.Window', {
		    title : '模块对象选择',
		    height : 400,
		    width : 700,
		    modal : true,
		    maximizable : true,
		    layout : 'fit',
		    items :[checktree],
		    bbar : ['->',{
					text : '取消',
					iconCls : 'icon-sys-cancel',
					handler : function() {
						win.close();
				}
			}, '-', {
				text : '确定',
				iconCls : 'icon-sys-apply',
				handler : function() {
					var selcheck = checktree.getChecked();
					if(selcheck.length == 0){
						Asc.common.Message.showError("未选择设计对象！");
						return;
					}else{
						objectId.setValue(selcheck[0].get("text"));
						objectName.setValue(selcheck[0].get("caption"));
						win.close();
					}
				}
			}]
		}).show();
	};
	
	var form = Ext.create('Ext.form.Panel', {
		xtype : 'form',
		layout : 'form',
		border : false,
		padding : '10 0 10 15',
		items : [
			{
				layout : 'column',
				border : false,
				items :  [ appTitle]
			},{
				layout : 'column',
				border : false,
				items :  [ moduleId, moduleTitle]
			},{
				layout : 'column',
				border : false,
				items :  [ functionName,tableName]
			},{
				layout : 'column',
				border : false,
				items :  [ ueBtn,ueCaption]
			},{
				layout : 'column',
				border : false,
				items :  [ objBtn, objectId]
			},{
				layout : 'column',
				border : false,
				items :  [ objectName]
			},{
				layout : 'column',
				border : false,
				items :  [ sqlWhere]
			},{
				layout : 'column',
				border : false,
				items :  [fNote]
			},{
				layout : 'column',
				border : false,
				items :  [creatorName,createTime]
			},{
				layout : 'column',
				border : false,
				items :  [ ueExp,id,appId, creatorId]
			}
		        ]
	});
	
	panel.choice = function(){
		Ext.create('Asc.common.CdruSelector',{
			selectType: 'cdru',
			modal:true,
			singleSelect: false,
			callback: function(value, rawValue){
					setReadOnlyCombo(ueExp,value);
					setReadOnlyCombo(ueCaption,rawValue);
					return true;
			}
		}).show();
	}
	
	panel.initComboData = function(){
		var opener = panel.up('window').opener;
		var appTree = opener.getAppTree();
		var appPanel = opener.getAppPanel();
		var selection = opener.selection;
		if (selection){
			DataFilterDirect.getPolicuById(selection.id, function(result, e){
				setReadOnlyCombo(appId,result.datas.f_app_id);
				setReadOnlyCombo(appTitle,result.datas.f_app_title);
				setCombo(moduleId,result.datas.f_module_id);
				setReadOnlyCombo(moduleTitle,result.datas.f_module_title);
				setCombo(functionName,result.datas.f_function_name);
				setReadOnlyCombo(objectId,result.datas.f_object_id);
				setReadOnlyCombo(objectName,result.datas.f_object_name);
				setCombo(tableName,result.datas.f_tablename);
				setReadOnlyCombo(ueExp,result.datas.f_ue_expressions);
				setReadOnlyCombo(ueCaption,result.datas.f_ue_captions);
				setCombo(fNote,result.datas.f_note);
				setCombo(sqlWhere,result.datas.f_sql_where);
				setReadOnlyCombo(creatorId,result.datas.f_creator_id);
				setReadOnlyCombo(creatorName,result.datas.f_creator_name);
				setReadOnlyCombo(id,result.datas.id);
				setReadOnlyCombo(createTime,result.datas.f_create_time);
			});	
		} else {
			setReadOnlyCombo(appId,appPanel.getSelectedId());
			setReadOnlyCombo(appTitle,appPanel.getSelectedText());
			setReadOnlyCombo(creatorId,<%=user.getId()%>);
			setReadOnlyCombo(creatorName,'<%=user.getF_name()%>');
		}
		moduleStore.load({params:{QI:appPanel.getSelectedId()}});
		moduleNameStore.load({params:{QI:appPanel.getSelectedId()}});
	}
	
	panel.getForm = function (){
		return form;
	}
	var setReadOnlyCombo = function(cmp,val){
		cmp.setValue(val);
		cmp.setReadOnly(true);
	}
	
	var setDisabledCombo = function(cmp,val){
		cmp.setValue(val);
		cmp.setDisabled(true);
	}
	
	var setCombo = function(cmp,val){
		cmp.setValue(val);
	}
	
	// 显示界面
	panel.add(form);
	panel.doLayout();
	panel.initComboData();
});
</script>