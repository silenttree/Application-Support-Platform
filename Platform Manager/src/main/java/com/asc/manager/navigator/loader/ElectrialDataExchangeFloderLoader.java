package com.asc.manager.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.manager.navigator.ILoader;
import com.asc.manager.navigator.INode;
import com.asc.manager.navigator.node.CommonTextFolderNode;

public class ElectrialDataExchangeFloderLoader implements ILoader{

	@Override
	public String getType() {
		return CommonTextFolderNode.TYPE_DATABASE_EXCHANGE_LIST;
	}

	@Override
	public List<INode> getChildren(String key) {
		List<INode> nodes = new ArrayList<INode>();
		INode node = null;
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATA_EXCHANGE_TABLE_FOLDER, key, "数据表同步", true);
		nodes.add(node);
		node = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATA_EXCHANGE_FILE_FOLDER, key, "文件同步", true);
		nodes.add(node);
		return nodes;
	}

}
