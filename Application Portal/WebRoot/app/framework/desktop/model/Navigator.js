Ext.define('Asc.framework.desktop.model.Navigator', {
	
	extend: 'Ext.data.Model',
   
	fields: [
		{name: 'id', type: 'int'},
		{name: 'f_parent_id', type: 'int'},
		{name: 'f_no'},
		{name: 'f_key'},
		{name: 'f_caption'},
		{name: 'f_icon'},
		{name: 'f_note'},
		{name: 'f_script'},
		{name: 'f_disabled'}
	]
});
