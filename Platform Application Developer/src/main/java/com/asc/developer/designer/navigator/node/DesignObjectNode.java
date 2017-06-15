package com.asc.developer.designer.navigator.node;

import com.asc.developer.designer.navigator.NavigatorLoaderManager;
import com.google.gson.JsonObject;

public class DesignObjectNode extends BasicNode {
	
	private String appId;
	
	private JsonObject object;
	
	public DesignObjectNode(String appId, JsonObject dObj){
		this.appId = appId;
		this.object = dObj;
	}

	public String getAppId() {
		return appId;
	}

	public String getType() {
		return object.get("f_class").getAsString();
	}

	public String getText() {
		return object.get("f_caption").getAsString();
	}

	public String getKey() {
		return object.get("id").getAsString();
	}

	public boolean isLeaf() {
		return !NavigatorLoaderManager.instance().hasLoader(this.getType());
	}
}
