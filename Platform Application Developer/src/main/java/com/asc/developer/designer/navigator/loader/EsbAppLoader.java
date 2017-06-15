package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;

public class EsbAppLoader implements ILoader{

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return CommonTextFolderNode.TYPE_ESB_SERVICE_EVENT;
	}

	@Override
	public List<INode> getChildren(String appId, String key) {
		// TODO Auto-generated method stub
		List<INode> nodes = new ArrayList<INode>();
		// 返回应用节点列表
		INode appevents = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_APPEVENT, appId, key, "应用事件", true);
		nodes.add(appevents);
		INode appservice = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_APPSERVICE, appId, key, "应用服务", true);
		nodes.add(appservice);
		return nodes;
	}

}
