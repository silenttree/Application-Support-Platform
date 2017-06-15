package com.asc.design.access.remote;

import com.asc.util.ws.WebServiceInvoke;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class RemoteDesignDataAccessTest extends TestCase {
	private String wsdlurl = "http://localhost/account/services/DesignObjectDataService?wsdl";
	private JsonObject values;
	
	public RemoteDesignDataAccessTest() {
		values = new JsonObject();
		values.addProperty("f_name", "mkTest");
		values.addProperty("f_caption", "测试模块");
	}
	
	public void testx() {
		assertEquals(1, 1);
	}

	public void stestCreateModule1() {
		WebServiceInvoke ws = new WebServiceInvoke(wsdlurl, null);
		
		try {
			Object obj = ws.invoke("createObject", new Object[]{null, "module", "mkTest", values.toString()}, null);
			System.out.println("testCreateModule1");
			System.out.println(obj.toString());
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stestCreateModule2() {
		WebServiceInvoke ws = new WebServiceInvoke("http://localhost/account/services/DesignObjectAccessService?wsdl", null);
		
		try {
			Object obj = ws.invoke("createObject", new Object[]{null, "module", "mkTest", values.toString()}, null);
			System.out.println("testCreateModule2");
			System.out.println(obj.toString());
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
