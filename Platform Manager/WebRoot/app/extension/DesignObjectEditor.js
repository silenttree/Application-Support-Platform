Ext.define('Asc.extension.DesignObjectEditor', {

	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscDesignObjectEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	minListHeight : 210,
	
	minListWidth : 550,
	
	editable : false,
	
	key : '',
	
	appId : '',
	
	config : {
		returnType : '',
		type : [],
		scope : ''
	},
	/**
	 * 重写createPicker方法，实现显示一个panel，panle中包含一个textarea文本框和确定、取消按钮
	 */
	createPicker : function() {
		var me = this, picker;
		Ext.define('designobjectModel',{
			extend : 'Ext.data.Model',
			fields :[
			       {name :'key',type:'string'},
			       {name :'type',type:'string'},
			       {name :'name',type:'string'},
			       {name :'caption',type:'string'}
			]
		});
		var store = Ext.create('Ext.data.Store', {
		    model : 'designobjectModel',
			proxy : {
				type : 'direct',
				directFn : DeveloperAppDirect.loadGridDesignObject,
				extraParams : {
					appId : me.appId,
					key : me.key,
					type : me.getType(),
					scope : me.getScope()
				},
				paramOrder : ['appId','key','type','scope'],
				paramsAsHash : true
			},
			sorters : [{
				property : 'type',
				direction: 'ASC'
			},{
				property : 'key',
				direction: 'ASC'
			}],
			autoLoad : true
		});

		//确定和取消按钮
		var buttons = ['->',{
			text : '取消',
			iconCls : 'icon-sys-cancel',
			handler : function() {
				me.collapse();
			}
		}, '-', {
			text : '确定',
			iconCls : 'icon-sys-confirm',
			handler : function() {
				var returnType = me.getReturnType();
				var len = me.picker.getSelectionModel().getSelection().length;
				if(len == 0){
					return;
				}else{
					var record = me.picker.getSelectionModel().getSelection()[0];
					var obj;
					if(returnType != '' && returnType == 'string'){
						obj = record.data.key;
					}else{
						if(record.data.type!=null && ''!=record.data.type && record.data.key!=null && ''!=record.data.key){
			    			obj = {'class' : record.data.type,'data' : record.data.key};
			    		}
					}
		    		me.setValue(obj);
					me.collapse();
				}
			}
		}];
		var width = me.bodyEl.getWidth()>me.minListWidth?me.bodyEl.getWidth():me.minListWidth;
		picker = me.picker = Ext.create('Ext.grid.Panel', {
		    height : me.minListHeight,
		    width : width,
		    floating : true,
		    useArrows : true,
		    rootVisible : false,
			bbar : buttons,
		    renderTo : Ext.getBody(),
		    store : store,
		    columns : [
		        { text: '类型', dataIndex: 'type',width: '20%',
		        renderer : function(v, md, record){
		        	return "<div class='icon-designer-" + record.data.type + "'></div>"  + record.data.type;
		        }},
		        {text: 'Key',  dataIndex: 'key',flex: 1},
		        { text: '中文名', dataIndex: 'name',width: '20%'},
		        { text: '英文名', dataIndex: 'caption',width: '20%'}
		    ],
		    listeners : {
		    	itemdblclick : function(view, record, item, index, e){
		    		var returnType = me.getReturnType();
		    		var obj;
		    		if(returnType != '' && returnType == 'string'){
						obj = record.data.key;
					}else{
						if(record.data.type!=null && ''!=record.data.type && record.data.key!=null && ''!=record.data.key){
			    			obj = {'class' : record.data.type,'data' : record.data.key};
			    		}
					}
		    		me.setValue(obj);
		    		me.collapse();
		    	}
		    }
		});
		return picker;
	},
	setValue : function(v){
		this.setRawValue(v);
		this.value = v;
	},
	/**
	 * 设置显示值
	 */
	setRawValue : function(value) {
        var me = this;
        if(typeof value == 'object'){
        	value = Ext.encode(value);
        }
        value = Ext.value(value, '');
        me.rawValue = value;
        //一些子类不能呈现一个inputEl
        if (me.inputEl) {
            me.inputEl.dom.value = value;
        }
        return value;
	},
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