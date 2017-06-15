Ext.define('Asc.extension.CheckCombEditor', {
	
	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscCheckCombEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	minListHeight : 230,
	
	minListWidth : 240,
	
	editable : false,

	key : '',
	
	appId : '',
	
	config : {
		type : ''
	},
	
	/**
	 * 重写createPicker方法，实现显示一个panel，panle中包含一个textarea文本框和确定、取消按钮
	 */
	createPicker : function() {
		var me = this, picker;
		Ext.define('flowRoleModel',{
			extend : 'Ext.data.Model',
			fields : [
//			   {name :'f_key',type:'string'},
			   {name :'id',type:'string'},
			   {name :'f_caption',type:'string'},
			   {name :'f_class',type:'string'}
//			   {name :'f_description',type:'string'},
//			   {name :'f_properties'},
//			   {name :'f_i_parent'},
//			   {name :'f_name',type:'string'}
			]
		});
		var store = Ext.create('Ext.data.Store', {
			clearRemovedOnLoad : true,
		    model : 'flowRoleModel',
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadObjects,
				extraParams : {
					appId : me.appId,
					key : me.key,
					type : me.getType()
				},
				paramOrder : ['appId','key','type'],
				paramsAsHash : true,
				reader : {
					type: 'json',
					root : 'datas',
					messageProperty : 'message'
				}
			},
			autoLoad : true,
			listeners : {
				beforeload : function(store, operation){
					operation.params = {};
					operation.params.appId = me.appId;
					operation.params.key = me.key;
					operation.params.type = me.getType();
				}
			}
		});
		picker = me.picker = Ext.create('Ext.grid.Panel', {
		    store : store,
		    hideHeaders : true,
		    columns: [
		        { text: 'id',  dataIndex: 'id', hidden : true},
		        { text: 'key',  dataIndex: 'f_key', hidden : true},
		        { text: '流程角色', dataIndex: 'f_caption',flex:1},
		        { text: '英文名',  dataIndex: 'f_name', hidden : true},
		        { text: '设计类', dataIndex: 'f_class',hidden : true},
		        { text: '说明信息', dataIndex: 'f_description',hidden : true},
		        { text: 'class父类', dataIndex: 'f_i_parent',hidden : true},
		        { text: '属性', dataIndex: 'f_properties',hidden : true}
		    ],
		    selType: 'checkboxmodel',
		    selModel: {
		    	mode : 'SIMPLE'
		    },
		    height: 260,
		    width: 350,
		    floating : true,
		    shadow : false,
		    renderTo: Ext.getBody(),
		    listeners : {
		    	selectionchange : function(grid, selected){
		    		var value = [];
		    		var rawValue = '';
		    		for(var i = 0;i < selected.length;i++){
		    			//value[i] = selected[i].data;
		    			value[i] = selected[i].get('id');
		    			console.log(selected[i].data);
		    			rawValue = rawValue + selected[i].get('f_caption') + ';';
		    		}
		    		me.setValue(value);
		    		me.setRawValue(rawValue);
		    	},
		    	afterlayout : function(grid,layout){
		    		//展开时，根据value值，设置流程角色为选中
		    		var value = me.getValue();
		    		var records = [];
		    		for(var i in value){
		    			grid.getStore().each(function(rs){
		    				if(value[i].id == rs.data.id){
		    					records.push(rs);
		    				}
		    			});
		    		}
		    		grid.getSelectionModel().select(records);
		    	}
		    }
		});
		return picker;
	},
	/**
	 * 设置显示值
	 */
	setRawValue: function(value) {
        var me = this, showValue = '';
        if(typeof value == 'object'){
        	for(var i in value){
        		showValue  = showValue + value[i].name  + ";";
        	}
        }else{
        	showValue = value;
        }
        me.rawValue = showValue;
        //一些子类不能呈现一个inputEl
        if (me.inputEl) {
            me.inputEl.dom.value = showValue;
        }
        return showValue;
	},
	getValue : function(){
		return this.value;
	},
	/**
	 * 重写onExpand，在展开下拉列表时，需要做的事情，在这个方法里处理。
	 */
	onExpand : function() {
		var me = this,picker = me.getPicker();
		//重新设置picker的宽度和高度,因为在调用expand方法时，已经重新设置了picker的宽度和高度
		picker.setSize(me.minListWidth,me.minListHeight);
		picker.alignTo(me.triggerWrap, me.pickerAlign);
		//给插件赋初值
		//me.setValue();
	}
})