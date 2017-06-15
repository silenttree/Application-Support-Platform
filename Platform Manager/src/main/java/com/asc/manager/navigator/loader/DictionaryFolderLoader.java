package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class DictionaryFolderLoader implements ILoader {

	@Override
	public String getType() {
		return "DictionaryFolder";
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICTIONARY_MANAGER, key, "字典管理", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICTIONARY_DATA_MANAGER, key, "数据管理", true);
		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICDATA_RELATION_MANAGER, key, "字典数据关系管理", true);
//		nodes.add(node);
//		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DICDATA_RELATION_DATA_MANAGER, key, "关系数据管理", true);
//		nodes.add(node);
		return nodes;
	}

}
