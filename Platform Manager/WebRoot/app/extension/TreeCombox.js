Ext.define('Asc.extension.TreeCombox', {
    extend: 'Ext.form.field.Picker',
    alias : 'widget.TreeCombox',
    triggerCls: Ext.baseCSSPrefix + 'form-arrow-trigger',
    config: {
        displayField: null,
        columns: null,
        rootVisible: true,
        selectOnTab: true,
        firstSelected: false,
        maxPickerWidth: 550,
        maxPickerHeight: 360,
        minPickerHeight: 100,
        relationId : null,
        dicType : null
    },
    editable: false,

    createPicker: function() {
        var me = this;
        
        var type = 't_asc_application_entrance_test';
		var typeClass = AscApp.ClassManager.getClass(type);
		// 获得对象属性列表
		var properties = AscApp.ClassManager.getProperties(type);
		// 初始化视图列
		var columns = AscApp.ClassManager.getGridColumns(properties, typeClass.propertyColumns);
		columns[1].xtype = 'treecolumn';
		var fields = AscApp.ClassManager.getStoreFields(properties);
		var dataId = 0;
		var store = new Ext.data.TreeStore({
			clearRemovedOnLoad : true,
			autoLoad : false,
			root : {
				id: 0,
				f_level: 0,
		        expanded: true,
		        children: []
		    },
		    fields : fields,
			proxy : {
				type : 'direct',
				directFn : DicdataRelationDirect.loadDictionaryDataNodes,
				extraParams:{
					relationId : me.relationId,
					dataId : dataId,
					dicType : me.dicType
				},
				reader : {
					type: 'json',
					root : 'datas',
					totalProperty : 'totals',
					successProperty : 'successed',
					messageProperty : 'message'
				}
			}
		});
        
           var picker = Ext.create('Ext.tree.Panel', {
                store: store,
                floating: true,
                hidden: true,
//              hideHeaders : true,
                width: me.maxPickerWidth,
                displayField: me.displayField,
                columns: columns,
                maxHeight: me.maxTreeHeight,
                shadow: false,
                rootVisible: false,
                manageHeight: false,
                listeners: {
                    itemclick: Ext.bind(me.onItemClick, me)
                },
                viewConfig: {
                    listeners: {
                        render: function(view) {
                            view.getEl().on('keypress', me.onPickerKeypress, me);
                        }
                    }
                }
            }),
            view = picker.getView();

        view.on('render', me.setPickerViewStyles, me);
        if (Ext.isIE9 && Ext.isStrict) {
            view.on('highlightitem', me.repaintPickerView, me);
            view.on('unhighlightitem', me.repaintPickerView, me);
            view.on('afteritemexpand', me.repaintPickerView, me);
            view.on('afteritemcollapse', me.repaintPickerView, me);
        }
        return picker;
    },
    initComponent: function() {
        var me = this;
        me.callParent(arguments);
        this.addEvents('select');
       //me.store.on('load', me.onLoad, me);
        
    },
    setPickerViewStyles: function(view) {
        view.getEl().setStyle({
            'min-height': this.minPickerHeight + 'px',
            'max-height': this.maxPickerHeight + 'px'
        });
    },
    repaintPickerView: function() {
        var style = this.picker.getView().getEl().dom.style;
        style.display = style.display;
    },
    alignPicker: function() {
        var me = this,
            picker;

        if (me.isExpanded) {
            picker = me.getPicker();
            if (me.matchFieldWidth) {
                picker.setWidth(this.picker.getWidth());
            }
            if (picker.isFloating()) {
                me.doAlign();
            }
        }
    },
    onItemClick: function(view, record, node, rowIndex, e) {
        this.selectItem(record);
    },
    onPickerKeypress: function(e, el) {
        var key = e.getKey();

        if(key === e.ENTER || (key === e.TAB && this.selectOnTab)) {
            this.selectItem(this.picker.getSelectionModel().getSelection()[0]);
        }
    },
    selectItem: function(record) {
        var me = this;
        me.setValue(record.get(this.valueField || 'id'));
        me.picker.hide();
        me.inputEl.focus();
        me.fireEvent('select', me, record)
    },
    onExpand: function() {
        var me = this,
            picker = me.picker,
            store = picker.store,
            value = me.value;
        if(value) {
        	var node = store.getNodeById(value);
        	if(node)
            	picker.selectPath(node.getPath());
        } else {
        	var hasOwnProp = me.store.hasOwnProperty('getRootNode');
        	if(hasOwnProp)
            	picker.getSelectionModel().select(store.getRootNode());
        }

        Ext.defer(function() {
            picker.getView().focus();
        }, 1);
    },
    setValue: function(value) {
    	
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
    getValue: function() {
        return this.value;
    },
    onLoad: function(store,node,records) {
        var value = this.value;
        if (value) {
            this.setValue(value);
        }else{
        	if(this.firstSelected){
	        	if(records && records.length > 0){
	        		var record = records[0];
	        		this.setValue(record.get(this.valueField));
	        	}
        	}
        }
    },
    getSubmitData: function() {
        var me = this,
            data = null;
        if (!me.disabled && me.submitValue) {
            data = {};
            data[me.getName()] = '' + me.getValue();
        }
        return data;
    },
    onTriggerClick: function() {
        var me = this;
        if (!me.readOnly && !me.disabled) {
            if (me.isExpanded) {
                me.collapse();
            } else {
                me.expand();
            }
            me.inputEl.focus();
        }
    }
});