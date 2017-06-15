package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class BaseDataManagerFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "BaseDataManagerFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ORGANIZATION_FOLDER, key, "组织机构", false);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICTIONARY_FOLDER, key, "数据字典", false);
		nodes.add(node);
		return nodes;
	}

}
