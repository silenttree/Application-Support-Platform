Ext.define('Asc.framework.workflow.model.NodeProcessLog', {
	
	extend: 'Ext.data.Model',
	
	idProperty : 'ID',
   
	fields: [
	 	{name: 'ID', type: 'int'},
	 	{name: 'F_NODE_ID'},
	 	{name: 'F_NODE_CAPTION'},
		{name: 'F_START_TIME'},
		{name: 'F_END_TIME'},
		{name: 'F_PROCESS_TYPE', type: 'int'},
		{name: 'F_STATE', type: 'int'},
		{name: 'F_OPINION'},
		{name: 'F_USER_NAME'}
	]
});