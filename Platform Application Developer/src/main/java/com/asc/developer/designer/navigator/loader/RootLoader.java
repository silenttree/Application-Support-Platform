package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.config.AppConnectionFactory;
import com.asc.developer.config.ApplicationConnection;
import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.AppConnectionNode;

public class RootLoader implements ILoader{

	public String getType() {
		return "root";
	}
	
	public List<INode> getChildren(String appId, String key) {
		List<ApplicationConnection> appconns = AppConnectionFactory.instance().listAppConnections();
		List<INode> nodes = new ArrayList<INode>();
		for(int i=0;i<appconns.size();i++){
			nodes.add(new AppConnectionNode(appconns.get(i)));
		}
		//nodes.add(new TemplatesFolderNode());
		return nodes;
	}

}
