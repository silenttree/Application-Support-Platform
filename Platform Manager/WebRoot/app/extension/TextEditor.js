Ext.define('Asc.extension.TextEditor', {
		
	extend : 'Ext.form.field.Picker',
	
	alias : 'widget.AscTextEditor',
	
	pickerAlign : 'tl-bl?',
	
	matchFieldWidth : true,
	
	minListHeight : 150,
	
	minListWidth : 200,
	
	editable : false,
	/**
	 * 重写createPicker方法，实现显示一个panel，panle中包含一个textarea文本框和确定、取消按钮
	 */
	createPicker : function() {
		var me = this, picker;
		var textField = new Ext.form.field.TextArea({
			hideLabel : true,
			height : '100%',
			listeners : {
				change : function(ta, newValue, oldValue){
					//设置ListTextArea的真实值
					me.setValue(ta.getValue());
				}
			}
		});
		//确定和取消按钮
		var buttons = ['->', {
			text : '取消',
			iconCls : 'icon-sys-cancel',
			handler : function() {
				me.collapse();
			}
		}, '-',{
			text : '确定',
			iconCls : 'icon-sys-confirm',
			handler : function() {
				//设置ListTextArea的真实值
				me.setValue(textField.getValue());
				me.collapse();
			}
		}];
		//宽度小于最小宽度的时候，list的宽度是设置的最小宽度，否则list的宽度是trigger的宽度
		var width = me.getWidth()>me.minListWidth?me.getWidth():me.minListWidth;
		picker = me.picker = Ext.create('Ext.panel.Panel',{
			renderTo : Ext.getBody(),
			//trigger的宽度小于最小宽度的时候，picker的宽度是设置的最小宽度，否则picker的宽度是trigger的宽度
			width : width,
			//picker的高度为给定的最小高度
			height : me.minListHeight,
			//layout : 'fit',//如果设置布局layout，则无法操作panel中的body，并且bodyStyle没效果
			floating : true,
			border : true,
			bodyBorder : false,
			shadow : false,
			bbar : buttons,
			items : textField
		});
		picker.textField = textField;
		return picker;
	},
	getValue : function(){
		return this.value;
	},
	/**
	 * 重写onExpand，在展开下拉列表时，需要做的事情，在这个方法里处理。
	 */
	onExpand : function() {
		var me = this,picker = me.getPicker();
		//宽度小于最小宽度的时候，list的宽度是设置的最小宽度，否则list的宽度是trigger的宽度
		var width = me.getWidth() > me.minListWidth?me.getWidth():me.minListWidth;
		//重新设置picker的宽度和高度,因为在调用expand方法时，已经重新设置了picker的宽度和高度
		picker.setSize(width,me.minListHeight);
		picker.textField.setSize(picker.getWidth(),me.picker.body.getHeight());
		picker.alignTo(me.triggerWrap, me.pickerAlign);
		//给textArea赋初值
		picker.textField.setValue(me.getValue());
	}
});