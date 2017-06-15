Ext.define('Asc.extension.GridCombox', {

	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscGridCombox',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	minListHeight : 300,
	
	minListWidth : 550,
	
	editable : false,	
	
	config : {
		relationId : null,
        dicType : null
	},
	/**
	 * 重写createPicker方法，实现显示一个panel，panle中包含一个textarea文本框和确定、取消按钮
	 */
	createPicker : function() {
			var me = this, picker;
	        var type = 't_asc_application_entrance_test';
			var typeClass = AscApp.ClassManager.getClass(type);
			// 获得对象属性列表
			var properties = AscApp.ClassManager.getProperties(type);
			// 初始化视图列
			var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
			var fields = AscApp.ClassManager.getStoreFields(properties);
			var dataId = 0;
			// 查询框
			var textfield = new Ext.form.TextField({
				width : 180,
				emptyText : '搜索关键字',
				enableKeyEvents : true,
				listeners : {
						'specialkey' : function (field, e) {
							if (e.getKey() == e.ENTER) {
								var query = field.getValue();
								store.reload();
								field.setValue();
							}
						}
					}
			});		
			var store = new Ext.data.Store({
				clearRemovedOnLoad : true,
				pageSize : 10,//每页显示几条数据(初始值)
			    fields : fields,
				proxy : {
					type : 'direct',
					directFn : DicdataRelationDirect.loadDictionaryDataListIntoPages,
					extraParams:{
						relationId : me.relationId,
						dicType : me.dicType,
						query : textfield.getValue()
					},
					paramOrder : 'relationId dicType page limit query',
					paramsAsHash : true,
					reader : {
						type: 'json',
						root : 'datas',
						totalProperty : 'total',
						successProperty : 'successed',
						messageProperty : 'message'
					}
				},
				sorters : [{
					property : 'f_key',
					direction: 'ASC'
				}],
				listeners : {
					'beforeload' : function(s, operation, eOpts){
						operation.params = {query : textfield.getValue()};
					}
				}
			});
			
		var width = me.bodyEl.getWidth()>me.minListWidth?me.bodyEl.getWidth():me.minListWidth;
		picker = me.picker = Ext.create('Ext.grid.Panel', {
		    height : me.minListHeight,
		    width : width,
		    floating : true,
		    useArrows : true,
		    rootVisible : false,
		    renderTo : Ext.getBody(),
		    store : store,
		    columns : columns,
		    tbar : [textfield,{
				text : '搜索',
				iconCls : 'icon-sys-search',
				handler : function(){
					store.reload();
				}
			}],
		    dockedItems : [{
		        xtype: 'AscPagingToolbar',
		        store: store,
		        dock: 'bottom',
		        displayInfo: true
		    }],
		    listeners : {
		    	itemclick : function(view, record, item, index, e){
		    	     me.setValue(record.get(this.valueField || 'id'));
		    	     me.picker.hide();
		    	     me.inputEl.focus();
		    	}
		    }
		});
		return picker;
	},
	setValue : function(value){
		 var me = this,record;
	        me.value = value;
	        if (me.store.loading) {
	            return me;
	        }
	        var display = value;
	        for(var i in me.store){
				if(me.store[i][0] == value){
					display = me.store[i][1];
				}
			}
	        me.setRawValue(display);
	        return me;
	},
	/**
	 * 设置显示值
	 */
	getValue : function(){
		return this.value;
	},
	/**
	 * 重写onExpand，在展开下拉列表时，需要做的事情，在这个方法里处理。
	 */
	onExpand : function() {
		var me = this,picker = me.getPicker();
		var width = me.bodyEl.getWidth()>me.minListWidth?me.bodyEl.getWidth():me.minListWidth;
		//重新设置picker的宽度和高度,因为在调用expand方法时，已经重新设置了picker的宽度和高度
		picker.setSize(width,me.minListHeight);
		picker.alignTo(me.triggerWrap, me.pickerAlign);
		picker.getStore().reload();
		//给grid赋初值
		//picker.textField.setValue(me.getValue());
	}
});