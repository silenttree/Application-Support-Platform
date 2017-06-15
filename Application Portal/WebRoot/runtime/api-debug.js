/**********************************************************************
 * 
 * Code generated automatically by DirectJNgine
 * Copyright (c) 2009, Pedro Agull¨® Soliveres
 * 
 * DO NOT MODIFY MANUALLY!!
 * 
 **********************************************************************/

Ext.namespace( 'Asc.portal');

Asc.portal.PROVIDER_BASE_URL=window.location.protocol + '//' + window.location.host + '/' + (window.location.pathname.split('/').length>2 ? window.location.pathname.split('/')[1]+ '/' : '')  + '/direct';

Asc.portal.POLLING_URLS = {
}

Asc.portal.REMOTING_API = {
  url: Asc.portal.PROVIDER_BASE_URL,
  type: 'remoting',
  actions: {
    WorkFlowUserSelectorDirect: [
      {
        name: 'getUsersByIds'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getDefaultCompany'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadToselectTreeNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ],
    PortalAppDirect: [
      {
        name: 'clearCache'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'savePreferencesTpl'/*(String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'getShortcuts'/*(String) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'findAllPreferenceTpl'/*() => com.google.gson.JsonArray */,
        len: 0,
        formHandler: false
      },
      {
        name: 'buildFiles'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getDocTree'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadPortalViewList'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadPortalData'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadPortalNavigators'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadWallpapers'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'deletePreferences'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadDeskStyles'/*() => com.google.gson.JsonArray */,
        len: 0,
        formHandler: false
      },
      {
        name: 'getPortlets'/*(String) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadApplications'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'savePreferences'/*(String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'buildIconCssFile'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'changePassword'/*(String, String, String) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      }
    ],
    CDRUDirect: [
      {
        name: 'loadSelectedDatas'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadToSelectDatas'/*(String, String, String) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadRangeTree'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ]
  }
}

