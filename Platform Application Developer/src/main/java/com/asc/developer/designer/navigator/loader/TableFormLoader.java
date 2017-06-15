package com.asc.developer.designer.navigator.loader;

import java.util.ArrayList;
import java.util.List;

import com.asc.commons.design.DesignObjectFactory;
import com.asc.commons.engine.design.entity.module.FormPositionLayout;
import com.asc.commons.engine.design.entity.module.FormTableLayout;
import com.asc.commons.engine.design.entity.module.TableForm;
import com.asc.commons.engine.exception.DesignObjectHandlerException;
import com.asc.developer.designer.navigator.ILoader;
import com.asc.developer.designer.navigator.INode;
import com.asc.developer.designer.navigator.node.DesignObjectNode;
import com.asc.developer.service.DesignObjectServiceProxy;
import com.google.gson.JsonArray;

public class TableFormLoader implements ILoader {

	@Override
	public String getType() {
		return DesignObjectFactory.instance().getTypeName(TableForm.class);
	}

	@Override
	public List<INode> getChildren(String appId, String key) {
		List<INode> nodes = new ArrayList<INode>();
		JsonArray ftlNodes = new JsonArray();
		JsonArray fplNodes = new JsonArray();
		try {
			//表格布局
			ftlNodes = DesignObjectServiceProxy.instance().loadObjects(appId, key, DesignObjectFactory.instance().getTypeName(FormTableLayout.class));
			if(ftlNodes != null){
				for(int i = 0;i < ftlNodes.size();i++) {
					DesignObjectNode node = new DesignObjectNode(appId, ftlNodes.get(i).getAsJsonObject());
					nodes.add(node);
				}
			}
			//背景布局
			fplNodes = DesignObjectServiceProxy.instance().loadObjects(appId, key, DesignObjectFactory.instance().getTypeName(FormPositionLayout.class));
			if(fplNodes != null){
				for(int i = 0;i < fplNodes.size();i++) {
					DesignObjectNode node = new DesignObjectNode(appId, fplNodes.get(i).getAsJsonObject());
					nodes.add(node);
				}
			}
		} catch (DesignObjectHandlerException e) {
			e.printStackTrace();
		}
		//返回表单信息列表（不包含输入域）
		return nodes;
	}

}
