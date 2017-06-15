package com.asc.manager.navigator.node;

import com.asc.manager.navigator.INode;
import com.google.gson.JsonObject;

public abstract class BasicNode implements INode {
	
	public String getId(){
		return this.getType() + "-" + this.getKey();
	}

	public String getIconCls() {
		return "icon-manager-" + this.getType().toLowerCase();
	}
	
	public String getTip(){
		return this.getText() + " " + "[" + this.getType() + " " + this.getKey() + "]";
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		json.addProperty("id", this.getId());
		json.addProperty("type", this.getType());
		json.addProperty("text", this.getText());
		json.addProperty("key", this.getKey());
		json.addProperty("iconCls", this.getIconCls());
		json.addProperty("leaf", this.isLeaf());
		if(this.getTip() != null){
			json.addProperty("qtip", this.getTip());
		}
		return json;
	}

}
