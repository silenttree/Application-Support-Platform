/**********************************************************************
 * 
 * Code generated automatically by DirectJNgine
 * Copyright (c) 2009, Pedro Agull¨® Soliveres
 * 
 * DO NOT MODIFY MANUALLY!!
 * 
 **********************************************************************/

Ext.namespace( 'Asc.manager');

Asc.manager.PROVIDER_BASE_URL=window.location.protocol + '//' + window.location.host + '/' + (window.location.pathname.split('/').length>2 ? window.location.pathname.split('/')[1]+ '/' : '')  + '/direct';

Asc.manager.POLLING_URLS = {
}

Asc.manager.REMOTING_API = {
  url: Asc.manager.PROVIDER_BASE_URL,
  type: 'remoting',
  actions: {
    ManagerAppDirect: [
      {
        name: 'loadOrganization'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadNavigators'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'buildFiles'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getNewSeqId'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ],
    DictionaryDirect: [
      {
        name: 'saveDictionaryData'/*(long, long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'getDictionaryDataList'/*(long, long) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'getDictionaryDatasByParentCode'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadDictionaryDataNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadDictionaryNodes'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'saveDictionary'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadDictionaryDataNode'/*(com.google.gson.JsonObject) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getDictionaryList'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      }
    ],
    ApplicationRegisterDirect: [
      {
        name: 'getDatabaseList'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadClusterApplicationNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveApplicationInstances'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'addDatabasesconf'/*(com.google.gson.JsonObject, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'deleteSyncTable'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveDatabase'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'saveApplications'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveDatasourcemapping'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadDataSyncTable'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveDatabasessync'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadApplicationDatasourceNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadApplicationInstances'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'addDatasourceScript'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadDatabasesconf'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'updateAppState'/*(long, String, int) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'loadApplicationsState'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadApplications'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'saveSyncTable'/*(long, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'addDatabasesExchange'/*(com.google.gson.JsonObject, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadDatabasessync'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      }
    ],
    DataFilterDirect: [
      {
        name: 'addPolicy'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'listPoliciesByAppid'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'listModulesByAppId'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getPolicuById'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'listObjectsByAppIdAndModuleId'/*(String, String) => com.google.gson.JsonArray */,
        len: 2,
        formHandler: false
      },
      {
        name: 'deletePolicy'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'listModuleNamesByAppId'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'copyPolicy'/*(String[]) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ],
    AppEntDirect: [
      {
        name: 'saveAppEntAuths'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadAppNavs'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'SaveAppEnts'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadAppEntranceTree'/*(com.google.gson.JsonObject) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadApplicationNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
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
        name: 'loadRelativeDatas'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadRangeTree'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ],
    PortalProfileDirect: [
      {
        name: 'save'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'findAll'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      }
    ],
    WorkFlowRoleMappingDirect: [
      {
        name: 'save'/*(int, com.google.gson.JsonArray) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'load'/*(long, long) => com.google.gson.JsonObject */,
        len: 2,
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
    OrganizationDirect: [
      {
        name: 'getDeptCaption'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'findUserRoles'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getUserDeptList'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadOrganization'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getOrgUser'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveRole'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'createOrgCode'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadUsers'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadCompanyNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveOrgUser'/*(String, long) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadRoleNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'updateUserOrg'/*(String, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'findUser'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadOrganizationNode'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveUsers'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'findRoleUsers'/*(long, long) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'retPassword'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'moveOrg'/*(long, long) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'saveOrg'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'saveRoleUsers'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'findUserOrgs'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveSelectorRoleUsers'/*(String, long, long) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'saveOrgUsers'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadRole'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadOrgUsers'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadOrganizationNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'deleteUser'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'findUserRolesName'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ],
    LogDirect: [
      {
        name: 'loadLogsByType'/*(String, String, int, int) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'loadAppTree'/*(String) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveLogConf'/*(String, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadLogConfig'/*(String) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      }
    ],
    DicdataRelationDirect: [
      {
        name: 'getRelationList'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'saveDicdataRelation'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadDictionaryDataList'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadDictionaryDataListIntoPages'/*(long, int, int, int, String) => com.google.gson.JsonObject */,
        len: 5,
        formHandler: false
      },
      {
        name: 'getDictionaryDataList'/*(long) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getRelationDataList'/*(long, int, int, String) => com.google.gson.JsonObject */,
        len: 4,
        formHandler: false
      },
      {
        name: 'loadDictionaryDataNodes'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'saveRelationData'/*(com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 1,
        formHandler: false
      },
      {
        name: 'loadRelationNodes'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      },
      {
        name: 'loadDictionaryDataNode'/*(com.google.gson.JsonObject) => com.google.gson.JsonArray */,
        len: 1,
        formHandler: false
      },
      {
        name: 'getDictionaryList'/*() => com.google.gson.JsonObject */,
        len: 0,
        formHandler: false
      }
    ],
    ExtpropertyDirect: [
      {
        name: 'saveProperty'/*(long, com.google.gson.JsonObject) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadExtproperty'/*(long, String) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'findExtpropertyData'/*(String, int) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      }
    ],
    ModuleRoleMappingDirect: [
      {
        name: 'save'/*(int, com.google.gson.JsonArray) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'save2'/*(int, com.google.gson.JsonArray) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      },
      {
        name: 'loadByCDRUAndAppId'/*(String, long, boolean) => com.google.gson.JsonObject */,
        len: 3,
        formHandler: false
      },
      {
        name: 'load'/*(long, long) => com.google.gson.JsonObject */,
        len: 2,
        formHandler: false
      }
    ]
  }
}

