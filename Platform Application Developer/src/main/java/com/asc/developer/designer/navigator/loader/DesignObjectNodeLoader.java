package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.DesignObjectNode;
import com.asc.developer.service.DesignObjectServiceProxy;
import com.google.gson.JsonArray;

public class DesignObjectNodeLoader implements ILoader {

	private String type;
	private String childrenType;
	
	public DesignObjectNodeLoader(String type, String childrenType){
		this.type = type;
		this.childrenType = childrenType;
	}

	public String getType() {
		return type;
	}
	
	public List<INode> getChildren(String appId, String key){
		if("".equals(key)){
			key = null;
		}
		List<INode> nodes = new ArrayList<INode>();
		// 获得父节点对象
		JsonArray results = null;
		try {
			results = DesignObjectServiceProxy.instance().loadObjects(appId, key, childrenType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(results != null){
			for(int i=0;i<results.size();i++){
				DesignObjectNode node = new DesignObjectNode(appId, results.get(i).getAsJsonObject());
				nodes.add(node);
			}
		}
		return nodes;
	}
}
