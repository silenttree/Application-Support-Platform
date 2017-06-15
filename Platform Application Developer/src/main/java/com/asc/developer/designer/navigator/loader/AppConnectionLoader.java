package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;
import com.asc.developer.designer.navigator.node.AppConnectionNode;

public class AppConnectionLoader implements ILoader {

	public String getType() {
		// TODO Auto-generated method stub
		return AppConnectionNode.TYPE;
	}
	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		// 返回应用节点列表
		INode dataAccess = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATA_ACCESS, appId, key, "数据访问", false);
		nodes.add(dataAccess);
		INode modules = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULES, appId, key, "模块列表", false);
		nodes.add(modules);
		INode workflows = new CommonTextFolderNode(CommonTextFolderNode.TYPE_WORKFLOWS, appId, key, "流程列表", false);
		nodes.add(workflows);
		INode portals = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PORTALS, appId, key, "门户集成", false);
		nodes.add(portals);
//		INode esbFlow = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_SERVICE_EVENT, appId, key, "业务流接口", false);
//		nodes.add(esbFlow);
		return nodes;
	}
	public List<INode> getChildren(String appId, String key, String type) {
		List<INode> nodes = new ArrayList<INode>();
		if(CommonTextFolderNode.TYPE_APP.equals(type)){
			// 返回应用节点列表
			INode dataAccess = new CommonTextFolderNode(CommonTextFolderNode.TYPE_DATA_ACCESS, appId, key, "数据访问", false);
			nodes.add(dataAccess);
			INode modules = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULES, appId, key, "模块列表", false);
			nodes.add(modules);
			INode workflows = new CommonTextFolderNode(CommonTextFolderNode.TYPE_WORKFLOWS, appId, key, "流程列表", false);
			nodes.add(workflows);
			INode portals = new CommonTextFolderNode(CommonTextFolderNode.TYPE_PORTALS, appId, key, "门户集成", false);
			nodes.add(portals);
//			INode esbFlow = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_SERVICE_EVENT, appId, key, "业务流接口", false);
//			nodes.add(esbFlow);
//			INode fileImport = new CommonTextFolderNode(CommonTextFolderNode.TYPE_FILE_IMPORT, appId, key, "文件导入", false);
//			nodes.add(fileImport); 
		}else{
//			INode esbWorkflow = new CommonTextFolderNode(CommonTextFolderNode.TYPE_ESB_WORKFLOW, appId, key, "业务流列表", false);
//			nodes.add(esbWorkflow);
		}
		return nodes;
	}

}
