/**********************************************************************
 * 
 * Code generated automatically by DirectJNgine
 * Copyright (c) 2009, Pedro Agull¨® Soliveres
 * 
 * DO NOT MODIFY MANUALLY!!
 * 
 **********************************************************************/

Ext.namespace( 'Asc.developer');

Asc.developer.PROVIDER_BASE_URL=window.location.protocol + '//' + window.location.host + '/' + (window.location.pathname.split('/').length>2 ? window.location.pathname.split('/')[1]+ '/' : '')  + '/direct';

Asc.developer.POLLING_URLS = {
}

Asc.developer.REMOTING_API = {
  url: Asc.developer.PROVIDER_BASE_URL,
  type: 'remoting',
  actions: {
    DeveloperAppDirect: [
      {
        name: 'saveAppConnection'/*(String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadDocumentAuthorityMapTree'/*(String, String) => com.google.gson.JsonArray */,
        len: 2,
        formHandler: false
      },
      {
        name: 'createTable'/*(String, String, String) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadCheckTreeObjects'/*(String, String, String[]) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'getModuleRole'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'buildFiles'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'importIdentify'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'updateAuthorities'/*(String, String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'deleteDocumentAuthority'/*(String, String, String, String) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'addDocumentAuthorityMap'/*(String, String, String, String, String) => com.google.gson.JsonObject */,
        len: 5,
        formHandler: false
      },
      {
        name: 'createObject'/*(String, String, String, String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 5,
        formHandler: false
      },
      {
        name: 'buildTableFields'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadDocCheckTree'/*(String, String, String[]) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'deleteReference'/*(String, String, String) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'importNodes'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'addDocumentDesignObject'/*(String, String, String[]) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'deleteAppConnection'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadReferenceCheckObjects'/*(String, String[], String) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadTreeObjects'/*(String, String, String) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadAuthoritymapGrid'/*(String, String) => com.google.gson.JsonArray */,
        len: 2,
        formHandler: false
      },
      {
        name: 'deleteAuthoritymap'/*(String, String, String) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadGridModuleAuth'/*(String, String) => com.google.gson.JsonArray */,
        len: 2,
        formHandler: false
      },
      {
        name: 'addAuthObject'/*(String, String, String[]) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadGridReferences'/*(String, String) => com.google.gson.JsonArray */,
        len: 2,
        formHandler: false
      },
      {
        name: 'importFields'/*(String, String, String, com.google.gson.JsonArray) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'loadObject'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'getSubObjectList'/*(String, String, String) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadCheckTreeObjectsAndRefer'/*(String, String, String[]) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'addReferences'/*(String, String, String[]) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadTableFields'/*(String, String, String) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadAppConnection'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveObjects'/*(String, com.google.gson.JsonArray) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'deleteObject'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadObjects'/*(String, String, String) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadDbFields'/*(String, String, String) => com.google.gson.JsonArray */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadNavigators'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadGridDesignObject'/*(String, String, String[], String) => com.google.gson.JsonArray */,
        len: 4,
        formHandler: false
      },
      {
        name: 'updateAuthoritiesMap'/*(String, String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      }
    ],
    CommonAppDirect: [
      {
        name: 'buildIconCssFile'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      }
    ],
    FlowAppDirect: [
      {
        name: 'setFlowLocked'/*(String, String, int, boolean) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'updateCaption'/*(String, String, int, String) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'insertNode'/*(String, String, int, String, int, int) => com.google.gson.JsonObject */,
        len: 6,
        formHandler: false
      },
      {
        name: 'insertRoute'/*(String, String, int, String, String) => com.google.gson.JsonObject */,
        len: 5,
        formHandler: false
      },
      {
        name: 'getFlowCells'/*(String, String, int, boolean) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'updateRouteTarget'/*(String, String, int, String, String) => com.google.gson.JsonObject */,
        len: 5,
        formHandler: false
      },
      {
        name: 'removeCell'/*(String, String, int, String) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'saveFlowPosition'/*(String, String, int, com.google.gson.JsonObject, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 5,
        formHandler: false
      }
    ]
  }
}

