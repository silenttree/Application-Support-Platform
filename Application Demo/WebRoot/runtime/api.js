Ext.namespace("Asc.app.defects");
Asc.app.defects.PROVIDER_BASE_URL=window.location.protocol+"//"+window.location.host+"/"+(window.location.pathname.split("/").length>2?window.location.pathname.split("/")[1]+"/":"")+"/direct";
Asc.app.defects.POLLING_URLS={};
Asc.app.defects.REMOTING_API={url:Asc.app.defects.PROVIDER_BASE_URL,type:"remoting",actions:{AppDemoWorkflowDemoDirect:[{name:"batchStartWf",len:4,formHandler:false},{name:"batchStartWf1",len:0,formHandler:false},{name:"batchSubmit",len:4,formHandler:false}]}};