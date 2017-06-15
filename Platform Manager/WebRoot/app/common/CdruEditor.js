Ext.define('Asc.common.CdruEditor', {
	
	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscCdruEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	pickerHeight : 400,
	
	pickerWidth : 550,
	
	shadow : 'side',
	
	editable : false,
	
	config : {
		
		/**
		 * @cfg {string} 单位范围表达式 空表示全局范围
		 */
		range : '',
		
		/**
		 * @cfg {string} 选择类型表达式 包含c表示要选单位 包含d表示要选部门 包含r表示要选角色 包含u表示要选用户
		 */
		selectType : 'cdru',
		
		/**
		 * @cfg {string} 已选数据表达式 默认为空
		 */
		//selectedDatas : '',
		
		/**
		 * @cfg {boolean} 是否单选
		 */
		singleSelect : false,
		
		/**
		 * @cfg {function} 回调函数,参数为authExpression,authDisplay
		 */
		callback: Ext.emptyFn
			
	},
	
	/**
	 * 重写createPicker方法，返回一个panel
	 */
	createPicker : function() {
		var me = this,
			picker;
		picker = Ext.create('Asc.common.CdruSelectorPanel', {
			range : me.range,
			selectType : me.selectType,
			singleSelect : me.singleSelect,
			//renderTo : Ext.getBody(),
			//renderTo : me.bodyEl,
			floating : true,
			value : '',
			callback : function(v, dv) {
				me.setValue(dv);
				me.callback(v,dv);
			},
			minHeight : me.pickerHeight,
			minWidth : me.pickerWidth,
			closeFn : function(){
				me.collapse();
			}
		});
		return picker;
	},
	
	alignPicker: function() {
        var me = this,
            picker;
        if (me.isExpanded) {
            picker = me.getPicker();
            if (picker.isFloating()) {
                me.doAlign();
            }
        }
    },
	
	getValueFn : Ext.emptyFn,
	
	onExpand : function() {
		var me = this;
		var picker = me.getPicker();
		picker.setSize(me.pickerWidth, me.pickerHeight);
		if (me.isExpanded && picker.isFloating()) {
			me.doAlign();
		}
		picker.setValue(me.getValueFn());
	}
});