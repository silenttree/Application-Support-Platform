package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ApplicationDictionaryFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "ApplicationDictionaryFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_DICTIONARY_OVERRIDE, key, "字典重载", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_APPLICATION_DICTIONARY_DATA_OVERRIDE, key, "数据重载", true);
		nodes.add(node);
		return nodes;
	}

}
