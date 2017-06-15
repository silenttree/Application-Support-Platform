package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.commons.design.DesignObjectFactory;
import com.asc.commons.engine.exception.DesignObjectHandlerException;
import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;
import com.asc.developer.designer.navigator.node.DesignObjectNode;
import com.asc.developer.service.DesignObjectServiceProxy;
import com.asc.workflow.core.template.Flow;
import com.asc.workflow.core.template.Node;
import com.google.gson.JsonArray;

public class WorkFlowNodeLoader implements ILoader {

	@Override
	public String getType() {
		return DesignObjectFactory.instance().getTypeName(Flow.class);
	}

	@Override
	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode flowRoles = new CommonTextFolderNode(CommonTextFolderNode.TYPE_FLOW_ROLE, appId, key, "流程角色列表", false);
		nodes.add(flowRoles);
		JsonArray flowNodes = new JsonArray();
		try {
			flowNodes = DesignObjectServiceProxy.instance().loadObjects(appId, key, DesignObjectFactory.instance().getTypeName(Node.class));
			if(null != flowNodes){
				for (int i = 0; i < flowNodes.size(); i++) {
					DesignObjectNode node = new DesignObjectNode(appId, flowNodes.get(i).getAsJsonObject());
					nodes.add(node);
				}
			}
		} catch (DesignObjectHandlerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nodes;
	}

}
