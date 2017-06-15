Ext.define('Asc.framework.dictionary.model.StaticDictionaryData', {
	
	extend: 'Ext.data.Model',
   
	fields: [
	 	{name: 'id'},
	 	{name: 'parentId'},
		{name: 'key'},
		{name: 'value'},
		{name: 'properties'}
	]
});