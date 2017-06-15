package com.asc.design.access.remote;

import java.util.Arrays;
import java.util.List;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class CxfArrayList {
	private static String url = "http://localhost/cms/services/DesignObjectAccessService?wsdl";
	
	public static void main(String[] args) throws Exception {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();;
		Client client = dcf.createClient(url);
		List<String> types = Arrays.asList("view", "treepage", "formtablelayout", "formpositionlayout", "urlpage", "layout");
	    //Object[] params = new Object[]{"mkCms", new Object[]{"view", "treepage", "formtablelayout", "formpositionlayout", "urlpage", "layout"}, "module"};
		Object[] params = new Object[]{"mkCms", types, "module"};
		Object[] objs = client.invoke("loadGridDesignObject", params);
		System.out.println(objs[0]);
		
	}
}
