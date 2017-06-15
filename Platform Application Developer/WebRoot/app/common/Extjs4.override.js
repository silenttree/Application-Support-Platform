Ext.override(Ext.grid.property.HeaderContainer,{
	
	nameWidth: 150,

	nameText : '名称',
	
	valueText : '值',
	
	dateFormat : 'Y-m-d'
});

Ext.override(Ext.grid.property.Grid,{
	
	onUpdate : function(store, record, operation) {
		var me = this,
			v, oldValue;

		if (me.rendered && operation == Ext.data.Model.EDIT) {
			v = record.get(me.valueField);
			oldValue = record.modified.value;
			if (me.fireEvent('beforepropertychange', me.source, record.getId(), v, oldValue) !== false) {
				if (me.source) {
					me.source[record.getId()] = v;
				}
				//record.commit();
				me.fireEvent('propertychange', me.source, record.getId(), v, oldValue);
			} else {
				record.reject();
			}
		}
	}
});


