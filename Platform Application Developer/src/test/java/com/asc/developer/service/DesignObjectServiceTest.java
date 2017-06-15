package com.asc.developer.service;

import com.asc.commons.design.Config;
import com.asc.commons.design.DesignObjectFactory;
import com.asc.commons.design.cache.DesignObjectCacheImp;
import com.asc.commons.design.cache.IDesignObjectCache;
import com.asc.commons.design.exception.DesignObjectException;
import com.asc.commons.design.serialize.AbstractJsonObjectSerializer;
import com.asc.commons.design.serialize.imp.JsonObjectSerializeFileImp;
import com.asc.commons.engine.design.entity.module.Module;
import com.asc.framework.designobject.accessor.imp.WsDesignObjectAccessor;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Application Developer/DesignObjectServiceTest.java<br>
 * 对象远程存取测试<br>
 * 1 增删改查功能测试<br>
 * 2 频繁请求对象性能测试
 * 
 * Mixky Co., Ltd. 2014<br>
 * @author Bill<br>
 */
public class DesignObjectServiceTest/* extends TestCase */{
	WsDesignObjectAccessor wsi;
	
	public DesignObjectServiceTest() {
		wsi = new WsDesignObjectAccessor();
	}
	
	protected void setUp() throws Exception {
		IDesignObjectCache cache = new DesignObjectCacheImp();
		DesignObjectFactory.instance().setObjCache(cache);
		AbstractJsonObjectSerializer serializer = new JsonObjectSerializeFileImp();
		DesignObjectFactory.instance().setJsonSerializer(serializer);
		Config config = new Config();
		config.setTarget("d:/designs/");
		DesignObjectFactory.instance().setConfig(config);
		DesignObjectFactory.instance().initialize();
		wsi.setNamespace("http://ws.service.framework.asc.com");
	}
	
	protected void runTest() throws Throwable {
		//testSaveObject();
	}
	
	public void createObject() {
		String appId = "app";
		String objId = "mkTest2";
		try {
			JsonObject json = wsi.createObject(appId, null, "module", objId, new JsonObject());
			System.out.println(json);
		} catch (DesignObjectException e) {
			e.printStackTrace();
		}
	}
	
	public void getObject() {
		String appId = "app";
		String objId = "mkTest";
		try {
			JsonObject json = wsi.getObject(appId, objId);
			System.out.println(json);
		} catch (DesignObjectException e) {
			e.printStackTrace();
		}
	}

	public void findObjects() {
		String appId = "app";
		String typeName = "document";
		String parentId = "mkTest";
		try {
			JsonArray docs = wsi.findObjects(appId, typeName, parentId);
			for (int i = 0; i < docs.size(); i++) {
				System.out.println(docs.get(i));
			}
		} catch (DesignObjectException e) {
			e.printStackTrace();
		}
	}
	
	public void testSaveObject() {
		String appId = "app";
		String objId = "mkTest1";
		try {
			JsonObject json = wsi.createObject(appId, null, "module", objId, new JsonObject());
			wsi.createObject(appId, null, "module", objId, new JsonObject());
			Module m = DesignObjectServiceProxy.instance().buildObject(json);
			m.setF_caption("中文测试");
			wsi.saveObject(appId, objId, m.asJsonObject(false));
			json = wsi.getObject(appId, objId);
			//assertNotNull(m);
			//assertEquals(m.getF_caption(), "中文测试");
			wsi.deleteObject(appId, objId);
			json = wsi.getObject(appId, objId);
			//assertNull(m);
		} catch (DesignObjectException e) {
			e.printStackTrace();
		}
	}
	
}
