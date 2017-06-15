package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationRegisterFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationRegisterFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_LIST, key, "应用系统连接", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATABASE_LIST, key, "数据库连接", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATABASE_CONFIGURATION_LIST, key, "数据库连接映射", true);
		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATABASE_EXCHANGE_LIST, key, "内外网交换", false);
//		nodes.add(node);
		return nodes;
	}

}
