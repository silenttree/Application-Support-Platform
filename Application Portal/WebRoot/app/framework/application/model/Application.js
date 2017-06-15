Ext.define('Asc.framework.application.model.Application', {
	
	extend: 'Ext.data.Model',
   
	fields: [
		{name: 'id', type: 'int'},
		{name: 'f_key'},
		{name: 'f_caption'},
		{name: 'f_url'},
		{name: 'f_group'},
		{name: 'f_note'},
		{name: 'f_state'},
		{name: 'f_is_init', type : 'boolean'}
	]
});
