package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.commons.design.DesignObjectFactory;
import com.asc.commons.engine.design.entity.module.Module;
import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.CommonTextFolderNode;

public class ModuleNodeLoader implements ILoader {

	public String getType() {
		return DesignObjectFactory.instance().getTypeName(Module.class);
	}

	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		// 返回应用节点列表
//		INode reference = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULE_REFREENCE, appId, key, "外部引用", true);
//		nodes.add(reference);
		INode menus = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULE_MENUS, appId, key, "模块菜单", false);
		nodes.add(menus);
		INode pages = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULE_PAGES, appId, key, "页面视图", false);
		nodes.add(pages);
		INode documents = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULE_DOCUMENTS, appId, key, "文档列表", false);
		nodes.add(documents);
		INode roles = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULE_ROLES, appId, key, "模块角色", false);
		nodes.add(roles);
		INode auths = new CommonTextFolderNode(CommonTextFolderNode.TYPE_MODULE_AUTHORITIES, appId, key, "模块权限", true);
		nodes.add(auths);
		return nodes;
	}

}
